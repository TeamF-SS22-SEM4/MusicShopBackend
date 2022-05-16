package at.fhv.ss22.ea.f.musicshop.backend.communication.rest.client;

import io.netty.handler.codec.http.HttpMethod;
import reactor.netty.http.client.HttpClient;

import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(RestClient.class)
public class RestClientImpl implements RestClient {
    private final HttpClient httpClient = HttpClient.create();

    @Override
    public void sendPurchaseToPlaylistMicroservice() {
        // TODO: Implement
        // TODO: Fix error: Connection refused: playlist-microservice/172.22.0.5:8081
        // Dummy implementation
        httpClient.baseUrl("http://playlist-microservice:8081")
                .request(HttpMethod.POST)
                .uri("/purchase")
                .responseContent()
                .asString()
                .subscribe(System.out::println);
    }
}
