import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class money {

	boolean flag = false;
	
	private static final String banksFile = new File("src/banks.txt").getAbsolutePath();
	private static final String customersFile = new File("src/customers.txt").getAbsolutePath();

	public static LinkedHashMap<String, Integer> customerMap = new LinkedHashMap<>();
	public static LinkedHashMap<String, Integer> bankMap = new LinkedHashMap<>();

	public static LinkedList<customer> custThreadList = new LinkedList<>();
	public static LinkedList<bank> bankThreadList = new LinkedList<>();
	
	public static Integer numberOfCostomerCompleted;
	public static LinkedList<String> resultMessages = new LinkedList<>();

	public static void main(String[] args) {

		String line = null;
		numberOfCostomerCompleted = 0;

		FileReader fileReader, fileReader1;
		try {
			fileReader = new FileReader(customersFile);
			fileReader1 = new FileReader(banksFile);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				line = line.replace("{", "").replace("}", "").replace(".", "")
						.replace("(", "").replace(")", "").trim();

				customerMap.put(line.split(",")[0],
						Integer.parseInt(line.split(",")[1]));

			}

			bufferedReader = new BufferedReader(fileReader1);

			while ((line = bufferedReader.readLine()) != null) {
				line = line.replace("{", "").replace("}", "").replace(".", "")
						.replace("(", "").replace(")", "").trim();

				bankMap.put(line.split(",")[0],
						Integer.parseInt(line.split(",")[1]));

			}

			bufferedReader.close();
			fileReader.close();
			fileReader1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Entry<String, Integer> entry : customerMap.entrySet()) {

			customer cust = new customer(entry.getKey());
			custThreadList.add(cust);
			cust.start();

		}

		for (Entry<String, Integer> entry : bankMap.entrySet()) {
			bank bank = new bank(entry.getKey());
			bankThreadList.add(bank);
			bank.start();

		}
		

	}

	public synchronized static void printMessage(String msg) {
		System.out.println(msg);
	}

	public static int getRandomIntegerBetweenRange(double min, double max) {
		double x = (int) (Math.random() * ((max - min) + 1)) + min;
		return (int) x;
	}

}