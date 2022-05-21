package at.fhv.ss22.ea.f.musicshop.backend.domain.event;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EventRepository;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ejb.*;
import java.util.Optional;

@Local(EventSender.class)
@Stateless
public class EventSenderImpl implements EventSender {
    private static final Logger logger = LogManager.getLogger(EventSenderImpl.class);

    private static final String PURCHASE_EVENT_QUEUE_NAME = "purchasedQueue";

    @EJB
    private EventRepository eventRepository;

    private JedisPool jedisPool;

    public EventSenderImpl() {
        this.jedisPool  = new JedisPool("localhost", 6379); //TODO get from .env (maybe move to constructor (not-testing))
    }
    public EventSenderImpl(EventRepository eventRepository, JedisPool jedisPool) {
        this.eventRepository = eventRepository;
        this.jedisPool = jedisPool;
    }

    @Override
    @Schedule(hour = "*", minute = "*", second = "*/5", info = "Send event timer")
    public void sendDigitalPurchase() {
        Optional< DigitalProductPurchased> opt =  this.eventRepository.getNextOutgoing();
        if (opt.isPresent()) {
            DigitalProductPurchased event = opt.get();
            Gson gson = new Gson();
            String jsonEvent = gson.toJson(event);

            try (Jedis jedis = jedisPool.getResource()) {
                jedis.lpush(PURCHASE_EVENT_QUEUE_NAME, jsonEvent);
                logger.info("Successfully published to Redis {}: {}", PURCHASE_EVENT_QUEUE_NAME, jsonEvent);
            }
        }
    }
}
