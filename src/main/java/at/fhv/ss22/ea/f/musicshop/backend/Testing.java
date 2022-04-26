package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.communication.dto.SoundCarrierOrderDTO;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Testing {
    //TODO remove this
    private static final String ORDER_TOPIC_NAME = "Orders";
    private static final String ORDER_CLIENT_CONNECTION_ID = "system_ordering_client";
    private static final String ORDER_CLIENT_NAME = "ordering_client";

    public static void main(String[] args) throws Exception {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            TopicConnection orderConnection = connectionFactory.createTopicConnection();
            orderConnection.setClientID(ORDER_CLIENT_CONNECTION_ID);
            orderConnection.start();
            TopicSession orderSession = orderConnection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);
            Topic topic = orderSession.createTopic(ORDER_TOPIC_NAME);
            TopicSubscriber subscriber = orderSession.createDurableSubscriber(topic, ORDER_CLIENT_NAME);
            subscriber.setMessageListener(msg -> System.out.println(msg));
//            orderSession.close();
//            orderConnection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
