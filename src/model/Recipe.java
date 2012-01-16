/**
 * 
 */
package model;

import java.util.Map.Entry;

/**
 * A class representing a recipe.
 * 
 * @author 217262
 */
public class Recipe extends ListedItem {

	/**
	 * The ID number of the recipe. Must be unique.
	 */
	private final Integer ID;
	/**
	 * The name of the drink.
	 */
	private final String name;
	/**
	 * The list of ingredients used in the drink.
	 */
	private final RecipeIngredients ingredients;
	/**
	 * The instructions for preparing the drink.
	 */
	private final String instructions;

	/**
	 * Constructs a new recipe object with the given attributes.
	 * 
	 * @param ID
	 *            unique ID of this recipe
	 * @param name
	 *            name of drink
	 * @param ingredients
	 *            the ingredients and amounts used in this recipe
	 * @param instructions
	 *            optional instructions for prepaoring the recipe
	 */
	public Recipe(Integer ID, String name, RecipeIngredients ingredients,
			String instructions) {
		this.ID = ID;
		this.name = name;
		this.ingredients = ingredients;
		this.instructions = instructions;
	}

	/**
	 * @return the ID of the drink
	 */
	@Override
	public Integer getID() {
		return this.ID;
	}

	/**
	 * @return the name of the drink
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @return the ingredients used in the drink
	 */
	public RecipeIngredients getIngredients() {
		return this.ingredients;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return this.instructions;
	}

	/**
	 * @return the calculated price of all required ingredients in the recipe
	 */
	public double getPrice() {
		double price = 0;

		for (Entry<Ingredient, Amount> entry : this.ingredients.entrySet()) {
			// add up the price of each ingredient
			double ingredientPrice = entry.getKey().getUnitPrice(Unit.CL)
					* entry.getValue().toUnit(Unit.CL);
			price += ingredientPrice;

		}

		return price;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param recipe
	 *            the recipe that is to be copied
	 * @return A new instance of {@link Recipe} with the same properties as
	 *         <code>recipe</code>.
	 */
	public static Recipe newInstance(Recipe recipe) {
		return new Recipe(recipe.getID(), recipe.getName(),
				RecipeIngredients.newInstance(recipe.getIngredients()),
				recipe.getInstructions());
	}
}
