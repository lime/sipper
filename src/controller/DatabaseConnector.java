package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Amount;
import model.Ingredient;
import model.ListedItem;
import model.Recipe;
import model.RecipeIngredients;
import model.Unit;
import controller.DatabaseConnector.DBConst.INGREDIENT_TABLE;
import controller.DatabaseConnector.DBConst.RECIPE_INGREDIENT_RELATIONSHIP_TABLE;

/**
 * A helper class for managing connections to the SQLite database. <br>
 * Is of default visibility (as are all methods at the most) so that no access
 * happens from outside of the controller package. This is to limit the classes
 * that have direct access to these methods. Most are indirectly available when
 * using {@link AvailableItems}.
 * 
 * @author 217262
 */
class DatabaseConnector {

	private final String dbPath;
	private static final String DEFAULT_DB_PATH = "jdbc:sqlite:data/sipper.db";

	/**
	 * Connection to the database, should be synchronized when used to prevent
	 * problems with multiple connections. (Which should be supported by the
	 * JDBC SQLite but caused errors on Windows...)
	 */
	private Connection conn;
	/**
	 * A list of {@link ChangeListener}s that listen for changes in the
	 * database.
	 */
	protected List<ChangeListener> changeListeners;

	/**
	 * Singleton instance to use only one static connector.
	 */
	private static final DatabaseConnector SINGLETON_INSTANCE;
	static {
		SINGLETON_INSTANCE = new DatabaseConnector();
	}

	/**
	 * Constants of table and column names used for more flexibility in the
	 * database access code.
	 */
	static class DBConst {

		/**
		 * Constants for the table of ingredients.
		 */
		static final class INGREDIENT_TABLE {
			static final String NAME = "ingredients";
			static final String ID_COLUMN = "ID";
			static final String NAME_COLUMN = "name";
			static final String CONTAINER_PRICE_COLUMN = "containerPrice";
			static final String CONTAINER_SIZE_VALUE_COLUMN = "containerSizeValue";
			static final String CONTAINER_SIZE_UNIT_COLUMN = "containerSizeUnit";
			static final String ALCOHOL_CONTENT_COLUMN = "alcoholContent";
			static final String STORE_COLUMN = "store";
			static final String COMMENT_COLUMN = "comment";
		}

		/**
		 * Constants for the table of recipes.
		 */
		static final class RECIPE_TABLE {
			static final String NAME = "recipes";
			static final String ID_COLUMN = "ID";
			static final String NAME_COLUMN = "name";
			static final String INSTRUCTIONS_COLUMN = "instructions";
		}

		/**
		 * Constants for the many-to-many relationship table between recipes and
		 * ingredients.
		 */
		static final class RECIPE_INGREDIENT_RELATIONSHIP_TABLE {
			static final String NAME = "recipeIngredients";
			static final String RECIPE_ID_COLUMN = "recipeID";
			static final String INGREDIENT_ID_COLUMN = "ingredientID";
			static final String AMOUNT_VALUE_COLUMN = "amountValue";
			static final String AMOUNT_UNIT_COLUMN = "amountUnit";
		}

		/**
		 * Constants for the view of combined values from all three tables. Not
		 * in use in the current implementation.
		 */
		static final class RECIPE_INGREDIENT_JOINED_VIEW {
			static final String NAME = "ingredientsToRecipes";
		}

	}

	/**
	 * Singleton accessor that returns the static instance of
	 * {@link DatabaseConnector} that is available.
	 * 
	 * @return the singleton instance
	 */
	public static DatabaseConnector getInstance() {
		return SINGLETON_INSTANCE;
	}

