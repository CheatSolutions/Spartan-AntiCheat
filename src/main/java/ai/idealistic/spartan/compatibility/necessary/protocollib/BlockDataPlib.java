package ai.idealistic.spartan.compatibility.necessary.protocollib;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedBlockData;

public class BlockDataPlib {

    public static WrappedBlockData getSafeBlockData(PacketContainer packet, int index) {
        try {
            return packet.getBlockData().read(index);
        } catch (Throwable t) {
            return (WrappedBlockData) packet.getModifier().read(index);
        }
    }
}
