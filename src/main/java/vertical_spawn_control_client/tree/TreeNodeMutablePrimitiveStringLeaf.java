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
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.minecraft.EntitySpawnDefinition;
import vertical_spawn_control_client.minecraft.NBT;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class TreeNodeMutablePrimitiveStringLeaf implements TreeNode, UIComponentsProvider, JsonSerializable {

	public final TreeNodeCollection<TreeNode> parent;
	private String value;
	JTextField inputField;
	JButton removeButton = new JButton("Remove");

	public TreeNodeMutablePrimitiveStringLeaf(TreeNodeCollection<TreeNode> parentIn, String valueIn) {
		parent = parentIn;
		value = valueIn;
		inputField = new JTextField();
    	inputField.setBorder(BorderFactory.createLineBorder(Color.black));
    	inputField.setText(value);
    	inputField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				value = inputField.getText();
			}
    	});
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
	
	public static Function<TreeNodeCollection<TreeNode>, TreeNodeMutablePrimitiveStringLeaf> getSupplier(String name, String defaultValue) {
		return new Function<TreeNodeCollection<TreeNode>, TreeNodeMutablePrimitiveStringLeaf>() {
			@Override
			public TreeNodeMutablePrimitiveStringLeaf apply(TreeNodeCollection<TreeNode> t) {
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
}
