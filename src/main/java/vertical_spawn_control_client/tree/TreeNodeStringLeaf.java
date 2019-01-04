package vertical_spawn_control_client.tree;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.function.Function;

import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.ui.JTreeNodeTextField;

public class TreeNodeStringLeaf extends TreeLeafBase implements TreeNodeValueHolder {
	
	private String value;
	JTextField inputField = new JTreeNodeTextField(this);

	public TreeNodeStringLeaf(TreeNode parentIn, String nameIn, String valueIn) {
		super(parentIn,nameIn);
		setValue(valueIn);
	}
	
	@Override
	public String toString() {
		return name+":"+value;
	}
	
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.name(name);
		writer.value(value);
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		if (reader.nextName().equals(name))
			value = reader.nextString();
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
		value = valueIn;
    	inputField.setText(value);
	}
	
	public static Function<TreeNode, TreeNodeStringLeaf> getSupplier(String name, String defaultValue) {
		return new Function<TreeNode, TreeNodeStringLeaf>() {
			@Override
			public TreeNodeStringLeaf apply(TreeNode t) {
				return new TreeNodeStringLeaf(t, name, defaultValue);
			}
		};
	}

	@Override
	public void parseValue(String string) {
		if(string == null)
			return;
		value = string;
	}

	@Override
	public boolean accept(String text) {
		value = text;
		return true;
	}

	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.NAME_VALUE_PAIR;
	}
}