	/**
	 * Private constructor that establishes database drivers and the like.
	 */
	private DatabaseConnector() {
		// use default path to database
		this.dbPath = DEFAULT_DB_PATH;

		/* Sets the JDBC SQLite wrapper to be used. It is worth to note that
		 * some drivers work better than others: the original Zentus driver
		 * (http://www.zentus.com/sqlitejdbc/) caused problems on Windows 7 with
		 * multiple connections, but the Xerial variant
		 * (http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC) wraps native
		 * versions of SQLite which eliminates the problem. */
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err
					.println("DatabaseConnector: could not load sqlite driver.");
			e.printStackTrace();
		}

		// Initialize connection so it is not null when first used.
		try {
			this.conn = DriverManager.getConnection(this.dbPath);
			this.conn.close();
		} catch (SQLException e) {
			this.handleSQLException(e);
		}

		// list of listeners
		this.changeListeners = new ArrayList<ChangeListener>();
	}

	/**
	 * @return a list of all available {@link Recipe}s in the database
	 */
	ArrayList<Recipe> getAvailableRecipeList() {

		return this.getListFromTable(DBConst.RECIPE_TABLE.NAME, null,
				new ResultSetParser.RecipeParser());

	}

	/**
	 * @return a list of all available {@link Ingredient}s in the database
	 */
	ArrayList<Ingredient> getAvailableIngredientsList() {
		// get the entire ingredient table and parse it
		return this.getListFromTable(INGREDIENT_TABLE.NAME, null,
				new ResultSetParser.IngredientParser());
	}

	private <T> ArrayList<T> getListFromTable(String table, String condition,
			ResultSetParser<T> parser) {

		ArrayList<T> parsedList = new ArrayList<T>();

		try {
			// Open the connection to the database and create a statement.
			this.conn = DriverManager.getConnection(this.dbPath);

			// Prepares a statement for selecting all items from the given table
			// with a given condition if specified.
			String where = condition != null ? " WHERE " + condition : "";
			PreparedStatement stat = this.conn
					.prepareStatement("SELECT * FROM " + table + where);
			ResultSet resultset = stat.executeQuery();

			// Parse the result set using the ResultSetParser class
			while (resultset.next()) {
				T parsedItem = parser.parseResultSet(resultset);
				parsedList.add(parsedItem);
			}

			// Close result set and connection.
			resultset.close();
			this.conn.close();

		} catch (SQLException e) {
			this.handleSQLException(e);
		}

		return parsedList;
	}

	/**
	 * Gets the {@link RecipeIngredients} for a specific recipe from the
	 * database
	 * 
	 * @param recipeID
	 *            ID of the requested {@link Recipe}
	 * @return the RecipeIngredients associated with the recipe
	 */
	RecipeIngredients getRecipeIngredients(int recipeID) {
		// Initialize an empty list of ingredients
		RecipeIngredients recipeIngredients = new RecipeIngredients();

		try {
			// Open the connection to the database and create a statement.
			this.conn = DriverManager.getConnection(this.dbPath);

			// Select those with matching recipeID
			PreparedStatement stat = this.conn
					.prepareStatement("SELECT * FROM "
							+ RECIPE_INGREDIENT_RELATIONSHIP_TABLE.NAME
							+ " WHERE "
							+ RECIPE_INGREDIENT_RELATIONSHIP_TABLE.RECIPE_ID_COLUMN
							+ " = " + recipeID);
			ResultSet resultset = stat.executeQuery();

			while (resultset.next()) {

				Ingredient ingredient = this
						.getIngredient(resultset
								.getInt(RECIPE_INGREDIENT_RELATIONSHIP_TABLE.INGREDIENT_ID_COLUMN));
				Amount amount = new Amount(
						resultset
								.getDouble(RECIPE_INGREDIENT_RELATIONSHIP_TABLE.AMOUNT_VALUE_COLUMN),
						Unit.valueOf(resultset
								.getString(RECIPE_INGREDIENT_RELATIONSHIP_TABLE.AMOUNT_UNIT_COLUMN)));

				recipeIngredients.put(ingredient, amount);
			}

			resultset.close();
			this.conn.close();
		} catch (SQLException e) {
			this.handleSQLException(e);
		}

		return recipeIngredients;
	}

	/**
	 * Gets a specific {@link Recipe} from the database based on ID.
	 * 
	 * @param recipeID
	 *            the ID to search by
	 * @return the first (and only) recipe with matching ID, or
	 *         <code>null</code> if no such entry exists
	 */
	@SuppressWarnings("unused")
	// unused, but left for completeness should the need arise
	private Recipe getRecipe(int recipeID) {
		// Gets the recipe where the ID column matches the argument.
		ArrayList<Recipe> list = this.getListFromTable(
				DBConst.RECIPE_TABLE.NAME, DBConst.RECIPE_TABLE.ID_COLUMN
						+ " = " + recipeID, new ResultSetParser.RecipeParser());

		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	/**
	 * Gets a specific {@link Ingredient} from the database based on ID.
	 * 
	 * @param ingredientID
	 *            the ID to search by
	 * @return the first (and only) ingredient with matching ID, or
	 *         <code>null</code> if no such entry exists
	 */
	private Ingredient getIngredient(Integer ingredientID) {
		ArrayList<Ingredient> list = this.getListFromTable(
				INGREDIENT_TABLE.NAME, INGREDIENT_TABLE.ID_COLUMN + " = "
						+ ingredientID, new ResultSetParser.IngredientParser());

		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	/**
	 * Updates a {@link Recipe} in the database. If the recipe has an ID of
	 * <code>null</code>, it will be inserted as a new entry and return the
	 * newly generated ID.
	 * 
	 * @param recipe
	 *            the recipe to update.
	 * @return the ID of the updated/inserted recipe
	 */
	int updateRecipe(Recipe recipe) {
		// The ID of the
		int recipeID = -1;

		try {
			// Connect to database
			this.conn = DriverManager.getConnection(this.dbPath);

			// Prepare a statement for populating a new recipe row
			PreparedStatement prep = this.conn
					.prepareStatement("INSERT OR REPLACE INTO "
							+ DBConst.RECIPE_TABLE.NAME + "("
							+ DBConst.RECIPE_TABLE.ID_COLUMN + ", "
							+ DBConst.RECIPE_TABLE.NAME_COLUMN + ", "
							+ DBConst.RECIPE_TABLE.INSTRUCTIONS_COLUMN
							+ ") VALUES (?1, ?2, ?3);");

			// Use values from selected recipe
			if (recipe.getID() != null) {
				/* Not setting the ID will leave it as null, which in turn means
				 * that a new entry row will be created. */
				prep.setInt(1, recipe.getID());
			}
			prep.setString(2, recipe.getName());
			prep.setString(3, recipe.getInstructions());

			// Execute (insert the new recipe)
			prep.execute();

			/* Get the newly generated ID of the new recipe (or the old one if
			 * that is the case) */
			recipeID = prep.getGeneratedKeys().getInt(1);

			/* ruthlessly delete all existing ingredient bindings from the
			 * recipe before adding the new list */
			prep = this.conn.prepareStatement("DELETE FROM "
					+ RECIPE_INGREDIENT_RELATIONSHIP_TABLE.NAME + " WHERE "
					+ RECIPE_INGREDIENT_RELATIONSHIP_TABLE.RECIPE_ID_COLUMN
					+ " = " + recipeID);
			prep.execute();

			// prepare a new statement for inserting the recipe ingredients
			prep = this.conn.prepareStatement("INSERT INTO "
					+ RECIPE_INGREDIENT_RELATIONSHIP_TABLE.NAME + "("
					+ RECIPE_INGREDIENT_RELATIONSHIP_TABLE.RECIPE_ID_COLUMN
					+ ", "
					+ RECIPE_INGREDIENT_RELATIONSHIP_TABLE.INGREDIENT_ID_COLUMN
					+ ", " + "amountValue" + ", " + "amountUnit"
					+ ") VALUES (?1, ?2, ?3, ?4);");

			// go through all ingredients and add the details
			for (Entry<Ingredient, Amount> ingredientEntry : recipe
					.getIngredients().entrySet()) {

				prep.setInt(1, recipeID); // recipeID
				prep.setInt(2, ingredientEntry.getKey().getID()); // ingredientID
				prep.setDouble(3, ingredientEntry.getValue().getQuantity()); // amountValue
				prep.setString(4, ingredientEntry.getValue().getUnit().name()); // amountUnit

				prep.addBatch();
			}
			// commit the ingredients in a batch
			this.conn.setAutoCommit(false);
			prep.executeBatch();
			this.conn.setAutoCommit(true);

			// Close the connection
			this.conn.close();

		} catch (SQLException e) {
			this.handleSQLException(e);
		}

		// Changes were made, inform listeners.
		this.fireChangeEvent();

		// return the ID, it might be new
		return recipeID;
	}

	/**
	 * Updates an {@link Ingredient} in the database. If the ingredient has an
	 * ID of <code>null</code>, it will be inserted as a new entry and return
	 * the newly generated ID.
	 * 
	 * @param ingredient
	 *            teh ingredient to update
	 * @return the ID of the updated/inserted ingredient
	 */
	int updateIngredient(Ingredient ingredient) {
		// The ID of the ingredient
		int ingredientID = -1;

		try {
			// Connect to database
			this.conn = DriverManager.getConnection(this.dbPath);

			// Prepare a statement for populating a new recipe row
			PreparedStatement prep = this.conn
					.prepareStatement("INSERT OR REPLACE INTO "
							+ DBConst.INGREDIENT_TABLE.NAME
							+ "("
							+ DBConst.INGREDIENT_TABLE.ID_COLUMN
							+ ", "
							+ DBConst.INGREDIENT_TABLE.NAME_COLUMN
							+ ", "
							+ DBConst.INGREDIENT_TABLE.CONTAINER_PRICE_COLUMN
							+ ", "
							+ DBConst.INGREDIENT_TABLE.CONTAINER_SIZE_VALUE_COLUMN
							+ ", "
							+ DBConst.INGREDIENT_TABLE.CONTAINER_SIZE_UNIT_COLUMN
							+ ", "
							+ DBConst.INGREDIENT_TABLE.ALCOHOL_CONTENT_COLUMN
							+ ", " + DBConst.INGREDIENT_TABLE.STORE_COLUMN
							+ ", " + DBConst.INGREDIENT_TABLE.COMMENT_COLUMN
							+ ") VALUES (?1,?2,?3,?4,?5,?6,?7,?8);");

			// Use values from selected recipe
			if (ingredient.getID() != null) {
				/* Not setting the ID will leave it as null, which in turn means
				 * that a new entry row will be created. */
				prep.setInt(1, ingredient.getID());
			}
			prep.setString(2, ingredient.getName());
			prep.setDouble(3, ingredient.getContainerPrize());
			prep.setDouble(4, ingredient.getContainerSize().getQuantity());
			prep.setString(5, ingredient.getContainerSize().getUnit().name());
			prep.setDouble(6, ingredient.getAlcoholContent());
			prep.setString(7, ingredient.getStore());
			prep.setString(8, ingredient.getComment());

			// Execute (insert the ingredient)
			prep.addBatch();
			prep.executeBatch();

			/* Get the (possibly new) ID */
			ingredientID = prep.getGeneratedKeys().getInt(1);

			this.conn.close();

		} catch (SQLException e) {
			this.handleSQLException(e);
		}

		// changes should be announced
		this.fireChangeEvent();

		return ingredientID;
	}

	/**
	 * Removes a {@link Recipe} from the table of recipes.
	 * 
	 * @param recipe
	 *            the recipe to be removed
	 */
	void removeRecipe(Recipe recipe) {
		this.removeListedItem(recipe, DBConst.RECIPE_TABLE.NAME,
				DBConst.RECIPE_TABLE.ID_COLUMN,
				DBConst.RECIPE_INGREDIENT_RELATIONSHIP_TABLE.RECIPE_ID_COLUMN);
	}

	/**
	 * Removes an {@link Ingredient} from the table of ingredient.
	 * 
	 * @param ingredient
	 *            the ingredient to be removed
	 */
	void removeIngredient(Ingredient ingredient) {
		this.removeListedItem(
				ingredient,
				DBConst.INGREDIENT_TABLE.NAME,
				DBConst.INGREDIENT_TABLE.ID_COLUMN,
				DBConst.RECIPE_INGREDIENT_RELATIONSHIP_TABLE.INGREDIENT_ID_COLUMN);
	}

	/**
	 * Helper method. Removes a {@link ListedItem} from the specified table.<br>
	 * Also removes the relationship bindings from the many-to-many relationship
	 * table to be sure, although the table is configured to remove these by
	 * itself.
	 * 
	 * @param item
	 *            the item to be removed from the table
	 * @param tableName
	 *            the table from which to remove
	 * @param idColumn
	 *            the name of the column containing the ID value
	 * @param relationshipColumn
	 *            the column in the relationship table that represents the same
	 *            ID value
	 */
	private void removeListedItem(ListedItem item, String tableName,
			String idColumn, String relationshipColumn) {
		try {
			// get connection
			this.conn = DriverManager.getConnection(this.dbPath);

			// Prepare a statement for removing a row
			PreparedStatement prep = this.conn.prepareStatement("DELETE FROM "
					+ tableName + " WHERE " + idColumn + "= ?1");

			// set id field
			prep.setInt(1, item.getID());
			// execute into database
			prep.execute();

			// Remove the corresponding rows from the relationship table
			prep = this.conn.prepareStatement("DELETE FROM "
					+ DBConst.RECIPE_INGREDIENT_RELATIONSHIP_TABLE.NAME
					+ " WHERE " + relationshipColumn + "= ?1");

			// set id field
			prep.setInt(1, item.getID());
			// execute into database
			prep.execute();

			// close connection
			this.conn.close();

		} catch (SQLException e) {
			this.handleSQLException(e);
		}

		// most likely removed something
		this.fireChangeEvent();
	}

	/**
	 * Registers a {@link ChangeListener} with this database, that will be
	 * informed when the database contents change.
	 * 
	 * @param listener
	 *            the ChangeListener object
	 */
	void addChangeListener(ChangeListener listener) {
		this.changeListeners.add(listener);
	}

	/**
	 * Calls the {@link ChangeListener#stateChanged(ChangeEvent)} method of all
	 * listeners registered on this database.
	 */
	protected void fireChangeEvent() {
		ChangeEvent event = new ChangeEvent(this);
		// call all listeners
		for (ChangeListener listener : this.changeListeners) {
			listener.stateChanged(event);
		}
	}

	/**
	 * Handles any {@link SQLException} by informing the user that an error
	 * occurred.
	 * 
	 * @param e
	 */
	private void handleSQLException(SQLException e) {
		// Also print the stack trace for debugging purposes
		e.printStackTrace();

		String title = "SQL Error: " + e.getLocalizedMessage();

		String message = "An SQL Error occurred.";
		do {
			message = message.concat("\n\n" + e.getLocalizedMessage());
			for (StackTraceElement trace : e.getStackTrace()) {
				/* finds the first trace that isn't native or part of SQLite
				 * driver */
				if (!trace.isNativeMethod()
						&& !trace.getClassName().startsWith("org.sqlite")) {
					message = message.concat(":\n\t" + trace.toString());
					break;
				}
			}
		} while ((e = e.getNextException()) != null);

		// Show an error message dialog about the error.
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.ERROR_MESSAGE);

	}

}
