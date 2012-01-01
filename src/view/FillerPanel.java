package view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class FillerPanel extends SelectionDisplayPanel<Object> {

	private final JLabel filler;

	public FillerPanel(String text) {
		this.filler = new JLabel(text);
		this.filler.setHorizontalAlignment(SwingConstants.CENTER);
		this.setLayout(new GridLayout(1, 1));
		this.add(this.filler);
	}

	/* (non-Javadoc)
	 * @see view.SelectionDisplayPanel#updateDisplayedItem(java.lang.Object) */
	@Override
	protected void updateDisplayedItem(Object displayItem) {
		// TODO Auto-generated method stub
		this.filler.setText(displayItem.toString());
	}
}
