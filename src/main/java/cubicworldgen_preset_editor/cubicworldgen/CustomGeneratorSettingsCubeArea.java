package cubicworldgen_preset_editor.cubicworldgen;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import foghrye4.swing.tree.JsonSerializableTreeNode;
import foghrye4.swing.tree.TreeNodeIntegerLeaf;
import foghrye4.swing.tree.TreeNodeStringLeaf;
import foghrye4.swing.tree.TreeNodeValueHolder;
import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.minecraft.SpawnLayer;
import vertical_spawn_control_client.ui.JTreeNodeTextField;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class CustomGeneratorSettingsCubeArea extends CustomGeneratorSettingsBase {
	private final Vector<TreeNode> childs = new Vector<TreeNode>();
	private String name = "New area";
	private final TreeNodeStringLeaf comment = new TreeNodeStringLeaf(this, "Comment", "");
	private final TreeNodeIntegerLeaf fromX = new TreeNodeIntegerLeaf(this, "fromX", -30000100);
	private final TreeNodeIntegerLeaf toX = new TreeNodeIntegerLeaf(this, "toX", 30000100);
	private final TreeNodeIntegerLeaf fromY = new TreeNodeIntegerLeaf(this, "fromY", -64);
	private final TreeNodeIntegerLeaf toY = new TreeNodeIntegerLeaf(this, "toY", 64);
	private final TreeNodeIntegerLeaf fromZ = new TreeNodeIntegerLeaf(this, "fromZ", -30000100);
	private final TreeNodeIntegerLeaf toZ = new TreeNodeIntegerLeaf(this, "toZ", 30000100);
	private final TreeNodeStringLeaf settings = new TreeNodeStringLeaf(this, "settings", "");
	private final PresetParser parent;
	JTextField inputField = new JTreeNodeTextField(this);
	private final JButton removeButton = new JButton("Remove");
	
	public CustomGeneratorSettingsCubeArea(PresetParser parentIn){
		super(parentIn);
		parent = parentIn;
		inputField.setText(name);
		removeButton.addActionListener(a -> {
			parent.remove(CustomGeneratorSettingsCubeArea.this);
			PresetParser.get().clearUI();
		});
		this.collectNodes();

	}
	
	private void collectNodes() {
		childs.addElement(comment);
		childs.addElement(fromX);
		childs.addElement(toX);
		childs.addElement(fromY);
		childs.addElement(toY);
		childs.addElement(fromZ);
		childs.addElement(toZ);
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
