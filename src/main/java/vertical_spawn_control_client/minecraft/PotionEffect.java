package vertical_spawn_control_client.minecraft;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.tree.JsonSerializable;
import vertical_spawn_control_client.tree.TreeNodeCollection;
import vertical_spawn_control_client.tree.TreeNodeFloatLeaf;
import vertical_spawn_control_client.tree.TreeNodeIntegerLeaf;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class PotionEffect implements TreeNode, UIComponentsProvider, JsonSerializable {

	public final TreeNodeCollection<TreeNode> parent;
	private int id = 8;
	TreeNodeIntegerLeaf duration = new TreeNodeIntegerLeaf(this, "Duration", 60);
	JTextField inputField = new JTextField();
	JButton removeButton = new JButton("Remove");

	public PotionEffect(TreeNodeCollection<TreeNode> parentIn, JsonReader reader) throws IOException {
		this(parentIn);
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("Id")) {
				id = reader.nextInt();
			} else if (name.equals("Duration")) {
				duration.setValue(reader.nextInt());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}

	public PotionEffect(TreeNodeCollection<TreeNode> parentIn) {
		parent = parentIn;
		inputField.setBorder(BorderFactory.createLineBorder(Color.black));
		inputField.setText(String.valueOf(id));
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
					int v = Integer.parseInt(inputField.getText());
					id = v;
				}
				catch (NumberFormatException exception) {}
			}
		});
		removeButton.addActionListener(a -> {
			parent.remove(PotionEffect.this);
			inputField.getParent().remove(inputField);
			removeButton.getParent().remove(removeButton);
		});
	}

	@Override
	public void removeComponents(JLayeredPane panel) {
		panel.remove(inputField);
		panel.remove(removeButton);
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

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name("Id");
		writer.value(id);
		duration.writeTo(writer);
		writer.endObject();
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return duration;
	}

	@Override
	public int getChildCount() {
		return 1;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		if (node == duration)
			return 1;
		return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public Enumeration<TreeNode> children() {
		Vector<TreeNode> vector = new Vector<TreeNode>();
		vector.add(duration);
		return vector.elements();
	}
	
	@Override
	public String toString() {
		return "Id:"+id;
	}
}
