package org.walter.server;

import io.grpc.stub.StreamObserver;
import org.walter.game.Die;
import org.walter.game.GameServiceGrpc.GameServiceImplBase;
import org.walter.game.GameState;
import org.walter.game.Player;
import org.walter.server.request.DieStreamingRequest;

public class GameService extends GameServiceImplBase {

    @Override
    public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {
        Player client = Player.newBuilder().setName("Client").setPosition(0).build();
        Player server = Player.newBuilder().setName("Server").setPosition(0).build();
        return new DieStreamingRequest(client, server, responseObserver);
    }
}
