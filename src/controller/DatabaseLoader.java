/**
 * 
 */
package controller;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Ingredient;
import model.Recipe;

/**
 * @author 217262
 */
public class DatabaseLoader {
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static final String defaultPath = "jdbc:sqlite:data/shroomware.db";

	public static ArrayList<Recipe> getRecipeList() throws SQLException {

		ArrayList<Recipe> recipeList = new ArrayList<Recipe>();

		// XXX openConnection(defaultPath);

			// start connection and
			Connection conn = DriverManager.getConnection(defaultPath);
			Statement stat = conn.createStatement();
			
		ResultSet rs = stat.executeQuery("select * from recipes;");
			while (rs.next()) {
				String name = rs.getString("name");
				double price = rs.getDouble("price");
				recipeList.add(new Recipe(name, price));
			}
			// Close resultset and connection.
			rs.close();
			conn.close();

		return recipeList;

	}

	/**
	 * @return
	 */
	public static ArrayList<Ingredient> getIngredientList() {

		ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();

		return ingredientList;
	}

	/**
	 * @param dBpath
	 *            path to the database
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private static void openConnection(String dBpath) throws SQLException,
			ClassNotFoundException {
		// start connection and
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection(defaultPath);
		Statement stat = conn.createStatement();


	}
	
	public static void main(String[] args) throws SQLException {
		ArrayList<Recipe> list = DatabaseLoader.getRecipeList();
		for (Recipe recipe : list) {
			System.out.println("DatabaseLoader.main():\t"+recipe.getName()+"\t"+recipe.getPrice());
		}
	}

	/*public static void main(String[] args) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection(defaultPath);
		Statement stat = conn.createStatement();
		stat.executeUpdate("drop table if exists people;");
		stat.executeUpdate("create table people (name, occupation);");
		PreparedStatement prep = conn
				.prepareStatement("insert into people values (?, ?);");

		prep.setString(1, "Gandhi");
		prep.setString(2, "politics");
		prep.addBatch();
		prep.setString(1, "Turing");
		prep.setString(2, "computers");
		prep.addBatch();
		prep.setString(1, "Wittgenstein");
		prep.setString(2, "smartypants");
		prep.addBatch();

		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);

		ResultSet rs = stat.executeQuery("select * from people;");
		while (rs.next()) {
			System.out.println("name = " + rs.getString("name"));
			System.out.println("job = " + rs.getString("occupation"));
		}
		rs.close();
		conn.close();
	}*/

}
