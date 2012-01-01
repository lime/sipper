/**
 * 
 */
package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Emil Sågfors
 */
public class Ingredient implements Comparable<Ingredient> {

	public static final String ID_COLUMN = "ID";
	public static final String NAME_COLUMN = "name";
	public static final String ALCOHOL_CONTENT_COLUMN = "alcoholContent";
	public static final String CONTAINER_SIZE_VALUE_COLUMN = "containerSizeValue";
	public static final String CONTAINER_SIZE_UNIT_COLUMN = "containerSizeUnit";
	public static final String CONTAINER_PRIZE_COLUMN = "containerPrice";

	/**
	 * ID number of the ingredient. Must be uniqe.
	 */
	private final int ID;
	/**
	 * Name of the ingredient.
	 */
	private String name;
	/**
	 * Alcohol content in percentage by volume.
	 */
	private BigDecimal alcoholContent;
	/**
	 * Size of the container the ingredient is bought in.
	 */
	private Amount containerSize;
	/**
	 * Price per container.
	 */
	private BigDecimal containerPrize;
	/**
	 * The store from which the ingredient is bought.
	 */
	private String store;
	/**
	 * Additional comments regarding the ingredient.
	 */
	private String comment;

	public Ingredient(int ID, String name, BigDecimal alcoholContent,
			Amount containerSize, BigDecimal containerPrize) {
		this.ID = ID;
		this.name = name;
		this.alcoholContent = alcoholContent;
		this.containerSize = containerSize;
		this.containerPrize = containerPrize;
	}

	/* ########### ########### Getters and setters ########### ########### */

	/**
	 * @return the ID of the ingredient
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * @return the name of the ingredient
	 */
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
	public BigDecimal getAlcoholContent() {
		return this.alcoholContent;
	}

	/**
	 * @param alcoholContent
	 *            the alcoholContent to set
	 */
	public void setAlcoholContent(BigDecimal alcoholContent) {
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
	public BigDecimal getContainerPrize() {
		return this.containerPrize;
	}

	/**
	 * @param containerPrize
	 *            the containerPrize to set
	 */
	public void setContainerPrize(BigDecimal containerPrize) {
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
	public BigDecimal getUnitPrice(Unit unit) {
		if (this.getContainerSize().toUnit(unit).equals(BigDecimal.ZERO))
			return BigDecimal.ZERO;
		else
			return this.getContainerPrize().divide(
					this.getContainerSize().toUnit(unit),10, RoundingMode.HALF_EVEN).stripTrailingZeros();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(Ingredient other) {
		// compare the names of the ingredients
		return this.getName().compareToIgnoreCase(other.getName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString() */
	@Override
	public String toString() {
		// TODO just temporary, use model
		return this.getName();
	}
}
