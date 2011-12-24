/**
 * 
 */
package view;

import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A panel for viewing the information stored in a list item.
 * 
 * @author 217262
 */
public abstract class SelectionDisplayPanel<T> extends JPanel implements
		ListSelectionListener {

	public SelectionDisplayPanel() {
		this.setPreferredSize(new Dimension(500, 500));
	}

	/* (non-Javadoc)
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent) */
	@Override
	public void valueChanged(ListSelectionEvent event) {

		if (event.getValueIsAdjusting()) {
			// only a completed selection is interesting
			return;
		}

		/* Gets the selected item and updates the displayed item to match. It is
		 * assumed that the list only contains items of type T. Exceptions to
		 * this should not happen unless SelectionDisplayPanel is used outside
		 * the context it was meant to be used in. */
		T displayItem = null;
		try {
			JList displayItemList = (JList) event.getSource();
			// cast the selected item to T
			displayItem = (T) displayItemList.getSelectedValue();
		} catch (ClassCastException e) {
			System.err.println("SelectionDisplayPanel.valueChanged(): ");
			e.printStackTrace();
		}

		// Use the subclass-specific upadting method.
		this.updateDisplayedItem(displayItem);
	}

	/**
	 * Updates the item currently displayed.
	 * 
	 * @param displayItem
	 *            the new item to display
	 */
	protected abstract void updateDisplayedItem(T displayItem);

}
