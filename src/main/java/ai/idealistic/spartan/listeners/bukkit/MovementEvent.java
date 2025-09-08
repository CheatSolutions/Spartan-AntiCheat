package ai.idealistic.spartan.listeners.bukkit;

import ai.idealistic.spartan.abstraction.event.PlayerTickEvent;
import ai.idealistic.spartan.abstraction.event.PlayerTransactionEvent;
import ai.idealistic.spartan.abstraction.event.RotationEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import kireiko.dev.millennium.types.Vec2f;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void MoveEvent(PlayerMoveEvent e) {
        CheckThread.run(() -> event(e, false));
    }

    public static void event(PlayerMoveEvent e, boolean packets) {
        PlayerProtocol protocol = PluginBase.getProtocol(e.getPlayer());

        if (protocol.packetsEnabled() == packets) {
            Location nto = e.getTo();

            if (nto == null) {
                return;
            }
            Location vehicle = protocol.getVehicleLocation();
            ServerLocation to = vehicle != null
                    ? new ServerLocation(vehicle)
                    : new ServerLocation(nto);
            RotationEvent rotationEvent = new RotationEvent(
                            protocol,
                            new Vec2f(e.getFrom().getYaw(), e.getFrom().getPitch()),
                            new Vec2f(e.getTo().getYaw(), e.getTo().getPitch())
                            );
            protocol.getSensitivityProcessor().setLastDeltaPitch(rotationEvent.getDelta().getY());
            protocol.getSensitivityProcessor().processSensitivity();
            protocol.getCinematicComponent().process(rotationEvent);
            protocol.executeRunners(false, rotationEvent);

            if (!protocol.processLastMoveEvent(
                    nto,
                    vehicle,
                    to,
                    e.getFrom(),
                    packets
            )) {
                return;
            }
            protocol.executeRunners(e.isCancelled(), e);
        }
    }

    public static void tick(PlayerTickEvent tickEvent) {
        PlayerProtocol protocol = tickEvent.protocol;
        protocol.lastTickEvent = tickEvent;
        protocol.packetWorld.tick(tickEvent);
        protocol.timerBalancer.pushDelay(tickEvent);
        protocol.executeRunners(false, tickEvent);
    }

    public static void transaction(PlayerTransactionEvent event) {
        PlayerProtocol protocol = event.protocol;
        protocol.executeRunners(false, event);
    }

}
