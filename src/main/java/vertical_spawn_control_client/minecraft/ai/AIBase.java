package vertical_spawn_control_client.minecraft.ai;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.tree.TreeNode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import foghrye4.swing.tree.JsonSerializableTreeNode;
import foghrye4.swing.tree.TreeLeafBase;
import foghrye4.swing.tree.TreeNodeCollection;
import vertical_spawn_control_client.json.SerializedJsonType;
import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class AIBase implements TreeNode, UIComponentsProvider, JsonSerializableTreeNode {

	public final TreeNodeCollection<AIBase> parent;
	private AIModificatorAction actionModificator = AIModificatorAction.ADD;
	private AIAction action = AIAction.ATTACK_MELEE_FIXED_DAMAGE;	
	
	JComboBox<String> actionModificatorSelector = new JComboBox<String>(AIModificatorAction.toArrayOfString());
	JComboBox<String> actionSelector = new JComboBox<String>(AIAction.toArrayOfString());
	JButton removeButton = new JButton("Remove");
	public final Vector<TreeLeafBase> childs = new Vector<TreeLeafBase>();
	
	public AIBase(TreeNodeCollection<AIBase> parentIn, JsonReader reader) throws IOException {
		this(parentIn);
		this.readFromJson(reader);
	}
	
	public AIBase(TreeNodeCollection<AIBase> parentIn) {
		parent = parentIn;
		actionSelector.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				AIBase.this.onActionSelection(AIAction.valueOf(((String)actionSelector.getSelectedItem()).toUpperCase()));
			}
		});
		actionSelector.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AIBase.this.onActionSelection(AIAction.valueOf(((String)actionSelector.getSelectedItem()).toUpperCase()));
			}
		});
		
		actionModificatorSelector.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				AIBase.this.onModificatorSelection(AIModificatorAction.valueOf(((String)actionModificatorSelector.getSelectedItem()).toUpperCase()));
			}
		});
		actionModificatorSelector.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AIBase.this.onModificatorSelection(AIModificatorAction.valueOf(((String)actionModificatorSelector.getSelectedItem()).toUpperCase()));
			}});

		removeButton.addActionListener(a -> {
			parent.remove(AIBase.this);
			PresetParser.get().clearUI();
		});
	}
	

	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		reader.beginObject();
		Map<String,String> otherData = new HashMap<String,String>();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if(name.equals("add") || name.equals("remove")) {
				actionModificator = AIModificatorAction.valueOf(name.toUpperCase());
				action = AIAction.valueOf(reader.nextString().toUpperCase());
			}
			else {
				if(reader.peek() == JsonToken.BOOLEAN)
					otherData.put(name, String.valueOf(reader.nextBoolean()));
				else
					otherData.put(name, reader.nextString());
			}
		}
		reader.endObject();
		childs.clear();
		for(Function<TreeNode, ? extends TreeLeafBase> leafSupplier:action.params) {
			TreeLeafBase node = leafSupplier.apply(AIBase.this);
			node.parseValue(otherData.get(node.name));
			childs.add(node);
		}
	}
	
	public AIBase onActionSelection(AIAction newAction) {
		action = newAction;
		childs.clear();
		for(Function<TreeNode, ? extends TreeLeafBase> leafSupplier:action.params) {
			childs.add(leafSupplier.apply(AIBase.this));
		}
		PresetParser parser = PresetParser.get();
		parser.tree.updateUI();
		return this;
	}
	
	public AIBase onModificatorSelection(AIModificatorAction newAIModificatorAction) {
		actionModificator = newAIModificatorAction;
		childs.clear();
		for(Function<TreeNode, ? extends TreeLeafBase> leafSupplier:action.params) {
			childs.add(leafSupplier.apply(AIBase.this));
		}
		PresetParser parser = PresetParser.get();
		parser.tree.updateUI();
		return this;
	}
	
	protected void removeAll() {
		if (removeButton.getParent() != null) {
			removeButton.getParent().remove(removeButton);
			actionSelector.getParent().remove(actionSelector);
			actionModificatorSelector.getParent().remove(actionModificatorSelector);
		}
	}

	@Override
	public void removeComponents(JLayeredPane panel) {
		removeAll();
		panel.repaint();
		panel.getParent().repaint();
	}

	@Override
	public void addComponents(JLayeredPane panel, Rectangle rectangle) {
		actionSelector.setBounds(rectangle);
		rectangle.setLocation(rectangle.x, rectangle.y + rectangle.height);
		actionModificatorSelector.setBounds(rectangle);
		rectangle.setLocation(rectangle.x, rectangle.y + rectangle.height);
		removeButton.setBounds(rectangle);
		panel.add(actionSelector, JLayeredPane.POPUP_LAYER);
		panel.add(actionModificatorSelector, JLayeredPane.POPUP_LAYER);
		panel.add(removeButton, JLayeredPane.POPUP_LAYER);
		panel.getParent().repaint();
	}

	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name(actionModificator.name().toLowerCase());
		writer.value(action.name().toLowerCase());
		for(TreeNode node:childs) {
			if (node instanceof JsonSerializableTreeNode) {
				((JsonSerializableTreeNode)node).writeTo(writer);
			}
		}
		writer.endObject();
	}
	
	public void add(TreeLeafBase node) {
		childs.add(node);
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
	public Enumeration<TreeLeafBase> children() {
		return childs.elements();
	}

	@Override
	public String toString() {
		return actionModificator.name().toLowerCase()+":"+action.name().toLowerCase();
	}

	@Override
	public SerializedJsonType getSerializedJsonType() {
		return SerializedJsonType.OBJECT;
	}
}
