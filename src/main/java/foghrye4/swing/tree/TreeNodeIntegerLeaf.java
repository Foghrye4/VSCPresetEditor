package foghrye4.swing.tree;

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
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.ui.JTreeNodeTextField;

public class TreeNodeIntegerLeaf extends TreeLeafBase implements TreeNodeValueHolder {
	
	private int value;
	JTextField inputField = new JTreeNodeTextField(this);
	static final int MINIMAL_STRING_LENGTH = 16;

	public TreeNodeIntegerLeaf(TreeNode parentIn, String nameIn, int valueIn) {
		super(parentIn,nameIn);
		this.setValue(valueIn);
	}
	
	@Override
	public String toString() {
		return name + ":" + value;
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
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.name(name);
		writer.value(value);
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NAME && reader.nextName().equals(name))
			value = reader.nextInt();
	}

	public void setValue(int valueIn) {
		value=valueIn;
    	inputField.setText(String.valueOf(value));
	}

	public int getValue() {
		return value;
	}
	
	public static Function<TreeNode, TreeNodeIntegerLeaf> getSupplier(String name, int defaultValue) {
		return new Function<TreeNode, TreeNodeIntegerLeaf>() {
			@Override
			public TreeNodeIntegerLeaf apply(TreeNode t) {
				return new TreeNodeIntegerLeaf(t, name, defaultValue);
			}
		};
	}

	@Override
	public void parseValue(String string) {
		if(string == null)
			return;
		value = Integer.parseInt(string);
	}
	
	@Override
	public boolean accept(String text) {
		try {
			int v = Integer.parseInt(text);
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
