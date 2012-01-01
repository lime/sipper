/**
 * 
 */
package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Emil Sågfors
 */
public class Amount {

	private final int millilitres;
	private final Unit originalUnit;

	/**
	 * 
	 */
	public Amount(double value, Unit unit) {
		this.millilitres = (int) (value * unit.toMilliliters());
		this.originalUnit = unit;
	}

	/**
	 * Returns the amount in the specified unit of measurement.
	 * 
	 * @param unit
	 *            the target unit
	 * @return the amount in the specified unit
	 */
	public BigDecimal toUnit(Unit unit) {
		return BigDecimal.valueOf(this.millilitres).divide(
				BigDecimal.valueOf(unit.toMilliliters()), 5, RoundingMode.HALF_EVEN).stripTrailingZeros();
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
		/* Two amounts are equal if they represent the same number of
		 * milliliters. */
		return this.toUnit(Unit.ML) == amounto.toUnit(Unit.ML);
	}
}
