package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.OrderingApplicationService;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierOrderDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;

import javax.jms.JMSException;
import java.util.Optional;

public class OrderingApplicationServiceImpl implements OrderingApplicationService {

    private JMSClient jmsClient;
    private SoundCarrierRepository soundCarrierRepository;

    public OrderingApplicationServiceImpl(JMSClient jmsClient, SoundCarrierRepository soundCarrierRepository) {
        this.jmsClient = jmsClient;
        jmsClient.createOrderSubscriber();
        this.soundCarrierRepository = soundCarrierRepository;
    }

    @Override
    @RequiresRole(UserRole.EMPLOYEE)
    public boolean placeOrder(@SessionKey String sessionId, SoundCarrierOrderDTO orderDTO) {
        try {
            jmsClient.publishOrder(orderDTO);
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
