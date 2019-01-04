package vertical_spawn_control_client.tree;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.ui.JTreeNodeTextField;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class TreeNodeMutablePrimitiveStringLeaf implements TreeNodeValueHolder, UIComponentsProvider, JsonSerializableTreeNode {

	public final TreeNodeCollection<JsonSerializableTreeNode> parent;
	private String value;
	JTextField inputField = new JTreeNodeTextField(this);
	JButton removeButton = new JButton("Remove");

	public TreeNodeMutablePrimitiveStringLeaf(TreeNodeCollection<JsonSerializableTreeNode> parentIn, String valueIn) {
		parent = parentIn;
		this.setValue(valueIn);
    	removeButton.addActionListener(a-> {
    		parent.remove(TreeNodeMutablePrimitiveStringLeaf.this);
    		inputField.getParent().remove(inputField);
    		removeButton.getParent().remove(removeButton);
    	});

	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.value(value);
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		value = reader.nextString();
	}
	
	@Override
	public void removeComponents(JLayeredPane panel) {
		panel.remove(inputField);
		panel.remove(removeButton);
		panel.updateUI();
		panel.repaint();
		panel.getParent().repaint();
	}

	@Override
	public void addComponents(JLayeredPane panel, Rectangle rectangle) {
		inputField.setBounds(rectangle);
		panel.add(inputField, JLayeredPane.POPUP_LAYER);
		panel.add(removeButton, JLayeredPane.POPUP_LAYER);
		panel.getParent().repaint();
	}

	public void setValue(String valueIn) {
		value = valueIn;
    	inputField.setText(value);
	}
	
	public static Function<TreeNodeCollection<JsonSerializableTreeNode>, TreeNodeMutablePrimitiveStringLeaf> getSupplier(String name, String defaultValue) {
		return new Function<TreeNodeCollection<JsonSerializableTreeNode>, TreeNodeMutablePrimitiveStringLeaf>() {
			@Override
			public TreeNodeMutablePrimitiveStringLeaf apply(TreeNodeCollection<JsonSerializableTreeNode> t) {
				return new TreeNodeMutablePrimitiveStringLeaf(t, defaultValue);
			}
		};
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
        return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public Enumeration<TreeNode> children() {
		return DefaultMutableTreeNode.EMPTY_ENUMERATION;
	}

	@Override
	public boolean accept(String valueIn) {
		value = valueIn;
		return true;
	}
	
	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.PRIMITIVE;
	}
}
