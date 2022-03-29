package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.BuyingService;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.BuyingApplicationService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class BuyingServiceImpl extends UnicastRemoteObject implements BuyingService {

     private BuyingApplicationService buyingApplicationService;

    public BuyingServiceImpl(BuyingApplicationService buyingApplicationService) throws RemoteException {
        super(RMIServer.getPort());
        this.buyingApplicationService = buyingApplicationService;
    }

    @Override
    public UUID buy(List<SoundCarrierAmountDTO> soundCarriers, String paymentMethod) throws CarrierNotAvailableException {
        return buyingApplicationService.buy(soundCarriers, paymentMethod);
    }
}
