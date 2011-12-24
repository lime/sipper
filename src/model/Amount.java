/**
 * 
 */
package model;

/**
 * @author Emil Sågfors
 */
public class Amount {

	private final int millilitres;
	private final Unit originalUnit;

	/**
	 * 
	 */
	public Amount(int value, Unit unit) {
		this.millilitres = value * unit.toMillilitres();
		this.originalUnit = unit;
	}

	/**
	 * Returns the amount in the specified unit of measurement.
	 * 
	 * @param unit
	 *            the target unit
	 * @return the amount in the specified unit
	 */
	public double toUnit(Unit unit) {
		return this.millilitres / unit.toMillilitres();
	}

	/**
	 * @return the unit this amount was originally created with
	 */
	public Unit getOriginalUnit() {
		return this.originalUnit;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Amount)) {
			return false;
		}
		Amount amounto = (Amount) o;
		return this.toUnit(Unit.ML) == amounto.toUnit(Unit.ML);
	}

}
