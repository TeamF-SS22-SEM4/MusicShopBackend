package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductApplicationService {

    Optional<ProductDetailsDTO> productById(UUID productId);

    List<ProductOverviewDTO> search(String queryString);

}
