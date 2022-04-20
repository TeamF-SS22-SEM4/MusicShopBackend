package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.communication.api.*;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.*;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.*;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.AuthenticationApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.CustomerApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.ProductApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.SaleApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RoleCheckInvocationHandler;
import at.fhv.ss22.ea.f.musicshop.backend.communication.authentication.LdapClient;
import at.fhv.ss22.ea.f.musicshop.backend.communication.internal.CustomerRMIClient;
import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant.*;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.*;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.*;

import java.lang.reflect.Proxy;
import java.rmi.RemoteException;

import static org.mockito.Mockito.mock;

public class InstanceProvider {
    private InstanceProvider() {
    }

    private static ProductApplicationService productApplicationService;
    private static ArtistRepository artistRepository;
    private static EmployeeRepository employeeRepository;
    private static ProductRepository productRepository;
    private static SaleRepository saleRepository;
    private static SoundCarrierRepository soundCarrierRepository;
    private static SaleApplicationService saleApplicationService;
    private static AuthenticationApplicationService authenticationApplicationService;
    private static SessionRepository sessionRepository;
    private static LdapClient ldapClient;
    private static AuthenticationService authenticationService;
    private static CustomerApplicationService customerApplicationService;
    private static CustomerRMIClient customerRMIClient;
    private static JMSClient jmsClient;
    private static MessagingApplicationService messagingApplicationService;

    private static MessagingApplicationService testingMessagingApplicationService;
    private static CustomerApplicationService testingCustomerApplicationService;
    private static ProductSearchService testingProductSearchService;
    private static ProductApplicationService testingProductApplicationService;
    private static SaleApplicationService testingBuyingApplicationService;
    private static AuthenticationApplicationService testingAuthenticationApplicationService;

    private static JMSClient mockedJMSClient;
    private static AuthenticationApplicationService mockedAuthenticationApplicationService;
    private static SessionRepository mockedSessionRepository;
    private static SaleApplicationService mockedBuyingApplicationService;
    private static ProductApplicationService mockedProductApplicationService;
    private static ArtistRepository mockedArtistRepository;
    private static EmployeeRepository mockedEmployeeRepository;
    private static ProductRepository mockedProductRepository;
    private static SaleRepository mockedSaleRepository;
    private static SoundCarrierRepository mockedSoundCarrierRepository;
    private static LdapClient mockedLdapClient;
    private static CustomerRMIClient mockedCustomerRmiClient;

    public static CustomerRMIClient getCustomerRMIClient() {
        if (null == customerRMIClient) {
            customerRMIClient = new CustomerRMIClient();
        }
        return customerRMIClient;
    }

    public static JMSClient getJmsClient() {
        if (null == jmsClient) {
            jmsClient = new JMSClient();
        }
        return jmsClient;
    }

    public static MessagingApplicationService getMessagingApplicationService() {
        if (null == messagingApplicationService) {
            messagingApplicationService = new MessagingApplicationServiceImpl(getJmsClient(), getEmployeeRepository(), getSessionRepository());
        }
        return messagingApplicationService;
    }

    public static MessagingApplicationService getTestingMessagingApplicationService() {
        if (null == testingMessagingApplicationService) {
            testingMessagingApplicationService = new MessagingApplicationServiceImpl(getMockedJMSClient(), getMockedEmployeeRepository(), getMockedSessionRepository());
        }
        return testingMessagingApplicationService;
    }

    public static JMSClient getMockedJMSClient() {
        if (null == mockedJMSClient) {
            mockedJMSClient = mock(JMSClient.class);
        }
        return mockedJMSClient;
    }

    public static CustomerRMIClient getMockedCustomerRmiClient() {
        if (null == mockedCustomerRmiClient) {
            mockedCustomerRmiClient = mock(CustomerRMIClient.class);
        }
        return mockedCustomerRmiClient;
    }

    public static CustomerApplicationService getCustomerApplicationService() {
        if (null == customerApplicationService) {
            CustomerApplicationService service = new CustomerApplicationServiceImpl(getCustomerRMIClient());
            customerApplicationService = (CustomerApplicationService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                    service.getClass().getInterfaces(),
                    new RoleCheckInvocationHandler(service, getAuthenticationApplicationService()));
        }
        return customerApplicationService;
    }

