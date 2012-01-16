/**
 * Is part of the controller package rather than view because it needs access to
 * {@link DatabaseConnector} and thus does more controller-y tasks than view-y.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import model.ListedItem;
import controller.AvailableItems;

/**
 * A container class for the typical view used in each tab. Consists of a
 * scrollable list on the side and a main view for displaying the selected item. <br>
 * Uses Java Generic Types to specify the type of items shown in the list.
 * 
 * @author 217262
 * @param <T>
 *            the type of items that this panel displays
 */
public class ScrollDisplayPanel<T extends ListedItem> extends JPanel {

	private final ItemList<T> scrollList;
	private final ItemEditPanel<T> editPanel;
	private final Container buttonContainer;
	private final AvailableItems<T> availableItems;

	/**
	 * Initializes a panel for viewing,
	 * 
	 * @param availableItems
	 *            a list of all the available items for displaying
	 * @param editPanel
	 *            the panel in which the items are displayed and edited
	 * @param name
	 *            name of the panel, used as name for the panel tab
	 */
	public ScrollDisplayPanel(AvailableItems<T> availableItems,
			ItemEditPanel<T> editPanel, String name) {

		this.setLayout(new BorderLayout());

		// Initiate arguments.
		this.scrollList = new ItemList<T>(availableItems);
		this.availableItems = availableItems;
		this.editPanel = editPanel;
		this.setName(name); // Used in tab

		/* The edit panel goes in the center */
		this.add(this.editPanel, BorderLayout.CENTER);

		// Initiate scroll panel on left.
		ScrollPane sideScrollPane = new ScrollPane();
		sideScrollPane.add(this.scrollList);
		sideScrollPane.setPreferredSize(new Dimension(180, 0));
		this.add(sideScrollPane, BorderLayout.LINE_START);

		// Buttons to the bottom
		this.buttonContainer = new EditButtonPanel();
		this.add(this.buttonContainer, BorderLayout.PAGE_END);

		// Event listeners for list selection
		// this.scrollList.addListSelectionListener(this.displayPanel);
		this.scrollList.addListSelectionListener(this.editPanel);

		// Select the first item in the list
		this.scrollList.setSelectedIndex(0);
	}

	/**
	 * An extension of {@link JList} for displaying specific types of
	 * 
	 * @author 217262
	 * @param <U>
	 *            type of items shown in the list
	 */
	class ItemList<U extends ListedItem> extends JList {

		/**
		 * @param availableItems
		 *            the available items that are to be lsited
		 */
		ItemList(AvailableItems<U> availableItems) {
			super(availableItems);
			this.setCellRenderer(new ListedItem.ItemCellRenderer());
		}

		/**
		 * Sets selection to a value based on its ID number.
		 * 
		 * @param ID
		 *            the ID of the item
		 */
		void setSelectedValueByID(int ID) {
			this.setSelectedValue(
					ScrollDisplayPanel.this.availableItems.getItemByID(ID),
					true);
			// Fire the selection event so the display is updated too.
			this.fireSelectionValueChanged(this.getSelectedIndex(),
					this.getSelectedIndex(), false);
		}
	}

	/**
	 * A panel showing the buttons used for editing.
	 * 
	 * @author 217262
	 */
	private class EditButtonPanel extends JPanel implements ActionListener {
		private final JToggleButton editButton;
		private final JButton addButton;
		private final JButton removeButton;
		private final JButton saveButton;
		private final JButton cancelButton;
		private boolean editModeEnabled;

		/**
		 * Creates a new {@link EditButtonPanel} containing buttons for
		 * enabling/disabling editing mode, adding/removing items as well as
		 * saving/discarding changes.
		 */
		public EditButtonPanel() {
			// Using flowlayout
			super(new FlowLayout(FlowLayout.LEADING));

			this.editButton = new JToggleButton("Edit");

			this.addButton = new JButton("+");
			this.removeButton = new JButton("−");
			this.saveButton = new JButton("Save");
			this.cancelButton = new JButton("Discard changes");

			this.add(this.editButton);
			this.add(this.addButton);
			this.add(this.removeButton);
			this.add(this.saveButton);
			this.add(this.cancelButton);

			this.editButton.addActionListener(this);
			this.addButton.addActionListener(this);
			this.removeButton.addActionListener(this);
			this.saveButton.addActionListener(this);
			this.cancelButton.addActionListener(this);
			this.setEditMode(false);
		}

