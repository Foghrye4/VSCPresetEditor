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

import foghrye4.swing.tree.JsonSerializableTreeNode;
import foghrye4.swing.tree.TreeNodeCollection;
import foghrye4.swing.tree.TreeNodeFloatLeaf;
import foghrye4.swing.tree.TreeNodeIntegerLeaf;
import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class Enchantment implements UIComponentsProvider, JsonSerializableTreeNode {

	public final TreeNodeCollection<JsonSerializableTreeNode> parent;
	TreeNodeIntegerLeaf enchantmentId = new TreeNodeIntegerLeaf(this, "id", 4);
	TreeNodeIntegerLeaf base = new TreeNodeIntegerLeaf(this, "lvl", 10);
	JTextField inputField = new JTextField();
	JButton removeButton = new JButton("Remove");

	public Enchantment(TreeNodeCollection<JsonSerializableTreeNode> parentIn, JsonReader reader) throws IOException {
		this(parentIn);
		this.readFromJson(reader);
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("id")) {
				enchantmentId.setValue(reader.nextInt());
			} else if (name.equals("lvl")) {
				base.setValue(reader.nextInt());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}

	public Enchantment(TreeNodeCollection<JsonSerializableTreeNode> parentIn) {
		parent = parentIn;
		inputField.setBorder(BorderFactory.createLineBorder(Color.black));
		inputField.setText(String.valueOf(enchantmentId));
		inputField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				enchantmentId.setValue(Integer.parseInt(inputField.getText()));
				PresetParser.get().tree.updateUI();
			}
		});
		removeButton.addActionListener(a -> {
			parent.remove(Enchantment.this);
			PresetParser.get().clearUI();
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
		enchantmentId.writeTo(writer);
		base.writeTo(writer);
		writer.endObject();
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return base;
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
		if (node == base)
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
		vector.add(base);
		return vector.elements();
	}
	
	@Override
	public String toString() {
		return this.enchantmentId.toString();
	}
	
	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.OBJECT;
	}
}
