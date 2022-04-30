package at.fhv.ss22.ea.f.musicshop.backend.communication.ejb;

import at.fhv.ss22.ea.f.communication.api.BuyingService;
import at.fhv.ss22.ea.f.communication.dto.ShoppingCartProductDTO;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Remote(BuyingService.class)
@Stateless
public class BuyingServiceImpl implements BuyingService {

    @EJB
    private SaleApplicationService buyingApplicationService;

    public String buy(String sessionId, List<SoundCarrierAmountDTO> soundCarriers, String paymentMethod, UUID customerId) throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation {
        return buyingApplicationService.buy(sessionId, soundCarriers, paymentMethod, customerId);
    }

    @Override
    public String buyWithShoppingCart(String sessionId, List<ShoppingCartProductDTO> cartDtos, String paymentMethod, UUID customerId) throws CarrierNotAvailableException, SessionExpired, NoPermissionForOperation {
        List<SoundCarrierAmountDTO> carrierAmountDTOS = cartDtos.stream().map(cartDTO ->
                SoundCarrierAmountDTO.builder()
                        .withAmount(cartDTO.getSelectedAmount())
                        .withCarrierId(cartDTO.getSoundCarrierId())
                        .build()
        ).collect(Collectors.toList());

        return buyingApplicationService.buy(sessionId, carrierAmountDTOS, paymentMethod, customerId);
    }
}
