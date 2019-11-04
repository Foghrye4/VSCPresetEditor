package vertical_spawn_control_client.ui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import foghrye4.swing.tree.TreeNodeValueHolder;
import vertical_spawn_control_client.minecraft.PresetParser;

public class JTreeNodeTextArea extends JTextArea {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8376685979422110806L;
	private TreeNodeValueHolder owner;

	public JTreeNodeTextArea(TreeNodeValueHolder ownerIn) {
		owner = ownerIn;
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(!JTreeNodeTextArea.this.owner.accept(JTreeNodeTextArea.this.getText())) {
					JTreeNodeTextArea.this.setBackground(Color.YELLOW);
					return;
				}
				JTreeNodeTextArea.this.setBackground(Color.WHITE);
				PresetParser.get().tree.updateUI();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (JTreeNodeTextArea.this.owner instanceof UIComponentsProvider) {
						UIComponentsProvider uiProvider = (UIComponentsProvider) JTreeNodeTextArea.this.owner;
						uiProvider.removeComponents(MainWindow.instance.panel);
					}
				}
			}
    	});

	}

}
