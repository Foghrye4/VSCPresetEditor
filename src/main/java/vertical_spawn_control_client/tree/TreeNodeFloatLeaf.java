package vertical_spawn_control_client.tree;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.ui.JTreeNodeTextField;

public class TreeNodeFloatLeaf extends TreeLeafBase implements TreeNodeValueHolder {
	
	private float value;
	JTextField inputField = new JTreeNodeTextField(this);

	public TreeNodeFloatLeaf(TreeNode parentIn, String nameIn, float valueIn) {
		super(parentIn,nameIn);
		this.setValue(valueIn);
	}
	
	@Override
	public String toString() {
		return name + ":" + value;
	}
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.name(name);
		writer.value(value);
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		if (reader.nextName().equals(name))
			value = (float) reader.nextDouble();
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

	public void setValue(float valueIn) {
		value = valueIn;
    	inputField.setText(String.valueOf(value));
	}
	
	public static Function<TreeNode, TreeNodeFloatLeaf> getSupplier(String name, float defaultValue) {
		return new Function<TreeNode, TreeNodeFloatLeaf>() {
			@Override
			public TreeNodeFloatLeaf apply(TreeNode t) {
				return new TreeNodeFloatLeaf(t, name, defaultValue);
			}
		};
	}

	@Override
	public void parseValue(String string) {
		if(string == null)
			return;
		value = Float.parseFloat(string);
	}

	@Override
	public boolean accept(String text) {
		try {
			float v = Float.parseFloat(text);
			value = v;
			return true;
		}
		catch (NumberFormatException exception) {
			return false;
		}
	}
	
	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.NAME_VALUE_PAIR;
	}
}
