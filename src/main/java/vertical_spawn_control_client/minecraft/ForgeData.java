package vertical_spawn_control_client.minecraft;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.minecraft.ai.AIAction;
import vertical_spawn_control_client.minecraft.ai.AIBase;
import vertical_spawn_control_client.tree.JsonSerializableTreeNode;
import vertical_spawn_control_client.tree.TreeNodeCollection;

public class ForgeData implements JsonSerializableTreeNode {
	
	public final TreeNode parent;
	public final TreeNodeCollection<AIBase> ai = new TreeNodeCollection<AIBase>(this,"CustomAI",()-> {
		return new AIBase(ForgeData.this.ai).onActionSelection(AIAction.ATTACK_MELEE_FIXED_DAMAGE);
	});

	public ForgeData(TreeNode parentIn) {
		parent = parentIn;
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.name("ForgeData");
		writer.beginObject();
		ai.writeTo(writer);
		writer.endObject();
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		if(reader.peek() == JsonToken.NAME)
			reader.nextName();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if(name.equals("CustomAI")) {
				reader.beginArray();
				while (reader.hasNext()) {
					ai.add(new AIBase(ai, reader));
				}
				reader.endArray();
			}
			else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		return ai;
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
		if(node==ai)
			return 0;
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
		vector.add(ai);
		return vector.elements();
	}
	
	@Override
	public String toString() {
		return "ForgeData";
	}

	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.NAME_VALUE_PAIR;
	}
}
