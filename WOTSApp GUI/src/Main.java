public class Main {
	static int accountValid = 0; //variable to handle login
	static int functionInput = 0;

	public static void main(String[] args) {
		DBConnect connect = new DBConnect(); //call database connection function
		while (accountValid == 0){
			connect.userLogin();
			if (accountValid == 0){
			System.out.println("Please login with valid details");
			}
		}
		System.out.println("Login successful");		
		while (functionInput == 0){
			connect.inputHandler();
		
		while (functionInput == 1){
			connect.getOrders();
			functionInput = 0;
		}
		while (functionInput == 2){
			connect.viewOrder();
			functionInput = 0;
		}
		while (functionInput == 3){
			connect.updateOrder();
			functionInput = 0;
		}
		while (functionInput == 4){
			connect.travelingSalesman();
			functionInput = 0;
		}
		}
		
	
	}
}