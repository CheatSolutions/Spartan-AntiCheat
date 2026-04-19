package ai.idealistic.spartan.compatibility.necessary.protocollib;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.MovingObjectPositionBlock;

public class MovingBlockPositionsPlib {

    public static MovingObjectPositionBlock getSafeMovingBlockPositions(PacketContainer packet, int index) {
        try {
            return packet.getMovingBlockPositions().read(index);
        } catch (Throwable t) {
            return (MovingObjectPositionBlock) packet.getModifier().read(index);
        }
    }
}
