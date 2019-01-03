package vertical_spawn_control_client.minecraft;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.function.Supplier;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.tree.CollectionAccessProvider;
import vertical_spawn_control_client.tree.JsonSerializable;
import vertical_spawn_control_client.tree.OnNodeClickActionProvider;
import vertical_spawn_control_client.tree.TreeLeafAddNewElement;
import vertical_spawn_control_client.ui.MainWindow;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class PresetParser implements TreeNode, CollectionAccessProvider<TreeNode> {

	private MainWindow owner;
	public Vector<TreeNode> spawnLayers = new Vector<TreeNode>();
	public JTree tree;
	
	public PresetParser(MainWindow ownerIn) {
		owner = ownerIn;
	}
	
	public JTree newTree() {
		spawnLayers.add(new TreeLeafAddNewElement<TreeNode>(this,"<add new>", new  Supplier<TreeNode>() {

			@Override
			public TreeNode get() {
				return new SpawnLayer(PresetParser.this);
			}
			
		}));
		tree = new JTree(this);
		tree.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				TreePath path = tree.getPathForLocation(e.getX(),e.getY());
				if (path == null)
					return;
				Object node = path.getLastPathComponent();
				if(node instanceof OnNodeClickActionProvider) {
					OnNodeClickActionProvider onClickProvider = (OnNodeClickActionProvider) node;
					onClickProvider.onNodeClick(tree, owner.panel);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (e.getOldLeadSelectionPath() != null
						&& e.getOldLeadSelectionPath().getLastPathComponent() instanceof UIComponentsProvider) {
					UIComponentsProvider uiProvider = (UIComponentsProvider) e.getOldLeadSelectionPath()
							.getLastPathComponent();
					uiProvider.removeComponents(owner.panel);
				}
				
				Object selected = e.getNewLeadSelectionPath().getLastPathComponent();
				if(selected instanceof UIComponentsProvider) {
					int row = tree.getRowForPath(e.getNewLeadSelectionPath());
					Rectangle selectedRectangle = tree.getRowBounds(row);
					selectedRectangle.setLocation(selectedRectangle.x+selectedRectangle.width, selectedRectangle.y+selectedRectangle.height);
					UIComponentsProvider uiProvider = (UIComponentsProvider) selected;
					uiProvider.addComponents(owner.panel,selectedRectangle);
				}
			}});
		
		return tree;
	}
	
	public JTree presetToTree(File file) {
		try {
			this.readFromJSON(file);
			return this.newTree();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void readFromJSON(File file) throws IOException {
		spawnLayers.clear();
        JsonReader reader = new JsonReader(new FileReader(file));
        reader.setLenient(true);
        reader.beginArray();
		while (reader.hasNext()) {
			spawnLayers.add(new SpawnLayer(this, reader));
		}
		reader.endArray();
	}
	
	public void saveToFile(File file) {
		try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
			writer.setIndent(" ");
        	writer.beginArray();
        	for(TreeNode spawnLayer:spawnLayers) {
        		if(spawnLayer instanceof JsonSerializable) {
            		((JsonSerializable)spawnLayer).writeTo(writer);
        		}
        	}
        	writer.endArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public TreeNode getChildAt(int childIndex) {
		return spawnLayers.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return spawnLayers.size();
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		return spawnLayers.indexOf(node);
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
		return spawnLayers.elements();
	}
	
	@Override
	public String toString() {
		return "root";
	}

	@Override
	public void add(int index, TreeNode node) {
		this.spawnLayers.add(index,node);
	}

	public static PresetParser get(TreeNode parent) {
		TreeNode parent1 = parent;
		while (parent1 != null && !(parent1 instanceof PresetParser)) {
			parent1 = parent1.getParent();
		}
		return (PresetParser) parent1;
	}

	public void remove(SpawnLayer spawnLayer) {
		spawnLayers.remove(spawnLayer);
	}
}
