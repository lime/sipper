/**
 * 
 */
package model;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

import controller.AvailableItems.AvailableIngredients;

/**
 * A list of ingredients and needed amounts. To be used in recipes.
 * 
 * @author 217262
 */
public class RecipeIngredients extends AbstractTableModel {

	/* Column constants */
	/**
	 * The column containing {@link Ingredient}s
	 */
	public static final int INGREDIENT_COL = 0;
	/**
	 * The column containing {@link Amount}s
	 */
	public static final int AMOUNT_COL = 1;
	/**
	 * The column containing {@link Unit}s
	 */
	public static final int UNIT_COL = 2;

	/**
	 * A list of the used ingredients and corresponding amounts.
	 */
	private final TreeMap<Ingredient, Amount> map;

	/**
	 * Constructor which initializes the {@link TreeMap} that backs this
	 * RecipeIngredients object.
	 */
	public RecipeIngredients() {
		this.map = new TreeMap<Ingredient, Amount>();
	}

	/**
	 * @param ingredient
	 *            the ingredient used as key
	 * @param amount
	 *            the amount of the specified ingredient
	 * @see java.util.TreeMap#put(Object, Object)
	 */
	public void put(Ingredient ingredient, Amount amount) {
		if (ingredient == null || amount == null) {
			return;
		} else {
			this.map.put(ingredient, amount);
			// Table has most likely changed, fire event.
			this.fireTableStructureChanged();
		}
	}

	/**
	 * @param ingredient
	 *            key for which mapping should be removed
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key. (A null return can also indicate that the map
	 *         previously associated null with key.)
	 * @see java.util.TreeMap#remove(java.lang.Object)
	 */
	private Amount remove(Ingredient ingredient) {
		// Save result
		Amount amount = this.map.remove(ingredient);
		// Fire event because table changes.
		this.fireTableStructureChanged();

		return amount;
	}

	/**
	 * @return a set view of the mappings contained in this map
	 * @see java.util.TreeMap#entrySet()
	 */
	public Set<Entry<Ingredient, Amount>> entrySet() {
		return this.map.entrySet();
	}

	/**
	 * @return a set view of the keys contained in this map
	 * @see java.util.TreeMap#keySet()
	 */
	public Set<Ingredient> keySet() {
		return this.map.keySet();
	}

	/**
	 * @param rowIndex
	 *            index of row to get
	 * @return a map {@link Entry} containing an {@link Ingredient} and an
	 *         {@link Amount}
	 */
	private Entry<Ingredient, Amount> getEntryAtRow(int rowIndex) {
		Iterator<Entry<Ingredient, Amount>> iterator = this.map.entrySet()
				.iterator();
		Entry<Ingredient, Amount> entry;

		// iterates all the way to the correct rows
		do {
			entry = iterator.next();
			rowIndex--;
		} while (iterator.hasNext() && rowIndex >= 0);

		return entry;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount() */
	@Override
	public int getColumnCount() {
		// ingreident name | amount | unit
		return 3;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount() */
	@Override
	public int getRowCount() {
		// number of key-value pairs
		return this.map.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int) */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Entry<Ingredient, Amount> entry = this.getEntryAtRow(rowIndex);

		switch (columnIndex) {
			case INGREDIENT_COL:
				return entry.getKey(); // ingredient
			case AMOUNT_COL:
				return entry.getValue().getQuantity();
			case UNIT_COL:
				return entry.getValue().getUnit();
			default:
				return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
	 * int, int) */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Entry<Ingredient, Amount> entry = this.getEntryAtRow(rowIndex);

		if (columnIndex == INGREDIENT_COL && value instanceof Ingredient) {
			// insert the new ingredient with the same amount
			Ingredient newIngredient = (Ingredient) value;
			Ingredient oldIngredient = entry.getKey();
			if (this.map.containsKey(newIngredient)) {
				/* Edits should not be allowed to set an existing ingredient,
				 * exit */
				return;
			}
			this.put(newIngredient, entry.getValue());
			this.remove(oldIngredient);

		} else if (columnIndex == AMOUNT_COL && value instanceof String) {
			// parse to double and set to amount value
			try {
				double doubleValue = Double.parseDouble((String) value);
				entry.getValue().setQuantity(doubleValue);
			} catch (NumberFormatException e) {
				// do nothing, wrong format
			}

		} else if (columnIndex == UNIT_COL && value instanceof Unit) {
			// set to amount unit
			entry.getValue().setUnit((Unit) value);

		}

		// fire updated if any changes were made
		this.fireTableStructureChanged();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int) */
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		/* all cells are editable, when the table is not supposed to be edited
		 * the entire table can be disabled instead */
		return true;
	}

	/**
	 * 
	 */
	public void insertEmptyRow() {
		// Get the first ingredient that is not yet in this map
		int i = 0;
		Ingredient ingredient;
		do {
			ingredient = AvailableIngredients.getInstance().getElementAt(i);
			i++;
		} while (this.map.containsKey(ingredient));

		// An empty amount
		Amount amount = new Amount(0, Unit.ML);

		// insert it into the table
		this.put(ingredient, amount);
	}

	/**
	 * Removes the specified row from the model.
	 * 
	 * @param row
	 *            row number to be removed
	 */
	public void removeRow(int row) {
		// get row entry and remove it
		this.remove(this.getEntryAtRow(row).getKey());
	}

	/**
	 * Copy constructor.
	 * 
	 * @param recipeIngredients
	 *            the ingredients that are to be copied
	 * @return A new instance of {@link RecipeIngredients} with the same
	 *         properties as <code>recipeIngredientss</code>.
	 */
	public static RecipeIngredients newInstance(
			RecipeIngredients recipeIngredients) {
		RecipeIngredients newIngredients = new RecipeIngredients();

		for (Entry<Ingredient, Amount> entry : recipeIngredients.entrySet()) {
			newIngredients.put(entry.getKey(), entry.getValue());
		}

		return newIngredients;
	}

}