package at.fhv.ss22.ea.f.musicshop.backend.communication.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSClient {
    private static JMSClient jmsClient;
    private final String PROTOCOL = "tcp://";
    private final String HOST = "jms-provider";
    private final String PORT = "61616";
    private final ActiveMQConnectionFactory connectionFactory;

    private JMSClient() {
        connectionFactory = new ActiveMQConnectionFactory(PROTOCOL + HOST + ":" + PORT);
    }

    public static JMSClient getJmsClient() {
        if(jmsClient == null) {
            jmsClient = new JMSClient();
        }

        return jmsClient;
    }

    public void publishMessage(String topic, String message) throws JMSException {
        // Sending test message to jms provider
        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        // Create the destination (Topic)
        // TODO: Make it working with createTopic
        Destination destination = session.createQueue(topic);

        // Create a MessageProducer from the Session to the Topic or Queue
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        // Create a message
        TextMessage textMessage = session.createTextMessage(message);

        // Tell the producer to send the message
        System.out.println("Sent message: " + message + " to topic " + topic);
        producer.send(textMessage);

        // Clean up
        session.close();
        connection.close();
    }
}
