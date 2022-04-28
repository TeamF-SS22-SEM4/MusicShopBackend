package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.OrderingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderingApplicationTests {

    private OrderingApplicationService orderingService = InstanceProvider.getTestingOrderingApplicationService();

    private JMSClient jmsClient = InstanceProvider.getMockedJMSClient();

    private SoundCarrierRepository carrierRepository = InstanceProvider.getMockedSoundCarrierRepository();


}
