package com.vagdedes.spartan.listeners.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.vagdedes.spartan.Register;
import com.vagdedes.spartan.abstraction.event.CPlayerVelocityEvent;
import com.vagdedes.spartan.abstraction.protocol.PlayerProtocol;
import com.vagdedes.spartan.functionality.concurrent.CheckThread;
import com.vagdedes.spartan.functionality.server.PluginBase;
import com.vagdedes.spartan.listeners.bukkit.VelocityEvent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VelocityListener extends PacketAdapter {


    public VelocityListener() {
        super(
                Register.plugin,
                ListenerPriority.MONITOR,
                PacketType.Play.Server.ENTITY_VELOCITY
        );
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);

        if (protocol.bukkitExtra.isBedrockPlayer()) {
            return;
        }
        PacketContainer packet = event.getPacket();

        if (!packet.getIntegers().getValues().isEmpty()) {
            int id = packet.getIntegers().getValues().get(0);
            if (protocol.bukkitExtra.getEntityId() == id) {
                CheckThread.run(() -> {
                    double x = packet.getIntegers().read(1).doubleValue() / 8000.0D,
                                    y = packet.getIntegers().read(2).doubleValue() / 8000.0D,
                                    z = packet.getIntegers().read(3).doubleValue() / 8000.0D;
                    CPlayerVelocityEvent velocityEvent = new CPlayerVelocityEvent(player, new Vector(x, y, z));
                    velocityEvent.setCancelled(event.isCancelled());
                    VelocityEvent.event(velocityEvent, true);
                });
            }
        }
    }

}
