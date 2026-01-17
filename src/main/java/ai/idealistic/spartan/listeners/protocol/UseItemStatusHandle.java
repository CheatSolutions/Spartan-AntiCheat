package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UseItemStatusHandle extends PacketAdapter {

    public UseItemStatusHandle() {
        super(Register.plugin, ListenerPriority.NORMAL, resolvePacketTypes());
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);

        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib")
                && protocol.isBedrockPlayer()) {
            return;
        }
        PacketContainer packet = event.getPacket();

        CheckThread.run(() -> {
            if (packet.getType().equals(PacketType.Play.Client.BLOCK_DIG)) {
                protocol.useItemPacket = false;
            } else {
                BlockPosition blockPosition = new BlockPosition(0, 0, 0);
                if (packet.getHands().getValues().isEmpty()) {
                    if (!packet.getMovingBlockPositions().getValues().isEmpty()) {
                        blockPosition = packet.getMovingBlockPositions().read(0).getBlockPosition();
                    }
                    if (!packet.getBlockPositionModifier().getValues().isEmpty()) {
                        blockPosition = packet.getBlockPositionModifier().read(0);
                    }
                    if (blockPosition.getY() != -1) return;
                }
                boolean isMainHand = !MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                        || event.getPacket().getHands().read(0) == EnumWrappers.Hand.MAIN_HAND;
                ItemStack itemStack = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                        ? (isMainHand ? protocol.getInventory().getItemInMainHand()
                        : protocol.getInventory().getItemInOffHand()) : player.getItemInHand();
                if (packet.getType().toString().contains("USE_ITEM_ON")) {
                    if (event.getPacket().getStructures().getValues().toString().contains("Serverbound"))
                        return;
                }
                if (itemStack.getType().toString().contains("SHIELD") ||
                        (itemStack.getType().isEdible() && (player.getFoodLevel() != 20
                                || itemStack.getType().toString().contains("GOLDEN_APPLE"))
                                && !protocol.getGameMode().equals(GameMode.CREATIVE))) {
                    protocol.useItemPacket = true;
                    protocol.useItemPacketReset =
                            !(itemStack.getType().toString().contains("SHIELD") ||
                                    itemStack.getType().toString().contains("GOLDEN_APPLE"));
                }
            }
        });
    }

    private static PacketType[] resolvePacketTypes() {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            return new PacketType[]{
                    PacketType.Play.Client.USE_ITEM,
                    PacketType.Play.Client.BLOCK_DIG
            };
        } else {
            if (ProtocolLib.isPacketSupported("USE_ITEM_ON")) {
                return new PacketType[]{
                        PacketType.Play.Client.USE_ITEM_ON,
                        PacketType.Play.Client.BLOCK_PLACE,
                        PacketType.Play.Client.BLOCK_DIG
                };
            } else {
                return new PacketType[]{
                        PacketType.Play.Client.BLOCK_PLACE,
                        PacketType.Play.Client.BLOCK_DIG
                };
            }
        }
    }


}