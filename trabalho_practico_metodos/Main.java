package trabalho_practico_metodos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Random;

public class Main {

	public static void main(String[] args) throws IOException {
		
		// This method looks lonely :(
		mainMenu();

	}
	
	private static void mainMenu() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		DecimalFormat df = new DecimalFormat("###, ###, ###.00 MT");
		Random rand = new Random();
				
		byte opt;		
		
		/*
		 * v_count[0] -> Sedan Counter
		 * v_count[1] -> SUV Counter
		 * v_count[2] -> pick-up counter
		 * v_count[3] -> Van counter
		 * 
		 * income_counter[0] -> Sedan Price accumulator
		 * income_counter[1] -> SUV Price accumulator
		 * income_counter[2] -> PICK-UP Price accumulator
		 * income_counter[3] -> Van Price accumulator
		 * income_counter[4] -> Complete Total
		 */
		int[] v_count = new int[4], driver_count = new int[1];
		double[] income_counter = new double[5];
		
		Object[] UserData = new Object[5];
		
		boolean isRegistered = false;
		
		do {
			System.out.println("\n\nWelcome to White Pearl lodge!!");			
			System.out.println("\n\n====== MENU =====\n\n");
			System.out.println("1. Register Data");
			System.out.println("2. View Table data");
			System.out.println("3. Calcular a quantidade de alugueres feitos por cada tipo de viatura");
			System.out.println("4. Calcular a valor total obtido por cada tipo de viatura");
			System.out.println("5. Show most requisited vehicle, and it's total value");
			System.out.println("6. Show how many rented vehicles included a driver");
			System.out.println("7. Show the value of the vehicle that brought the most income");
			System.out.println("8. Calculate the total profit received");
			System.out.println("9. Developer Info");
			System.out.println("10. Exit Program");
			System.out.print("\n=> ");
			opt = Byte.parseByte(br.readLine());
			
			switch (opt) {
				case 1:
					int customer_code = rand.nextInt(1001, 9999);
					UserData = getVehicleRentData(customer_code, v_count, income_counter, driver_count);
					isRegistered = true;
					break;
				case 2:
					if (isRegistered)
						displayUserTable(UserData, df);
						//displayUser(UserData);
					else
						System.out.println("Register first, select option 1");
					
					break;
				case 3:
					displayVehicleRentCounters(v_count);
					break;
				case 4:
					displayVehicleIncome(income_counter, df);
					break;
				case 5:
					findPopularVehicle(v_count, income_counter, df);
					break;
				case 6:
					displayDriverCount(driver_count);
					break;
				case 7:
					findLucrative(v_count, income_counter, df);
					break;
				case 8:
					displayTotalProfit(income_counter, df);
					break;
				case 9:
					vizualizarDadosDoProgrammador();
					break;
				case 10:
					System.out.println("Goodbye...");
					break;
					
			}
		} while (opt != 10);
	}
	
	public static Object[] getVehicleRentData(int code, int[] vCounter, double[] iCounter, int[] dCounter) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.printf("%n%nYou have been assigned the code: %d%n", code);
		
		char v_type;
		int days;
		boolean driver = false;
		
		// Abstract class for clean transportation of data
		Object[] VehicleData = new Object[] { vCounter, iCounter, dCounter };
		
		do {
			System.out.println("Enter Vehicle Type");
			System.out.println("[S] - Sedan");
			System.out.println("[U] - SUV");
			System.out.println("[P] - Pick-up");
			System.out.println("[V] - Van");
			System.out.println("[C] - Cancel");
			System.out.print("\n=> ");
			v_type = Character.toUpperCase(br.readLine().charAt(0));
			
			switch (v_type) {
				case 'S': case 'U': case 'P': case 'V':					
					char d_opt;
					
					do {
						System.out.println("How many days do you look to rent");
						System.out.print("\n=> ");
						days = Integer.parseInt(br.readLine());
						
						if (!isValidNumber(days)) {
							System.out.println("Invalid Number of days, keep it above 0");
							continue;
						}
													
						do {							
							System.out.println("Would you like to hire a driver?");
							System.out.println("[S] - Sim");
							System.out.println("[N] - No");
							System.out.print("\n=> ");
							d_opt = Character.toUpperCase(br.readLine().charAt(0));
							
							if (d_opt != 'S' && d_opt != 'N') {
								System.out.println("Invalid Option.. try again");
								continue;
							}
							
							if (d_opt == 'S')
								driver = true;
							
							// Convert the boolean to string to add to the table
							String driver_status = driver ? "Yes" : "No";
														
							/* 
							 * 
							 * customer ID -> 2034
							 * customer vehicle ID -> P
							 * days to rent -> 3
							 * driver assigned -> true || false
							 * total cost to pay -> (undetermined...)
							 * 
							 * */
							
							// return the user abstract class for creating tables and neat data handling
							return new Object[] {
									code,
									v_type,
									days,
									driver_status,
									getPaymentValue(v_type, days, driver, VehicleData)
							};
							
						} while (d_opt != 'S' && d_opt != 'N');			
												
					} while (!isValidNumber(days));	
					
				case 'C':
					System.out.println("Quitting...");
					return null;
				default:
					System.out.println("Invalid vehicle option...");
					break;
			}
			
		} while(!isVehicleValid(v_type));
		
		return null;
	}
	
	private static void displayUserTable(Object[] User, DecimalFormat df) {
		
		int n = User.length;
		
		String[][] table = new String[2][];
		table[0] = new String[] { "Codigo", "Tipo Viatura", "Dias", "Motorista", "Valor a pagar (Mt)" };
		table[1] = new String[n];
		
		// parse each object in User[] to a string except the last index
		for (int i = 0; i < (n-1); i++)
			table[1][i] = String.valueOf(User[i]);
		
		// parse the last index to DecimalFormat
		table[1][n-1] = df.format(User[n-1]);

		System.out.print("========================================================================================================");
		for (String[] row : table) {
			System.out.format("%n|  %15s  |  %15s  |  %15s  |  %15s  |  %18s  |%n"
					+ "========================================================================================================", row);
		}
		
	}
	
	// only for debugging
	// swap the user table method with this one to see the raw data
	private static void displayUser(Object[] User) {
		System.out.print("[ ");
		
		for (Object val : User)
			System.out.print(" " + val + " ");
		
		System.out.print("]\n");
	}
	
	private static double getPaymentValue(char vehicle, int days, boolean driver, Object[] VehicleData) {
		final double SEDAN = 2000.00;
		final double SUV = 4500.00;
		final double PICKUP = 6500.00;
		final double VAN = 7000.00;
		
		// destructure data
		int[] vehicle_count = (int[]) VehicleData[0];
		double[] income_count = (double[]) VehicleData[1];
		int[] driver_count = (int[]) VehicleData[2];
		
		double currentCost;
		
		switch (vehicle) {
			case 'S':
				currentCost = ((double) calcPrice(days, SEDAN, driver, driver_count));
				vehicle_count[0]++;
				income_count[4] += income_count[0] += currentCost;
				
				return currentCost;
			case 'U':
				currentCost = ((double) calcPrice(days, SUV, driver, driver_count));
				vehicle_count[1]++;
				income_count[4] += income_count[1] += currentCost;
				
				return currentCost;
			case 'P':
				currentCost = ((double) calcPrice(days, PICKUP, driver, driver_count));
				vehicle_count[2]++;
				income_count[4] += income_count[2] += currentCost;
				
				return currentCost;
			case 'V':
				currentCost = ((double) calcPrice(days, VAN, driver, driver_count)); 
				vehicle_count[3]++;
				income_count[4] += income_count[3] += currentCost;
				
				return currentCost;
			default:
				System.out.println("Invalid data...");			
				break;
		}
		
		
		return 0;
	}
	
	private static double calcPrice(int days, double rentPerDay, boolean withDriver, int[] driver_count) {
		final double DISCOUNT_VALUE_MAX = 0.1;
		final double DISCOUNT_VALUE_MIN = 0.05;
		final double DRIVER_VALUE = 1500.0;
		double initialCost = days * rentPerDay;
		
		if (withDriver) {
			System.out.println("Driver cost applied");
			driver_count[0]++;
			initialCost += (initialCost + (days * DRIVER_VALUE));
		}

		if (days > 15)
			getDiscount(initialCost, DISCOUNT_VALUE_MAX);
		else if (days <= 15 && days > 7)
			getDiscount(initialCost, DISCOUNT_VALUE_MIN);
		
		return initialCost;
	}
	
	private static void displayTotalProfit(double[] income_counter, DecimalFormat df) {
		System.out.println("Total Profit");
		System.out.println("\n--------------------");
		System.out.printf("|  %10s  |", df.format(income_counter[4]));
		System.out.println("\n--------------------");
		
	}
	
	private static void displayVehicleRentCounters(int[] vCount) {
		System.out.println("\n\n------------------------");
		System.out.printf("| %d Sedans were rented |%n", vCount[0]);
		System.out.println("------------------------\n");
		
		System.out.println("----------------------");
		System.out.printf("| %d SUVs were rented |%n", vCount[1]);
		System.out.println("----------------------\n");
		
		System.out.println("--------------------------");
		System.out.printf("| %d Pick-ups were rented |%n", vCount[2]);
		System.out.println("--------------------------\n");
		
		System.out.println("----------------------");
		System.out.printf("| %d Vans were rented |%n", vCount[3]);
		System.out.println("----------------------\n");
	}
	
	private static void displayVehicleIncome(double[] income, DecimalFormat df) {
		
		
		String[][] table = new String[2][];
		table[0] = new String[] { "Sedan", "SUV", "Pickup", "Van", "Total" };
		table[1] = new String[income.length];
		
		for (int i = 0; i < income.length; i++)
			table[1][i] = df.format(income[i]);
		
		System.out.println("===========================================================================================");
		for (String[] row : table) {
			System.out.format("| %15s | %15s | %15s | %15s | %15s | %n"
			+ "===========================================================================================%n", row);			
		}
	}
	
	private static void findPopularVehicle(int[] vCounter, double[] income_counter, DecimalFormat df) {
		int targetIndex = 0, 
				largestElem = findMaxValue(vCounter);
		
		// find the index that the largest number belongs to in vCounter
		targetIndex = searchVehicle(vCounter, largestElem);
		
		System.out.println("Most popular vehicle\n");
		// streamData() -> a utility method to parse, organize and display the data
		streamData(targetIndex, vCounter, df.format(income_counter[targetIndex]));
	}
	
	private static void displayDriverCount(int[] dCount) {
		System.out.printf("%n=============="
				+ "%n|  %d Drivers  |"
				+ "%n==============", dCount[0]);
	}
	
	private static void findLucrative(int[] vCount, double[] iCounter, DecimalFormat df) {
		double lucro = findMaxValue(iCounter); // get the max value from income_count
		int targetIndex = 0;

		targetIndex = searchVehicle(iCounter, lucro); 
		System.out.println("Most lucrative vehicle\n");
		
		if (targetIndex > 0) targetIndex -= 1;
		
		streamData(targetIndex, vCount, df.format(iCounter[targetIndex]));
	}
	
	private static void streamData(int targetIndex, int[] vehicle_quantity, String vehicle_cost) {		
		String[] values = new String[] {
				String.valueOf(vehicle_quantity[targetIndex]),
				vehicle_cost
		};
		
		switch (targetIndex) {
			case 0:
				displayVehicle("Sedans", values);
				break;
			case 1:
				displayVehicle("SUVs", values);
				break;
			case 2:
				displayVehicle("Pick-ups", values);
				break;
			case 3:
				displayVehicle("Vans", values);
				break;
		}
	}
	
	private static void displayVehicle(String v_name, String[] rowValues) {
		
		String[][] table = new String[2][];
		table[0] = new String[] { v_name, "Total Profit" };
		table[1] = rowValues;
		
		System.out.println("====================================");
		for (String[] row : table) {
			System.out.format("| %10s | %20s | %n"
					+ "====================================%n", row);
		}
		
	}
	
	// Linear Search algorithm to find the index in vehicle_counter that matches the target
	private static int searchVehicle(int[] vCounter, int target) {
		for (int i = 0; i < vCounter.length; i++)
			if (vCounter[i] == target)
				return i;
		
		return 0;
	}
	
	private static int searchVehicle(double[] iCounter, double target) {
		for (int i = 0; i < iCounter.length; i++)
			if (iCounter[i] == target)
				return i;
		
		return 0;
	}
	
	private static int findMaxValue(int[] vCounter) {
		int n = vCounter.length, temp = 0;
		int[] new_arr = new int[n];
		
		// clone vCounter
		for (int i = 0; i < n; i++)
			new_arr[i] = vCounter[i];
		
		// use the clone to sort and return the max value
		for (int i = 0; i < n; i++) {
			for (int j = i+1; j < n; j++) {
				if (new_arr[i] > new_arr[j]) {
					temp = new_arr[i];
					new_arr[i] = new_arr[j];
					new_arr[j] = temp;
				}
			}
		}
		
		return new_arr[n-1];
	}
	
	private static double findMaxValue(double[] iCounter) {
		int n = iCounter.length;
		double temp = 0;
		double[] new_arr = new double[n];
		
		for (int i = 0; i < n; i++)
			new_arr[i] = iCounter[i];
		
		for (int i = 0; i < n; i++) {
			for (int j = i+1; j < n; j++) {
				if (new_arr[i] > new_arr[j]) {
					temp = new_arr[i];
					new_arr[i] = new_arr[j];
					new_arr[j] = temp;
				}
			}
		}
		
		return new_arr[n-1];
	}
	
	private static double getDiscount(double cost, double discount) {
		System.out.println("\nDiscount Applied");
		return (cost += (cost * discount));
	}
	
	private static boolean isVehicleValid(char c) {
		if (c == 'S' || c == 'U' || c == 'P' || c == 'V')
			return true;
		else
			return false;
	}
	
	private static boolean isValidNumber(int n) {
		if (n > 0)
			return true;
		else
			return false;
	}
	
	private static void vizualizarDadosDoProgrammador() {
		String[][] table = new String[4][];
		table[0] = new String[] { "Nome", "Codigo de estudante" };
		table[1] = new String[] { "Daniel Kruger", "20220303" };
		table[2] = new String[] { "Mariamo Narotam", "20220985" };
		table[3] = new String[] { "Yasser Abdul", "20220384" };
		
		System.out.println("===============================================");
		for (String[] row : table) {
			System.out.format("| %20s | %20s | %n"
			+ "===============================================%n", row);			
		}
	}
	
}



