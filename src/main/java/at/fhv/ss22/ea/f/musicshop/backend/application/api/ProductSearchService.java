package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import java.util.List;

public interface ProductSearchService {

    List<ProductOverviewDTO> fullTextSearch(String query);
}
