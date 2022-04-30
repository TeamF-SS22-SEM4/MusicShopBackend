package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.OrderingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.OrderingApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderingApplicationTests {

    private OrderingApplicationService orderingService;

    private JMSClient jmsClient = mock(JMSClient.class);
    private SoundCarrierRepository carrierRepository = mock(SoundCarrierRepository.class);
    private EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
    private ProductRepository productRepository = mock(ProductRepository.class);
    private SessionRepository sessionRepository = mock(SessionRepository.class);

    @BeforeAll
    void setup() {
        this.orderingService = new OrderingApplicationServiceImpl(jmsClient, carrierRepository, employeeRepository, productRepository, sessionRepository);
    }


}
