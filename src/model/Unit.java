/**
 * 
 */
package model;

/**
 * An enumerator for expressing units of measurement per volume.
 * 
 * @author Emil Sågfors
 */
public enum Unit {
	L {
		@Override
		public int toMilliliters() {
			return 1000;
		}
	},
	DL {
		@Override
		public int toMilliliters() {
			return 100;
		}
	},
	CL {
		@Override
		public int toMilliliters() {
			return 10;
		}
	},
	ML {
		@Override
		public int toMilliliters() {
			return 1;
		}
	},
	TBPS {
		@Override
		public int toMilliliters() {
			return 15;
		}
	},
	TSP {
		@Override
		public int toMilliliters() {
			return 5;
		}
	};

	// public abstract String toLongerString(); //TODO longer String?
	/**
	 * @return The size per unit in milliliters.
	 */
	public abstract int toMilliliters();

	public static void main(String[] args) {
		Unit unit = Unit.valueOf("ML");
		System.out.println("Unit.main(): " + unit);
	}

}
