package foghrye4.swing.tree;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Enumeration;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.minecraft.EntityNBT;
import vertical_spawn_control_client.minecraft.NBTBase;
import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.ui.JTreeNodeTextField;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class TreeNodeMutableNBTStringLeaf implements TreeNodeValueHolder, UIComponentsProvider, JsonSerializableTreeNode {

	public final TreeNodeCollection<JsonSerializableTreeNode> parent;
	private String nameValuePair;
	JTextField inputField = new JTreeNodeTextField(this);
	JButton removeButton = new JButton("Remove");

	public TreeNodeMutableNBTStringLeaf(TreeNodeCollection<JsonSerializableTreeNode> nbtBase, String nameIn, String valueIn) {
		parent = nbtBase;
		this.setValue(nameIn + ":"+valueIn);
    	removeButton.addActionListener(a-> {
    		parent.remove(TreeNodeMutableNBTStringLeaf.this);
    		PresetParser.get().clearUI();
    	});

	}
	
	@Override
	public String toString() {
		return nameValuePair;
	}
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		String[] pair = nameValuePair.split(":", 2);
		writer.name(pair[0]);
		try {
			int intValue = Integer.parseInt(pair[1]);
			writer.value(intValue);
		} catch (NumberFormatException e1) {
			try {
				float floatValue = Float.parseFloat(pair[1]);
				writer.value(floatValue);
			} catch (NumberFormatException e2) {
				writer.value(pair[1]);
			}
		}
	}	

	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		nameValuePair = reader.nextName() + ":" + reader.nextString();
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
		rectangle.setLocation(rectangle.x, rectangle.y + rectangle.height);
		removeButton.setBounds(rectangle);
		panel.add(inputField, JLayeredPane.POPUP_LAYER);
		panel.add(removeButton, JLayeredPane.POPUP_LAYER);
		panel.getParent().repaint();
	}

	public void setValue(String valueIn) {
		nameValuePair = valueIn;
    	inputField.setText(nameValuePair);
	}
	
	public static Function<EntityNBT, TreeNodeMutableNBTStringLeaf> getSupplier(String name, String defaultValue) {
		return new Function<EntityNBT, TreeNodeMutableNBTStringLeaf>() {
			@Override
			public TreeNodeMutableNBTStringLeaf apply(EntityNBT t) {
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
