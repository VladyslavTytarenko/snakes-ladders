package org.walter.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.walter.client.resoponse.GameStateStreamingResponse;
import org.walter.game.Die;
import org.walter.game.GameServiceGrpc.GameServiceStub;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.walter.game.GameServiceGrpc.newStub;

@TestInstance(PER_CLASS)
class GameClientTest {

    private static final String DOMAIN = "localhost";
    private static final int PORT = 6565;
    private GameServiceStub stub;

    @BeforeAll
    public void setup() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(DOMAIN, PORT)
                .usePlaintext()
                .build();
        stub = newStub(channel);
    }

    @Test
    void clientGame() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        GameStateStreamingResponse response = new GameStateStreamingResponse(latch);
        StreamObserver<Die> dieStreamObserver = stub.roll(response);

        response.setDieStreamObserver(dieStreamObserver);
        response.roll();
        latch.await();
    }
}
