package vertical_spawn_control_client.tree;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.function.Supplier;

import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.minecraft.EntitySpawnDefinition;
import vertical_spawn_control_client.minecraft.PresetParser;

public class TreeNodeCollection<E extends TreeNode> implements CollectionAccessProvider<E>, JsonSerializable {
	
	Vector<TreeNode> childs = new Vector<TreeNode>();
	TreeNode parent;
	String name;

	public TreeNodeCollection(TreeNode parentIn, String nameIn) {
		parent = parentIn;
		name = nameIn;
	}
	
	public void add(E node) {
		childs.add(node);
	}
	
	@Override
	public void add(int index, E node) {
		childs.add(index, node);
	}
	
	public void addNodeSupplier(String name, Supplier<E> supplier) {
		childs.add(new TreeLeafAddNewElement<E>(this, name, supplier));
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return childs.elementAt(childIndex);
	}

	@Override
	public int getChildCount() {
		return childs.size();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return childs.indexOf(node);
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
		return childs.elements();
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.name(name);
		writer.beginArray();
		for(TreeNode node:childs) {
			if (node instanceof JsonSerializable) {
				((JsonSerializable)node).writeTo(writer);
			}
		}
		writer.endArray();
	}

	public void remove(E node) {
		childs.remove(node);
		PresetParser parser = PresetParser.get(parent);
		parser.tree.updateUI();
	}
}
