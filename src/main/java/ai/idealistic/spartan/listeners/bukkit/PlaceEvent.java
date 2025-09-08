package ai.idealistic.spartan.listeners.bukkit;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.event.CBlockPlaceEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void event(BlockPlaceEvent e) {
        PlayerProtocol protocol = PluginBase.getProtocol(e.getPlayer(), true);
        CheckThread.run(() -> event(protocol, e.getBlock(), e.getBlockAgainst(), e, false));

        if (protocol.getRunner(CheckEnums.HackType.FAST_PLACE).prevent()
                || protocol.getRunner(CheckEnums.HackType.BLOCK_REACH).prevent()
                || protocol.getRunner(CheckEnums.HackType.IMPOSSIBLE_ACTIONS).prevent()) {
            e.setCancelled(true);
        }
    }

    public static void event(
            PlayerProtocol protocol,
            Block block,
            Block blockAgainst,
            Object event,
            boolean packets
    ) {
        if (protocol.packetsEnabled() == packets) {
            if (protocol.getWorld() != block.getWorld()) {
                return;
            }
            protocol.executeRunners(null, event);

            // Detections
            if (!ItemsAdder.is(block)) {
                protocol.executeRunners(
                        event,
                        new CBlockPlaceEvent(
                                protocol.bukkit(),
                                block,
                                blockAgainst,
                                event instanceof Cancellable && ((Cancellable) event).isCancelled()
                        )
                );
            }
        }
    }

}
