package org.walter.server.request;

import io.grpc.stub.StreamObserver;
import org.walter.game.Die;
import org.walter.game.GameState;
import org.walter.game.Player;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.walter.server.map.SnakesAndLaddersMap.getPosition;

public class DieStreamingRequest implements StreamObserver<Die> {

    private static final int ONE_HUNDRED = 100;
    private static final int ONE = 1;
    private static final int SEVEN = 7;
    private Player client;
    private Player server;
    private StreamObserver<GameState> gameStateStreamObserver;

    public DieStreamingRequest(Player client, Player server,
                               StreamObserver<GameState> gameStateStreamObserver) {
        this.client = client;
        this.server = server;
        this.gameStateStreamObserver = gameStateStreamObserver;
    }

    @Override
    public void onNext(Die die) {
        client = getNewPlayerPosition(client, die.getValue());
        if (client.getPosition() != ONE_HUNDRED) {
            server = getNewPlayerPosition(server, current().nextInt(ONE, SEVEN));
        }
        gameStateStreamObserver.onNext(getGameState());
    }

    @Override
    public void onError(Throwable throwable) {
        gameStateStreamObserver.onError(throwable);
    }

    @Override
    public void onCompleted() {
        gameStateStreamObserver.onCompleted();
    }

    private GameState getGameState() {
        return GameState.newBuilder()
                .addPlayer(client)
                .addPlayer(server)
                .build();
    }

    private Player getNewPlayerPosition(Player player, int dieValue) {
        int position = player.getPosition() + dieValue;
        position = getPosition(position);
        if (position <= ONE_HUNDRED) {
            player = player.toBuilder()
                    .setPosition(position).build();
        }

        return player;
    }
}
