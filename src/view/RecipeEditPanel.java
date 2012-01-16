/**
 * 
 */
package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import model.Recipe;
import model.RecipeIngredients;

/**
 * A panel for displaying and editing the properties of a {@link Recipe}.
 * 
 * @author 217262
 */
public class RecipeEditPanel extends ItemEditPanel<Recipe> {

	private final JTextField nameField;
	private final JLabel priceLabel;
	private final JTextArea instructionArea;
	private final JTable ingredientTable;
	private Recipe uneditedItem;
	private final IngredientButtons ingredientButtons;
	private final JComponent[] editComponents;

	/**
	 * Constructor which initializes all display components of the panel, places
	 * them out with a GridBagLayout, and sets any styles needed for their
	 * appearance.
	 */
	public RecipeEditPanel() {

		// Initialize labels
		this.add(this.nameField = new JTextField(" "));
		this.add(this.priceLabel = new JLabel(" "));

		// Instruction area
		this.instructionArea = new JTextArea(" ");
		// Instruction area made scrollable with scrollpane
		JScrollPane instructionScrollPane;
		this.add(instructionScrollPane = new JScrollPane(this.instructionArea));
		this.add(this.ingredientButtons = new IngredientButtons());

		/* A container for the table, so that it's possible to add a border
		 * without it messing with the table. */
		JComponent ingredientContainer = new JPanel(new BorderLayout());
		this.ingredientTable = new RecipeIngredientsTable();
		ingredientContainer.add(this.ingredientTable, BorderLayout.CENTER);
		// Buttons go here as well
		ingredientContainer.add(this.ingredientButtons, BorderLayout.LINE_END);

		// Plus scrollbars, in case the table gets long.
		JScrollPane ingredientScrollPane = new JScrollPane(ingredientContainer);
		this.add(ingredientScrollPane);

		// Creates the array of editable components
		this.editComponents = new JComponent[] { this.nameField,
				this.instructionArea, this.ingredientButtons,
				this.ingredientTable, ingredientContainer,
				ingredientScrollPane, instructionScrollPane,
				this.ingredientButtons };

		// layout and constraints
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constr = new GridBagConstraints();
		constr.gridx = 0;
		constr.gridy = 0;
		constr.gridwidth = GridBagConstraints.RELATIVE;
		constr.gridheight = 1;
		constr.weightx = 1.0;
		constr.weighty = 0.0;
		constr.anchor = GridBagConstraints.FIRST_LINE_START;
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.insets = new Insets(10, 10, 10, 10);
		this.setLayout(layout);

		// use the layout to place out the editComponents
		layout.addLayoutComponent(this.nameField, constr);

		constr.gridx = 3;
		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.anchor = GridBagConstraints.FIRST_LINE_END;
		constr.fill = GridBagConstraints.NONE;
		constr.insets.set(10, 0, 10, 10);
		layout.addLayoutComponent(this.priceLabel, constr);

		constr.gridx = 0;
		constr.gridy = 1;
		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.gridheight = GridBagConstraints.RELATIVE;
		constr.weightx = 1.0;
		constr.weighty = 4.0;
		constr.fill = GridBagConstraints.BOTH;
		constr.insets.set(10, 10, 10, 10);
		layout.addLayoutComponent(ingredientScrollPane, constr);

		constr.gridx = 0;
		constr.gridy = 3;
		constr.gridheight = GridBagConstraints.REMAINDER;
		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.weighty = 3.0;
		layout.addLayoutComponent(instructionScrollPane, constr);

		/* * * Perform additional styling * * */

		// Name
		this.nameField.setFont(this.nameField.getFont().deriveFont(Font.BOLD,
				21f));
		// Price
		this.priceLabel.setFont(this.priceLabel.getFont().deriveFont(Font.BOLD,
				17f));
		this.priceLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		// Instructions
		this.instructionArea.setLineWrap(true);
		this.instructionArea.setWrapStyleWord(true);
		instructionScrollPane.setBorder(BorderFactory
				.createTitledBorder("Instructions"));

		// Ingredients
		ingredientScrollPane.setBorder(BorderFactory
				.createTitledBorder("Ingredients"));
		this.ingredientTable.setBackground(ingredientContainer.getBackground());
		// removes table headers and scrollpane borders
		this.ingredientTable.setTableHeader(null);
		// ingredientScrollPane.setBorder(BorderFactory.createEmptyBorder());
		// System.out.println("RecipeEditPanel.RecipeEditPanel()"+ingredientScrollPane.getForeground());
	}

