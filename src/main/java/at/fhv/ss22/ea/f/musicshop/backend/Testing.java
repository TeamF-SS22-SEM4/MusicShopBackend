package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.communication.dto.SoundCarrierOrderDTO;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Testing {
    //TODO remove this

    public static void main(String[] args) throws Exception {
        // Sending test message to jms provider
        // Create a Connection
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        TopicConnection connection = connectionFactory.createTopicConnection();
        connection.setClientID("orderTestingClient");
        connection.start();

        // Create a Session
        TopicSession session = connection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);

        Topic topic = session.createTopic("Orders");

        TopicSubscriber consumer = session.createDurableSubscriber(topic, "TEST");

        System.out.println("setting listener");
        consumer.setMessageListener(msg -> {
            try {
                ObjectMessage objectMessage = (ObjectMessage) msg;
                SoundCarrierOrderDTO orderDTO = (SoundCarrierOrderDTO) objectMessage.getObject();

                System.out.println("received order for " + orderDTO.getAmount() + " of carrier.id " + orderDTO.getSoundCarrierId());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
        System.out.println("set listener");
    }
}
