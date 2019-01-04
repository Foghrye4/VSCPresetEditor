package vertical_spawn_control_client.tree;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import vertical_spawn_control_client.ui.UIComponentsProvider;

public abstract class TreeLeafBase implements JsonSerializableTreeNode, UIComponentsProvider {
	public final TreeNode parent;
	public final String name;

	public TreeLeafBase(TreeNode parentIn, String nameIn) {
		parent = parentIn;
		name = nameIn;
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
        return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public Enumeration<TreeNode> children() {
		return DefaultMutableTreeNode.EMPTY_ENUMERATION;
	}

	public abstract void parseValue(String string);
}
