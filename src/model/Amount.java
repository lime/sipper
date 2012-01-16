/**
 * 
 */
package model;

/**
 * A class representing a measured amount. Consists of a value as well as a
 * {@link Unit}.
 * 
 * @author 217262
 */
public class Amount implements Comparable<Amount> {

	private double quantity;
	private Unit unit;

	/**
	 * Creates a new {@link Amount}, storing the original value and {@link Unit}
	 * as the default form of representation.
	 * 
	 * @param quantity
	 *            the amount in the specified unit that this object will
	 *            represent
	 * @param unit
	 *            the unit that the quantity is given in
	 */
	public Amount(double quantity, Unit unit) {
		this.quantity = quantity;
		this.unit = unit;
	}

	/**
	 * Returns the amount in the specified unit of measurement.
	 * 
	 * @param unit
	 *            the target unit
	 * @return the amount in the specified unit
	 */
	public double toUnit(Unit unit) {
		return this.quantity * this.unit.toMilliliters() / unit.toMilliliters();
		/* return BigDecimal .valueOf(this.millilitres)
		 * .divide(BigDecimal.valueOf(unit.toMilliliters()), 5,
		 * RoundingMode.HALF_EVEN).stripTrailingZeros(); */
	}

	/**
	 * @return the unit this amount uses
	 */
	public Unit getUnit() {
		return this.unit;
	}

	/**
	 * Sets the unit that this Amount is to useIt will only change the unit, not
	 * convert the value to match. For getting the AMount in another unit, see
	 * {@link Amount#toUnit(Unit)}.
	 * 
	 * @param unit
	 *            the unit to use
	 */
	void setUnit(Unit unit) {
		this.unit = unit;
	}

	/**
	 * @return the numeric value of the amount in the original (read: preferred)
	 *         unit
	 */
	public double getQuantity() {
		return this.quantity;
	}

	/**
	 * @param quantity
	 *            the new quantity to set
	 */
	void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object) */
	/**
	 * @return <code>true</code> if the two {@link Amount}s represent the same
	 *         quantity when expressed with the same {@link Unit}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Amount)) {
			return false;
		}
		Amount amounto = (Amount) o;
		/* Two amounts are equal if they represent the same number of
		 * milliliters. */
		return this.toUnit(Unit.ML) == amounto.toUnit(Unit.ML);
	}

	@Override
	public int compareTo(Amount o) {
		// compare the two quantities when both are in the same unit
		return Double.valueOf(this.toUnit(Unit.ML))
				.compareTo(o.toUnit(Unit.ML));
	}
}
