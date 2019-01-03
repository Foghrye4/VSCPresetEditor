package vertical_spawn_control_client.tree;

import javax.swing.tree.TreeNode;

public interface CollectionAccessProvider<E> extends TreeNode {
	public void add(int index, E node);
}
