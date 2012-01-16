package view;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

/**
 * The main application window of the program.
 * 
 * @author 217262
 */
public class MainFrame extends JFrame {

	/**
	 * Creates the main application window with a tab interface for displaying
	 * the provided panels.
	 * 
	 * @param title
	 *            the title for the frame
	 * @param panels
	 *            panels to be displayed as tabs in the frame
	 */
	public MainFrame(String title, JPanel[] panels) {
		super(title);

		/* Application logo */
		ImageIcon glassIcon = null;
		try {
			glassIcon = new ImageIcon(ImageIO.read(this.getClass().getResource(
					"/resource/glass.png")));
			// set as window icon
			this.setIconImage(glassIcon.getImage());
		} catch (IOException e) {
			// Do nothing, it's no big deal to lose the icon.
			e.printStackTrace();
		}

		/* Note: these actions do not affect the menu bar title or dock icon on
		 * Mac OS X, that is accomplished with the VM arguments:
		 * -Xdock:name=Sipper -Xdock:icon=src/resource/glass.png */

		// Create a panel with tabs.
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);

		// Add the tabs passed as arguments.
		for (JPanel panel : panels) {
			tabbedPane.addTab(panel.getName(), panel);
		}

		// set as content pane
		this.setContentPane(tabbedPane);
	}
}
