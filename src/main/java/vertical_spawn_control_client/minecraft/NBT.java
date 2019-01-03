package vertical_spawn_control_client.minecraft;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Vector;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import vertical_spawn_control_client.tree.CollectionAccessProvider;
import vertical_spawn_control_client.tree.JsonSerializable;
import vertical_spawn_control_client.tree.TreeLeafAddNewElement;
import vertical_spawn_control_client.tree.TreeNodeCollection;
import vertical_spawn_control_client.tree.*;

public class NBT implements CollectionAccessProvider<TreeNode>, JsonSerializable {
	public final TreeNode parent;
	public final TreeNodeCollection<TreeNode> attributes = new TreeNodeCollection<TreeNode>(this, "Attributes");
	public final TreeNodeCollection<TreeNode> potionEffects = new TreeNodeCollection<TreeNode>(this, "ActiveEffects");
	public final TreeNodeCollection<TreeNode> handItems = new TreeNodeCollection<TreeNode>(this, "HandItems");
	public final TreeNodeCollection<TreeNode> armorItems = new TreeNodeCollection<TreeNode>(this, "ArmorItems");
	public final ForgeData forgeData = new ForgeData(this);
	final Vector<TreeNode> childs = new Vector<TreeNode>();

	public NBT(TreeNode parentIn) {
		parent = parentIn;
		this.collectNodes();
	}

	private void collectNodes() {
		childs.add(attributes);
		childs.add(forgeData);
		childs.add(potionEffects);
		childs.add(handItems);
		childs.add(armorItems);
		
		attributes.add(new TreeLeafAddNewElement<TreeNode>(attributes, "<add new>", new Supplier<TreeNode>() {
			@Override
			public TreeNode get() {
				return new Attribute(attributes);
			}
		}));
		potionEffects.add(new TreeLeafAddNewElement<TreeNode>(potionEffects, "<add new>", new Supplier<TreeNode>() {
			@Override
			public TreeNode get() {
				return new PotionEffect(potionEffects);
			}
		}));
		handItems.add(new TreeLeafAddNewElement<TreeNode>(handItems, "<add new>", new Supplier<TreeNode>() {
			@Override
			public TreeNode get() {
				return new Item(handItems).setId("minecraft:stone_sword");
			}
		}));
		armorItems.add(new TreeLeafAddNewElement<TreeNode>(armorItems, "<add new>", new Supplier<TreeNode>() {
			@Override
			public TreeNode get() {
				return new Item(armorItems).setId("minecraft:leather_boots");
			}
		}));
		childs.add(new TreeLeafAddNewElement<TreeNode>(this, "<add new>", new Supplier<TreeNode>() {
			@Override
			public TreeNode get() {
				return new TreeNodeMutableNBTStringLeaf(NBT.this, "Health", "20.0");
			}
		}));
		
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		StringWriter swriter1 = new StringWriter();
		JsonWriter writer1 = new JsonWriter(swriter1);
		writer1.beginObject();
		for (TreeNode node : childs) {
			if (node instanceof JsonSerializable) {
				((JsonSerializable) node).writeTo(writer1);
			}
		}
		writer1.endObject();
		writer.name("nbt");
		writer.value(swriter1.toString());
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
		return "nbt";
	}

	@Override
	public void add(int index, TreeNode node) {
		this.childs.add(index, node);
	}

	public void remove(TreeNode treeNode) {
		this.childs.remove(treeNode);
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
					forgeData.readFrom(reader);
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
