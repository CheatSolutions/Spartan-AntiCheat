package ai.idealistic.spartan.abstraction.data;

import ai.idealistic.spartan.abstraction.event.PlayerTickEvent;
import lombok.Getter;
import org.bukkit.GameMode;

@Getter
public class TimerBalancer {

    private long balancer, result;
    private int negative, forced, latency;

    public TimerBalancer() {
        balancer = 1000;
        result = 0;
        forced = 0;
        latency = 0;
    }

    public void pushDelay(PlayerTickEvent event) {
        GameMode gameMode = event.protocol.getGameMode();

        if (gameMode != GameMode.SURVIVAL
                && gameMode != GameMode.ADVENTURE) {
            balancer = 1000;
            result = 0;
            forced = 0;
            latency = 0;
            return;
        }
        long delay = event.getDelay();
        forced = ((delay > 15 && delay < 48) ? forced + 1 : 0);
        latency = ((delay > 40 && delay < 48) ? latency + 10 : latency / 2);

        if (delay > 50) {
            balancer += delay - 50;
            if (delay > 53) negative++;
        } else if (delay < 50) {
            final long deviance = 50 - delay;

            if (balancer - deviance >= 0) {
                balancer -= deviance;
            } else {
                final long totalDeviance = deviance - balancer;
                result += totalDeviance;
                balancer = 0;
            }
            negative = 0;
        } else if (result >= 0) {
            result /= 2L;
            balancer /= 2L;
            negative = 0;
        }
    }

    public boolean isNegativeTimer() {
        return this.negative > 15;
    }

    public void addBalance(long balance) {
        this.balancer += balance;
    }

}
