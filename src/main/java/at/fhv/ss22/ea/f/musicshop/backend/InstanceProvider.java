package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.communication.api.BuyingService;
import at.fhv.ss22.ea.f.communication.api.ProductSearchService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.BuyingApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.ProductApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.BuyingApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant.BuyingServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant.ProductSearchServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.*;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.*;

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
    private static BuyingApplicationService buyingApplicationService;

    private static BuyingService testingBuyingService;
    private static ProductSearchService testingProductSearchService;
    private static ProductApplicationService testingProductApplicationService;
    private static BuyingApplicationService testingBuyingApplicationService;

    private static BuyingApplicationService mockedBuyingApplicationService;
    private static ProductApplicationService mockedProductApplicationService;
    private static ArtistRepository mockedArtistRepository;
    private static EmployeeRepository mockedEmployeeRepository;
    private static ProductRepository mockedProductRepository;
    private static SaleRepository mockedSaleRepository;
    private static SoundCarrierRepository mockedSoundCarrierRepository;

    public static ProductApplicationService getProductApplicationService() {
        if (null == productApplicationService) {
            productApplicationService = new ProductApplicationServiceImpl(getProductRepository(), getArtistRepository(), getSoundCarrierRepository());
        }
        return productApplicationService;
    }

    public static BuyingApplicationService getSoundCarrierApplicationService() {
        if (null == buyingApplicationService) {
            buyingApplicationService = new BuyingApplicationServiceImpl(getSoundCarrierRepository(), getSaleRepository());
        }
        return buyingApplicationService;
    }

    public static BuyingApplicationService getTestingSoundCarrierApplicationService() {
        if (null == testingBuyingApplicationService) {
            testingBuyingApplicationService = new BuyingApplicationServiceImpl(getMockedSoundCarrierRepository(), getMockedSaleRepository());
        }
        return testingBuyingApplicationService;
    }

    public static BuyingApplicationService getMockedSoundCarrierApplicationService() {
        if (null == mockedBuyingApplicationService) {
            mockedBuyingApplicationService = mock(BuyingApplicationService.class);
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
            return new BuyingServiceImpl(getBuyingApplicationService());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BuyingApplicationService getBuyingApplicationService() {
        if (null == buyingApplicationService) {
            buyingApplicationService = new BuyingApplicationServiceImpl(getSoundCarrierRepository(), getSaleRepository());
        }
        return buyingApplicationService;
    }

    public static BuyingApplicationService getTestingBuyingApplicationService() {
        if (null == testingBuyingApplicationService) {
            testingBuyingApplicationService = new BuyingApplicationServiceImpl(getMockedSoundCarrierRepository(), getMockedSaleRepository());
        }
        return testingBuyingApplicationService;
    }

    public static BuyingApplicationService getMockedBuyingApplicationService() {
        if (null == mockedBuyingApplicationService) {
            mockedBuyingApplicationService = mock(BuyingApplicationService.class);
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
