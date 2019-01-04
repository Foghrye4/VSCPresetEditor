package vertical_spawn_control_client.tree;

import javax.swing.tree.TreeNode;

public interface TreeNodeValueHolder extends TreeNode {

	boolean accept(String text);

}
