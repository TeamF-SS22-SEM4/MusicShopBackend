package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.MessageDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.MessagingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.MessagingApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.communication.authentication.LdapClient;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.UserRepository;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.jms.JMSException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessagingApplicationTests {


    private MessagingApplicationService messagingApplicationService;

    private AuthenticationApplicationService authenticationService = mock(AuthenticationApplicationService.class);

    private SessionRepository sessionRepository = mock(SessionRepository.class);

    private JMSClient jmsClient = mock(JMSClient.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private JMSClient jmsClient = InstanceProvider.getMockedJMSClient();

    @BeforeAll
    void setup() throws SessionExpired {
        messagingApplicationService = new MessagingApplicationServiceImpl(jmsClient, userRepository, sessionRepository);
        when(authenticationService.hasRole(any(), any())).thenReturn(true);
    }

    @Test
    void when_publish_message_ok_then_true() throws Exception {
        //given
        MessageDTO messageDTO = MessageDTO.builder()
                        .withEmployeeUsername("mustermann")
                        .withContent("some message")
                        .withTitle("very Important")
                        .withTopicName("test")
                        .build();
        doNothing().when(jmsClient).publishMessage(anyString(), anyString());

        //when
        boolean success = messagingApplicationService.publish("placeholder", messageDTO);

        //then
        assertTrue(success);
    }

    @Test
    void when_publish_message_fails_then_false() throws Exception {
        //given
        MessageDTO messageDTO = MessageDTO.builder()
                .withEmployeeUsername("mustermann")
                .withContent("some message")
                .withTitle("very Important")
                .withTopicName("test")
                .build();
        doThrow(new JMSException("")).when(jmsClient).publishMessage(anyString(), anyString());

        //when
        boolean success = messagingApplicationService.publish("placeholder", messageDTO);

        //then
        assertFalse(success);
    }

    @Test
    void get_subscribed_topics() throws Exception {
        //given
        Employee employee = Employee.create(
                new EmployeeId(UUID.randomUUID()),
                "test-user",
                "max",
                "mustermann",
                List.of(UserRole.EMPLOYEE),
                List.of()
        );
        Session session = Session.newForEmployee(employee.getEmployeeId());
        employee.subscribeTo("testTopicA");
        employee.subscribeTo("testTopicB");
        when(sessionRepository.sessionById(session.getSessionId())).thenReturn(Optional.of(session));
        when(employeeRepository.employeeById(employee.getEmployeeId())).thenReturn(Optional.of(employee));

        //when
        List<String> topicsAct = messagingApplicationService.getSubscribedTopics(session.getSessionId().getValue());

        //then
        assertTrue(topicsAct.contains("testTopicA"));
        assertTrue(topicsAct.contains("testTopicB"));
        assertFalse(topicsAct.contains("testTopicC"));
    }

    @Test
    void update_last_viewed() throws SessionExpired, NoPermissionForOperation {
        // given
        LocalDateTime lastViewedExpected = LocalDateTime.now();
        UUID employeeIdUUID = UUID.randomUUID();
        UserId userIdExpected = new UserId(employeeIdUUID);
        String usernameExpected = "john42";
        String firstnameExpected = "John";
        String lastNameExpected = "Doe";

        User user = User.create(
                userIdExpected,
                usernameExpected,
                firstnameExpected,
                lastNameExpected,
                List.of(UserRole.EMPLOYEE),
                Collections.emptyList()
        );

        when(sessionRepository.sessionById(any())).thenReturn(Optional.of(Session.newForUser(userIdExpected)));
        when(userRepository.userById(userIdExpected)).thenReturn(Optional.of(user));

        // when
        messagingApplicationService.updateLastViewed("some", lastViewedExpected);
        LocalDateTime lastViewedActual = user.getLastViewed();

        // then
        assertEquals(lastViewedExpected, lastViewedActual);
    }
}
