package vertical_spawn_control_client.tree;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonWriter;

public class TreeNodeFloatLeaf extends TreeLeafBase {
	
	private float value;
	JTextField inputField;

	public TreeNodeFloatLeaf(TreeNode parentIn, String nameIn, float valueIn) {
		super(parentIn,nameIn);
		value = valueIn;
		inputField = new JTextField();
    	inputField.setBorder(BorderFactory.createLineBorder(Color.black));
    	inputField.setText(String.valueOf(value));
    	inputField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					float v = Float.parseFloat(inputField.getText());
					value = v;
				}
				catch (NumberFormatException exception) {}
			}
    	});

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
}
