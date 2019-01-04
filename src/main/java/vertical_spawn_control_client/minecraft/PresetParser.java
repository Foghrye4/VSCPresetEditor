package vertical_spawn_control_client.minecraft;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.function.Supplier;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vertical_spawn_control_client.tree.JsonSerializableTreeNode;
import vertical_spawn_control_client.tree.OnNodeClickActionProvider;
import vertical_spawn_control_client.tree.TreeNodeCollection;
import vertical_spawn_control_client.ui.MainWindow;
import vertical_spawn_control_client.ui.UIComponentsProvider;

public class PresetParser extends TreeNodeCollection<SpawnLayer> implements ClipboardOwner {

	private MainWindow owner;
	public JTree tree;
	public Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	public PresetParser(MainWindow ownerIn) {
		super(null, "root", new  Supplier<SpawnLayer>() {

			@Override
			public SpawnLayer get() {
				return new SpawnLayer(ownerIn.parser);
			}
			
		});
		owner = ownerIn;
	}
	
	public JTree newTree() {
		tree = new JTree(this);
		tree.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				JsonSerializableTreeNode node = PresetParser.this.getSelectedNode();
				if(node == null)
					return;
				if(e.getModifiers() != 2) // Ctrl
					return;
				if(e.getKeyCode() == 67) { //c
					PresetParser.this.copy(node);
				}
				
				if(e.getKeyCode() == 86) {//v
					PresetParser.this.paste(node);
				}
			}
		});
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
					selectedRectangle.width = 200;
					UIComponentsProvider uiProvider = (UIComponentsProvider) selected;
					uiProvider.addComponents(owner.panel,selectedRectangle);
				}
			}});
		
		return tree;
	}
	
	public JsonSerializableTreeNode getSelectedNode() {
		TreePath path = tree.getSelectionPath();
		if (path == null)
			return null;
		Object selected = path.getLastPathComponent();
		if (!(selected instanceof JsonSerializableTreeNode))
			return null;
		return (JsonSerializableTreeNode) selected;
	}

	public void paste(JsonSerializableTreeNode node) {
		Object data = null;
		try {
			data = clipboard.getData(DataFlavor.stringFlavor);
			if(data==null)
				return;
			StringReader sreader = new StringReader((String)data);
			JsonReader reader = new JsonReader(sreader);
			switch(node.getSerializedJsonType()) {
			case NAME_VALUE_PAIR:
				reader.beginObject();
				break;
			case PRIMITIVE:
				reader.beginObject();
				reader.nextName();
				break;
			case OBJECT:
			default:
				break;
			}
			node.readFromJson(reader);
			switch(node.getSerializedJsonType()) {
			case PRIMITIVE:
			case NAME_VALUE_PAIR:
				reader.endObject();
				break;
			case OBJECT:
			default:
				break;
			}
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		this.tree.updateUI();
	}

	public void copy(JsonSerializableTreeNode node) {
		StringWriter swriter = new StringWriter();
		JsonWriter writer = new JsonWriter(swriter);
		writer.setLenient(true);
		try {
			switch(node.getSerializedJsonType()) {
			case NAME_VALUE_PAIR:
				writer.beginObject();
				break;
			case PRIMITIVE:
				writer.beginObject();
				writer.name("primitive");
				break;
			case OBJECT:
			default:
				break;
			}
			node.writeTo(writer);
			switch(node.getSerializedJsonType()) {
			case PRIMITIVE:
			case NAME_VALUE_PAIR:
				writer.endObject();
				break;
			case OBJECT:
			default:
				break;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String toBuffer = swriter.toString();
		clipboard.setContents(new Transferable() {
			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.stringFlavor };
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return flavor.equals(DataFlavor.stringFlavor);
			}

			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				return toBuffer;
			}
		}, PresetParser.this);
		clearUI();
		this.tree.updateUI();
	}
	
	public void clearUI() {
		for(Component component:this.owner.panel.getComponents()) {
			if(component!=tree)
				this.owner.panel.remove(component);
		};
	}

	public JTree presetToTree(File file) {
		try(FileReader fileReader = new FileReader(file)) {
			this.readFromJSON(fileReader);
			return this.newTree();
		} catch (IOException | IllegalStateException e) {
    		return null;
		}
	}
	
	private void readFromJSON(FileReader fileReader) throws IOException {
        JsonReader reader = new JsonReader(fileReader);
        this.readFromJson(reader);
	}
	
	@Override
	public void readFromJson(JsonReader reader) throws IOException {
		childs.clear();
        reader.setLenient(true);
        reader.beginArray();
		while (reader.hasNext()) {
			childs.add(new SpawnLayer(this, reader));
		}
		reader.endArray();
	}
	
	public void saveToFile(File file) {
		try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
			this.writeTo(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.setIndent(" ");
    	writer.beginArray();
    	for(JsonSerializableTreeNode spawnLayer:childs) {
       		spawnLayer.writeTo(writer);
    	}
    	writer.endArray();
	}

	@Override
	public String toString() {
		return "root";
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {	}

	public static PresetParser get() {
		return MainWindow.instance.parser;
	}
}