		/* (non-Javadoc)
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * ) */
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source.equals(this.editButton)) {
				/* If there is an ongoing edit, the user must decide whether to
				 * save it or not. */
				if (this.editModeEnabled
						&& ScrollDisplayPanel.this.scrollList
								.getSelectedValue() != null) {
					int save = JOptionPane
							.showConfirmDialog(
									null,
									"Do you want to save the changes you have made to "
											+ ((ListedItem) ScrollDisplayPanel.this.scrollList
													.getSelectedValue())
													.getName()
											+ "?\nChoose Yes to save, No to discard all changes\nand Cancel to return to editing.",
									"Save changes",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE);
					// Handles the choice with helper methods
					switch (save) {
						case JOptionPane.YES_OPTION:
							this.saveEdits();
							break;
						case JOptionPane.NO_OPTION:
							this.cancelEdits();
							break;
						case JOptionPane.CANCEL_OPTION:
							// exit the method, edit mode isn't changing after
							// all
							return;
					}
				}
				// Set edit mode accordingly
				this.setEditMode(this.editButton.isSelected());

			} else if (source.equals(this.addButton)) {
				this.addNewItem();
			} else if (source.equals(this.removeButton)) {
				this.removeSelectedItem();
			} else if (source.equals(this.saveButton)) {
				this.saveEdits();
			} else if (source.equals(this.cancelButton)) {
				this.cancelEdits();
			}
		}

		private void setEditMode(boolean editEnabled) {
			this.editModeEnabled = editEnabled;

			/* Set the edit mode of the display panel too */
			ScrollDisplayPanel.this.editPanel.setEditMode(editEnabled);

			/* Disables the JList, so that edit mode must be exited between
			 * edits. This is not an optimal solution, but it prevents from
			 * having to insert even more checks and pop-ups. */
			ScrollDisplayPanel.this.scrollList.setEnabled(!editEnabled);

			this.editButton.setSelected(editEnabled);

			// Visibility of edit buttons
			this.addButton.setVisible(editEnabled);
			this.removeButton.setVisible(editEnabled);
			this.saveButton.setVisible(editEnabled);
			this.cancelButton.setVisible(editEnabled);
		}

		private void addNewItem() {
			int newItemID = ScrollDisplayPanel.this.availableItems
					.addEmptyItem();
			ScrollDisplayPanel.this.scrollList.setSelectedValueByID(newItemID);
		}

		/**
		 * 
		 */
		private void removeSelectedItem() {
			// The list can only contain values of type T
			@SuppressWarnings("unchecked")
			T selectedItem = (T) ScrollDisplayPanel.this.scrollList
					.getSelectedValue();

			int valinta = JOptionPane.showConfirmDialog(null,
					"Haluatko varmasti poistaa '" + selectedItem.getName()
							+ "' listalta?",
					"Poista " + selectedItem.getName(),
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

			if (valinta == JOptionPane.OK_OPTION) {
				ScrollDisplayPanel.this.availableItems.remove(selectedItem);
			}
			// Get out of the edit mode
			this.setEditMode(false);
		}

		/**
		 * Reverts edits back to the last saved state.
		 */
		private void cancelEdits() {

			T unedited = ScrollDisplayPanel.this.editPanel.getUneditedItem();

			int ID = ScrollDisplayPanel.this.availableItems
					.updateItem(unedited);

			// Sets the unedited value as selected
			ScrollDisplayPanel.this.scrollList.setSelectedValueByID(ID);

			// Cancel exits the edit mode.
			this.setEditMode(false);
		}

		/**
		 * Saves the changes made to the database
		 */
		private void saveEdits() {
			T edited = ScrollDisplayPanel.this.editPanel.getEditedItem();

			int ID = ScrollDisplayPanel.this.availableItems.updateItem(edited);

			// Sets the edited value as selected
			ScrollDisplayPanel.this.scrollList.setSelectedValueByID(ID);

			// Exits the edit mode
			this.setEditMode(false);

		}
	}
}
