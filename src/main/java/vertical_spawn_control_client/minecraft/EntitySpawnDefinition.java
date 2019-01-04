package vertical_spawn_control_client.minecraft;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.tree.JsonSerializableTreeNode;
import vertical_spawn_control_client.tree.TreeNodeCollection;
import vertical_spawn_control_client.tree.TreeNodeFloatLeaf;
import vertical_spawn_control_client.tree.TreeNodeIntegerLeaf;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class EntitySpawnDefinition implements TreeNode, UIComponentsProvider, JsonSerializableTreeNode {
	final Vector<TreeNode> childs = new Vector<TreeNode>();

	String entityClass = "minecraft:zombie";
	public final TreeNodeFloatLeaf chance = new TreeNodeFloatLeaf(this, "chance", 1.0f);
	public final TreeNodeIntegerLeaf groupSize = new TreeNodeIntegerLeaf(this, "group_size", 4);
	public final TreeNodeIntegerLeaf spawnLimit = new TreeNodeIntegerLeaf(this, "spawn_limit", 4);
	public final NBT nbt = new NBT(this);
	public final TreeNodeIntegerLeaf minLightLevel = new TreeNodeIntegerLeaf(this, "min_light_level", 0);
	public final TreeNodeIntegerLeaf maxLightLevel = new TreeNodeIntegerLeaf(this, "max_light_level", 16);
	private final TreeNodeCollection<EntitySpawnDefinition> parent;
	JTextField inputField = new JTextField();
	JButton removeButton = new JButton("Remove");

	public EntitySpawnDefinition(TreeNodeCollection<EntitySpawnDefinition> collection, JsonReader reader)
			throws IOException {
		this(collection);
		this.readFromJson(reader);
	}

	public EntitySpawnDefinition(TreeNodeCollection<EntitySpawnDefinition> collection) {
		parent = collection;
		inputField.setBorder(BorderFactory.createLineBorder(Color.black));
		inputField.setText(entityClass);
		inputField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				entityClass = inputField.getText();
				PresetParser.get().tree.updateUI();
			}
		});
		removeButton.addActionListener(a -> {
			parent.remove(EntitySpawnDefinition.this);
		});
		collectNodes();
	}

	private void collectNodes() {
		childs.addElement(chance);
		childs.addElement(groupSize);
		childs.addElement(spawnLimit);
		childs.addElement(nbt);
		childs.addElement(minLightLevel);
		childs.addElement(maxLightLevel);
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
		return entityClass;
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
		inputField.setBounds(rectangle);
		rectangle.setLocation(rectangle.x, rectangle.y + rectangle.height);
		removeButton.setBounds(rectangle);
		panel.add(inputField, JLayeredPane.POPUP_LAYER);
		panel.add(removeButton, JLayeredPane.POPUP_LAYER);
		panel.getParent().repaint();
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name("class");
		writer.value(entityClass);
		for (TreeNode node : childs) {
			if (node instanceof JsonSerializableTreeNode) {
				((JsonSerializableTreeNode) node).writeTo(writer);
			}
		}
		writer.endObject();
	}

	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("class")) {
				entityClass = reader.nextString();
			} else if (name.equals("chance")) {
				chance.setValue((float) reader.nextDouble());
			} else if (name.equals("group_size")) {
				groupSize.setValue(reader.nextInt());
			} else if (name.equals("spawn_limit")) {
				spawnLimit.setValue(reader.nextInt());
			} else if (name.equals("nbt")) {
				nbt.read(reader.nextString());
			} else if (name.equals("min_light_level")) {
				minLightLevel.setValue(reader.nextInt());
			} else if (name.equals("max_light_level")) {
				maxLightLevel.setValue(reader.nextInt());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}
	
	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.OBJECT;
	}
}
