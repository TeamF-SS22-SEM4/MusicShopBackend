package at.fhv.ss22.ea.f.musicshop.backend.communication.jms;

import at.fhv.ss22.ea.f.communication.dto.DetailedOrderDTO;

import javax.jms.JMSException;

public interface JMSClient {
    void createOrderSubscriber();

    void reconnect();

    void close();

    void publishMessage(String topic, String message) throws JMSException;

    void publishOrder(DetailedOrderDTO orderDTO) throws JMSException;
}
