package foghrye4.swing.tree;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.function.Supplier;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.ui.MainWindow;

public class TreeNodeCollection<E extends JsonSerializableTreeNode> implements CollectionAccessProvider<E>, JsonSerializableTreeNode {
	
	protected final Vector<JsonSerializableTreeNode> childs = new Vector<JsonSerializableTreeNode>();
	protected final Vector<TreeLeafAddNewElement<JsonSerializableTreeNode>> nodeSuppliers = new Vector<TreeLeafAddNewElement<JsonSerializableTreeNode>>();
	TreeNode parent;
	final String name;

	public TreeNodeCollection(TreeNode parentIn, String nameIn) {
		parent = parentIn;
		name = nameIn;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TreeNodeCollection(TreeNode parentIn, String nameIn, Supplier<E> supplier) {
		this(parentIn,nameIn);
		nodeSuppliers.add(new TreeLeafAddNewElement(this, "<add new>", supplier));
	}
	
	@Override
	public void add(E node) {
		childs.add(node);
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		if(childIndex >= childs.size())
			return nodeSuppliers.get(childIndex-childs.size());
		return childs.elementAt(childIndex);
	}

	@Override
	public int getChildCount() {
		return childs.size()+nodeSuppliers.size();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		int index = childs.indexOf(node);
		if(index == -1)
			return nodeSuppliers.indexOf(node);
		return index;
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
		Vector<TreeNode> childs1 = new Vector<TreeNode>();
		childs1.addAll(childs);
		childs1.addAll(nodeSuppliers);
		return childs1.elements();
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.name(name);
		writer.beginArray();
		for(JsonSerializableTreeNode node:childs) {
			node.writeTo(writer);
		}
		writer.endArray();
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		if(reader.peek() == JsonToken.NAME && !reader.nextName().equals(name))
			return;
		reader.beginArray(); 
		while(reader.hasNext()){
			JsonSerializableTreeNode node = nodeSuppliers.get(0).nodeConstructor.get();
			node.readFromJson(reader);
			childs.add(node);
		}
		reader.endArray();
	}

	public void remove(E node) {
		childs.remove(node);
		PresetParser parser = MainWindow.instance.parser;
		parser.clearUI();
		PresetParser.updateUI();
	}
	
	public void clear() {
		childs.clear();
	}
	
	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.NAME_VALUE_PAIR;
	}

	@Override
	public Vector<TreeLeafAddNewElement<JsonSerializableTreeNode>> getNodeSuppliers() {
		return this.nodeSuppliers;
	}
}
