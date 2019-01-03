package vertical_spawn_control_client.tree;

import javax.swing.JLayeredPane;
import javax.swing.JTree;

public interface OnNodeClickActionProvider {
	
	public void onNodeClick(JTree tree, JLayeredPane panel);
}
