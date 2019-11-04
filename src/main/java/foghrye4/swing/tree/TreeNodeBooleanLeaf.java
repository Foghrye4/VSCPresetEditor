package foghrye4.swing.tree;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLayeredPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.minecraft.PresetParser;

public class TreeNodeBooleanLeaf extends TreeLeafBase {
	
	public boolean value;
	JCheckBox inputField;
	

	public TreeNodeBooleanLeaf(TreeNode parentIn, String nameIn, boolean valueIn) {
		super(parentIn, nameIn);
		value = valueIn;
		inputField = new JCheckBox();
    	inputField.setBorder(BorderFactory.createLineBorder(Color.black));
    	inputField.setSelected(value);
    	inputField.setText(name);
    	inputField.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				value = inputField.isSelected();
				PresetParser.updateUI();
			}});
	}

	
	@Override
	public String toString() {
		return name + ":" + value;
	}

	@Override
	public void addComponents(JLayeredPane panel, Rectangle rectangle) {
		inputField.setBounds(rectangle);
		panel.add(inputField, JLayeredPane.POPUP_LAYER);
		panel.getParent().repaint();
	}


	@Override
	public void removeComponents(JLayeredPane panel) {
		panel.remove(inputField);
		panel.updateUI();
		panel.repaint();
		panel.getParent().repaint();
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.name(name);
		writer.value(value);
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		if (reader.nextName().equals(name))
			value = reader.nextBoolean();
	}

	public void setValue(boolean valueIn) {
		value = valueIn;
    	inputField.setSelected(value);
	}
	
	public static Function<TreeNode, TreeNodeBooleanLeaf> getSupplier(String name, boolean defaultValue) {
		return new Function<TreeNode, TreeNodeBooleanLeaf>() {
			@Override
			public TreeNodeBooleanLeaf apply(TreeNode t) {
				return new TreeNodeBooleanLeaf(t, name, defaultValue);
			}
		};
	}

	@Override
	public void parseValue(String string) {
		if(string == null)
			return;
		value = Boolean.parseBoolean(string);
	}
	
	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.NAME_VALUE_PAIR;
	}
}
