package vertical_spawn_control_client.minecraft;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.tree.JsonSerializableTreeNode;
import vertical_spawn_control_client.tree.TreeNodeBooleanLeaf;
import vertical_spawn_control_client.tree.TreeNodeCollection;
import vertical_spawn_control_client.tree.TreeNodeIntegerLeaf;
import vertical_spawn_control_client.tree.TreeNodeMutablePrimitiveStringLeaf;
import vertical_spawn_control_client.tree.TreeNodeStringLeaf;
import vertical_spawn_control_client.tree.TreeNodeValueHolder;
import vertical_spawn_control_client.ui.JTreeNodeTextField;
import vertical_spawn_control_client.ui.MainWindow;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class SpawnLayer implements JsonSerializableTreeNode, UIComponentsProvider, TreeNodeValueHolder {
	
	private final Vector<TreeNode> childs = new Vector<TreeNode>();
	private String name = "New area";
	private final TreeNodeStringLeaf comment = new TreeNodeStringLeaf(this, "Comment", "");
	private final TreeNodeIntegerLeaf fromX = new TreeNodeIntegerLeaf(this, "fromX", -100000);
	private final TreeNodeIntegerLeaf toX = new TreeNodeIntegerLeaf(this, "toX", 100000);
	private final TreeNodeIntegerLeaf fromY = new TreeNodeIntegerLeaf(this, "fromY", -64);
	private final TreeNodeIntegerLeaf toY = new TreeNodeIntegerLeaf(this, "toY", 64);
	private final TreeNodeIntegerLeaf fromZ = new TreeNodeIntegerLeaf(this, "fromZ", -100000);
	private final TreeNodeIntegerLeaf toZ = new TreeNodeIntegerLeaf(this, "toZ", 100000);
	private final TreeNodeBooleanLeaf blockNaturalSpawn = new TreeNodeBooleanLeaf(this,"block_natural_spawn", true);
	public final TreeNodeCollection<EntitySpawnDefinition> spawnList = new TreeNodeCollection<EntitySpawnDefinition>(this,"spawn_list",()-> {
		return new EntitySpawnDefinition(SpawnLayer.this.spawnList);
	});
	public final TreeNodeCollection<JsonSerializableTreeNode> blackList = new TreeNodeCollection<JsonSerializableTreeNode>(this,"black_list",()-> {
		return new TreeNodeMutablePrimitiveStringLeaf(SpawnLayer.this.blackList,"minecraft:zombie");
	}); // entity registry name list, for example "minecraft:zombie"
	public final TreeNodeCollection<JsonSerializableTreeNode> biomeBlackList = new TreeNodeCollection<JsonSerializableTreeNode>(this,"exclude_biomes", ()-> {
		return new TreeNodeMutablePrimitiveStringLeaf(SpawnLayer.this.biomeBlackList, "minecraft:taiga");
	}); // Biome registry name set, for example "minecraft:taiga"
	public final TreeNodeCollection<JsonSerializableTreeNode> biomeWhiteList = new TreeNodeCollection<JsonSerializableTreeNode>(this,"only_in_biomes", ()-> {
		return new TreeNodeMutablePrimitiveStringLeaf(SpawnLayer.this.biomeWhiteList, "minecraft:taiga");
	});
	private final PresetParser parent;
	JTextField inputField = new JTreeNodeTextField(this);
	private final JButton removeButton = new JButton("Remove");
	
	public SpawnLayer(PresetParser parentIn) {
		parent = parentIn;
		inputField.setText(name);
		removeButton.addActionListener(a -> {
			parent.remove(SpawnLayer.this);
		});
		this.collectNodes();
	}
	
	public SpawnLayer(PresetParser parentIn, JsonReader reader) throws IOException {
		this(parentIn);
		this.readFromJson(reader);
	}
	

	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		boolean setDefaultName = true;
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("from")||name.equals("fromY")) {
				fromY.setValue(reader.nextInt());
			} else if (name.equals("to")||name.equals("toY")) {
				toY.setValue(reader.nextInt());
			} else if (name.equals("fromX")) {
				fromX.setValue(reader.nextInt());
			} else if (name.equals("toX")) {
				toX.setValue(reader.nextInt());
			} else if (name.equals("fromZ")) {
				fromZ.setValue(reader.nextInt());
			} else if (name.equals("toZ")) {
				toZ.setValue(reader.nextInt());
			} else if (name.equals("name")) {
				setDefaultName = false;
				this.name = reader.nextString();
				inputField.setText(this.name);
			} else if (name.equalsIgnoreCase("comment")) {
				comment.setValue(reader.nextString());
			} else if (name.equals("exclude_biomes")) {
				reader.beginArray();
				while (reader.hasNext()) {
					String biome = reader.nextString();
					biomeBlackList.add(new TreeNodeMutablePrimitiveStringLeaf(biomeBlackList, biome));
				}
				reader.endArray();
			} else if (name.equals("only_in_biomes")) {
				reader.beginArray();
				while (reader.hasNext()) {
					String biome = reader.nextString();
					biomeWhiteList.add(new TreeNodeMutablePrimitiveStringLeaf(biomeWhiteList, biome));
				}
				reader.endArray();
			} else if (name.equals("block_natural_spawn")) {
				blockNaturalSpawn.setValue(reader.nextBoolean());
			} else if (name.equals("black_list")) {
				reader.beginArray();
				while (reader.hasNext()) {
					String ename = reader.nextString();
					blackList.add(new TreeNodeMutablePrimitiveStringLeaf(blackList, ename));
				}
				reader.endArray();
			} else if (name.equals("spawn_list")) {
				reader.beginArray();
				while (reader.hasNext()) {
					spawnList.add(new EntitySpawnDefinition(spawnList, reader));
				}
				reader.endArray();
			}
			else {
				reader.skipValue();
			}
		}
		reader.endObject();
		if (fromY.getValue() > toY.getValue()) {
			int a = fromY.getValue();
			fromY.setValue(toY.getValue());
			toY.setValue(a);
		}
		if (fromX.getValue() > toX.getValue()) {
			int a = fromX.getValue();
			fromX.setValue(toX.getValue());
			toX.setValue(a);
		}
		if (fromZ.getValue() > toZ.getValue()) {
			int a = fromZ.getValue();
			fromZ.setValue(toZ.getValue());
			toZ.setValue(a);
		}
		if(setDefaultName)
			setDefaultName();
	}

	
	private void setDefaultName() {
		name = "Layer from Y="+fromY.getValue()+" to Y="+toY.getValue();
		inputField.setText(name);
	}
	
	private void collectNodes() {
		childs.addElement(comment);
		childs.addElement(fromX);
		childs.addElement(toX);
		childs.addElement(fromY);
		childs.addElement(toY);
		childs.addElement(fromZ);
		childs.addElement(toZ);
		childs.addElement(blockNaturalSpawn);
		childs.addElement(this.spawnList);
		childs.addElement(this.biomeBlackList);
		childs.addElement(this.biomeWhiteList);
		childs.addElement(this.blackList);
	}
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name("name");
		writer.value(name);
		for(TreeNode node:childs) {
			if (node instanceof JsonSerializableTreeNode) {
				((JsonSerializableTreeNode)node).writeTo(writer);
			}
		}
		writer.endObject();
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return childs.elementAt(childIndex);
	}

	@Override
	public int getChildCount() {
		return childs.size();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return childs.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public Enumeration<TreeNode> children() {
		return childs.elements();
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public void removeComponents(JLayeredPane panel) {
		panel.remove(inputField);
		panel.remove(removeButton);
		panel.repaint();
		panel.getParent().repaint();
	}

	@Override
	public void addComponents(JLayeredPane panel, Rectangle rectangle) {
		rectangle.width = 200; 
		inputField.setBounds(rectangle);
		rectangle.setLocation(rectangle.x, rectangle.y + rectangle.height);
		removeButton.setBounds(rectangle);
		panel.add(inputField, JLayeredPane.POPUP_LAYER);
		panel.add(removeButton, JLayeredPane.POPUP_LAYER);
		panel.getParent().repaint();
	}

	@Override
	public boolean accept(String text) {
		name = text;
		return true;
	}
	
	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.OBJECT;
	}
}
