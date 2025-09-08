package ai.idealistic.spartan.listeners.bukkit.standalone;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class VehicleDeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Event(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();

        if (entity instanceof Vehicle) {
            Entity[] passengers = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                    ? entity.getPassengers().toArray(new Entity[0])
                    : new Entity[]{entity.getPassenger()};

            if (passengers.length > 0) {
                for (Entity passenger : passengers) {
                    if (passenger instanceof Player) {
                        PlayerProtocol p = PluginBase.getProtocol((Player) passenger, true);
                        CheckThread.run(() -> p.executeRunners(
                                false,
                                new VehicleExitEvent(
                                        (Vehicle) entity,
                                        (Player) passenger,
                                        false
                                )
                        ));
                    }
                }
            }
        }
    }

}
