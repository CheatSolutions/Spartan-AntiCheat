package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.event.CPlayerRiptideEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.TridentEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TridentListener extends PacketAdapter {

    public TridentListener() {
        super(
                Register.plugin,
                ListenerPriority.LOWEST,
                PacketType.Play.Client.USE_ITEM,
                PacketType.Play.Client.BLOCK_DIG
        );
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (!event.isCancelled()) { // PlayerRiptideEvent does not implement cancellable
            PlayerProtocol protocol = PluginBase.getProtocol(event.getPlayer());

            if (!Config.settings.getBoolean("Important.bedrock_on_protocollib")
                    && protocol.isBedrockPlayer()) {
                return;
            }
            ItemStack item = protocol.getInventory().getItemInMainHand();

            if (item.getType().equals(Material.TRIDENT)) {
                double r = Math.toRadians(protocol.getLocation().getYaw());

                CheckThread.run(() -> TridentEvent.event(
                        new CPlayerRiptideEvent(
                                protocol,
                                item,
                                new Vector(-Math.sin(r), protocol.getLocation().getPitch() / 90, Math.cos(r))
                        ),
                        true
                ));
            }
        }
    }

}