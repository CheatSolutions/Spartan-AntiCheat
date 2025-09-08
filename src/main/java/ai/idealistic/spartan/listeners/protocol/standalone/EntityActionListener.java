package ai.idealistic.spartan.listeners.protocol.standalone;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class EntityActionListener extends PacketAdapter {

    private enum AbilitiesEnum {
        START_SPRINTING,
        STOP_SPRINTING,
        PRESS_SHIFT_KEY,
        RELEASE_SHIFT_KEY
    }

    public EntityActionListener() {
        super(
                Register.plugin,
                ListenerPriority.HIGHEST,
                PacketType.Play.Client.ENTITY_ACTION
        );
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PlayerProtocol protocol = PluginBase.getProtocol(event.getPlayer());

        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib")
                && protocol.isBedrockPlayer()) {
            return;
        }
        CheckThread.run(() -> {
            if (event.getPacket().getModifier().getValues().size() > 1) {
                String typeString = event.getPacket().getModifier().getValues().get(1).toString();
                AbilitiesEnum type = getEnum(typeString);

                if (type != null) {
                    CheckThread.run(() -> {
                        if (type == AbilitiesEnum.PRESS_SHIFT_KEY) {
                            protocol.sneaking = true;
                        } else if (type == AbilitiesEnum.RELEASE_SHIFT_KEY) {
                            protocol.sneaking = false;
                        } else if (type == AbilitiesEnum.START_SPRINTING) {
                            protocol.sprinting = true;
                        } else if (type == AbilitiesEnum.STOP_SPRINTING) {
                            protocol.sprinting = false;
                        }
                    });
                }
            }
        });
    }

    private AbilitiesEnum getEnum(String s) {
        for (AbilitiesEnum type : AbilitiesEnum.values()) {
            if (type.toString().equals(s)) {
                return type;
            }
        }
        return null;
    }

}
