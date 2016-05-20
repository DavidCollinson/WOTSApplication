import java.sql.*;
import java.util.Scanner;
import java.util.Arrays;

public class DBConnect {
	private Connection con; //database connection variables
	private Statement st; //
	private ResultSet rs; //
	Scanner user_input = new Scanner(System.in); //scanner
	int userID; //handles logged in user id

	public DBConnect() { 
		try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/wots_application", "root", 
					"root"); //database info
			st = con.createStatement();

		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
	}

	public void getOrders() {
		try {
			String query = "SELECT * FROM wots_application.table_orders;"; //selects all entries in order table
			rs = st.executeQuery(query);
			System.out
					.println("--------------------------------------------------------------------------");
			System.out.println("Current Orders:");
			System.out
					.println("--------------------------------------------------------------------------");
			while (rs.next()) { //assigns new variables for column data
				String orderID = rs.getString("OrderID"); 
				String customerID = rs.getString("CustomerID");
				String orderDate = rs.getString("OrderDate");
				String staffID = rs.getString("StaffID");
				String orderStatus = rs.getString("OrderStatus");
				System.out.println("OrderID: " + orderID + "\n" 
						+ "CustomerID: " + customerID + "\n" + "Order Date: "
						+ orderDate + "\n" + "Staff member assigned: "
						+ staffID + "\n" + "Current order status:"
						+ orderStatus); //prints data
				System.out
						.println("--------------------------------------------------------------------------");
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
	}

	public void userLogin() {
		String username; //variable to handle inputed username
		String password; //variable to handle inputed password

		try {
			System.out.print("Enter Username :");
			username = user_input.next(); //user input for username
			System.out.print("Enter Password :");
			password = user_input.next(); //userinput for password
			String query = "SELECT StaffID FROM wots_application.table_staff WHERE UserName = '"
					+ username + "' AND Password = '" + password + "'"; //pulls staff id associated with username and password entered
			rs = st.executeQuery(query);
			while (rs.next()) {
				int staffID = rs.getInt("StaffID");
				userID = staffID; //sets pulled value to variable to the top of the page
				System.out.println("Welcome Staff ID : " + staffID);
				if (staffID > 0) {
					Main.accountValid = 1; //checks if inpuitted details are valid
				} else {
					System.out.println("Account details invalid");
				}
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}

	}

	public void inputHandler() {
		int functionInput; //Main menu to handle core inputs the user will be using
		System.out
				.println(">Input desired function (1 - 'view order list', 2 -'view items on order', 3 - 'change order status', 4 - 'Lookup item location')");
		System.out.println(">");
		functionInput = user_input.nextInt();
		Main.functionInput = functionInput;

	}

	public void viewOrder() {
		int inputOrderID;
		System.out.println("View Order function called:");
		try {
			System.out.print("Enter Order ID you would like to view: ");
			inputOrderID = user_input.nextInt(); //User inputs order ID
			String query = "SELECT * FROM wots_application.table_order_items WHERE OrderID = '"
					+ inputOrderID + "'"; //Selects order information based on inputed order ID
			rs = st.executeQuery(query);
			while (rs.next()) { //Assigns variables to pulled column data
				String orderLineID = rs.getString("OrderLineID");
				String orderID = rs.getString("OrderID");
				String itemID = rs.getString("ItemID");
				String itemName = rs.getString("ItemName");
				String itemQuantity = rs.getString("ItemQuantity");
				String porusWare = rs.getString("PorusWareStatus");
				System.out.println("Line: " + orderLineID + "\n" + "OrderID: "
						+ orderID + "\n" + "ItemID " + itemID + "\n"
						+ "Item name: " + itemName + "\n" + "Quantity: "
						+ itemQuantity + "\n" + "PorusWare Status: "
						+ porusWare); //Displays pulled information
				System.out
						.println("--------------------------------------------------------------------------");
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
	}

	public void updateOrder() {
		
		try {
			String[] orderStatus = { "Pending", "Picking", "Picked", "Packing","Packed", "Dispatched" }; //Array to handle preset order status 
			System.out.println("Update Order status function called");
			System.out.println("Enter order ID: ");
			int inputOrderID = user_input.nextInt(); //User input to define order ID
			System.out.println(">Input order status update (0 - 'Pending', 1 -'Picking', 2 - 'Picked', 3 - 'Packing', 4 - 'Packed', 5 - 'Dispatched')");
			int inputStatus = user_input.nextInt(); //Handles an int value as user input to define value which is pulled from an array later
			
			String update = "UPDATE wots_application.table_orders SET StaffID = '"+ userID + "', OrderStatus = '" + orderStatus[inputStatus] + "' WHERE OrderID = '" + inputOrderID + "'";
			st.executeUpdate(update); //Updates order with order status and the staff member responsible for the change in status
			
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
	}

	public void travelingSalesman(){
		System.out.println("View item location function called:");
		int itemID;
		String[][] map = { { "|", "-", "A", "B", "-", "C", "D", "-", "E", "F", "-", "|" }, 
				{ "1", " ", "#", "#", " ", "#", "#", " ", "#", "#", " ", "1" }, 
				{ "2", " ", "#", "#", " ", "#", "#", " ", "#", "#", " ", "2"}, 
				{ "3", " ", "#", "#", " ", "#", "#", " ", "#", "#", " ", "3" }, 
				{ "4", " ", "#", "#", " ", "#", "#", " ", "#", "#", " ", "4" }, 
				{ "5", " ", "#", "#", " ", "#", "#", " ", "#", "#", " ", "5" }, 
				{ "6", " ", "#", "#", " ", "#", "#", " ", "#", "#", " ", "6" }, 
				{ "7", " ", "#", "#", " ", "#", "#", " ", "#", "#", " ", "7" }, 
				{ "8", " ", "#", "#", " ", "#", "#", " ", "#", "#", " ", "8" }, 
				{ "|", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "|" }, 
				{ "|", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "|" },
				{ "|", "0", "|"}}; //Multi dim array to handle the map
		try {
			System.out.print("Enter Order ID you would like to view: "); //User input to define item item the user wants to look up
			itemID = user_input.nextInt();
			String query = "SELECT * FROM wots_application.table_items WHERE ItemID = '" + itemID + "'"; //Select sql query selecting data associated with enterer itemID
			rs = st.executeQuery(query);
			while (rs.next()) {
				String itemName = rs.getString("ItemName");
				int locationx = rs.getInt("ItemLocationKeyA"); //Variables assigned to pulled data
				int locationy = rs.getInt("ItemLocationKeyB");
				String itemLocation = rs.getString("ItemLocation");
				map [locationx][locationy] = "X"; //array values set to inputed x and y values
				System.out.println("Item Name: " + itemName + "\n" +"Location :" + itemLocation); // Item location printed along with map
				System.out
						.println("--------------------Warehouse map '0' = entrance-------------X = Item--------------------");
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
			StringBuilder sb = new StringBuilder(); //Handles multidimensional array to print one array line, per line
			String lineSeparator = System.getProperty("line.separator");
			for (String[] row : map)
				sb.append(Arrays.toString(row)).append(lineSeparator);
			String text = sb.toString();
			System.out.println(text);
			System.out
			.println("-----------------------------------------------------------------------------------------");
	}


}
