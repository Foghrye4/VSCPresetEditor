package vertical_spawn_control_client.minecraft;

import java.io.IOException;
import java.io.StringReader;
import java.util.function.Supplier;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import foghrye4.swing.tree.JsonSerializableTreeNode;
import foghrye4.swing.tree.TreeNodeCollection;
import foghrye4.swing.tree.TreeNodeMutableNBTStringLeaf;

public class ItemNBT extends TreeNodeCollection<JsonSerializableTreeNode>  {
	public final TreeNodeCollection<JsonSerializableTreeNode> enchantments = new TreeNodeCollection<JsonSerializableTreeNode>(this, "ench",new Supplier<JsonSerializableTreeNode>() {
		@Override
		public JsonSerializableTreeNode get() {
			return new Enchantment(enchantments);
		}
	});
	
	public ItemNBT(Item parentIn) {
		super(parentIn, "tag", new Supplier<JsonSerializableTreeNode>() {
			@Override
			public JsonSerializableTreeNode get() {
				return new NBTBase(parentIn.nbt, "display");
			}
		});
		this.collectNodes();
	}

	private void collectNodes() {
		childs.add(enchantments);
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.name("tag");
		writer.beginObject();
		for (JsonSerializableTreeNode node : childs) {
				node.writeTo(writer);
		}
		writer.endObject();
	}

	@Override
	public String toString() {
		return "tag";
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		reader.beginObject();
		reader.setLenient(true);
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("ench")) {
				reader.beginArray();
				while (reader.hasNext()) {
					enchantments.add(new Enchantment(enchantments, reader));
				}
				reader.endArray();
			} else {
				JsonToken token = reader.peek();
				if(token == JsonToken.BEGIN_OBJECT) {
					NBTBase nbt = new NBTBase(this, name);
					nbt.readFromJson(reader);
					childs.add(childs.size() - 1, nbt);
				} else {
					childs.add(childs.size() - 1, new TreeNodeMutableNBTStringLeaf(this, name, reader.nextString()));
				}
			}
		}
		reader.endObject();
	}
}
