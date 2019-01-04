package vertical_spawn_control_client.ui;

import java.awt.Rectangle;

import javax.swing.JLayeredPane;

public interface UIComponentsProvider {

	void removeComponents(JLayeredPane panel);

	void addComponents(JLayeredPane panel, Rectangle rectangle);

}
