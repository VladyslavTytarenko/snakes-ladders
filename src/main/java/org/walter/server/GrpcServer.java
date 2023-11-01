package org.walter.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

    private static final int PORT = 6565;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(PORT)
                .addService(new GameService())
                .build();
        server.start();
        server.awaitTermination();
    }
}
