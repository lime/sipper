/**
 * 
 */
package model;

/**
 * A class representing an ingredient.
 * 
 * @author 217262
 */
public class Ingredient extends ListedItem {

	/**
	 * ID number of the ingredient. Must be uniqe.
	 */
	private final Integer ID;
	/**
	 * Name of the ingredient.
	 */
	private String name;
	/**
	 * Alcohol content in percentage by volume.
	 */
	private double alcoholContent;
	/**
	 * Size of the container the ingredient is bought in.
	 */
	private Amount containerSize;
	/**
	 * Price per container.
	 */
	private double containerPrize;
	/**
	 * The store from which the ingredient is bought.
	 */
	private String store;
	/**
	 * Additional comments regarding the ingredient.
	 */
	private String comment;

	/**
	 * Creates a new ingredient object.
	 * 
	 * @param ID
	 *            unique ID number of the ingredient
	 * @param name
	 *            name of ingredient
	 * @param alcoholContent
	 *            alcohol percentage expressed as a <code>double</code>
	 * @param containerSize
	 *            size of a container in which the ingredient is bought
	 * @param containerPrize
	 *            price of a container in which the ingredient is bought
	 * @param store
	 *            name of the store from which this ingredient is typicall
	 *            bought
	 * @param comment
	 *            optional comment
	 */
	public Ingredient(Integer ID, String name, double alcoholContent,
			Amount containerSize, double containerPrize, String store,
			String comment) {
		this.ID = ID;
		this.name = name;
		this.alcoholContent = alcoholContent;
		this.containerSize = containerSize;
		this.containerPrize = containerPrize;
		this.store = store;
		this.comment = comment;
	}

	/* ########### ########### Getters and setters ########### ########### */

	/**
	 * @return the ID of the ingredient
	 */
	@Override
	public Integer getID() {
		return this.ID;
	}

	/**
	 * @return the name of the ingredient
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the alcoholContent
	 */
	public double getAlcoholContent() {
		return this.alcoholContent;
	}

	/**
	 * @param alcoholContent
	 *            the alcoholContent to set
	 */
	public void setAlcoholContent(double alcoholContent) {
		this.alcoholContent = alcoholContent;
	}

	/**
	 * @return the containerSize
	 */
	public Amount getContainerSize() {
		return this.containerSize;
	}

	/**
	 * @param containerSize
	 *            the containerSize to set
	 */
	public void setContainerSize(Amount containerSize) {
		this.containerSize = containerSize;
	}

	/**
	 * @return the containerPrize
	 */
	public double getContainerPrize() {
		return this.containerPrize;
	}

	/**
	 * @param containerPrize
	 *            the containerPrize to set
	 */
	public void setContainerPrize(double containerPrize) {
		this.containerPrize = containerPrize;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the store
	 */
	public String getStore() {
		return this.store;
	}

	/**
	 * @param store
	 *            the store to set
	 */
	public void setStore(String store) {
		this.store = store;
	}

	/**
	 * Returns the price per each unit of the ingredient.
	 * 
	 * @param unit
	 *            the requested unit
	 * @return the price per given unit of the ingredient
	 */
	public double getUnitPrice(Unit unit) {
		if (this.getContainerSize().toUnit(unit) == 0) {
			return 0;
		} else {
			return this.getContainerPrize()
					/ this.getContainerSize().toUnit(unit);
			/* this .getContainerPrize()
			 * .divide(this.getContainerSize().toUnit(unit), 10,
			 * RoundingMode.HALF_EVEN).stripTrailingZeros(); */
		}
	}

	/**
	 * Copy constructor.
	 * 
	 * @param ingredient
	 *            the Ingredient to be copied
	 * @return A new instance of {@link Ingredient} with the same properties as
	 *         <code>ingredient</code>.
	 */
	public static Ingredient newInstance(Ingredient ingredient) {
		return new Ingredient(ingredient.getID(), ingredient.getName(),
				ingredient.getAlcoholContent(), ingredient.getContainerSize(),
				ingredient.getContainerPrize(), ingredient.getStore(),
				ingredient.getComment());
	}
}
