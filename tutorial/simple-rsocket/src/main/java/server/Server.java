package server;

import io.rsocket.core.RSocketServer;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.server.TcpServerTransport;
import reactor.core.Disposable;

public class Server {
    private static Disposable disposable;

    public static void start() {
        RSocketServer server = RSocketServer.create();
        server.acceptor(new SimpleRSocketAcceptor());
        server.payloadDecoder(PayloadDecoder.ZERO_COPY);
        disposable = server.bind(TcpServerTransport.create(6565))
                .subscribe();

    }

    public void stop() {
        disposable.dispose();
    }
}
