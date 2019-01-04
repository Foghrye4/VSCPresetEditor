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
import vertical_spawn_control_client.minecraft.NBT;
import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.ui.JTreeNodeTextField;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class TreeNodeMutableNBTStringLeaf implements TreeNodeValueHolder, UIComponentsProvider, JsonSerializableTreeNode {

	public final NBT parent;
	private String nameValuePair;
	JTextField inputField = new JTreeNodeTextField(this);
	JButton removeButton = new JButton("Remove");

	public TreeNodeMutableNBTStringLeaf(NBT parentIn, String nameIn, String valueIn) {
		parent = parentIn;
		this.setValue(nameIn + ":"+valueIn);
    	removeButton.addActionListener(a-> {
    		parent.remove(TreeNodeMutableNBTStringLeaf.this);
    		inputField.getParent().remove(inputField);
    		removeButton.getParent().remove(removeButton);
    	});

	}
	
	@Override
	public String toString() {
		return nameValuePair;
	}
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		String[] pair = nameValuePair.split(":",2);
		writer.name(pair[0]);
		writer.value(pair[1]);
	}
	

	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		nameValuePair = reader.nextName() + ":" + reader.nextString();
	}

	@Override
	public void removeComponents(JLayeredPane panel) {
		panel.remove(inputField);
		panel.updateUI();
		panel.repaint();
		panel.getParent().repaint();
	}

	@Override
	public void addComponents(JLayeredPane panel, Rectangle rectangle) {
		inputField.setBounds(rectangle);
		panel.add(inputField, JLayeredPane.POPUP_LAYER);
		panel.getParent().repaint();
	}

	public void setValue(String valueIn) {
		nameValuePair = valueIn;
    	inputField.setText(nameValuePair);
	}
	
	public static Function<NBT, TreeNodeMutableNBTStringLeaf> getSupplier(String name, String defaultValue) {
		return new Function<NBT, TreeNodeMutableNBTStringLeaf>() {
			@Override
			public TreeNodeMutableNBTStringLeaf apply(NBT t) {
				return new TreeNodeMutableNBTStringLeaf(t, name, defaultValue);
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
	public boolean accept(String text) {
		String[] pair = text.split(":", 2);
		if (pair.length != 2 || pair[0].length() == 0 || pair[1].length() == 0)
			return false;
		nameValuePair = text;
		return true;
	}
	
	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.NAME_VALUE_PAIR;
	}
}
