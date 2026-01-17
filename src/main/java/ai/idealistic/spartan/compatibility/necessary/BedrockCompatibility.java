package ai.idealistic.spartan.compatibility.necessary;

import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.Permissions;
import org.bukkit.entity.Player;

public class BedrockCompatibility {

    public static boolean isPlayer(Player p) {
        return ProtocolSupport.isBedrockPlayer(p)

                || Floodgate.isBedrockPlayer(
                ProtocolLib.isTemporary(p) ? null : p.getUniqueId(),
                p.getName()
        )

                || Config.settings.getBoolean("Important.bedrock_client_permission")
                && Permissions.onlyHas(p, Permission.BEDROCK);
    }

}
