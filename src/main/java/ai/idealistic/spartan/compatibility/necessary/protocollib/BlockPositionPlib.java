package ai.idealistic.spartan.compatibility.necessary.protocollib;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;

public class BlockPositionPlib {

    public static BlockPosition getSafeBlockPosition(PacketContainer packet, int index) {
        try {
            return packet.getBlockPositionModifier().read(index);
        } catch (Throwable t) {
            return (BlockPosition) packet.getModifier().read(index);
        }
    }

    public static BlockPosition getSafeSectionPositions(PacketContainer packet, int index) {
        try {
            return packet.getSectionPositions().read(index);
        } catch (Throwable t) {
            return (BlockPosition) packet.getModifier().read(index);
        }
    }

}
