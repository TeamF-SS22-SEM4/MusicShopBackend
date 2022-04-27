package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.DetailedOrderDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.OrderingApplicationService;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierOrderDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;

import javax.jms.JMSException;
import java.util.Optional;

public class OrderingApplicationServiceImpl implements OrderingApplicationService {

    private JMSClient jmsClient;
    private SoundCarrierRepository soundCarrierRepository;
    private EmployeeRepository employeeRepository;
    private ProductRepository productRepository;
    private SessionRepository sessionRepository;

    public OrderingApplicationServiceImpl(JMSClient jmsClient, SoundCarrierRepository soundCarrierRepository, EmployeeRepository employeeRepository, ProductRepository productRepository, SessionRepository sessionRepository) {
        this.jmsClient = jmsClient;
        jmsClient.createOrderSubscriber();
        this.soundCarrierRepository = soundCarrierRepository;
        this.employeeRepository = employeeRepository;
        this.productRepository = productRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public boolean placeOrder(@SessionKey String sessionId, SoundCarrierOrderDTO orderDTO) {
        try {
            SoundCarrier carrier = soundCarrierRepository.soundCarrierById(new SoundCarrierId(orderDTO.getSoundCarrierId())).orElseThrow(IllegalStateException::new);
            Product product = productRepository.productById(carrier.getProductId()).orElseThrow(IllegalStateException::new);
            Session session = sessionRepository.sessionById(new SessionId(sessionId)).orElseThrow(IllegalAccessError::new);
            Employee employee = employeeRepository.employeeById(session.getEmployeeId()).orElseThrow(IllegalStateException::new);

            DetailedOrderDTO detailedOrder = DetailedOrderDTO.builder()
                    .withCarrierId(orderDTO.getSoundCarrierId())
                    .withOrderId(orderDTO.getOrderId())
                    .withAmount(orderDTO.getAmount())
                    .withEmployeeName(employee.getUsername())
                    .withCarrierType(carrier.getType().getFriendlyName())
                    .withProductName(product.getName())
                    .build();

            jmsClient.publishOrder(detailedOrder);
        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    @RequiresRole(UserRole.OPERATOR)
    public boolean approveOrder(@SessionKey String sessionId, SoundCarrierOrderDTO orderDTO) {
        Optional<SoundCarrier> carrierOpt = soundCarrierRepository.soundCarrierById(new SoundCarrierId(orderDTO.getSoundCarrierId()));
        if (carrierOpt.isEmpty()) {
            return false;
        }
        SoundCarrier carrier = carrierOpt.get();
        EntityManagerUtil.beginTransaction();
        carrier.approvedOrder(orderDTO);
        EntityManagerUtil.commit();
        return true;
    }

    @Override
    @RequiresRole(UserRole.OPERATOR)
    public boolean denyOrder(@SessionKey String sessionId, SoundCarrierOrderDTO orderDTO) {
        //TODO figure out how this can be done
        return false;
    }
}
