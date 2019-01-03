package vertical_spawn_control_client.tree;

import java.util.Enumeration;
import java.util.function.Supplier;

import javax.swing.JLayeredPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class TreeLeafAddNewElement<E extends TreeNode> implements TreeNode, OnNodeClickActionProvider {

	public final CollectionAccessProvider<E> parent;
	public final String name;
	private final Supplier<E> nodeContructor;

	public TreeLeafAddNewElement(CollectionAccessProvider<E> parentIn, String nameIn, Supplier<E> nodeContructorIn) {
		parent = parentIn;
		name = nameIn;
		nodeContructor = nodeContructorIn;
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

	@Override
	public void onNodeClick(JTree tree, JLayeredPane panel) {
		parent.add(parent.getChildCount()-1,nodeContructor.get());
		tree.updateUI();
	}

	@Override
	public String toString() {
		return name;
	}
}
