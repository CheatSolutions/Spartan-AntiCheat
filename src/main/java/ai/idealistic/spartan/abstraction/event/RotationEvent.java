package ai.idealistic.spartan.abstraction.event;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import kireiko.dev.millennium.types.Vec2f;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class RotationEvent {
    private PlayerProtocol profile;
    private Vec2f from;
    private Vec2f to;

    private static double absDelta(float v1, float v2) {
        return Math.abs(Math.abs(v1) - Math.abs(v2));
    }

    public Vec2f getDelta() {
        return new Vec2f(to.getX() - from.getX(), to.getY() - from.getY());
    }

    public Vec2f getAbsDelta() {
        return new Vec2f(
                absDelta(to.getX(), from.getX()),
                absDelta(to.getY(), from.getY())
        );
    }
}
