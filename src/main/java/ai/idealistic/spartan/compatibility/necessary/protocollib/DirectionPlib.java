package ai.idealistic.spartan.compatibility.necessary.protocollib;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class DirectionPlib {

    public static EnumWrappers.Direction getSafeDirection(PacketContainer packet, int index) {
        try {
            return packet.getDirections().read(index);
        } catch (Throwable t) {
            int directionInt = BackPlib.getSafeInt(packet, index);
            return EnumWrappers.Direction.values()[directionInt];
        }
    }
}
