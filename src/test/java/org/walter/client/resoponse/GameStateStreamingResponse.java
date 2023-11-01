package org.walter.client.resoponse;

import io.grpc.stub.StreamObserver;
import org.walter.game.Die;
import org.walter.game.GameState;
import org.walter.game.Player;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;

public class GameStateStreamingResponse implements StreamObserver<GameState> {

    private static final int ONE_HUNDRED = 100;
    private static final int ONE = 1;
    private static final int SEVEN = 7;
    private final CountDownLatch latch;
    private StreamObserver<Die> dieStreamObserver;

    public GameStateStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(GameState gameState) {
        List<Player> playerList = gameState.getPlayerList();
        playerList.forEach(player -> System.out.println(player.getName() + " : " + player.getPosition()));
        boolean isGameOver = playerList.stream()
                .anyMatch(player -> player.getPosition() == ONE_HUNDRED);
        if (isGameOver) {
            System.out.println("Game Over!");
            dieStreamObserver.onCompleted();
        } else {
            sleepUninterruptibly(ONE, SECONDS);
            roll();
        }
        System.out.println("----------------------------");
    }

    @Override
    public void onError(Throwable throwable) {
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        latch.countDown();
    }

    public void setDieStreamObserver(StreamObserver<Die> dieStreamObserver) {
        this.dieStreamObserver = dieStreamObserver;
    }

    public void roll() {
        int dieValue = ThreadLocalRandom.current().nextInt(ONE, SEVEN);
        Die die = Die.newBuilder().setValue(dieValue).build();
        dieStreamObserver.onNext(die);
    }
}
