package vertical_spawn_control_client.tree;

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
	
	protected Vector<JsonSerializableTreeNode> childs = new Vector<JsonSerializableTreeNode>();
	TreeNode parent;
	final String name;
	private final TreeLeafAddNewElement<E> nodeCreator;

	public TreeNodeCollection(TreeNode parentIn, String nameIn, Supplier<E> supplier) {
		parent = parentIn;
		name = nameIn;
		nodeCreator = new TreeLeafAddNewElement<E>(this, "<add new>", supplier);
	}
	
	public void add(E node) {
		childs.add(node);
	}
	
	@Override
	public void add(int index, E node) {
		childs.add(index, node);
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		if(childIndex == childs.size())
			return nodeCreator;
		return childs.elementAt(childIndex);
	}

	@Override
	public int getChildCount() {
		return childs.size()+1;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		if(node==nodeCreator)
			return childs.size();
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
		Vector<TreeNode> childs1 = new Vector<TreeNode>();
		childs1.addAll(childs);
		childs1.add(nodeCreator);
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
			JsonSerializableTreeNode node = nodeCreator.nodeConstructor.get();
			node.readFromJson(reader);
			childs.add(node);
		}
		reader.endArray();
	}

	public void remove(E node) {
		childs.remove(node);
		PresetParser parser = MainWindow.instance.parser;
		JTree tree = parser.tree;
		parser.clearUI();
		tree.updateUI();
	}
	
	public void clear() {
		childs.clear();
	}
	
	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.NAME_VALUE_PAIR;
	}
}
