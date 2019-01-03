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

public class TreeNodeMutableNBTStringLeaf implements TreeNode, UIComponentsProvider, JsonSerializable {

	public final NBT parent;
	private String nameValuePair;
	JTextField inputField;
	JButton removeButton = new JButton("Remove");

	public TreeNodeMutableNBTStringLeaf(NBT parentIn, String nameIn, String valueIn) {
		parent = parentIn;
		nameValuePair = nameIn + ":"+valueIn;
		inputField = new JTextField();
    	inputField.setBorder(BorderFactory.createLineBorder(Color.black));
    	inputField.setText(nameValuePair);
    	inputField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				String[] pair = inputField.getText().split(":",2);
				if(pair.length!=2 || pair[0].length()==0 || pair[1].length()==0) {
					inputField.setBackground(Color.YELLOW);
					return;
				}
				inputField.setBackground(Color.WHITE);
				nameValuePair = inputField.getText();
			}
    	});
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
}
