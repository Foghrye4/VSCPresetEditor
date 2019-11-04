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
import foghrye4.swing.tree.TreeNodeBooleanLeaf;
import foghrye4.swing.tree.TreeNodeCollection;
import foghrye4.swing.tree.TreeNodeIntegerLeaf;
import foghrye4.swing.tree.TreeNodeStringLeaf;
import foghrye4.swing.tree.TreeNodeValueHolder;
import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.minecraft.EntitySpawnDefinition;
import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.minecraft.SpawnLayer;
import vertical_spawn_control_client.ui.JTreeNodeTextField;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class CustomGeneratorSettingsBase implements JsonSerializableTreeNode, UIComponentsProvider, TreeNodeValueHolder {
	private final Vector<TreeNode> childs = new Vector<TreeNode>();
	private String name = "New area";
	private final TreeNodeStringLeaf comment = new TreeNodeStringLeaf(this, "Comment", "");
	private final TreeNodeIntegerLeaf version = new TreeNodeIntegerLeaf(this, "version", 3);
	private final TreeNodeIntegerLeaf waterLevel = new TreeNodeIntegerLeaf(this, "waterLevel", 63);
	private final TreeNodeBooleanLeaf strongholds = new TreeNodeBooleanLeaf(this, "strongholds", true);
	private final TreeNodeBooleanLeaf alternateStrongholdsPositions = new TreeNodeBooleanLeaf(this, "alternateStrongholdsPositions", true);
	private final TreeNodeBooleanLeaf villages = new TreeNodeBooleanLeaf(this, "villages", true);
	private final TreeNodeBooleanLeaf mineshafts = new TreeNodeBooleanLeaf(this, "mineshafts", true);
	private final TreeNodeBooleanLeaf temples = new TreeNodeBooleanLeaf(this, "temples", true);
	private final TreeNodeBooleanLeaf oceanMonuments = new TreeNodeBooleanLeaf(this, "oceanMonuments", true);
	private final TreeNodeBooleanLeaf woodlandMansions = new TreeNodeBooleanLeaf(this, "woodlandMansions", true);
	private final TreeNodeBooleanLeaf ravines = new TreeNodeBooleanLeaf(this, "ravines", true);
	private final TreeNodeBooleanLeaf dungeons = new TreeNodeBooleanLeaf(this, "dungeons", true);
	private final TreeNodeIntegerLeaf dungeonCount = new TreeNodeIntegerLeaf(this, "dungeonCount", 7);
	private final TreeNodeBooleanLeaf waterLakes = new TreeNodeBooleanLeaf(this, "waterLakes", true);
	private final TreeNodeIntegerLeaf waterLakeRarity = new TreeNodeIntegerLeaf(this, "waterLakeRarity", 4);
	private final TreeNodeBooleanLeaf lavaLakes = new TreeNodeBooleanLeaf(this, "lavaLakes", true);
	private final TreeNodeIntegerLeaf lavaLakeRarity = new TreeNodeIntegerLeaf(this, "lavaLakeRarity", 8);
	private final TreeNodeIntegerLeaf aboveSeaLavaLakeRarity = new TreeNodeIntegerLeaf(this, "aboveSeaLavaLakeRarity", 13);
	private final TreeNodeBooleanLeaf lavaOceans = new TreeNodeBooleanLeaf(this, "lavaOceans", false);
	private final TreeNodeIntegerLeaf biome = new TreeNodeIntegerLeaf(this, "biome", -1);
	private final TreeNodeIntegerLeaf biomeSize = new TreeNodeIntegerLeaf(this, "biomeSize", 4);
	private final TreeNodeIntegerLeaf riverSize = new TreeNodeIntegerLeaf(this, "riverSize", 4);
	public final TreeNodeCollection<EntitySpawnDefinition> spawnList = new TreeNodeCollection<EntitySpawnDefinition>(this,"spawn_list",()-> {
		return new EntitySpawnDefinition(CustomGeneratorSettingsBase.this.spawnList);
	});
	
	private final PresetParser parent;
	JTextField inputField = new JTreeNodeTextField(this);
	
	public CustomGeneratorSettingsBase(PresetParser parentIn){
		parent = parentIn;
		inputField.setText(name);
		this.collectNodes();

	}
	
	private void collectNodes() {
		childs.addElement(comment);
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
