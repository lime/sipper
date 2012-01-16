package controller;

import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import model.Ingredient;
import model.Recipe;
import view.IngredientEditPanel;
import view.MainFrame;
import view.RecipeEditPanel;
import view.ScrollDisplayPanel;
import controller.AvailableItems.AvailableIngredients;
import controller.AvailableItems.AvailableRecipes;

/**
 * The main execution class of the program.
 * 
 * @author 217262
 */
public class Sipper {

	private static AvailableItems<Recipe> recipeList;
	private static AvailableItems<Ingredient> ingredientList;

	/**
	 * The main method, which calls helper methods to load data lists from the
	 * database and build the main window and GUI.
	 * 
	 * @param args
	 *            any eventual runtime arguments (not implemented for any usage)
	 */
	public static void main(String[] args) {

		// Initialize the lists from the database.
		initializeLists();

		// Queue to the event dispatching thread for thread safety.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Gets the lists that are to be used in the GUI with the help of
	 * DatabaseConnector.
	 * 
	 * @throws SQLException
	 */
	private static void initializeLists() {

		/* Initialize lists as empty so that loading the lists can fail
		 * gracefully. */
		recipeList = AvailableRecipes.getInstance();
		ingredientList = AvailableIngredients.getInstance();

	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread. (source: Oracle Java SE tutorial)
	 * 
	 * @see Java SE tutorial
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		try {
			// Use system look and feel.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create the panels that are to be used in the tabs
		JPanel recipesPanel = new ScrollDisplayPanel<Recipe>(recipeList,
				new RecipeEditPanel(), "Drinks");

		JPanel ingredientsPanel = new ScrollDisplayPanel<Ingredient>(
				ingredientList, new IngredientEditPanel(), "Ingredients");

		// Create a new main window, giving it its tabs
		MainFrame mainFrame = new MainFrame("Sipper", new JPanel[] {
				recipesPanel, ingredientsPanel });

		// Display the window.
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize(new Dimension(640, 480));
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null); // center on screen
		mainFrame.setVisible(true);
	}

}
