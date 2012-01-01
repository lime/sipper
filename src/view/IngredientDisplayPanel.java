/**
 * 
 */
package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import model.Ingredient;

/**
 * @author 217262
 */
public class IngredientDisplayPanel extends SelectionDisplayPanel<Ingredient> {

	private final JLabel nameLabel;
	private final JLabel containerPriceLabel;
	private final JLabel containerSizeLabel;
	private final JLabel alcoholContentLabel;
	private final JLabel commentLabel;
	private final JLabel storeLabel;

	/**
	 * @param name
	 */
	public IngredientDisplayPanel() {

		// Initialize labels
		this.add(this.nameLabel = new JLabel());
		this.add(this.containerPriceLabel = new JLabel());
		this.add(this.containerSizeLabel = new JLabel());
		this.add(this.alcoholContentLabel = new JLabel());
		this.add(this.commentLabel = new JLabel());
		this.add(this.storeLabel = new JLabel());

		// layout and constraints
		GridBagLayout layout = new GridBagLayout();

		GridBagConstraints constr = new GridBagConstraints();
		constr.gridx = 0;
		constr.gridy = 0;
		constr.gridwidth = 1;
		constr.gridheight = 1;
		constr.weightx = 1.0;
		constr.weighty = 1.0;
		constr.anchor = GridBagConstraints.FIRST_LINE_START;
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.insets = new Insets(5, 5, 5, 5);
		this.setLayout(layout);

		// use the layout to place out the components

		layout.addLayoutComponent(this.nameLabel, constr);

		constr.gridy = 1;
		layout.addLayoutComponent(this.containerPriceLabel, constr);
		this.containerPriceLabel.setVerticalTextPosition(SwingConstants.TOP);

		constr.gridy = 2;
		//constr.gridwidth = GridBagConstraints.REMAINDER;
		layout.addLayoutComponent(this.containerSizeLabel, constr);
		
		constr.gridx = 1;
		constr.gridy = 0;
		constr.gridwidth = 1;
		layout.addLayoutComponent(this.alcoholContentLabel, constr);
		
		constr.gridy=1;
		layout.addLayoutComponent(this.storeLabel, constr);

		constr.gridy = 2;
		//constr.gridwidth = GridBagConstraints.REMAINDER;
		layout.addLayoutComponent(this.commentLabel, constr);
		this.commentLabel.setVerticalTextPosition(SwingConstants.TOP);
	}

	/* (non-Javadoc)
	 * @see view.SelectionDisplayPanel#updateDisplayedItem(java.lang.Object) */
	@Override
	protected void updateDisplayedItem(Ingredient ingredient) {

		this.nameLabel.setText(ingredient.getName());

		// Container price in currency format.
		BigDecimal containerPrice = ingredient.getContainerPrize().setScale(2,
				BigDecimal.ROUND_UP);
		this.containerPriceLabel.setText(CURRENCY_FORMAT.format(containerPrice));

		// Container size in the stated unit.
		this.containerSizeLabel.setText(ingredient.getContainerSize().toUnit(
				ingredient.getContainerSize().getOriginalUnit()).toPlainString()
				+ " "
				+ ingredient.getContainerSize().getOriginalUnit().toString()
						.toLowerCase());

		// Alcohol content in percentages
		this.alcoholContentLabel.setText(NumberFormat.getPercentInstance()
				.format(ingredient.getAlcoholContent()));

		// Name of the store
		this.storeLabel.setText(ingredient.getStore());

		// Comment attached to the ingredient.
		this.commentLabel.setText(ingredient.getComment());
	}

}
