package at.fhv.ss22.ea.f.musicshop.backend.communication.rest.client;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;


@Stateless
@Local(RestClient.class)
public class RestClientImpl implements RestClient {
    Client client = ClientBuilder.newClient();

    @Override
    public void sendPurchaseToPlaylistMicroservice() {
        // TODO: Implement
        // Dummy implementation
        WebTarget target = client.target("http://playlist-microservice:8081").path("purchase");
        target.request(MediaType.TEXT_PLAIN).post(
                Entity.entity("hello", MediaType.TEXT_PLAIN), String.class
        );
    }
}
