package com.vagdedes.spartan.functionality.connection.cloud;

import com.vagdedes.spartan.Register;
import com.vagdedes.spartan.functionality.notifications.CrossServerNotifications;
import com.vagdedes.spartan.functionality.server.Config;
import com.vagdedes.spartan.functionality.server.SpartanBukkit;
import com.vagdedes.spartan.utils.java.RequestUtils;
import com.vagdedes.spartan.utils.java.StringUtils;
import com.vagdedes.spartan.utils.math.AlgebraUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.UUID;

public class CloudConnections {

    static int getUserIdentification() {
        try {
            File path = new File("plugins/");

            if (path.exists()
                    && path.isDirectory()) {
                File[] files = path.listFiles();
                String pluginName = Register.plugin.getName();
                String fileType = ".jar";

                if (files != null
                        && files.length > 0) {
                    for (File file : files) {
                        if (file.isFile()) {
                            String name = file.getName();

                            if (name.startsWith(pluginName) && name.endsWith(fileType)) {
                                name = name.replace(fileType, "")
                                        .replace("Spartan", "");

                                if (!name.isEmpty()) {
                                    String[] reply = RequestUtils.get(StringUtils.decodeBase64(CloudBase.accountWebsite) + "?token=" + name);

                                    if (reply.length > 0) {
                                        String line = reply[0];

                                        if (AlgebraUtils.validInteger(line)) {
                                            int id = Integer.parseInt(line);
                                            CloudBase.token = name;
                                            IDs.set(id, name.hashCode());
                                            return id;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Separator

            String[] reply = RequestUtils.get(StringUtils.decodeBase64(CloudBase.website)
                            + "?action=get"
                            + "&data=userIdentification"
                            + "&version=" + CloudBase.version,
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

    static boolean ownsProduct(String productID) { // Once
        try {
            String[] results = RequestUtils.get(StringUtils.decodeBase64(CloudBase.website) + "?" + CloudBase.identification
                    + "&action=get&data=ownsProduct&version=" + CloudBase.version + "&value=" + productID);

            if (results.length > 0) {
                return results[0].equals("true");
            }
        } catch (Exception e) {
            CloudBase.throwError(e, "ownsProduct:GET");
        }
        return false;
    }

    static boolean hasAccount() { // Once
        try {
            String[] results = RequestUtils.get(StringUtils.decodeBase64(CloudBase.website) + "?" + CloudBase.identification
                    + "&action=get&data=hasAccount&version=" + CloudBase.version);

            if (results.length > 0) {
                return !results[0].equals("false");
            }
        } catch (Exception e) {
            CloudBase.throwError(e, "hasAccount:GET");
        }
        return true;
    }

    static String[][] getStaffAnnouncements() { // Once
        try {
            String[] results = RequestUtils.get(StringUtils.decodeBase64(CloudBase.website) + "?" + CloudBase.identification
                    + "&action=get&data=staffAnnouncements&version=" + CloudBase.version);

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

    public static void executeDiscordWebhook(String webhook, UUID uuid, String name, int x, int y, int z, String type, String information) { // Once
        String url = Config.settings.getString("Discord." + webhook + "_webhook_url");

        if (url.startsWith("https://") || url.startsWith("http://")) {
            String color = Config.settings.getString("Discord.webhook_hex_color");
            int length = color.length();

            if (length >= 3 && length <= 6) {
                SpartanBukkit.connectionThread.executeIfSyncElseHere(() -> {
                    try {
                        int webhookVersion = 2;
                        String crossServerInformationOption = CrossServerNotifications.getServerName();
                        RequestUtils.get(StringUtils.decodeBase64(CloudBase.website) + "?" + CloudBase.identification
                                + "&action=add&data=discordWebhooks&version=" + CloudBase.version + "&value="
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
