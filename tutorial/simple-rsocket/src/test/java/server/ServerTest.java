package server;


import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ServerTest {
    private static RSocket rSocket;

    @BeforeAll
    public static void setUpClient() {
        Server.start();
        rSocket = RSocketConnector.connectWith(TcpClientTransport.create(6565))
                .block();
    }

    private Flux<Payload> getRequestPayload() {
        return Flux.just("hi", "hello", "how", "are", "you")
                .delayElements(Duration.ofSeconds(1))
                .map(DefaultPayload::create);
    }

    @Test
    public void fireAndForget() {
        this.getRequestPayload()
                .flatMap(payload -> rSocket.fireAndForget(payload))
                .blockLast(Duration.ofMinutes(1));
    }

    @Test
    public void requestResponse() {
        this.getRequestPayload()
                .flatMap(payload -> rSocket.requestResponse(payload))
                .doOnNext(res -> System.out.println("Response from server :: " + res.getDataUtf8()))
                .blockLast(Duration.ofMinutes(1));
    }

    @Test
    public void requestStream() {
        this.getRequestPayload()
                .flatMap(payload -> rSocket.requestStream(payload))
                .doOnNext(res -> System.out.println("Response from server :: " + res.getDataUtf8()))
                .blockLast(Duration.ofMinutes(1));
    }
}
