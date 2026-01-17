package ai.idealistic.spartan.compatibility.necessary;

import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.utils.java.ReflectionUtils;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class Floodgate {

    private static boolean classExists = false;

    static {
        reload();
    }

    public static void reload() {
        classExists = ReflectionUtils.classExists(
                "org.geysermc.floodgate.api.FloodgateApi"
        );
    }

    static boolean isBedrockPlayer(UUID uuid, String name) {
        return uuid != null
                && Compatibility.CompatibilityType.FLOODGATE.isFunctional()
                && classExists
                && FloodgateApi.getInstance().isFloodgatePlayer(uuid)

                || name != null && isBedrockPlayer(name);
    }

    private static boolean isBedrockPlayer(String name) {
        String prefix = Config.settings.getString("Important.bedrock_player_prefix");
        return !prefix.isEmpty() && name.startsWith(prefix);
    }
}
