package at.fhv.ss22.ea.f.musicshop.backend.communication.ejb;

import at.fhv.ss22.ea.f.communication.api.ProductSearchService;
import at.fhv.ss22.ea.f.communication.dto.ProductDetailsDTO;
import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.ProductApplicationService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.List;
import java.util.UUID;

@Remote(ProductSearchService.class)
@Stateless
public class ProductSearchServiceImpl implements ProductSearchService {
    @EJB
    private ProductApplicationService productApplicationService;

    @Override
    public ProductDetailsDTO productById(UUID productId) {
        return productApplicationService.productById(productId);
    }

    @Override
    public List<ProductOverviewDTO> fullTextSearch(String query) {
        // pass 0 as pageNumber because 0 returns all products and fx client has no pagination
        return productApplicationService.search(query, 0);
    }
}
