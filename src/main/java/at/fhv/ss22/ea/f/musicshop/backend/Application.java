package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.communication.dto.ProductOverviewDTO;
import at.fhv.ss22.ea.f.musicshop.backend.communication.RMIServer;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        System.out.println("Starting Server....");
        RMIServer.startRMIServer();

        List<ProductOverviewDTO> productOverviewDTOList = InstanceProvider.getProductApplicationService().search("Pink Floyd");
        System.out.println(productOverviewDTOList.size());
    }
}
