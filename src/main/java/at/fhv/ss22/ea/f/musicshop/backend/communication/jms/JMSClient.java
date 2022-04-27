package at.fhv.ss22.ea.f.musicshop.backend.communication.jms;

import at.fhv.ss22.ea.f.communication.dto.DetailedOrderDTO;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.*;

public class JMSClient {
    private static final Logger logger = LogManager.getLogger(JMSClient.class);

    private static final String ORDER_TOPIC_NAME = "Orders";
    private static final String ORDER_CLIENT_CONNECTION_ID = "system_ordering_client";
    private static final String ORDER_CLIENT_NAME = "ordering_client";
    private static final String PROTOCOL = "tcp://";
    private static final String HOST = "jms-provider";
    private static final String PORT = "61616";
    private TopicSession session;
    private TopicConnection connection;
    private ActiveMQConnectionFactory connectionFactory;

    public JMSClient() {
        this.reconnect();
    }
    public void createOrderSubscriber() {
        try {
            TopicConnection orderConnection = connectionFactory.createTopicConnection();
            this.connectionFactory.setTrustAllPackages(true);
            orderConnection.setClientID(ORDER_CLIENT_CONNECTION_ID);
            orderConnection.start();
            TopicSession orderSession = orderConnection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);
            Topic topic = orderSession.createTopic(ORDER_TOPIC_NAME);
            TopicSubscriber subscriber = orderSession.createDurableSubscriber(topic, ORDER_CLIENT_NAME);
            subscriber.close();
            orderSession.close();
            orderConnection.stop();
            orderConnection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        this.close();
        try {
            this.connectionFactory = new ActiveMQConnectionFactory(PROTOCOL + HOST + ":" + PORT);
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

        logger.info("Sent message: {} to topic {}", message, topic);
        producer.send(textMessage);
    }
    public void publishOrder(DetailedOrderDTO orderDTO) throws JMSException {
        Destination destination = session.createTopic(ORDER_TOPIC_NAME);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        Message order = session.createObjectMessage(orderDTO);
        producer.send(order);
    }
}
