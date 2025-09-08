package ai.idealistic.spartan.functionality.connection;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.functionality.moderation.CrossServerNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.java.RequestUtils;
import ai.idealistic.spartan.utils.java.StringUtils;
import ai.idealistic.spartan.utils.math.AlgebraUtils;

import java.net.URLEncoder;
import java.util.UUID;

public class CloudConnections {

    static int getUserIdentification() {
        try {
            String[] reply = RequestUtils.get(StringUtils.decodeBase64(JarVerification.website)
                            + "?action=get"
                            + "&data=userIdentification"
                            + "&version=" + Register.plugin.getDescription().getVersion(),
                    RequestUtils.defaultTimeOut);

            if (reply.length > 0) {
                String line = reply[0];

                if (AlgebraUtils.validInteger(line)) {
                    int id = Integer.parseInt(line);
                    IDs.set(id, id);
                    return id;
                }
            }
        } catch (Exception e) {
            CloudBase.throwError(e, "userIdentification:GET");
            return 0;
        }
        return -1;
    }

    static String[][] getStaffAnnouncements() {
        try {
            String[] results = RequestUtils.get(StringUtils.decodeBase64(JarVerification.website) + "?" + CloudBase.identification()
                    + "&action=get&data=staffAnnouncements&version=" + Register.plugin.getDescription().getVersion());

            if (results.length > 0) {
                String[] announcements = results[0].split(CloudBase.separator);
                String[][] array = new String[results.length][0];

                for (int i = 0; i < announcements.length; i++) {
                    array[i] = StringUtils.decodeBase64(announcements[i]).split(CloudBase.separator);
                }
                return array;
            }
        } catch (Exception e) {
            CloudBase.throwError(e, "staffAnnouncements:GET");
        }
        return new String[][]{};
    }

    static String[] getOwnedEditions() {
        try {
            String[] results = RequestUtils.get(
                    StringUtils.decodeBase64(JarVerification.website)
                            + "?" + CloudBase.identification()
                            + "&action=get"
                            + "&data=ownedEditions"
                            + "&email_address=" + URLEncoder.encode(Config.settings.getString("Purchases.email_address"), "UTF-8")
                            + "&patreon_full_name=" + URLEncoder.encode(Config.settings.getString("Purchases.patreon_full_name"), "UTF-8")
                            + "&version=" + Register.plugin.getDescription().getVersion()
            );

            if (results.length > 0) {
                return results[0].split(CloudBase.separator, Check.DataType.values().length);
            }
        } catch (Exception e) {
            CloudBase.throwError(e, "ownedEditions:GET");
        }
        if (IDs.enabled) {
            return new String[]{
                    Check.DataType.JAVA.toString(),
                    Check.DataType.BEDROCK.toString()
            };
        } else {
            return null;
        }
    }

    public static void executeDiscordWebhook(String webhook, UUID uuid, String name, int x, int y, int z, String type, String information) { // Once
        String url = Config.settings.getString("Discord." + webhook + "_webhook_url");

        if (url.startsWith("https://") || url.startsWith("http://")) {
            String color = Config.settings.getString("Discord.webhook_hex_color");
            int length = color.length();

            if (length >= 3 && length <= 6) {
                PluginBase.connectionThread.executeIfUnknownThreadElseHere(() -> {
                    try {
                        int webhookVersion = 2;
                        String crossServerInformationOption = CrossServerNotifications.getServerName();
                        RequestUtils.get(StringUtils.decodeBase64(JarVerification.website) + "?" + CloudBase.identification()
                                + "&action=add&data=discordWebhooks&version=" + Register.plugin.getDescription().getVersion() + "&value="
                                + URLEncoder.encode(
                                webhookVersion + CloudBase.separator
                                        + url + CloudBase.separator
                                        + color + CloudBase.separator
                                        + (!crossServerInformationOption.isEmpty() ? crossServerInformationOption : "NULL") + CloudBase.separator
                                        + name + CloudBase.separator
                                        + uuid + CloudBase.separator
                                        + x + CloudBase.separator
                                        + y + CloudBase.separator
                                        + z + CloudBase.separator
                                        + StringUtils.getClearColorString(type) + CloudBase.separator
                                        + StringUtils.getClearColorString(information), "UTF-8"));
                    } catch (Exception e) {
                        CloudBase.throwError(e, "discordWebhooks:ADD");
                    }
                });
            }
        }
    }

}
