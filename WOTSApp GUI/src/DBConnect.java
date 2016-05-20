import java.sql.*;
import java.util.Scanner;
import java.util.Arrays;

public class DBConnect {
	private Connection con;
	private Statement st;
	private ResultSet rs;
	Scanner user_input = new Scanner(System.in);
	int userID;

	public DBConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/wots_application", "root",
					"root");
			st = con.createStatement();

		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
	}

	public void getOrders() {
		try {
			String query = "SELECT * FROM wots_application.table_orders;";
			rs = st.executeQuery(query);
			System.out
					.println("--------------------------------------------------------------------------");
			System.out.println("Current Orders:");
			System.out
					.println("--------------------------------------------------------------------------");
			while (rs.next()) {
				String orderID = rs.getString("OrderID");
				String customerID = rs.getString("CustomerID");
				String orderDate = rs.getString("OrderDate");
				String staffID = rs.getString("StaffID");
				String orderStatus = rs.getString("OrderStatus");
				System.out.println("OrderID: " + orderID + "\n"
						+ "CustomerID: " + customerID + "\n" + "Order Date: "
						+ orderDate + "\n" + "Staff member assigned: "
						+ staffID + "\n" + "Current order status:"
						+ orderStatus);
				System.out
						.println("--------------------------------------------------------------------------");
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
	}

	public void userLogin() {
		String username;
		String password;

		try {
			System.out.print("Enter Username :");
			username = user_input.next();
			System.out.print("Enter Password :");
			password = user_input.next();
			String query = "SELECT StaffID FROM wots_application.table_staff WHERE UserName = '"
					+ username + "' AND Password = '" + password + "'";
			rs = st.executeQuery(query);
			while (rs.next()) {
				int staffID = rs.getInt("StaffID");
				userID = staffID;
				System.out.println("Welcome Staff ID : " + staffID);
				if (staffID > 0) {
					Main.accountValid = 1;
				} else {
					System.out.println("Account details invalid");
				}
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}

	}

	public void inputHandler() {
		int functionInput;
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
			inputOrderID = user_input.nextInt();
			String query = "SELECT * FROM wots_application.table_order_items WHERE OrderID = '"
					+ inputOrderID + "'";
			rs = st.executeQuery(query);
			while (rs.next()) {
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
						+ porusWare);
				System.out
						.println("--------------------------------------------------------------------------");
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
	}

	public void updateOrder() {
		
		try {
			String[] orderStatus = { "Pending", "Picking", "Picked", "Packing","Packed", "Dispatched" };
			System.out.println("Update Order status function called");
			System.out.println("Enter order ID: ");
			int inputOrderID = user_input.nextInt();
			System.out.println(">Input order status update (0 - 'Pending', 1 -'Picking', 2 - 'Picked', 3 - 'Packing', 4 - 'Packed', 5 - 'Dispatched')");
			int inputStatus = user_input.nextInt();
			
			String update = "UPDATE wots_application.table_orders SET StaffID = '"+ userID + "', OrderStatus = '" + orderStatus[inputStatus] + "' WHERE OrderID = '" + inputOrderID + "'";
			st.executeUpdate(update);
			
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
				{ "|", "0", "|"}};
		try {
			System.out.print("Enter Order ID you would like to view: ");
			itemID = user_input.nextInt();
			String query = "SELECT * FROM wots_application.table_items WHERE ItemID = '" + itemID + "'";
			rs = st.executeQuery(query);
			while (rs.next()) {
				String itemName = rs.getString("ItemName");
				int locationx = rs.getInt("ItemLocationKeyA");
				int locationy = rs.getInt("ItemLocationKeyB");
				String itemLocation = rs.getString("ItemLocation");
				map [locationx][locationy] = "X";
				System.out.println("Item Name: " + itemName + "\n" +"Location :" + itemLocation);
				System.out
						.println("--------------------Warehouse map '0' = entrance-------------X = Item--------------------");
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
			StringBuilder sb = new StringBuilder();
			String lineSeparator = System.getProperty("line.separator");
			for (String[] row : map)
				sb.append(Arrays.toString(row)).append(lineSeparator);
			String text = sb.toString();
			System.out.println(text);
			System.out
			.println("-----------------------------------------------------------------------------------------");
	}


}