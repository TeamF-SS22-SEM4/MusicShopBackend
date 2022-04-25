package at.fhv.ss22.ea.f.musicshop.backend.communication.jms;

import at.fhv.ss22.ea.f.communication.dto.SoundCarrierOrderDTO;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSClient {
    private static final String ORDER_TOPIC_NAME = "Orders";
    private static final String PROTOCOL = "tcp://";
    private static final String HOST = "jms-provider";
    private static final String PORT = "61616";
    private TopicSession session;
    private TopicConnection connection;

    public JMSClient() {
        this.reconnect();
    }
    public void reconnect() {
        this.close();
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(PROTOCOL + HOST + ":" + PORT);
            connection = connectionFactory.createTopicConnection();
            connection.start();
            session = connection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    public void close() {
        try {
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.stop();
                connection.close();
            }
        } catch (JMSException e) {
            e.getLinkedException();
        }
    }

    public void publishMessage(String topic, String message) throws JMSException {
        Destination destination = session.createTopic(topic);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        TextMessage textMessage = session.createTextMessage(message);

        System.out.println("Sent message: " + message + " to topic " + topic);
        producer.send(textMessage);
    }
    public void publishOrder(SoundCarrierOrderDTO orderDTO) throws JMSException {
        Destination destination = session.createTopic(ORDER_TOPIC_NAME);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        Message order = session.createObjectMessage(orderDTO);
        producer.send(order);
    }
}
