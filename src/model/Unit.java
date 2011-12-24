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
		public int toMillilitres() {
			return 1000;
		}
	},
	DL {
		@Override
		public int toMillilitres() {
			return 100;
		}
	},
	CL {
		@Override
		public int toMillilitres() {
			return 10;
		}
	},
	ML {
		@Override
		public int toMillilitres() {
			return 1;
		}
	},
	TBPS {
		@Override
		public int toMillilitres() {
			return 15;
		}
	},
	TSP {
		@Override
		public int toMillilitres() {
			return 5;
		}
	};

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString() */
	/**
	 * Returns the name of the unit as a lowercase String.
	 */
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

	// public abstract String toLongerString(); //TODO longer String?
	/**
	 * @return The size per unit in millilitres.
	 */
	public abstract int toMillilitres();

}
