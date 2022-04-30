package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;

import javax.ejb.Local;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Local
public interface ProductApplicationService {

    Optional<ProductDetailsDTO> productById(String sessionId, UUID productId) throws SessionExpired, NoPermissionForOperation;

    List<ProductOverviewDTO> search(String sessionId, String queryString) throws SessionExpired, NoPermissionForOperation;

}