	@Override
	public void setEditMode(boolean editEnabled) {
		super.setEditMode(editEnabled);
		// Hide these buttons entirely
		this.ingredientButtons.setVisible(editEnabled);
	}

	@Override
	protected JComponent[] getEditComponents() {
		return this.editComponents;
	}

	@Override
	protected void updateDisplayedItem(Recipe recipe) {

		// store the unedited item as a clone
		this.uneditedItem = Recipe.newInstance(recipe);

		// reload all fields with data from the new recipe
		this.nameField.setText(recipe.getName());

		this.priceLabel.setText(CURRENCY_FORMAT.format(recipe.getPrice()));

		this.instructionArea.setText(recipe.getInstructions());

		this.ingredientTable.setModel(recipe.getIngredients());

		// make the price field update when changes are made
		recipe.getIngredients()
				.addTableModelListener(new PriceUpdateListener());
	}

	/* (non-Javadoc)
	 * @see view.ItemEditPanel#getUneditedItem() */
	@Override
	public Recipe getUneditedItem() {
		return this.uneditedItem;
	}

	/* (non-Javadoc)
	 * @see view.ItemEditPanel#getEditedItem() */
	@Override
	public Recipe getEditedItem() {
		// Get values from fields/saved copy.
		int ID = this.uneditedItem.getID();
		String name = this.nameField.getText();
		RecipeIngredients ingredients = (RecipeIngredients) this.ingredientTable
				.getModel();

		String instructions = this.instructionArea.getText();

		return new Recipe(ID, name, ingredients, instructions);
	}

	private class PriceUpdateListener implements TableModelListener {

		/* (non-Javadoc)
		 * @see
		 * javax.swing.event.TableModelListener#tableChanged(javax.swing.event
		 * .TableModelEvent) */
		@Override
		public void tableChanged(TableModelEvent arg0) {
			// Update price to match the new ingredients
			RecipeEditPanel.this.priceLabel.setText(CURRENCY_FORMAT
					.format(RecipeEditPanel.this.getEditedItem().getPrice()));
		}

	}

	/**
	 * A container with buttons for doing editing actions.
	 */
	private class IngredientButtons extends Box {
		/** Plus-button with an action to add a new line to the table */
		private final JButton addButton;
		/**
		 * Minus-button with an action to remove the selected line from the
		 * table
		 */
		private final JButton removeButton;

		/**
		 * Creates the two buttons for removing and adding new entries in the
		 * table, connecting actions to them for the desired funtionality.
		 */
		public IngredientButtons() {
			super(BoxLayout.PAGE_AXIS);
			// Buttons, Actions and Strings to display
			this.addButton = new JButton(new AbstractAction("+") {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Get the model
					RecipeIngredients model = (RecipeIngredients) RecipeEditPanel.this.ingredientTable
							.getModel();
					// insert an empty row
					model.insertEmptyRow();
				}

			});

			this.removeButton = new JButton(new AbstractAction("−") {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Removes the selected row from the table

					int row = RecipeEditPanel.this.ingredientTable
							.getSelectedRow();
					// Check for selection
					if (row > -1) {
						RecipeIngredients model = (RecipeIngredients) RecipeEditPanel.this.ingredientTable
								.getModel();
						// remove with row as index
						model.removeRow(row);
					}
				}
			});

			this.add(this.addButton);
			this.add(this.removeButton);
		}
	}
}
