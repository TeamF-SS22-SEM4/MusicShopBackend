package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;

import javax.ejb.Local;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Local
public interface ProductApplicationService {

    ProductDetailsDTO productById(UUID productId) throws NoSuchElementException;

    List<ProductOverviewDTO> search(String queryString, int pageNumber);

}
