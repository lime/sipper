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

import model.Recipe;

/**
 * A panel for displaying the details of a recipe.
 * 
 * @author 217262
 */
public class RecipeDisplayPanel extends SelectionDisplayPanel<Recipe> {

	private final JLabel nameLabel;
	private final JLabel priceLabel;
	private final JLabel instructionLabel;
	private final JTable ingredientTable;

	/**
	 * @param name
	 *            Name of the panel, used as title.
	 */
	public RecipeDisplayPanel() {

		// Initialize labels
		this.nameLabel = new JLabel();
		this.add(this.nameLabel);
		this.priceLabel = new JLabel();
		this.add(this.priceLabel);
		this.instructionLabel = new JLabel();
		this.add(this.instructionLabel);
		this.ingredientTable = new JTable();
		this.add(this.ingredientTable);

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
		layout.addLayoutComponent(this.priceLabel, constr);
		this.priceLabel.setVerticalTextPosition(SwingConstants.TOP);

		constr.gridy = 0;
		constr.gridx = 1;
		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.gridheight = 2;
		constr.ipadx = 100;
		layout.addLayoutComponent(this.ingredientTable, constr);

		constr.gridy = 2;
		constr.gridx = 0;
		constr.gridheight = 5;
		layout.addLayoutComponent(this.instructionLabel, constr);
		this.instructionLabel.setVerticalTextPosition(SwingConstants.TOP);

	}

	@Override
	protected void updateDisplayedItem(Recipe recipe) {
		// reload all fields with data from the new recipe
		this.nameLabel.setText(recipe.getName());

		BigDecimal price = recipe.getPrice().setScale(2, BigDecimal.ROUND_UP);
		this.priceLabel.setText(CURRENCY_FORMAT.format(price));

		this.instructionLabel.setText(recipe.getInstructions());
		
		this.ingredientTable.setModel(recipe.getIngredientList());
		
	}
}
