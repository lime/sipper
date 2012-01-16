/**
 * 
 */
package model;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * A subinterface to {@link Comparable} for all items that are to be displayed
 * in the lists and panels of the GUI. Exists mainly for semantics, as "
 * {@link ListedItem}" has more meaning than just "{@link Comparable}".
 * 
 * @author 217262
 */
public abstract class ListedItem implements Comparable<ListedItem> {

	/**
	 * @return the name of the item
	 */
	public abstract String getName();

	/**
	 * @return the ID of the item
	 */
	public abstract Integer getID();

	@Override
	public int compareTo(ListedItem other) {
		// compare the ingredients based on names
		int nameCompare = this.getName().compareToIgnoreCase(other.getName());
		if (nameCompare == 0) {
			// same names compare by difference in ID
			return this.getID() - other.getID();
		} else {
			return nameCompare;
		}
	}

	/**
	 * A {@link ListCellRenderer} that sees to it that {@link ListedItem}s are
	 * rendered with the method {@link ListedItem#getName()} instead of the
	 * usual {@link Object#toString()}. Thus, no tampering with the
	 * toString-method is needed.
	 * 
	 * @author 217262
	 */
	public static class ItemCellRenderer extends DefaultListCellRenderer
			implements TableCellRenderer {

		/* (non-Javadoc)
		 * @see
		 * javax.swing.DefaultListCellRenderer#getListCellRendererComponent(
		 * javax.swing.JList, java.lang.Object, int, boolean, boolean) */
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			// get the name of the item if at all possible (should be)
			String name;
			try {
				name = ((ListedItem) value).getName();
			} catch (ClassCastException e) {
				name = value.toString(); // safety
			}

			// super method, but on the name instead of just the object
			return super.getListCellRendererComponent(list, name, index,
					isSelected, cellHasFocus);
		}

		/* (non-Javadoc)
		 * @see
		 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent
		 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int) */
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			String name;
			try {
				name = ((ListedItem) value).getName();
			} catch (ClassCastException e) {
				name = value.toString(); // safety
			}

			return new JLabel(name);
		}

	}
}
