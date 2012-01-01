/**
 * 
 */
package controller;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import model.Ingredient;
import model.Recipe;
import model.Unit;

/**
 * @author 217262
 */
public class Testing {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) {
		locales();
		//bigDecimalIngredients();
		//bigDecimalRecipes();
	}


	private static void locales() {
		for (Locale locale : NumberFormat.getAvailableLocales()) {
		System.out.println("Testing.main() "+locale.getDisplayName() + ": "+locale.getCountry());	
		}
	}

	private static void bigDecimalIngredients() {
		// TODO Auto-generated method stub
		DatabaseLoader db = new DatabaseLoader(null);
		ArrayList<Ingredient> list = null;
		try {
			list = db.getIngredientList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Ingredient ingredient : list) {
			StringBuilder sb = new StringBuilder();
			sb.append(ingredient.getUnitPrice(Unit.L) + "\t");
			sb.append(ingredient.getUnitPrice(Unit.DL) + "\t");
			sb.append(ingredient.getUnitPrice(Unit.ML) + "\t");
			sb.append(ingredient.getName() + "\t");
			// sb.append(ingredient.getContainerSize() + "\t");
			sb.append(ingredient.getContainerPrize() + "\t");

			System.out.println(sb);
		}
	}
		
		private static void bigDecimalRecipes() {
			// TODO Auto-generated method stub
			DatabaseLoader db = new DatabaseLoader(null);
			ArrayList<Recipe> list = null;
			try {
				list = db.getRecipeList();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (Recipe recipe : list) {
				StringBuilder sb = new StringBuilder();
				sb.append(recipe.getPrice() + "\t");
				sb.append(recipe.getName() + "\t");

				System.out.println(sb);
			}
}
}
