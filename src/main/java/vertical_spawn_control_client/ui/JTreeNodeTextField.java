package vertical_spawn_control_client.ui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import vertical_spawn_control_client.minecraft.PresetParser;
import vertical_spawn_control_client.tree.TreeNodeValueHolder;

public class JTreeNodeTextField extends JTextField {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8376685979422110806L;
	private TreeNodeValueHolder owner;

	public JTreeNodeTextField(TreeNodeValueHolder ownerIn) {
		owner = ownerIn;
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(!JTreeNodeTextField.this.owner.accept(JTreeNodeTextField.this.getText())) {
					JTreeNodeTextField.this.setBackground(Color.YELLOW);
					return;
				}
				JTreeNodeTextField.this.setBackground(Color.WHITE);
				PresetParser.get().tree.updateUI();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (JTreeNodeTextField.this.owner instanceof UIComponentsProvider) {
						UIComponentsProvider uiProvider = (UIComponentsProvider) JTreeNodeTextField.this.owner;
						uiProvider.removeComponents(MainWindow.instance.panel);
					}
				}
			}
    	});

	}

}
