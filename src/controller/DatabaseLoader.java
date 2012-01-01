/**
 * 
 */
package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Amount;
import model.Ingredient;
import model.Recipe;
import model.RecipeIngredients;
import model.Unit;

/**
 * A helper class for managing connections to the SQLite database.
 * 
 * @author 217262
 */
class DatabaseLoader {

	// TODO don't throw so many SQLExceptions but handle them here?

	private String dbPath;
	private static final String DEFAULT_DB_PATH = "jdbc:sqlite:data/shroomware.db";
	private static final String RECIPE_TABLE = "recipes";
	private static final String INGREDIENT_TABLE = "importedSpirits"; // XXX
																		// "ingredients";
	private static final String RECIPE_INGREDIENT_RELATIONSHIP_TABLE = "recipeIngredients";
	private static final String RECIPE_ID_COLUMN = "recipeID";
	private static final String INGREDIENT_ID_COLUMN = "ingredientID";

	// private static final String SET_TABLE = "sets";

	DatabaseLoader(String path) {

		if (path == null || path.isEmpty()) {
			this.dbPath = DEFAULT_DB_PATH;
		} else {
			this.dbPath = path;
		}

		// Use the JDBC SQLite wrapper.
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err.println("DatabaseLoader: could not load sqlite driver.");
			e.printStackTrace();
		}
	}

	ArrayList<Recipe> getRecipeList() throws SQLException {

		return this.getListFromTable(RECIPE_TABLE, null,
				new ResultSetParser.RecipeParser());
	}

	/**
	 * @return a
	 * @throws SQLException
	 */
	ArrayList<Ingredient> getIngredientList() throws SQLException {

		return this.getListFromTable(INGREDIENT_TABLE, null,
				new ResultSetParser.IngredientParser());
	}

	private <T> ArrayList<T> getListFromTable(String table, String condition,
			ResultSetParser<T> parser) throws SQLException {

		ArrayList<T> parsedList = new ArrayList<T>();

		// Open the connection to the database and create a statement.
		Connection conn = DriverManager.getConnection(this.dbPath);

		// Prepares a statement for selecting all items from the given table
		// with a given condition if specified.
		String where = condition != null ? " WHERE " + condition : "";
		PreparedStatement stat = conn.prepareStatement("SELECT * FROM " + table
				+ where);
		ResultSet resultset = stat.executeQuery();

		// Parse the result set using the ResultSetParser class
		while (resultset.next()) {
			T parsedItem = parser.parseResultSet(resultset);
			parsedList.add(parsedItem);
		}

		// Close result set and connection.
		resultset.close();
		conn.close();

		return parsedList;
	}

	RecipeIngredients getRecipeIngredients(int recipeID) throws SQLException {
		// TODO generalize and move part to resultsetparser

		RecipeIngredients recipeIngredients = new RecipeIngredients();

		// Open the connection to the database and create a statement.
		Connection conn = DriverManager.getConnection(this.dbPath);

		// Select those with matching recipeID
		PreparedStatement stat = conn.prepareStatement("SELECT * FROM "
				+ RECIPE_INGREDIENT_RELATIONSHIP_TABLE + " WHERE "
				+ RECIPE_ID_COLUMN + " = " + recipeID);
		ResultSet resultset = stat.executeQuery();

		// TODO constants
		while (resultset.next()) {

			Ingredient ingredient = this.getIngredient(resultset
					.getInt("ingredientID"));
			Amount amount = new Amount(resultset.getDouble("amountValue"),
					Unit.valueOf(resultset.getString("amountUnit")));

			recipeIngredients.put(ingredient, amount);
		}

		return recipeIngredients;
	}

	/**
	 * @param ingredientID
	 * @return
	 * @throws SQLException
	 */
	private Ingredient getIngredient(int ingredientID) throws SQLException {
		ArrayList<Ingredient> list = this.getListFromTable(INGREDIENT_TABLE,
				"ID" + " = " + ingredientID,
				new ResultSetParser.IngredientParser());
		return list.get(0);
	}

	private Recipe getRecipe(int recipeID) throws SQLException {
		ArrayList<Recipe> list = this.getListFromTable(RECIPE_TABLE, "ID"
				+ " = " + recipeID, new ResultSetParser.RecipeParser());
		return list.get(0);
	}

	/* public static void main(String[] args) throws Exception {
	 * Class.forName("org.sqlite.JDBC"); Connection conn =
	 * DriverManager.getConnection(defaultPath); Statement stat =
	 * conn.createStatement();
	 * stat.executeUpdate("drop table if exists people;");
	 * stat.executeUpdate("create table people (name, occupation);");
	 * PreparedStatement prep = conn
	 * .prepareStatement("insert into people values (?, ?);"); prep.setString(1,
	 * "Gandhi"); prep.setString(2, "politics"); prep.addBatch();
	 * prep.setString(1, "Turing"); prep.setString(2, "computers");
	 * prep.addBatch(); prep.setString(1, "Wittgenstein"); prep.setString(2,
	 * "smartypants"); prep.addBatch(); conn.setAutoCommit(false);
	 * prep.executeBatch(); conn.setAutoCommit(true); ResultSet rs =
	 * stat.executeQuery("select * from people;"); while (rs.next()) {
	 * System.out.println("name = " + rs.getString("name"));
	 * System.out.println("job = " + rs.getString("occupation")); } rs.close();
	 * conn.close(); } */

}
