package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;

import java.util.List;

public interface ProductApplicationService {

    List<ProductOverviewDTO> search(String queryString);

}
