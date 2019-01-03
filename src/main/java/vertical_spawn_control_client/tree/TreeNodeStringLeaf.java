package vertical_spawn_control_client.tree;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonWriter;

public class TreeNodeStringLeaf extends TreeLeafBase {
	
	private String value;
	JTextField inputField;

	public TreeNodeStringLeaf(TreeNode parentIn, String nameIn, String valueIn) {
		super(parentIn,nameIn);
		value = valueIn;
		inputField = new JTextField();
    	inputField.setBorder(BorderFactory.createLineBorder(Color.black));
    	inputField.setText(value);
    	inputField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				value = inputField.getText();
			}
    	});

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
}
