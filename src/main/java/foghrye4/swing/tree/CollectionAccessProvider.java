package foghrye4.swing.tree;

import java.util.Vector;

import javax.swing.tree.TreeNode;

public interface CollectionAccessProvider<E> extends TreeNode {
	public void add(E node);
	public Vector<TreeLeafAddNewElement<JsonSerializableTreeNode>> getNodeSuppliers();
}
