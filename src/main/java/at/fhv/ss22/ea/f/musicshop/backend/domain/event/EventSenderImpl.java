package at.fhv.ss22.ea.f.musicshop.backend.domain.event;

import at.fhv.ss22.ea.f.communication.dto.DigitalProductPurchasedDTO;
import at.fhv.ss22.ea.f.communication.dto.SongDTO;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ArtistRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EventRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Local(EventSender.class)
@Stateless
public class EventSenderImpl implements EventSender {
    private static final Logger logger = LogManager.getLogger(EventSenderImpl.class);

    private static final String PLAYLIST_SERVICE_QUEUE_NAME = "playlistQueue";
    private static final String DOWNLOAD_SERVICE_QUEUE_NAME = "downloadQueue";
    private static final String REDIS_HOST = System.getenv("REDIS_HOST");
    private static final String REDIS_PORT = System.getenv("REDIS_PORT");

    @EJB
    private ProductRepository productRepository;
    @EJB
    private ArtistRepository artistRepository;
    @EJB
    private EventRepository eventRepository;

    private JedisPool jedisPool;

    public EventSenderImpl() {
        this.jedisPool  = new JedisPool(REDIS_HOST, Integer.parseInt(REDIS_PORT));
    }
    public EventSenderImpl(EventRepository eventRepository, ProductRepository productRepository, JedisPool jedisPool) {
        this.eventRepository = eventRepository;
        this.productRepository = productRepository;
        this.jedisPool = jedisPool;
    }

    @Override
    @Schedule(hour = "*", minute = "*", second = "*/5", info = "Send event timer")
    public void sendDigitalPurchase() {
        List<DigitalProductPurchased> events =  this.eventRepository.getNextOutgoings();
        for (DigitalProductPurchased event : events) {
            Product product = productRepository.productById(event.getProductId()).orElseThrow(NoSuchElementException::new);

            DigitalProductPurchasedDTO eventDTO = DigitalProductPurchasedDTO.builder()
                    .withUsername(event.getUsername())
                    .withAlbumName(product.getName())
                    .withArtists(event.getArtists())
                    .withPurchasedSongs(product.getSongs()
                            .stream()
                            .map(song -> SongDTO.builder()
                                    .withTitle(song.getTitle())
                                    .withDuration(song.getDuration())
                                    .build()
                            ).collect(Collectors.toList())
                    )
                    .build();

            Gson gson = new Gson();
            String jsonEvent = gson.toJson(eventDTO);

            try (Jedis jedis = jedisPool.getResource()) {
                jedis.lpush(PLAYLIST_SERVICE_QUEUE_NAME, jsonEvent);
                logger.info("Successfully published to Redis {}: {}", PLAYLIST_SERVICE_QUEUE_NAME, jsonEvent);

                jedis.lpush(DOWNLOAD_SERVICE_QUEUE_NAME, jsonEvent);
                logger.info("Successfully published to Redis {}: {}", DOWNLOAD_SERVICE_QUEUE_NAME, jsonEvent);
            }
            this.eventRepository.remove(event);
        }
    }
}
