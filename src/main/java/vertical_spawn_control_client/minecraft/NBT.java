package vertical_spawn_control_client.minecraft;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.function.Supplier;

import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import vertical_spawn_control_client.tree.JsonSerializableTreeNode;
import vertical_spawn_control_client.tree.TreeNodeCollection;
import vertical_spawn_control_client.tree.TreeNodeMutableNBTStringLeaf;

public class NBT extends TreeNodeCollection<JsonSerializableTreeNode>  {
	public final TreeNode parent;
	public final TreeNodeCollection<JsonSerializableTreeNode> attributes = new TreeNodeCollection<JsonSerializableTreeNode>(this, "Attributes",new Supplier<JsonSerializableTreeNode>() {
		@Override
		public JsonSerializableTreeNode get() {
			return new Attribute(attributes);
		}
	});
	public final TreeNodeCollection<JsonSerializableTreeNode> potionEffects = new TreeNodeCollection<JsonSerializableTreeNode>(this, "ActiveEffects", new Supplier<JsonSerializableTreeNode>() {
		@Override
		public JsonSerializableTreeNode get() {
			return new PotionEffect(potionEffects);
		}
	});
	public final TreeNodeCollection<JsonSerializableTreeNode> handItems = new TreeNodeCollection<JsonSerializableTreeNode>(this, "HandItems", new Supplier<JsonSerializableTreeNode>() {
		@Override
		public JsonSerializableTreeNode get() {
			return new Item(handItems).setId("minecraft:stone_sword");
		}
	});
	public final TreeNodeCollection<JsonSerializableTreeNode> armorItems = new TreeNodeCollection<JsonSerializableTreeNode>(this, "ArmorItems", new Supplier<JsonSerializableTreeNode>() {
		@Override
		public JsonSerializableTreeNode get() {
			return new Item(armorItems).setId("minecraft:leather_boots");
		}
	});
	public final ForgeData forgeData = new ForgeData(this);
	
	public NBT(EntitySpawnDefinition parentIn) {
		super(parentIn, "nbt", new Supplier<JsonSerializableTreeNode>() {
			@Override
			public JsonSerializableTreeNode get() {
				return new TreeNodeMutableNBTStringLeaf(parentIn.nbt, "Health", "20.0");
			}
		});
		parent = parentIn;
		this.collectNodes();
	}

	private void collectNodes() {
		childs.add(attributes);
		childs.add(forgeData);
		childs.add(potionEffects);
		childs.add(handItems);
		childs.add(armorItems);
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		StringWriter swriter1 = new StringWriter();
		JsonWriter writer1 = new JsonWriter(swriter1);
		writer1.beginObject();
		for (JsonSerializableTreeNode node : childs) {
				node.writeTo(writer1);
		}
		writer1.endObject();
		writer.name("nbt");
		writer.value(swriter1.toString());
	}

	@Override
	public String toString() {
		return "nbt";
	}
	
	public void read(String nbtStringRaw) throws IOException {
		StringBuffer formattedResult = new StringBuffer();
		boolean expectEndOfString = false;
		boolean allowColon = false;
		for (int i = 0; i < nbtStringRaw.length(); i++) {
			char c = nbtStringRaw.charAt(i);
			if (c == '"') {
				expectEndOfString = !expectEndOfString;
				allowColon = !allowColon;
			} else if (!expectEndOfString && isAcceptableString(c, false)) {
				formattedResult.append("\"");
				expectEndOfString = true;
			} else if (expectEndOfString && !isAcceptableString(c, allowColon)) {
				formattedResult.append("\"");
				expectEndOfString = false;
			}
			formattedResult.append(c);
		}

		try (JsonReader reader = new JsonReader(new StringReader(nbtStringRaw))) {
			if(reader.peek() == JsonToken.NAME)
				reader.nextName();
			reader.beginObject();
			reader.setLenient(true);
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("Attributes")) {
					reader.beginArray();
					while (reader.hasNext()) {
						attributes.add(new Attribute(attributes, reader));
					}
					reader.endArray();
				} else if (name.equals("ActiveEffects")) {
					reader.beginArray();
					while (reader.hasNext()) {
						potionEffects.add(new PotionEffect(potionEffects, reader));
					}
					reader.endArray();
				} else if (name.equals("HandItems")) {
					reader.beginArray();
					while (reader.hasNext()) {
						handItems.add(new Item(handItems, reader));
					}
					reader.endArray();
				} else if (name.equals("ArmorItems")) {
					reader.beginArray();
					while (reader.hasNext()) {
						armorItems.add(new Item(armorItems, reader));
					}
					reader.endArray();
				} else if (name.equals("ForgeData")) {
					forgeData.readFromJson(reader);
				} else {
					childs.add(childs.size() - 1, new TreeNodeMutableNBTStringLeaf(this, name, reader.nextString()));
				}
			}
			reader.endObject();
			reader.close();
		} catch (MalformedJsonException | IllegalStateException e) {
			MalformedJsonException e1 = new MalformedJsonException(nbtStringRaw);
			e1.addSuppressed(e);
			throw e1;
		}
	}

	private static boolean isAcceptableString(char c, boolean includeColon) {
		if (includeColon && c == ':')
			return true;
		return 97 <= c && c <= 122 || 65 <= c && c <= 90 || 47 <= c && c <= 57 || c == 46 || c == 95;
	}
}
