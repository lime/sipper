/**
 * 
 */
package view;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import model.Amount;
import model.Ingredient;
import model.Unit;

/**
 * The panel used for displaying and editing {@link Ingredient}s.
 * 
 * @author 217262
 */
public class IngredientEditPanel extends ItemEditPanel<Ingredient> {

	private JTextField nameField;
	private JFormattedTextField containerPriceField;
	private JFormattedTextField containerSizeField;
	private JComboBox containerUnitComboBox;
	private JFormattedTextField alcoholContentField;
	private JTextField commentField;
	private JTextField storeField;
	private final JComponent[] editComponents;
	private Ingredient uneditedItem;

	/**
	 * Constructor which initializes and layouts all fields used for displaying
	 * and editing {@link Ingredient}s.
	 */
	public IngredientEditPanel() {
		// Initialize Fields
		this.editComponents = new JComponent[] {
				new JLabel("Name:"),
				this.nameField = new JTextField(),
				new JLabel("Price:"),
				this.containerPriceField = new JFormattedTextField(
						CURRENCY_FORMAT),
				new JLabel("Size:"),
				this.containerSizeField = new JFormattedTextField(
						NumberFormat.getNumberInstance()),
				new JLabel("Unit:"),
				this.containerUnitComboBox = new JComboBox(Unit.values()),
				new JLabel("Alcohol:"),
				this.alcoholContentField = new JFormattedTextField(
						NumberFormat.getPercentInstance()),
				new JLabel("Comment:"), this.commentField = new JTextField(),
				new JLabel("Store:"), this.storeField = new JTextField() };

		for (JComponent component : this.editComponents) {
			// add all fields to the panel
			this.add(component);
		}

		/* Place out the fields with a SpringLayout */
		this.setLayout(new SpringLayout());
		/* uses the convenience method makeCompactGrid from the SpringUtilities
		 * class provided by Oracle in their tutorial pages
		 * http://docs.oracle.com/javase/tutorial/uiswing/layout/spring.html */
		SpringUtilities.makeCompactGrid(this, this.editComponents.length / 2,
				2, 6, 6, 6, 6);

		this.setEditMode(false);
	}

	/* (non-Javadoc)
	 * @see view.ItemDisplayPanel#updateDisplayedItem(java.lang.Object) */
	@Override
	protected void updateDisplayedItem(Ingredient ingredient) {
		// save a copy before edits can be made
		this.uneditedItem = Ingredient.newInstance(ingredient);

		// Name
		this.nameField.setText(ingredient.getName());

		// Container price in currency format.
		this.containerPriceField.setText(CURRENCY_FORMAT.format(ingredient
				.getContainerPrize()));

		// Container size in the stated unit.
		this.containerSizeField.setText(NumberFormat.getNumberInstance()
				.format(ingredient.getContainerSize().getQuantity()));
		// Container size unit
		this.containerUnitComboBox.setSelectedItem(ingredient
				.getContainerSize().getUnit());

		// Alcohol content in percentages
		this.alcoholContentField.setText(NumberFormat.getPercentInstance()
				.format(ingredient.getAlcoholContent()));

		// Name of the store
		this.storeField.setText(ingredient.getStore());

		// Comment attached to the ingredient.
		this.commentField.setText(ingredient.getComment());
	}

	/* (non-Javadoc)
	 * @see view.ItemEditPanel#setEditMode(boolean) */
	@Override
	public void setEditMode(boolean editEnabled) {
		super.setEditMode(editEnabled);
		// those not in the components list
		this.containerSizeField.setEditable(editEnabled);
	}

	/* (non-Javadoc)
	 * @see view.ItemEditPanel#getUneditedItem() */
	@Override
	Ingredient getUneditedItem() {
		return this.uneditedItem;
	}

	/* (non-Javadoc)
	 * @see view.ItemEditPanel#getEditedItem() */
	@Override
	Ingredient getEditedItem() {
		// Get values from fields/saved copy.
		int ID = this.uneditedItem.getID();

		String name = this.nameField.getText();

		double alcoholContent;
		double containerPrize;
		Amount containerSize;

		/* Tries to parse the contents of the percent and currency fields, will
		 * default to unsaved values in case parsing fails */

		try {
			alcoholContent = NumberFormat.getPercentInstance()
					.parse(this.alcoholContentField.getText()).doubleValue();
		} catch (ParseException e) {
			alcoholContent = this.uneditedItem.getAlcoholContent();
		}

		try {
			containerPrize = CURRENCY_FORMAT.parse(
					this.containerPriceField.getText()).doubleValue();
		} catch (ParseException e) {
			containerPrize = this.uneditedItem.getContainerPrize();
		}

		try {
			double quantity = NumberFormat.getNumberInstance()
					.parse(this.containerSizeField.getText()).doubleValue();
			containerSize = new Amount(quantity,
					(Unit) this.containerUnitComboBox.getSelectedItem());
		} catch (ParseException e) {
			containerSize = this.uneditedItem.getContainerSize();
		}

		// Gets the string from each text area

		String store = this.storeField.getText();

		String comment = this.commentField.getText();

		return new Ingredient(ID, name, alcoholContent, containerSize,
				containerPrize, store, comment);
	}

	/* (non-Javadoc)
	 * @see view.ItemEditPanel#getEditComponents() */
	@Override
	protected JComponent[] getEditComponents() {
		return this.editComponents;
	}
}