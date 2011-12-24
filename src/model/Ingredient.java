/**
 * 
 */
package model;

/**
 * @author Emil Sågfors
 */
public class Ingredient {

	/**
	 * Name of the ingredient.
	 */
	private String name;
	/**
	 * Alcohol content in percentagae by volume.
	 */
	private double alcoholContent;

	/**
	 * 
	 */
	public Ingredient() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the name of the ingredient
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the alcoholContent
	 */
	public double getAlcoholContent() {
		return this.alcoholContent;
	}
}
