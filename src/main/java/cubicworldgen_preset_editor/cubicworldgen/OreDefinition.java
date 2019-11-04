package cubicworldgen_preset_editor.cubicworldgen;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JLayeredPane;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import foghrye4.swing.tree.JsonSerializableTreeNode;
import foghrye4.swing.tree.TreeNodeCollection;
import foghrye4.swing.tree.TreeNodeValueHolder;
import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class OreDefinition implements JsonSerializableTreeNode, UIComponentsProvider, TreeNodeValueHolder {
	private final Vector<TreeNode> childs = new Vector<TreeNode>();
	private final TreeNodeCollection<OreDefinition> parent;
	public OreDefinition(TreeNodeCollection<OreDefinition> parentIn) {
		parent = parentIn;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Enumeration children() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean accept(String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeComponents(JLayeredPane panel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addComponents(JLayeredPane panel, Rectangle rectangle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SerializedJsonType getSerializedJsonType() {
		// TODO Auto-generated method stub
		return null;
	}

}
