package vertical_spawn_control_client.minecraft.ai;

import java.util.function.Function;

import javax.swing.tree.TreeNode;

import vertical_spawn_control_client.tree.*;

@SuppressWarnings("unchecked")
public enum AIAction {
	NEAREST_ATTACKABLE_TARGET(
			TreeNodeIntegerLeaf.getSupplier("priority", 1),
			TreeNodeStringLeaf.getSupplier("target_class", "\"minecraft:villager\""),
			TreeNodeBooleanLeaf.getSupplier("check_sight", true)),
	LEAP_AT_TARGET(
			TreeNodeIntegerLeaf.getSupplier("priority", 1),
			TreeNodeFloatLeaf.getSupplier("leap_motion_y", 4.0f)),
	ATTACK_MELEE(
			TreeNodeIntegerLeaf.getSupplier("priority", 1),
			TreeNodeFloatLeaf.getSupplier("speed", 0.5f), 
			TreeNodeBooleanLeaf.getSupplier("use_long_memory", false)),
	ATTACK_MELEE_FIXED_DAMAGE(
			TreeNodeIntegerLeaf.getSupplier("priority", 1),
			TreeNodeFloatLeaf.getSupplier("speed", 0.5f), 
			TreeNodeBooleanLeaf.getSupplier("use_long_memory", false),
			TreeNodeFloatLeaf.getSupplier("damage", 1.0f)),
	WANDER(
			TreeNodeIntegerLeaf.getSupplier("priority", 1),
			TreeNodeFloatLeaf.getSupplier("speed", 0.5f));
	
	public Function<TreeNode, ? extends TreeLeafBase>[] params;
	AIAction(Function<TreeNode, ? extends TreeLeafBase>... paramsIn) {
		params = paramsIn;
	}
	
	public static String[] toArrayOfString() {
		String[] values = new String[values().length];
		for (int i = 0; i < values().length; i++) {
			values[i] = values()[i].name().toLowerCase();
		}
		return values;
	}
}
