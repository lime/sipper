/**
 * 
 */
package model;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

/**
 * A list of ingredients and needed amounts. To be used in recipes.
 * 
 * @author Emil Sågfors
 */
// XXX name confusion with database list
public class RecipeIngredients extends AbstractTableModel {

	/**
	 * A map of the used ingredients and corresponding amounts.
	 */
	private final TreeMap<Ingredient, Amount> map;

	public RecipeIngredients() {
		this.map = new TreeMap<Ingredient, Amount>();
	}

	public Amount get(Ingredient ingredient) {
		return this.map.get(ingredient);
	}

	/**
	 * @param ingredient
	 * @param amount
	 */
	public void put(Ingredient ingredient, Amount amount) {
		this.map.put(ingredient, amount);
	}

	/**
	 * @return
	 * @see java.util.TreeMap#entrySet()
	 */
	public Set<Entry<Ingredient, Amount>> entrySet() {
		return this.map.entrySet();
	}

	/**
	 * @return
	 * @see java.util.TreeMap#navigableKeySet()
	 */
	public NavigableSet<Ingredient> navigableKeySet() {
		return this.map.navigableKeySet();
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
		Iterator<Entry<Ingredient, Amount>> iterator = this.map.entrySet()
				.iterator();
		Entry<Ingredient, Amount> entry;
		do {
			entry = iterator.next();
			rowIndex--;
		} while (iterator.hasNext() && rowIndex >= 0);

		switch (columnIndex) {
			case 0:
				return entry.getKey().getName(); // ingredient
			case 1:
				return entry.getValue().toUnit(
						entry.getValue().getOriginalUnit()).stripTrailingZeros();
			case 2:
				return entry.getValue().getOriginalUnit();
			default:
				return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int) */
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	/* int[] leveydet = { 10, 80, 50, 30, 120 }; OtsikkoRenderer renderer = new
	 * OtsikkoRenderer(); for (int i = 0; i <
	 * tulosTable.getColumnModel().getColumnCount(); i++) { TableColumn sarake =
	 * tulosTable.getColumnModel().getColumn(i);
	 * sarake.setPreferredWidth(leveydet[i]); // tyyliä otsikoille
	 * sarake.setHeaderRenderer(renderer); } */// TODO columnmodel
}