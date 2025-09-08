package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.event.PlayerLeftClickEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;

public class ClicksListener extends PacketAdapter {

    public ClicksListener() {
        super(
                Register.plugin,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.ARM_ANIMATION,
                PacketType.Play.Client.BLOCK_DIG
        );
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);

        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib")
                && protocol.isBedrockPlayer()) {
            return;
        }
        synchronized (protocol) {
            if (event.getPacket().getType().equals(PacketType.Play.Client.ARM_ANIMATION)) {
                long delay = System.currentTimeMillis() - protocol.oldClickTime;

                CheckThread.run(() -> {
                    if (delay > 150) {
                        protocol.clickBlocker = false;
                    }
                    if (!protocol.clickBlocker) {
                        protocol.executeRunners(
                                false,
                                new PlayerLeftClickEvent(
                                        player,
                                        delay
                                )
                        );
                    }
                    protocol.oldClickTime = System.currentTimeMillis();
                });
            } else if (event.getPacket().getType().equals(PacketType.Play.Client.BLOCK_DIG)) {
                String s = event.getPacket().getStructures().getValues().toString();

                CheckThread.run(() -> {
                    protocol.oldClickTime = System.currentTimeMillis();
                    protocol.clickBlocker = !s.contains("ABORT");
                });
            }
        }
    }

}