    public static CustomerApplicationService getTestingCustomerApplicationService() {
        if (null == testingCustomerApplicationService) {
            testingCustomerApplicationService = new CustomerApplicationServiceImpl(getMockedCustomerRmiClient());
        }
        return testingCustomerApplicationService;
    }

    public static AuthenticationApplicationService getMockedAuthenticationApplicationService() {
        if (null == mockedAuthenticationApplicationService) {
            mockedAuthenticationApplicationService = mock(AuthenticationApplicationService.class);
        }
        return mockedAuthenticationApplicationService;
    }

    public static AuthenticationService getAuthenticationService() {
        if (null == authenticationService) {
            try {
                authenticationService = new AuthenticationServiceImpl(getAuthenticationApplicationService());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return authenticationService;
    }

    public static SessionRepository getSessionRepository() {
        if (null == sessionRepository) {
            sessionRepository = new HibernateSessionRepository();
        }
        return sessionRepository;
    }

    public static SessionRepository getMockedSessionRepository() {
        if (null == mockedSessionRepository) {
            mockedSessionRepository = mock(SessionRepository.class);
        }
        return mockedSessionRepository;
    }

    public static LdapClient getLdapClient() {
        if (null == ldapClient) {
            ldapClient = new LdapClient();
        }
        return ldapClient;
    }

    public static LdapClient getMockedLdapClient() {
        if (null == mockedLdapClient) {
            mockedLdapClient = mock(LdapClient.class);
        }
        return mockedLdapClient;
    }

    public static ProductApplicationService getProductApplicationService() {
        if (null == productApplicationService) {
            ProductApplicationService service = new ProductApplicationServiceImpl(getProductRepository(), getArtistRepository(), getSoundCarrierRepository());
            productApplicationService = (ProductApplicationService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                    service.getClass().getInterfaces(),
                    new RoleCheckInvocationHandler(service, getAuthenticationApplicationService()));
        }
        return productApplicationService;
    }

    public static SaleApplicationService getSoundCarrierApplicationService() {
        if (null == saleApplicationService) {
            SaleApplicationService service = new SaleApplicationServiceImpl(getSessionRepository(), getSoundCarrierRepository(), getSaleRepository(), getProductRepository(), getArtistRepository());
            saleApplicationService = (SaleApplicationService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                    service.getClass().getInterfaces(),
                    new RoleCheckInvocationHandler(service, getAuthenticationApplicationService()));
        }
        return saleApplicationService;
    }

    public static AuthenticationApplicationService getAuthenticationApplicationService() {
        if (null == authenticationApplicationService) {
            authenticationApplicationService = new AuthenticationApplicationServiceImpl(getLdapClient(), getSessionRepository(), getEmployeeRepository());
        }
        return authenticationApplicationService;
    }

    public static AuthenticationApplicationService getTestingAuthenticationApplicationService() {
        if (null == testingAuthenticationApplicationService) {
            testingAuthenticationApplicationService = new AuthenticationApplicationServiceImpl(getMockedLdapClient(), getMockedSessionRepository(), getMockedEmployeeRepository());
        }
        return testingAuthenticationApplicationService;
    }

    public static SaleApplicationService getTestingSoundCarrierApplicationService() {
        if (null == testingBuyingApplicationService) {
            testingBuyingApplicationService = new SaleApplicationServiceImpl(getMockedSessionRepository(), getMockedSoundCarrierRepository(), getMockedSaleRepository(), getMockedProductRepository(), getMockedArtistRepository());
        }
        return testingBuyingApplicationService;
    }

    public static SaleApplicationService getMockedSoundCarrierApplicationService() {
        if (null == mockedBuyingApplicationService) {
            mockedBuyingApplicationService = mock(SaleApplicationService.class);
        }
        return mockedBuyingApplicationService;
    }

    //rmi instances are only components that currently arent singletons
    public static ProductSearchService getProductSearchService() {
        try {
            return new ProductSearchServiceImpl(getProductApplicationService());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static BuyingService getBuyingService() {
        try {
            return new BuyingServiceImpl(getSaleApplicationService());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SaleSearchService getSaleSearchService() {
        try {
            return new SaleSearchServiceImpl(getSaleApplicationService());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RefundSaleService getRefundSaleService() {
        try {
            return new RefundSaleServiceImpl(getSaleApplicationService());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MessagingService getMessagingService() {
        try {
            return new MessagingServiceServant(getMessagingApplicationService());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CustomerSearchService getCustomerSearchService() {
        try {
            return new CustomerSearchService(getCustomerApplicationService());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SaleApplicationService getSaleApplicationService() {
        if (null == saleApplicationService) {
            saleApplicationService = new SaleApplicationServiceImpl(getSessionRepository(), getSoundCarrierRepository(), getSaleRepository(), getProductRepository(), getArtistRepository());
        }
        return saleApplicationService;
    }

    public static SaleApplicationService getTestingBuyingApplicationService() {
        if (null == testingBuyingApplicationService) {
            testingBuyingApplicationService = new SaleApplicationServiceImpl(getMockedSessionRepository(), getMockedSoundCarrierRepository(), getMockedSaleRepository(), getMockedProductRepository(), getMockedArtistRepository());
        }
        return testingBuyingApplicationService;
    }

    public static SaleApplicationService getMockedBuyingApplicationService() {
        if (null == mockedBuyingApplicationService) {
            mockedBuyingApplicationService = mock(SaleApplicationServiceImpl.class);
        }
        return mockedBuyingApplicationService;
    }


    public static ProductSearchService getTestingProductSearchService() {
        if (null == testingProductSearchService) {
            try {
                testingProductSearchService = new ProductSearchServiceImpl(getMockedProductApplicationService());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return testingProductSearchService;
    }

    public static ProductApplicationService getMockedProductApplicationService() {
        if (null == mockedProductApplicationService) {
            mockedProductApplicationService = mock(ProductApplicationService.class);
        }
        return mockedProductApplicationService;
    }

    public static ArtistRepository getArtistRepository() {
        if (null == artistRepository) {
            artistRepository = new HibernateArtistRepository();
        }
        return artistRepository;
    }

    public static EmployeeRepository getEmployeeRepository() {
        if (null == employeeRepository) {
            employeeRepository = new HibernateEmployeeRepository();
        }
        return employeeRepository;
    }

    public static ProductRepository getProductRepository() {
        if (null == productRepository) {
            productRepository = new HibernateProductRepository();
        }
        return productRepository;
    }

    public static SaleRepository getSaleRepository() {
        if (null == saleRepository) {
            saleRepository = new HibernateSaleRepository();
        }
        return saleRepository;
    }

    public static SoundCarrierRepository getSoundCarrierRepository() {
        if (null == soundCarrierRepository) {
            soundCarrierRepository = new HibernateSoundCarrierRepository();
        }
        return soundCarrierRepository;
    }

    public static ProductApplicationService getTestingProductApplicationService() {
        if (null == testingProductApplicationService) {
            testingProductApplicationService = new ProductApplicationServiceImpl(
                    getMockedProductRepository(),
                    getMockedArtistRepository(),
                    getMockedSoundCarrierRepository()
            );
        }

        return testingProductApplicationService;
    }


    public static ArtistRepository getMockedArtistRepository() {
        if (null == mockedArtistRepository) {
            mockedArtistRepository = mock(ArtistRepository.class);
        }
        return mockedArtistRepository;
    }

    public static EmployeeRepository getMockedEmployeeRepository() {
        if (null == mockedEmployeeRepository) {
            mockedEmployeeRepository = mock(EmployeeRepository.class);
        }
        return mockedEmployeeRepository;
    }

    public static ProductRepository getMockedProductRepository() {
        if (null == mockedProductRepository) {
            mockedProductRepository = mock(ProductRepository.class);
        }
        return mockedProductRepository;
    }

    public static SaleRepository getMockedSaleRepository() {
        if (null == mockedSaleRepository) {
            mockedSaleRepository = mock(SaleRepository.class);
        }
        return mockedSaleRepository;
    }

    public static SoundCarrierRepository getMockedSoundCarrierRepository() {
        if (null == mockedSoundCarrierRepository) {
            mockedSoundCarrierRepository = mock(SoundCarrierRepository.class);
        }
        return mockedSoundCarrierRepository;
    }
}
