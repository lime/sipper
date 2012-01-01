package controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Amount;
import model.Ingredient;
import model.Recipe;
import model.RecipeIngredients;
import model.Unit;

/**
 * @author 217262
 */
abstract class ResultSetParser<T> {

	public abstract T parseResultSet(ResultSet resultset) throws SQLException;

	public static BigDecimal getBigDecimal(ResultSet resultset, String columnLabel)
			throws SQLException, NumberFormatException {
		String numberString = resultset.getString(columnLabel);
		BigDecimal bigDecimal;
		if (numberString == null || numberString.isEmpty()) {
			bigDecimal = BigDecimal.ZERO;
		} else {
			bigDecimal = new BigDecimal(numberString);
		}
		return bigDecimal;
	}

	static class IngredientParser extends ResultSetParser<Ingredient> {

		/* (non-Javadoc)
		 * @see controller.ResultSetParser#parseResultSet(java.sql.ResultSet) */
		@Override
		public Ingredient parseResultSet(ResultSet resultset)
				throws SQLException {

			int ID = resultset.getInt(Ingredient.ID_COLUMN);
			String name = resultset.getString(Ingredient.NAME_COLUMN);

			BigDecimal alcoholContent = getBigDecimal(resultset,
					Ingredient.ALCOHOL_CONTENT_COLUMN);

			Amount containerSize = new Amount(
					resultset.getInt(Ingredient.CONTAINER_SIZE_VALUE_COLUMN),
					Unit.valueOf(resultset
							.getString(Ingredient.CONTAINER_SIZE_UNIT_COLUMN)));

			BigDecimal containerPrize = getBigDecimal(resultset,
					Ingredient.CONTAINER_PRIZE_COLUMN);

			return new Ingredient(ID, name, alcoholContent, containerSize,
					containerPrize);
		}
	}

	static class RecipeParser extends ResultSetParser<Recipe> {

		/* (non-Javadoc)
		 * @see controller.ResultSetParser#parseResultSet(java.sql.ResultSet) */
		@Override
		public Recipe parseResultSet(ResultSet resultset) throws SQLException {

			int ID = resultset.getInt(Recipe.ID_COLUMN);
			String name = resultset.getString(Recipe.NAME_COLUMN);

			// FIXME one-to-one relationship recipe-ingredientlist
			DatabaseLoader db = new DatabaseLoader(null);

			RecipeIngredients ingredients = db.getRecipeIngredients(ID);

			String instructions = resultset
					.getString(Recipe.INSTRUCTIONS_COLUMN);
			return new Recipe(ID, name, ingredients, instructions);
		}

	}

	/**
	 * @author 217262
	 */
	static class RecipeIngredientsParser extends
			ResultSetParser<RecipeIngredients> {

		/* (non-Javadoc)
		 * @see controller.ResultSetParser#parseResultSet(java.sql.ResultSet) */
		@Override
		public RecipeIngredients parseResultSet(ResultSet resultset)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
