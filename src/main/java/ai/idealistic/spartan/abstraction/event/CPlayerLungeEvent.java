package ai.idealistic.spartan.abstraction.event;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CPlayerLungeEvent {

    public final PlayerProtocol protocol;
    public final ItemStack item;
    public final Vector velocity;

    public CPlayerLungeEvent(PlayerProtocol protocol, ItemStack item, Vector velocity) {
        this.protocol = protocol;
        this.item = item;
        this.velocity = velocity;
    }

}
