/**
 * 
 */
package view;

import java.awt.Color;
import java.text.NumberFormat;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import model.Amount;
import model.Ingredient;
import model.ListedItem;
import model.RecipeIngredients;
import model.Unit;
import controller.AvailableItems.AvailableIngredients;

/**
 * A {@link JTable} specifically for displaying the contents of a
 * {@link RecipeIngredients} object.
 * 
 * @author 217262
 */
public class RecipeIngredientsTable extends JTable {

	private TableCellEditor ingredientEditor;
	private final TableCellEditor amountEditor;
	private final TableCellEditor unitEditor;

	/**
	 * Creates a table for displaying the {@link Ingredient}s and {@link Amount}
	 * s contained in a {@link RecipeIngredients} list.
	 */
	public RecipeIngredientsTable() {

		// *Create cell editors for the table*/
		// Listen for changes to the list of ingredients
		IngredientUpdater ingredientUpdater = new IngredientUpdater();
		AvailableIngredients.getInstance().addListDataListener(
				ingredientUpdater);
		// call a first time to initialize combobox
		ingredientUpdater.updateIngredientComboBox();

		this.amountEditor = new DefaultCellEditor(new JFormattedTextField(
				NumberFormat.getNumberInstance()));

		// a drop-down menu for units
		ComboBoxModel unitBoxModel = new DefaultComboBoxModel(Unit.values());
		this.unitEditor = new DefaultCellEditor(new JComboBox(unitBoxModel));

	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean) */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		// Set colors and other stuff
		Color enabledBg = UIManager.getColor("Table.background");
		Color disabledBG = this.getParent().getBackground(); // "transparent"

		this.setBackground(enabled ? enabledBg : disabledBG);
		this.setShowGrid(enabled);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#tableChanged(javax.swing.event.TableModelEvent) */
	@Override
	public void tableChanged(TableModelEvent e) {
		super.tableChanged(e);

		// For some reason the cell editors need to be reassigned every time...

		// check that there actually are enough columns
		if (this.getColumnCount() > RecipeIngredients.INGREDIENT_COL
				&& this.getColumnCount() > RecipeIngredients.UNIT_COL) {

			// Get columns
			TableColumn nameColumn = this.getColumnModel().getColumn(
					RecipeIngredients.INGREDIENT_COL);
			TableColumn amountColumn = this.getColumnModel().getColumn(
					RecipeIngredients.AMOUNT_COL);
			TableColumn unitColumn = this.getColumnModel().getColumn(
					RecipeIngredients.UNIT_COL);

			// Set (relative) column widths
			nameColumn.setPreferredWidth(50);
			amountColumn.setPreferredWidth(10);
			unitColumn.setPreferredWidth(10);

			// Set cell editors to columns.
			nameColumn.setCellEditor(this.ingredientEditor);
			nameColumn.setCellRenderer(new ListedItem.ItemCellRenderer());
			amountColumn.setCellEditor(this.amountEditor);
			unitColumn.setCellEditor(this.unitEditor);

		}
	}

	/**
	 * A {@link ListDataListener} that keeps track of when the
	 * {@link AvailableIngredients} change and updates the renderer in the table
	 * to match.
	 * 
	 * @author 217262
	 */
	private class IngredientUpdater implements ListDataListener {

		/**
		 * Updates the ComboBox that shows the available ingredients by
		 * re-fetching the list of available items.
		 */
		private void updateIngredientComboBox() {

			// a drop-down menu for ingredients
			ComboBoxModel ingredientBoxModel = new DefaultComboBoxModel(
					AvailableIngredients.getInstance().toArray());
			JComboBox ingredientComboBox = new JComboBox(ingredientBoxModel);
			ingredientComboBox.putClientProperty("JComboBox.isTableCellEditor",
					Boolean.TRUE);

			// Renderer
			ingredientComboBox.setRenderer(new ListedItem.ItemCellRenderer());
			RecipeIngredientsTable.this.ingredientEditor = new DefaultCellEditor(
					ingredientComboBox);
		}

		/* (non-Javadoc)
		 * @see
		 * javax.swing.event.ListDataListener#contentsChanged(javax.swing.event
		 * .ListDataEvent) */
		@Override
		public void contentsChanged(ListDataEvent e) {
			this.updateIngredientComboBox();
		}

		/* (non-Javadoc)
		 * @see
		 * javax.swing.event.ListDataListener#intervalAdded(javax.swing.event
		 * .ListDataEvent) */
		@Override
		public void intervalAdded(ListDataEvent e) {
			this.updateIngredientComboBox();
		}

		/* (non-Javadoc)
		 * @see
		 * javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event
		 * .ListDataEvent) */
		@Override
		public void intervalRemoved(ListDataEvent e) {
			this.updateIngredientComboBox();
		}

	}
}
