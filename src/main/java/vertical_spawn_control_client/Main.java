package vertical_spawn_control_client;

import javax.swing.SwingUtilities;

import vertical_spawn_control_client.ui.MainWindow;        

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainWindow();
            }
        });
    }
}
