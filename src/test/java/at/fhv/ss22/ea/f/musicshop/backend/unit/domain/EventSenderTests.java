package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.EventSender;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.EventSenderImpl;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchasedId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EventRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventSenderTests {

    private EventSender eventSender;
    private EventRepository eventRepository = mock(EventRepository.class);
    private ProductRepository productRepository = mock(ProductRepository.class);
    private JedisPool jedisPool = mock(JedisPool.class);

    @BeforeAll
    void setup() {
        this.eventSender = new EventSenderImpl(eventRepository, productRepository, jedisPool);
    }

    @Test
    void when_event_in_repository_then_json_published_to_queue() {
        //given
        Jedis jedis = mock(Jedis.class);
        when(jedisPool.getResource()).thenReturn(jedis);

        ProductId productId = new ProductId(UUID.randomUUID());
        Product product = Product.create(
                productId,
                "a product",
                "1980",
                List.of("Rock"),
                "Label",
                "20:10",
                Collections.emptyList(),
                Collections.emptyList()
        );

        DigitalProductPurchased event = new DigitalProductPurchased(
                new DigitalProductPurchasedId(UUID.randomUUID()),
                "jdo1111",
                productId,
                ""
        );

        when(eventRepository.getNextOutgoings()).thenReturn(List.of(event));
        when(productRepository.productById(productId)).thenReturn(Optional.of(product));

        //when
        eventSender.sendDigitalPurchase();

        verify(eventRepository, times(1)).getNextOutgoings();
        verify(jedis, times(1)).lpush(anyString(), contains(event.getUsername()));
    }

    @Test
    void when_no_event_in_repo_then_no_op() {
        //given
        Jedis jedis = mock(Jedis.class);
        reset(jedisPool, eventRepository);
        when(jedisPool.getResource()).thenReturn(jedis);

        when(eventRepository.getNextOutgoings()).thenReturn(new LinkedList<>());

        //when
        eventSender.sendDigitalPurchase();

        //then
        verify(eventRepository, times(1)).getNextOutgoings();
        verify(jedis, never()).lpush(anyString(), anyString());
    }
}
