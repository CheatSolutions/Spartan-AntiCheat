package ai.idealistic.spartan.functionality.command;

import ai.idealistic.spartan.abstraction.inventory.implementation.PlayerInfo;
import ai.idealistic.spartan.abstraction.inventory.implementation.SynMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandTab implements TabCompleter {

    private static final Map<String, Permission[]> commands = new LinkedHashMap<>(18); // Attention

    static {
        commands.put("info", PlayerInfo.permissions);
        commands.put("syn", SynMenu.permissions);
        commands.put("charge", Permissions.staffPermissions);
        commands.put("panic", new Permission[]{Permission.MANAGE});
        commands.put("toggle", new Permission[]{Permission.MANAGE});
        commands.put("rl", new Permission[]{Permission.RELOAD});
        commands.put("reload", new Permission[]{Permission.RELOAD});
        commands.put("notifications", new Permission[]{Permission.NOTIFICATIONS});
        commands.put("verbose", new Permission[]{Permission.NOTIFICATIONS});
        commands.put("kick", new Permission[]{Permission.KICK});
        commands.put("warn", new Permission[]{Permission.WARN});
        commands.put("bypass", new Permission[]{Permission.USE_BYPASS});
        commands.put("conditions", new Permission[]{Permission.CONDITION});
        commands.put("moderation", new Permission[]{
                Permission.KICK,
                Permission.WARN,
                Permission.USE_BYPASS,
                Permission.WAVE
        });
        commands.put("proxy-command", new Permission[]{});
        commands.put("wave add", new Permission[]{Permission.WAVE});
        commands.put("wave remove", new Permission[]{Permission.WAVE});
        commands.put("wave clear", new Permission[]{Permission.WAVE});
        commands.put("wave run", new Permission[]{Permission.WAVE});
        commands.put("wave list", new Permission[]{Permission.WAVE});
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String arg, String[] args) {
        List<String> list = new ArrayList<>();
        int length = args.length;

        if (length == 1) {
            boolean isPlayer = sender instanceof Player;
            Player p = isPlayer ? (Player) sender : null;
            String argAbstract = args[0].toLowerCase();

            for (Map.Entry<String, Permission[]> entry : commands.entrySet()) {
                boolean add;

                if (!isPlayer) {
                    add = true;
                } else {
                    add = false;

                    if (entry.getValue().length > 0) {
                        for (Permission permission : entry.getValue()) {
                            if (Permissions.has(p, permission)) {
                                add = true;
                                break;
                            }
                        }
                    } else {
                        add = Permissions.has(p, Permission.ADMIN);
                    }
                }

                if (add) {
                    String key = entry.getKey();

                    if (key.contains(argAbstract)) {
                        list.add(key);
                    }
                }
            }
        } else if (length > 1) {
            Collection<PlayerProtocol> protocols = PluginBase.getProtocols();

            if (!protocols.isEmpty()) {
                String argAbstract = args[length - 1].toLowerCase();
                boolean player = sender instanceof Player;
                PlayerProtocol p = player ? PluginBase.getProtocol((Player) sender) : null;
                player &= p != null;

                for (PlayerProtocol protocol : protocols) {
                    if (!player || p.bukkit().canSee(protocol.bukkit())) {
                        String name = protocol.bukkit().getName();

                        if (name.toLowerCase().contains(argAbstract)) {
                            list.add(name);
                        }
                    }
                }
            }
        }
        return list;
    }
}
