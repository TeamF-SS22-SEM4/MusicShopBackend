package at.fhv.ss22.ea.f.musicshop.backend.application.impl;

import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    @Override
    public Optional<CustomerDTO> customerById(UUID uuid) {
        // TODO: pass to Server 2 by RMI
        return Optional.empty();
    }

    @Override
    public List<CustomerDTO> customerListByIds(List<UUID> uuidList) {
        // TODO: pass to Server 2 by RMI
        return null;
    }
}
