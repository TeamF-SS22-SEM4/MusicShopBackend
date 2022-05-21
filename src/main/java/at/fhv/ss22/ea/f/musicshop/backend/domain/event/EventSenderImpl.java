package at.fhv.ss22.ea.f.musicshop.backend.domain.event;

import at.fhv.ss22.ea.f.musicshop.backend.Application;
import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EventRepository;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import java.util.Optional;

@Local(EventSender.class)
@Stateless
public class EventSenderImpl implements EventSender {
    private static final Logger logger = LogManager.getLogger(EventSenderImpl.class);

    @EJB
    EventRepository eventRepository;

    public EventSenderImpl() {
    }
    public EventSenderImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Schedule(hour = "*", minute = "*", second = "*/5", info = "Send event timer")
    public void sendDigitalPurchase() {
        Optional< DigitalProductPurchased> opt =  this.eventRepository.getNextOutgoing();
        if (opt.isPresent()) {
            DigitalProductPurchased event = opt.get();
            Gson gson = new Gson();
            logger.warn("this is a DEBUG log. Sending to queue {}", gson.toJson(event));
            System.out.println(gson.toJson(event));
            //TODO create json
            //TODO place in queue provider (e.g. redis)

        }
    }
}
