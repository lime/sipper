/**
 * 
 */
package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import model.Recipe;

/**
 * A panel for displaying the details of a recipe.
 * 
 * @author 217262
 */
public class RecipeDisplayPanel extends SelectionDisplayPanel<Recipe> {

	private JLabel nameLabel;
	private JTextField priceField;

	/**
	 * 
	 */
	public RecipeDisplayPanel() {
		// name
		this.setName("Recipes");

		// Initialize labels
		this.nameLabel = new JLabel();
		this.add(nameLabel);
		this.priceField = new JTextField();
		this.add(priceField);

		// layout and constraints
		GridBagLayout layout = new GridBagLayout();
		/* GridBagConstraints constr = new GridBagConstraints(1, 1, 3, 5, 1, 1,
		 * GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1,
		 * 1, 1, 1), 0, 0); */
		GridBagConstraints constr = new GridBagConstraints();
		constr.gridwidth = 1;
		constr.weightx = 0;
		constr.weighty = 0;
		constr.anchor = GridBagConstraints.EAST;
		constr.fill = GridBagConstraints.BOTH;
		this.setLayout(layout);

		// use the layout to place out the components
		layout.addLayoutComponent(nameLabel, constr);
		constr.gridx++;
		layout.addLayoutComponent(priceField, constr);

	}

	protected void updateDisplayedItem(Recipe recipe) {
		// recipe.
		this.nameLabel.setText(recipe.getName());
		this.priceField.setText(recipe.getPrice().toString());
		this.priceField.setEnabled(false);
	}
}
