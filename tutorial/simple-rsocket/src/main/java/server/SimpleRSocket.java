package server;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SimpleRSocket implements RSocket {

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        String value = payload.getDataUtf8();
        System.out.println("Received :: " + value);
        return Mono.empty();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        String value = payload.getDataUtf8();
        return Mono.just(DefaultPayload.create(value.toUpperCase()));
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        String value = payload.getDataUtf8();
        return Flux.fromStream(value.chars().mapToObj(i -> (char) i))
                .map(Object::toString)
                .map(DefaultPayload::create);
    }


}
