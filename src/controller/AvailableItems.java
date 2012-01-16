/**
 * 
 */
package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import model.Amount;
import model.Ingredient;
import model.ListedItem;
import model.Recipe;
import model.RecipeIngredients;
import model.Unit;

/**
 * A {@link ListModel} containing all the {@link ListedItem}s of a given type
 * that are available for use.
 * 
 * @author 217262
 * @param <T>
 *            The type of items contained in this model. Must subclass
 *            {@link ListedItem}.
 */
public abstract class AvailableItems<T extends ListedItem> extends
		AbstractListModel implements ChangeListener {

	/**
	 * The list that this class delegates requests to.
	 */
	protected List<T> availableList;

	/**
	 * Singleton instances created upon class loading.
	 */
	private static final AvailableIngredients INGREDIENTS_INSTANCE;
	private static final AvailableRecipes RECIPES_INSTANCE;
	static {
		INGREDIENTS_INSTANCE = new AvailableIngredients();
		RECIPES_INSTANCE = new AvailableRecipes();
	}

	/**
	 * A private constructor for prohibiting new instances to be created.
	 */
	private AvailableItems() {
		// add the sorter as a listener to always sort the list if it changes
		this.addListDataListener(new ListSorter());

		DatabaseConnector.getInstance().addChangeListener(this);

		// refresh the list to initialize and sort it
		this.refreshList();
	}

	/**
	 * Loads the list of items from the database.
	 * 
	 * @return the list of available items from the database
	 */
	protected abstract ArrayList<T> loadList();

	private void refreshList() {
		this.availableList = this.loadList();
		this.fireContentsChanged(this, 0, this.availableList.size());
	}

	/**
	 * Gets an instance of the item with the specified ID from the list.
	 * 
	 * @param ID
	 *            ID number of the ingredient
	 * @return an instance of the requested item, or <code>null</code> if list
	 *         contains no item with corresponding ID
	 */
	public T getItemByID(int ID) {
		/* This method requires an iteration to fetch by ID. The other option
		 * would have been a Map<ID, item>, but for sorting and fetching on
		 * index Lists worked better. Performance hit should be negligible. */
		for (T item : this.availableList) {
			if (item.getID() == ID) {
				return item;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int) */
	@Override
	public T getElementAt(int index) {
		return this.availableList.get(index);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize() */
	@Override
	public int getSize() {
		return this.availableList.size();
	}

	/* Here follows some delegate methods for accessing the underlying list.
	 * Most of them follow the same pattern: call the matching method on the
	 * list; store the return value if any; fire an event about the change;
	 * return the return value. */

	/**
	 * Appends the specified item to this list.
	 * 
	 * @param item
	 *            item to added to the list
	 * @return <code>true</code>, as specified by {@link Collection#add(Object)}
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(T item) {

		boolean result = this.availableList.add(item);
		// fire the added event
		this.fireIntervalAdded(this, this.availableList.lastIndexOf(item),
				this.availableList.lastIndexOf(item));

		return result;
	}

	/**
	 * Adds a new empty item to the list.
	 * 
	 * @return the ID of the created item
	 */
	public abstract int addEmptyItem();

	/**
	 * @param o
	 *            element whose presence in this list is to be tested
	 * @return a boolean value telling whether this structure already contains
	 *         the specified hobbit
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return this.availableList.contains(o);
	}

	/**
	 * @param index
	 *            index of the item to be removed
	 * @return the item previously at the specified postition
	 * @see java.util.List#remove(int)
	 */
	public T remove(int index) {
		// store the result of removing from the list
		T removed = this.availableList.remove(index);
		// call the other remove method to propagate changes to database too
		this.remove(removed);

		return removed;
	}

	/**
	 * Removes the item from the list of available animals
	 * 
	 * @param item
	 *            the item to remove
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public abstract void remove(T item);

	/**
	 * Updates the contents of the given argument to the list of available items
	 * 
	 * @param item
	 *            the item to remove
	 * @return the ID of the item (new if item was added)
	 */
	public abstract int updateItem(T item);

	// /**
	// * @see java.util.List#set(int, java.lang.Object)
	// */
	// private T set(int index, T element) {
	// T result = this.availableList.set(index, element);
	//
	// this.fireContentsChanged(this, index, index);
	//
	// return result;
	// }

	/**
	 * Shorthand method for getting the available items in array form.
	 * 
	 * @return an array of the available items
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {
		return this.availableList.toArray();
	}

	/* (non-Javadoc)
	 * @see
	 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent
	 * ) */
	@Override
	public void stateChanged(ChangeEvent e) {
		// Refresh the list from database since it apparently has changed.
		this.refreshList();
	}

	/**
	 * A singleton class for static access to the list of all available
	 * ingredients.
	 * 
	 * @author 217262
	 */
	public static class AvailableIngredients extends AvailableItems<Ingredient> {

		@Override
		protected ArrayList<Ingredient> loadList() {
			return DatabaseConnector.getInstance()
					.getAvailableIngredientsList();
		}

		/**
		 * Gets the signleton instance of this class
		 * 
		 * @return the singleton instance of this class
		 */
		public static AvailableIngredients getInstance() {
			return INGREDIENTS_INSTANCE;
		}

		/* (non-Javadoc)
		 * @see controller.AvailableItems#addEmptyItem() */
		@Override
		public int addEmptyItem() {
			// create a new ingredient, add it and return it
			Ingredient newIngredient = new Ingredient(null, "New Ingredient",
					0.0, new Amount(0, Unit.ML), 0.0, " ", " ");

			DatabaseConnector dbLoader = DatabaseConnector.getInstance();
			int ID = dbLoader.updateIngredient(newIngredient);

			return ID;
		}

		/* (non-Javadoc)
		 * @see controller.AvailableItems#remove(model.ListedItem) */
		@Override
		public void remove(Ingredient item) {
			// get index for event
			int index = this.availableList.indexOf(item);
			// save result
			DatabaseConnector.getInstance().removeIngredient(item);

			this.fireIntervalRemoved(this, index, index);
		}

		@Override
		public int updateItem(Ingredient item) {
			return DatabaseConnector.getInstance().updateIngredient(item);
		}

	}

	/**
	 *  A singleton class for static access to the list of all available
	 * recipes.
	 * @author 217262
	 */
	public static class AvailableRecipes extends AvailableItems<Recipe> {

		/* (non-Javadoc)
		 * @see controller.AvailableItems#loadList() */
		@Override
		protected ArrayList<Recipe> loadList() {
			return DatabaseConnector.getInstance().getAvailableRecipeList();
		}

		/**
		 * Gets the signleton instance of this class
		 * 
		 * @return the singleton instance of this class
		 */
		public static AvailableRecipes getInstance() {
			return RECIPES_INSTANCE;
		}

		/* (non-Javadoc)
		 * @see controller.AvailableItems#addEmptyItem() */
		@Override
		public int addEmptyItem() {
			// create a new recipe, add it and return it
			Recipe newRecipe = new Recipe(null, "New Recipe",
					new RecipeIngredients(), "");

			DatabaseConnector dbLoader = DatabaseConnector.getInstance();
			int ID = dbLoader.updateRecipe(newRecipe);

			return ID;
		}

		/* (non-Javadoc)
		 * @see controller.AvailableItems#remove(model.ListedItem) */
		@Override
		public void remove(Recipe item) {
			// get index for intervalremoved event
			int index = this.availableList.indexOf(item);
			// save result
			DatabaseConnector.getInstance().removeRecipe(item);

			this.fireIntervalRemoved(this, index, index);
		}

		@Override
		public int updateItem(Recipe item) {
			return DatabaseConnector.getInstance().updateRecipe(item);
		}

	}

	/**
	 * @author 217262
	 */
	private class ListSorter implements ListDataListener {

		/**
		 * Sorts the list of available items. May perform a check to see whether
		 * sorting is needed, but may also sort every time if it is deemed safe.
		 * 
		 * @param source
		 *            the event source, likely always <code>this</code> of the
		 *            containing class
		 */
		private void sortListIfNeeded(Object source) {
			// private class, so other sources are unlikely, but check still
			if (AvailableItems.this.equals(source)) {
				Collections.sort(AvailableItems.this.availableList);
			}
		}

		/* (non-Javadoc)
		 * @see
		 * javax.swing.event.ListDataListener#contentsChanged(javax.swing.event
		 * .ListDataEvent) */
		@Override
		public void contentsChanged(ListDataEvent e) {
			this.sortListIfNeeded(e.getSource());
		}

		/* (non-Javadoc)
		 * @see
		 * javax.swing.event.ListDataListener#intervalAdded(javax.swing.event
		 * .ListDataEvent) */
		@Override
		public void intervalAdded(ListDataEvent e) {
			this.sortListIfNeeded(e.getSource());
		}

		/* (non-Javadoc)
		 * @see
		 * javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event
		 * .ListDataEvent) */
		@Override
		public void intervalRemoved(ListDataEvent e) {
			this.sortListIfNeeded(e.getSource());
		}

	}

}
