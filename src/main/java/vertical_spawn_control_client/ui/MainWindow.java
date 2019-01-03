package vertical_spawn_control_client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import vertical_spawn_control_client.Settings;
import vertical_spawn_control_client.minecraft.PresetParser;

public class MainWindow {
	
	public JFrame frame = new JFrame("Vertical Spawn Control preset editor");
	public JLayeredPane panel = new JLayeredPane();
    JScrollPane scroolPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private File file;
	private static final File settingsFile = new File("./settings.json");
	Settings settings = Settings.fromFile(settingsFile);
	PresetParser parser = new PresetParser(this);
	private JTree tree;
	
	public MainWindow() {
        //Create and set up the window.
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setBackground(Color.BLUE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                attemptExit();
            }
        });
        JMenuBar menuBar = new JMenuBar();
        frame.getContentPane().add(menuBar, BorderLayout.NORTH);
    	panel.setPreferredSize(new Dimension(1000,1000));
        scroolPane.setPreferredSize(new Dimension(800, 500));
        frame.getContentPane().add(scroolPane, BorderLayout.CENTER);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem mntmNewFile = new JMenuItem("New");
        mntmNewFile.addActionListener(e -> {
        	parser.spawnLayers.clear();
        	if(tree!=null && tree.getParent()!=null)
        		panel.remove(tree);
        	tree = parser.newTree();
        	tree.setBounds(0, 0, 1000, 2000);
        	panel.add(tree, JLayeredPane.DEFAULT_LAYER);
        	panel.setPreferredSize(new Dimension(1000,2000));
            frame.pack();
        });
        
        JMenuItem mntmOpenFile = new JMenuItem("Open...");
        mntmOpenFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setCurrentDirectory(settings.recentDirectory);
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
            	file = chooser.getSelectedFile();
            	settings.recentDirectory = file.getParentFile();
            	settings.toFile(settingsFile);
            	parser.spawnLayers.clear();
            	if(tree!=null && tree.getParent()!=null)
            		panel.remove(tree);
            	tree = parser.presetToTree(file);
            	tree.setBounds(0, 0, 1000, 2000);
            	panel.add(tree, JLayeredPane.DEFAULT_LAYER);
            	panel.setPreferredSize(new Dimension(1000,2000));
                frame.pack();
            }
        });
        JMenuItem mntmSaveFile = new JMenuItem("Save");
        mntmSaveFile.addActionListener(e-> {
        	if(file==null)
        		return;
        	parser.saveToFile(file);
        });
        JMenuItem mntmSaveAsFile = new JMenuItem("Save as...");
        mntmSaveAsFile.addActionListener(e-> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setCurrentDirectory(settings.recentDirectory);
            int result = chooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
            	file = chooser.getSelectedFile();
            	parser.saveToFile(file);
            }
        });
        
        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(e -> {
            attemptExit();
        });
        fileMenu.add(mntmNewFile);
        fileMenu.add(mntmOpenFile);
        fileMenu.add(mntmSaveFile);
        fileMenu.add(mntmSaveAsFile);
        fileMenu.add(mntmExit);
        frame.pack();
        frame.setVisible(true);
	}
	
    protected void attemptExit() {
//        if (confirmLoseChanges()) {
//            state.save();
            System.exit(0);
//        }
    }

    protected boolean confirmLoseChanges() {
  //      if (state.getNumUnsavedChanges() > 0) {
            int result = JOptionPane.showConfirmDialog(frame,
                    "You have " + 1
                            + " unsaved changes! Are you sure you want to discard them?",
                    "Discard Changes", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.NO_OPTION) {
                return false;
            }
//        }
        return true;
    }
}