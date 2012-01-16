/**
 * 
 */
package view;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

/**
 * A panel for viewing the information stored in a list item.
 * 
 * @author 217262
 * @param <T>
 *            type of the displayed items
 */
public abstract class ItemEditPanel<T> extends JPanel implements
		ListSelectionListener {

	/**
	 * The default locale, more specifically Finnish (Finland), used for
	 * displaying currencies.
	 */
	protected static final Locale DEFAULT_LOCALE;
	/**
	 * NumberFormat for the default currency (EUR), with the added property of
	 * always rounding up (so the seller doesn't lose money even though the
	 * profit is very marginal).
	 */
	protected static final NumberFormat CURRENCY_FORMAT;

	/* initializes static constants */
	static {
		DEFAULT_LOCALE = new Locale("fi", "FI");

		CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(DEFAULT_LOCALE);
		CURRENCY_FORMAT.setRoundingMode(RoundingMode.UP);
	}

	static {
		/* Changes inactive text to by default be shown as normal text. Thus,
		 * disabling JTextFields and JTextAreas will make them unselectable and
		 * uneditable, yet showing text like when enabled (resembling JLabels in
		 * appearance). */
		Color defaultTextColor = UIManager.getColor("textText");
		if (defaultTextColor == null) {
			defaultTextColor = Color.BLACK;
		}
		UIManager.put("textInactiveText", defaultTextColor);
	}

	/* (non-Javadoc)
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent) */
	@SuppressWarnings("unchecked")
	@Override
	public void valueChanged(ListSelectionEvent event) {

		if (event.getValueIsAdjusting()) {
			// only a completed selection is interesting
			return;
		}

		// Gets the list that changed
		JList displayItemList = (JList) event.getSource();

		// Select the first value if no selection is made (like when removing)
		if (displayItemList.getSelectedIndex() == -1) {
			displayItemList.setSelectedIndex(0);
		}

		/* Gets the selected item and updates the displayed item to match. It is
		 * assumed that the list only contains items of type T. Exceptions to
		 * this should not happen unless ItemDisplayPanel is used outside the
		 * context it was meant to be used in. Therefore we can quite safely
		 * suppress the "unchecked" warning. */
		// cast the selected item to T
		T displayItem = (T) displayItemList.getSelectedValue();

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

	/**
	 * @return the original item before unsaved edits were made
	 */
	abstract T getUneditedItem();

	/**
	 * @return the item represented by the unsaved edits made
	 */
	abstract T getEditedItem();

	/**
	 * Gets the components that are to be enabled and disabled according to the
	 * editing state of the panel.
	 * 
	 * @return the components used for editing in this panel
	 */
	protected abstract JComponent[] getEditComponents();

	/**
	 * Enables or disables the editing mode of the panel.
	 * 
	 * @param editEnabled
	 *            whether editing should be enabled or not
	 */
	protected void setEditMode(boolean editEnabled) {
		/* The main point of this method is to make JTextFields and JTextAreas
		 * look more like JLabels. This happens by removing their borders and
		 * background, as well as setting fields to use enabled font even when
		 * disabled, like faked labels instead of switching between label and
		 * field. */

		Border emptyBorder = BorderFactory.createEmptyBorder();
		Color disabledBg = this.getBackground(); // same as container

		// Get the default border for fields
		Border enabledFieldBorder = UIManager.getBorder("TextField.border");
		if (enabledFieldBorder == null) {
			enabledFieldBorder = BorderFactory.createLoweredBevelBorder();
		}
		// Default background
		Color enabledFieldBg = UIManager.getColor("TextField.background");
		if (enabledFieldBg == null) {
			enabledFieldBg = Color.WHITE;
		}

		// Get color for enabled text
		Color enabledTextColor = UIManager.getColor("textText");
		if (enabledTextColor == null) {
			enabledTextColor = Color.BLACK; // safety
		}

		for (JComponent component : this.getEditComponents()) {

			// Disable/enable all editComponents
			component.setEnabled(editEnabled);

			// Set background color
			component.setBackground(editEnabled ? enabledFieldBg : disabledBg);

			if (component instanceof JTextComponent) {
				// Removes border when not editing
				component.setBorder(editEnabled ? enabledFieldBorder
						: emptyBorder);
				// Set to use enabled color when disabled
				((JTextComponent) component)
						.setDisabledTextColor(enabledTextColor);
			}
		}
	}
}
