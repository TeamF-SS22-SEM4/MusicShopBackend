package at.fhv.ss22.ea.f.musicshop.backend;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Testing {
    //TODO remove this

    public static void main(String[] args) throws Exception {
        // Sending test message to jms provider
        // Create a Connection
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        TopicConnection connection = connectionFactory.createTopicConnection();
        connection.setClientID("TODO");
        connection.start();

        // Create a Session
        TopicSession session = connection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);

        Topic topic = session.createTopic("Orders");

        MessageConsumer consumer = session.createDurableSubscriber(topic, "TEST");

        consumer.setMessageListener((msg) -> {
            try {
                System.out.println(((TextMessage) msg).getText());
                msg.acknowledge();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }
}
