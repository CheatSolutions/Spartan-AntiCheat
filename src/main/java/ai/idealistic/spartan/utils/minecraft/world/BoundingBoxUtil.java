package ai.idealistic.spartan.utils.minecraft.world;

import ai.idealistic.spartan.abstraction.world.ServerBlock;
import ai.idealistic.spartan.listeners.bukkit.standalone.ChunksEvent;
import ai.idealistic.spartan.utils.minecraft.entity.AxisAlignedBB;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Step;
import org.bukkit.material.WoodenStep;

import java.util.ArrayList;
import java.util.List;

public class BoundingBoxUtil {

    public static List<AxisAlignedBB> getBoundingBoxes(final Block block) {
        final List<AxisAlignedBB> boxes = new ArrayList<>();
        final double x = block.getX();
        final double y = block.getY();
        final double z = block.getZ();
        final BlockData data = ChunksEvent.isLoaded(block)
                ? block.getBlockData()
                : null;

        if (data instanceof Slab) {
            final Slab slab = (Slab) data;
            if (slab.getType() == Slab.Type.TOP) {
                boxes.add(wrap(new AxisAlignedBB(x, y + 0.5, z, x + 1, y + 1, z + 1)));
            } else {
                boxes.add(wrap(new AxisAlignedBB(x, y, z, x + 1, y + 0.5, z + 1)));
            }
        } else if (data instanceof Stairs) {
            Stairs stairs = (Stairs) data;
            boolean isTop = false;
            isTop = stairs.getHalf() == Bisected.Half.TOP;
            BlockFace facing = stairs.getFacing();
            AxisAlignedBB base;
            AxisAlignedBB step;

            if (!isTop) {
                base = new AxisAlignedBB(x, y, z, x + 1, y + 0.5, z + 1);
                switch (facing) {
                    case NORTH:
                        step = new AxisAlignedBB(x, y + 0.5, z + 0.5, x + 1, y + 1, z + 1);
                        break;
                    case SOUTH:
                        step = new AxisAlignedBB(x, y + 0.5, z, x + 1, y + 1, z + 0.5);
                        break;
                    case EAST:
                        step = new AxisAlignedBB(x, y + 0.5, z, x + 0.5, y + 1, z + 1);
                        break;
                    case WEST:
                        step = new AxisAlignedBB(x + 0.5, y + 0.5, z, x + 1, y + 1, z + 1);
                        break;
                    default:
                        step = new AxisAlignedBB(x, y + 0.5, z, x + 1, y + 1, z + 1);
                        break;
                }
            } else {
                base = new AxisAlignedBB(x, y + 0.5, z, x + 1, y + 1, z + 1);
                switch (facing) {
                    case NORTH:
                        step = new AxisAlignedBB(x, y, z + 0.5, x + 1, y + 0.5, z + 1);
                        break;
                    case SOUTH:
                        step = new AxisAlignedBB(x, y, z, x + 1, y + 0.5, z + 0.5);
                        break;
                    case EAST:
                        step = new AxisAlignedBB(x, y, z, x + 0.5, y + 0.5, z + 1);
                        break;
                    case WEST:
                        step = new AxisAlignedBB(x + 0.5, y, z, x + 1, y + 0.5, z + 1);
                        break;
                    default:
                        step = new AxisAlignedBB(x, y, z, x + 1, y + 0.5, z + 1);
                        break;
                }
            }
            boxes.add(wrap(base));
            boxes.add(wrap(step));
        } else {
            if (ignore(new ServerBlock(block).getType().toString().toLowerCase())) {
                return null;
            } else boxes.add(wrap(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1)));
        }
        return boxes;
    }

    public static List<AxisAlignedBB> getBoundingBoxesLegacy(final Block block) {
        final List<AxisAlignedBB> boxes = new ArrayList<>();
        double x = block.getX();
        double y = block.getY();
        double z = block.getZ();
        final MaterialData data = block.getState().getData();

        if (data instanceof Step || data instanceof WoodenStep) {
            byte dataValue = block.getData();
            if ((dataValue & 0x8) != 0) {
                boxes.add(wrap(new AxisAlignedBB(x, y + 0.5, z, x + 1, y + 1, z + 1)));
            } else {
                boxes.add(wrap(new AxisAlignedBB(x, y, z, x + 1, y + 0.5, z + 1)));
            }
        } else if (data instanceof org.bukkit.material.Stairs) {
            org.bukkit.material.Stairs stairs = (org.bukkit.material.Stairs) data;
            boolean isTop = (block.getData() & 0x4) != 0;
            AxisAlignedBB base;
            AxisAlignedBB step;

            if (!isTop) {
                base = new AxisAlignedBB(x, y, z, x + 1, y + 0.5, z + 1);
                switch (stairs.getFacing()) {
                    case NORTH:
                        step = new AxisAlignedBB(x, y + 0.5, z + 0.5, x + 1, y + 1, z + 1);
                        break;
                    case SOUTH:
                        step = new AxisAlignedBB(x, y + 0.5, z, x + 1, y + 1, z + 0.5);
                        break;
                    case EAST:
                        step = new AxisAlignedBB(x, y + 0.5, z, x + 0.5, y + 1, z + 1);
                        break;
                    case WEST:
                        step = new AxisAlignedBB(x + 0.5, y + 0.5, z, x + 1, y + 1, z + 1);
                        break;
                    default:
                        step = new AxisAlignedBB(x, y + 0.5, z, x + 1, y + 1, z + 1);
                        break;
                }
            } else {
                base = new AxisAlignedBB(x, y + 0.5, z, x + 1, y + 1, z + 1);
                switch (stairs.getFacing()) {
                    case NORTH:
                        step = new AxisAlignedBB(x, y, z + 0.5, x + 1, y + 0.5, z + 1);
                        break;
                    case SOUTH:
                        step = new AxisAlignedBB(x, y, z, x + 1, y + 0.5, z + 0.5);
                        break;
                    case EAST:
                        step = new AxisAlignedBB(x, y, z, x + 0.5, y + 0.5, z + 1);
                        break;
                    case WEST:
                        step = new AxisAlignedBB(x + 0.5, y, z, x + 1, y + 0.5, z + 1);
                        break;
                    default:
                        step = new AxisAlignedBB(x, y, z, x + 1, y + 0.5, z + 1);
                        break;
                }
            }
            boxes.add(wrap(base));
            boxes.add(wrap(step));
        } else {
            if (ignore(new ServerBlock(block).getType().toString().toLowerCase())) {
                return null;
            } else boxes.add(wrap(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1)));
        }
        return boxes;
    }

    public static boolean ignore(final String block) {
        return block.matches(".*(snow|step|frame|table|slab|stair|ladder|vine|waterlily|wall|carpet|fence|rod|bed|skull|pot|hopper|door|bars|piston|lily).*");
    }

    public static AxisAlignedBB wrap(final AxisAlignedBB bb) {
        return bb.expand(-1e-4, -1e-4, -1e-4);
    }
}
