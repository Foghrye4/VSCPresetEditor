package vertical_spawn_control_client.minecraft;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import foghrye4.swing.tree.JsonSerializableTreeNode;
import foghrye4.swing.tree.TreeLeafAddNewElement;
import foghrye4.swing.tree.TreeNodeCollection;
import foghrye4.swing.tree.TreeNodeMutableNBTStringLeaf;
import foghrye4.swing.tree.TreeNodeMutablePrimitiveStringLeaf;
import foghrye4.swing.tree.TreeNodeRemovableNBTStringLeaf;
import foghrye4.swing.tree.TreeNodeValueHolder;
import vertical_spawn_control_client.ui.JTreeNodeTextField;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class NBTBase extends TreeNodeCollection<JsonSerializableTreeNode> implements TreeNodeValueHolder, UIComponentsProvider  {
	public final TreeNodeCollection<JsonSerializableTreeNode> parent;
	private String name = "nbt";
	JTextField inputField = new JTreeNodeTextField(this);
	JButton removeButton = new JButton("Remove");
	
	public NBTBase(TreeNodeCollection<JsonSerializableTreeNode> parentIn, String nameIn) {
		super(parentIn, nameIn);
		Supplier<JsonSerializableTreeNode> supplier = new Supplier<JsonSerializableTreeNode>() {
			@Override
			public JsonSerializableTreeNode get() {
				return new TreeNodeMutableNBTStringLeaf(NBTBase.this, "color", "16351261");
			}
		};
		nodeSuppliers.add(new TreeLeafAddNewElement<JsonSerializableTreeNode>(this, "<add new>", supplier));
		inputField.setText(nameIn);
		name = nameIn;
		parent = parentIn;
    	removeButton.addActionListener(a-> {
    		parent.remove(NBTBase.this);
			PresetParser.get().clearUI();
    	});
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.name(name);
		writer.beginObject();
		for (JsonSerializableTreeNode node : childs) {
				node.writeTo(writer);
		}
		writer.endObject();
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		reader.beginObject();
		reader.setLenient(true);
		while (reader.hasNext()) {
			String name = reader.nextName();
			JsonToken token = reader.peek();
			if(token == JsonToken.BEGIN_OBJECT) {
				NBTBase child = new NBTBase(this, name);
				child.readFromJson(reader);
				childs.add(childs.size(), child);
			} else {
				childs.add(childs.size(), new TreeNodeMutableNBTStringLeaf(this, name, reader.nextString()));
			}
		}
		reader.endObject();
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


	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean accept(String text) {
		name = text;
		return true;
	}
}
