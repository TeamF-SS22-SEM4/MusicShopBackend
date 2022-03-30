package at.fhv.ss22.ea.f.musicshop.backend.communication;

import at.fhv.ss22.ea.f.communication.api.BuyingService;
import at.fhv.ss22.ea.f.communication.dto.ShoppingCartProductDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.BuyingApplicationService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public UUID buyWithShoppingCart(List<ShoppingCartProductDTO> cartDtos, String paymentMethod) throws CarrierNotAvailableException {
        List<SoundCarrierAmountDTO> carrierAmountDTOS = cartDtos.stream().map(cartDTO ->
            SoundCarrierAmountDTO.builder()
                    .withAmount(cartDTO.getSelectedAmount())
                    .withCarrierId(cartDTO.getSoundCarrierId())
                    .build()
        ).collect(Collectors.toList());

        return buyingApplicationService.buy(carrierAmountDTOS, paymentMethod);
    }
}
