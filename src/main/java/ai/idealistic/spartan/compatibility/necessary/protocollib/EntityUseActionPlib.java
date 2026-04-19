package ai.idealistic.spartan.compatibility.necessary.protocollib;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;

public class EntityUseActionPlib {

    public static EnumWrappers.EntityUseAction getSafeEntityUseActions(PacketContainer packet, int index) {
        try {
            return packet.getEntityUseActions().read(index);
        } catch (Throwable t) {
            return (EnumWrappers.EntityUseAction) packet.getModifier().read(index);
        }
    }

    public static WrappedEnumEntityUseAction getSafeEnumEntityUseActions(PacketContainer packet, int index) {
        try {
            return packet.getEnumEntityUseActions().read(index);
        } catch (Throwable t) {
            return (WrappedEnumEntityUseAction) packet.getModifier().read(index);
        }
    }

}
