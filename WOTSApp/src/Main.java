public class Main {
	static int accountValid = 0; //variable to handle login
	static int functionInput = 0; //variable to handle user input

	public static void main(String[] args) {
		DBConnect connect = new DBConnect(); //call database connection function
		while (accountValid == 0){ //if default
			connect.userLogin(); //call login
			if (accountValid == 0){
			System.out.println("Please login with valid details");//error is data entered is incorrect
			}
		}
		System.out.println("Login successful");	//success message	
		while (functionInput == 0){
			connect.inputHandler(); //Base input menu
		
		while (functionInput == 1){
			connect.getOrders(); //get orders function call
			functionInput = 0;
		}
		while (functionInput == 2){
			connect.viewOrder(); //view order items call
			functionInput = 0;
		}
		while (functionInput == 3){
			connect.updateOrder(); //update order status call
		}
		while (functionInput == 4){
			connect.travelingSalesman(); //display item in warehouse call
			functionInput = 0;
		}
		}
		
	
	}
}