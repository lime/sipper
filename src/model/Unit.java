package model;

/**
 * An enumerator for expressing units of measurement per volume.
 * 
 * @author 217262
 */
public enum Unit {
	/**
	 * Liter
	 */
	L {
		@Override
		public int toMilliliters() {
			return 1000;
		}
	},
	/**
	 * Deciliter
	 */
	DL {
		@Override
		public int toMilliliters() {
			return 100;
		}
	},
	/**
	 * Centiliter
	 */
	CL {
		@Override
		public int toMilliliters() {
			return 10;
		}
	},
	/**
	 * Milliliter
	 */
	ML {
		@Override
		public int toMilliliters() {
			return 1;
		}
	},
	/**
	 * Tablespoon
	 */
	TBPS {
		@Override
		public int toMilliliters() {
			return 15;
		}
	},
	/**
	 * Teaspoon
	 */
	TSP {
		@Override
		public int toMilliliters() {
			return 5;
		}
	},
	/**
	 * Fluid ounce<br>
	 * Set as exactly 29 mL for convenience, exact value varies depending on
	 * source.
	 */
	OZ {

		@Override
		public int toMilliliters() {
			return 29;
		}

	};

	/**
	 * @return The size per unit in milliliters.
	 */
	public abstract int toMilliliters();

}
