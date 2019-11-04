package foghrye4.swing.tree;

import java.util.Enumeration;
import java.util.function.Supplier;

import javax.swing.JLayeredPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class TreeLeafAddNewElement<E extends JsonSerializableTreeNode> implements TreeNode, OnNodeClickActionProvider {

	public final TreeNodeCollection<E> parent;
	public final String name;
	public final Supplier<E> nodeConstructor;
	public boolean singleUse = false;

	public TreeLeafAddNewElement(TreeNodeCollection<E> treeNodeCollection, String nameIn, Supplier<E> supplier) {
		parent = treeNodeCollection;
		name = nameIn;
		nodeConstructor = supplier;
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
		parent.add(nodeConstructor.get());
		if(singleUse)
			parent.getNodeSuppliers().remove(this);
		tree.updateUI();
	}

	@Override
	public String toString() {
		return name;
	}
}
