package ai.idealistic.spartan.compatibility.necessary.protocollib;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class HandsPlib {

    public static EnumWrappers.Hand getSafeHand(PacketContainer packet, int index) {
        try {
            return packet.getHands().read(index);
        } catch (Throwable t) {
            return (EnumWrappers.Hand) packet.getModifier().read(index);
        }
    }
}
