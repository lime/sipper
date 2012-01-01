/**
 * 
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JPanel;

/**
 * A container class for the typical view used in each tab. Consists of a
 * scrollable list on the side and a main view for displaying the selected item. <br>
 * Uses Java generics to define the type of items shown in the list. May be
 * buggy, hopefully not. :)
 * 
 * @author Emil Sågfors
 */
public class ScrollViewPanel<T> extends JPanel {

	private final JList scrollList;
	private final SelectionDisplayPanel<T> displayPanel;

	/**
	 * 
	 */
	public ScrollViewPanel(ArrayList<T> displayItemList,
			SelectionDisplayPanel<T> displayPanel, String name) {

		this.setLayout(new BorderLayout());

		// Initiate arguments.
		this.scrollList = new JList(displayItemList.toArray());
		this.displayPanel = displayPanel;
		this.setName(name); // Used in tab

		// Set the display panel in the center
		this.add(this.displayPanel, BorderLayout.CENTER);

		// translucent backgrounds
		this.setBackground(new Color(0, true));
		this.displayPanel.setBackground(this.getBackground());

		// Initiate scroll panel on left.
		ScrollPane sideScrollPane = new ScrollPane();
		sideScrollPane.add(this.scrollList);
		sideScrollPane.setPreferredSize(new Dimension(200, 0));
		this.add(sideScrollPane, BorderLayout.LINE_START);

		// Event listeners.
		this.scrollList.addListSelectionListener(this.displayPanel);
	}
}
