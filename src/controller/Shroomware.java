/**
 * 
 */
package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import model.Ingredient;
import model.Recipe;
import view.FillerPanel;
import view.IngredientDisplayPanel;
import view.MainFrame;
import view.RecipeDisplayPanel;
import view.ScrollViewPanel;

/**
 * The main execution class of the program.
 * 
 * @author Emil Sågfors
 */
public class Shroomware {

	private static ArrayList<Recipe> recipeList;
	private static ArrayList<Ingredient> ingredientList;

	/**
	 * The main method, which calls helper method to load data lists from the
	 * database and build the main window and GUI.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Initialize the lists from the database.
		try {
			initializeLists();
		} catch (SQLException e) {
			System.out
					.println("Shroomware.main(): error in loading lists from database.");
			e.printStackTrace();
		}

		// Queue to the event dispatching thread for thread safety.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Gets the lists to be used in the GUI with the help of DatabaseLoader.
	 * 
	 * @throws SQLException
	 */
	private static void initializeLists() throws SQLException {

		DatabaseLoader dbLoader = new DatabaseLoader(null);
		// TODO contants go where?
		recipeList = dbLoader.getRecipeList();
		ingredientList = dbLoader.getIngredientList();

	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 * 
	 * @author Java SE tutorial
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		try {
			// Use system look and feel.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JPanel recipesPanel = new ScrollViewPanel<Recipe>(recipeList,
				new RecipeDisplayPanel(), "Drinks");

		JPanel ingredientsPanel = new ScrollViewPanel<Ingredient>(
				ingredientList, new IngredientDisplayPanel(), "Ingredients");

		// extra fillers
		FillerPanel fillerPanel2 = new FillerPanel("tillin tallin");
		FillerPanel fillerPanel3 = new FillerPanel("Fnurkeli");

		// TODO Auto-generated constructor stub

		// Create a new main window, giving it its tabs
		MainFrame mainFrame = new MainFrame("Shroomware", new JPanel[] {
				recipesPanel, ingredientsPanel, fillerPanel2, fillerPanel3 });

		// Display the window.
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

}
