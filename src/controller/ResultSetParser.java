package controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Amount;
import model.Ingredient;
import model.Recipe;
import model.RecipeIngredients;
import model.Unit;
import controller.DatabaseConnector.DBConst;

/**
 * Helper classes which know how to parse some types of items from
 * {@link ResultSet}s.
 * 
 * @author 217262
 * @param <T>
 *            type that the parsed results will be in
 */
abstract class ResultSetParser<T> {

	/**
	 * Parses a {@link ResultSet} at its current position and returns the parsed
	 * item.
	 * 
	 * @param resultset
	 *            the resultset from which to parse items
	 * @return a parsed item from the database
	 * @throws SQLException
	 *             exceptions are to be handled by {@link DatabaseConnector}
	 */
	public abstract T parseResultSet(ResultSet resultset) throws SQLException;

	/**
	 * A perser of {@link Ingredient}s.
	 * 
	 * @author 217262
	 */
	static class IngredientParser extends ResultSetParser<Ingredient> {

		/* (non-Javadoc)
		 * @see controller.ResultSetParser#parseResultSet(java.sql.ResultSet) */
		@Override
		public Ingredient parseResultSet(ResultSet resultset)
				throws SQLException {
			/* Gets each field from the result set using DBConst column values */

			int ID = resultset.getInt(DBConst.INGREDIENT_TABLE.ID_COLUMN);

			String name = resultset
					.getString(DBConst.INGREDIENT_TABLE.NAME_COLUMN);

			double alcoholContent = resultset
					.getDouble(DBConst.INGREDIENT_TABLE.ALCOHOL_CONTENT_COLUMN);

			Amount containerSize = new Amount(
					resultset
							.getInt(DBConst.INGREDIENT_TABLE.CONTAINER_SIZE_VALUE_COLUMN),
					Unit.valueOf(resultset
							.getString(DBConst.INGREDIENT_TABLE.CONTAINER_SIZE_UNIT_COLUMN)));

			double containerPrize = resultset
					.getDouble(DBConst.INGREDIENT_TABLE.CONTAINER_PRICE_COLUMN);

			String store = resultset
					.getString(DBConst.INGREDIENT_TABLE.STORE_COLUMN);

			String comment = resultset
					.getString(DBConst.INGREDIENT_TABLE.COMMENT_COLUMN);

			return new Ingredient(ID, name, alcoholContent, containerSize,
					containerPrize, store, comment);
		}
	}

	/**
	 * A parser of {@link Recipe}s.
	 * 
	 * @author 217262
	 */
	static class RecipeParser extends ResultSetParser<Recipe> {

		/* (non-Javadoc)
		 * @see controller.ResultSetParser#parseResultSet(java.sql.ResultSet) */
		@Override
		public Recipe parseResultSet(ResultSet resultset) throws SQLException {

			// get ID
			int ID = resultset.getInt(DBConst.RECIPE_TABLE.ID_COLUMN);
			// get name
			String name = resultset.getString(DBConst.RECIPE_TABLE.NAME_COLUMN);

			DatabaseConnector db = DatabaseConnector.getInstance();

			// get ingredients
			RecipeIngredients ingredients = db.getRecipeIngredients(ID);

			// get instructions
			String instructions = resultset
					.getString(DBConst.RECIPE_TABLE.INSTRUCTIONS_COLUMN);

			return new Recipe(ID, name, ingredients, instructions);
		}

	}
}
