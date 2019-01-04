package vertical_spawn_control_client.tree;

import java.io.IOException;

import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.json.SerializedJsonType;

public interface JsonSerializableTreeNode extends TreeNode {

	void writeTo(JsonWriter writer) throws IOException;

	void readFromJson(JsonReader reader) throws IOException;
	
	SerializedJsonType getSerializedJsonType();
}
