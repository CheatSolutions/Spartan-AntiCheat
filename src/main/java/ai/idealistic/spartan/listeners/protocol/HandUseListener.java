package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.event.CPlayerLungeEvent;
import ai.idealistic.spartan.abstraction.event.CPlayerRiptideEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.TridentEvent;
import ai.idealistic.spartan.utils.minecraft.inventory.MaterialUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class HandUseListener extends PacketAdapter {

    private static final Material
            WOODEN_SPEAR = MaterialUtils.get("wooden_spear"),
            STONE_SPEAR = MaterialUtils.get("stone_spear"),
            IRON_SPEAR = MaterialUtils.get("iron_spear"),
            GOLDEN_SPEAR = MaterialUtils.get("golden_spear"),
            DIAMOND_SPEAR = MaterialUtils.get("diamond_spear"),
            NETHERITE_SPEAR = MaterialUtils.get("netherite_spear");

    public HandUseListener() {
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
            ItemStack item = protocol.getInventory().getItemInHand();

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
            } else if ((item.getType().equals(WOODEN_SPEAR)
                    && item.getType().equals(STONE_SPEAR)
                    && item.getType().equals(IRON_SPEAR)
                    && item.getType().equals(GOLDEN_SPEAR)
                    && item.getType().equals(DIAMOND_SPEAR)
                    && item.getType().equals(NETHERITE_SPEAR))
                    && item.getEnchantmentLevel(Enchantment.LUNGE) > 0) {
                double r = Math.toRadians(protocol.getLocation().getYaw());
                CheckThread.run(() -> protocol.executeRunners(
                        false,
                        new CPlayerLungeEvent(
                                protocol,
                                item,
                                new Vector(-Math.sin(r), protocol.getLocation().getPitch() / 90, Math.cos(r))
                        )
                ));
            }
        }
    }

}