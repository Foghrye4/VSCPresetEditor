package foghrye4.swing.tree;

import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JLayeredPane;

import vertical_spawn_control_client.minecraft.EntityNBT;
import vertical_spawn_control_client.minecraft.NBTBase;
import vertical_spawn_control_client.minecraft.PresetParser;

public class TreeNodeRemovableNBTStringLeaf extends TreeNodeStringLeaf {

	JButton removeButton = new JButton("Remove");
	private TreeLeafAddNewElement<JsonSerializableTreeNode> creator;
	
	public TreeNodeRemovableNBTStringLeaf(TreeNodeCollection<JsonSerializableTreeNode> parentIn, String nameIn, String valueIn, TreeLeafAddNewElement<JsonSerializableTreeNode> creatorIn) {
		super(parentIn, nameIn, valueIn);
		creator = creatorIn;
    	removeButton.addActionListener(a-> {
    		((NBTBase)parent).remove(TreeNodeRemovableNBTStringLeaf.this);
    		((NBTBase)parent).nodeSuppliers.add(creator);
    		PresetParser.get().clearUI();
    		PresetParser.get().tree.updateUI();
    	});
	}
	
	@Override
	public void removeComponents(JLayeredPane panel) {
		panel.remove(removeButton);
		super.removeComponents(panel);
	}

	@Override
	public void addComponents(JLayeredPane panel, Rectangle rectangle) {
		super.addComponents(panel, rectangle);
		rectangle.setLocation(rectangle.x, rectangle.y + rectangle.height);
		removeButton.setBounds(rectangle);
		panel.add(removeButton, JLayeredPane.POPUP_LAYER);
		panel.getParent().repaint();
	}


}
