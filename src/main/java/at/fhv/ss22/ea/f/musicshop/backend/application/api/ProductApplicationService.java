package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductApplicationService {

    @RequiresRole(role = UserRole.EMPLOYEE)
    Optional<ProductDetailsDTO> productById(String sessionId, UUID productId) throws SessionExpired, NoPermissionForOperation;

    @RequiresRole(role = UserRole.EMPLOYEE)
    List<ProductOverviewDTO> search(String sessionId, String queryString) throws SessionExpired, NoPermissionForOperation;

}
