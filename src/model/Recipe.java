/**
 * 
 */
package model;

import java.math.BigDecimal;
import java.util.Map.Entry;

/**
 * @author Emil Sågfors
 */
public class Recipe {

	public static final String ID_COLUMN = "ID";
	public static final String NAME_COLUMN = "name";
	public static final String PRICE_COLUMN = "price"; // TODO remove
	public static final String INGREDIENT_LIST_COLUMN = "ingredientID";
	public static final String INSTRUCTIONS_COLUMN = "instructions";

	/**
	 * The ID number of the recipe. Must be unique.
	 */
	private final int ID;
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
	 * @param name
	 * @param ingredients
	 * @param instructions
	 * @param price
	 */
	public Recipe(int ID, String name, RecipeIngredients ingredients,
			String instructions) {
		this.ID = ID;
		this.name = name;
		this.ingredients = ingredients;
		this.instructions = instructions;
	}

	/**
	 * @return the ID of the drink
	 */
	public int getID() {
		// TODO Auto-generated method stub
		return this.ID;
	}

	/**
	 * @return the name of the drink
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the ingredients used in the drink
	 */
	public RecipeIngredients getIngredientList() {
		return this.ingredients;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return "Lorem ipsum dolor sit amet, conseceutur adepiscin velit.";
		// return this.instructions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString() */
	@Override
	public String toString() {
		// TODO use a listcellrenderer instead
		return this.getName();
	}

	/**
	 * @return the calculated price of all required ingredients in the recipe
	 */
	public BigDecimal getPrice() {
		BigDecimal price = BigDecimal.ZERO;
		// add the price of each ingredient
		//FIXME inexact values... get rid of doubles
		for (Entry<Ingredient, Amount> entry : this.ingredients.entrySet()) {
			
			BigDecimal ingredientPrice = entry.getKey().getUnitPrice(Unit.CL)
					.multiply(entry.getValue().toUnit(Unit.CL));
			price = price.add(ingredientPrice);
			System.out.println("Recipe.getPrice() unitPrice = "+entry.getKey().getUnitPrice(Unit.CL)+", CL = "+entry.getValue().toUnit(Unit.CL));
			System.out.println("Recipe.getPrice() ingredientPrice = "+ingredientPrice+", price = "+price);
		}
		return price;
	}
}
