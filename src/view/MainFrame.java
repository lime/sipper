/**
 * 
 */
package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

/**
 * @author Emil Sågfors
 */
public class MainFrame extends JFrame {

	public MainFrame(String title, JPanel[] panels) {
		super(title);

		// TODO maybe this class doesn't provide any added value?

		// Create a tabbed pane
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);

		// Add the tabs passed as arguments.
		for (JPanel panel : panels) {
			tabbedPane.addTab(panel.getName(), panel);
		}

		this.add(tabbedPane);
	}
}
