package vertical_spawn_control_client.tree;

import java.io.IOException;

import com.google.gson.stream.JsonWriter;

public interface JsonSerializable {

	void writeTo(JsonWriter writer) throws IOException;
}
