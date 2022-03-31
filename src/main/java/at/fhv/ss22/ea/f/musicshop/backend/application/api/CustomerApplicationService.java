package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerApplicationService {
    Optional<CustomerDTO> customerById(UUID uuid);

    List<CustomerDTO> customerListByIds(List<UUID> uuidList);
}
