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
import vertical_spawn_control_client.tree.TreeNodeIntegerLeaf;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class Item implements JsonSerializableTreeNode, UIComponentsProvider {

	public final TreeNodeCollection<JsonSerializableTreeNode> parent;
	private String id = "minecraft:lava_bucket";
	TreeNodeIntegerLeaf count = new TreeNodeIntegerLeaf(this, "Count", 1);
	JTextField inputField = new JTextField();
	JButton removeButton = new JButton("Remove");

	public Item(TreeNodeCollection<JsonSerializableTreeNode> parentIn, JsonReader reader) throws IOException {
		this(parentIn);
		this.readFromJson(reader);
	}

	public Item(TreeNodeCollection<JsonSerializableTreeNode> parentIn) {
		parent = parentIn;
		inputField.setBorder(BorderFactory.createLineBorder(Color.black));
		inputField.setText(id);
		inputField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				id = inputField.getText();
				PresetParser.get().tree.updateUI();
			}
		});
		removeButton.addActionListener(a -> {
			parent.remove(Item.this);
			inputField.getParent().remove(inputField);
			removeButton.getParent().remove(removeButton);
		});
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
		writer.name("id");
		writer.value(id);
		count.writeTo(writer);
		writer.endObject();
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return count;
	}

	@Override
	public int getChildCount() {
		return 1;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		if (node == count)
			return 1;
		return -1;
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
		Vector<TreeNode> vector = new Vector<TreeNode>();
		vector.add(count);
		return vector.elements();
	}

	@Override
	public String toString() {
		return "id:" + id;
	}

	public JsonSerializableTreeNode setId(String string) {
		id = string;
		inputField.setText(id);
		return this;
	}

	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("id")) {
				id = reader.nextString();
			} else if (name.equals("Count")) {
				count.setValue(reader.nextInt());
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
