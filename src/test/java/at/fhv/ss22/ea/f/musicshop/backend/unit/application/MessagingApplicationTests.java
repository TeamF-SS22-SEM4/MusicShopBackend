package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.MessageDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.MessagingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.Employee;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.employee.EmployeeId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EmployeeRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.jms.JMSException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessagingApplicationTests {

    private MessagingApplicationService messagingService = InstanceProvider.getTestingMessagingApplicationService();
    private EmployeeRepository employeeRepository = InstanceProvider.getMockedEmployeeRepository();
    private SessionRepository sessionRepository = InstanceProvider.getMockedSessionRepository();
    private JMSClient jmsClient = InstanceProvider.getMockedJMSClient();

    @Test
    void publish_message() throws Exception {
        //given
        MessageDTO message = MessageDTO.builder()
                .withTopicName("testing topic")
                .withTitle("testing Message")
                .withContent("testing Content")
                .build();
        doNothing().when(jmsClient).publishMessage(anyString(), anyString());

        assertTrue(messagingService.publish("placeholder", message));

        verify(jmsClient, atLeastOnce()).publishMessage(anyString(), any());
    }
    @Test
    void when_sending_message_fails_then_return_false() throws JMSException, SessionExpired, NoPermissionForOperation {
        //given
        MessageDTO message = MessageDTO.builder()
                .withTopicName("testing topic")
                .withTitle("testing Message")
                .withContent("testing Content")
                .build();
        doThrow(new JMSException("")).when(jmsClient).publishMessage(anyString(), anyString());

        assertFalse(messagingService.publish("placeholder", message));

        verify(jmsClient, atLeastOnce()).publishMessage(anyString(), any());
    }

}
