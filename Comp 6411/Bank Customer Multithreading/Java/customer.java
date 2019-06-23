import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

class customer extends Thread {
	money m;
	public int firstBalance;
	public int balance;
	public int askedMoney;
	public String customerName;
	public List<String> deniedBanks;

	public customer(String name) {
		this.customerName = name;
	}

	public void run() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int customerMoney = 0;
		boolean flag = false, flag1 = true;
		deniedBanks = new ArrayList<String>();

		while ((!(customerMoney == 0 && flag == true)) && flag1 == true && money.customerMap.get(this.customerName) != 0) {
			
			try {
				Thread.sleep(money.getRandomIntegerBetweenRange(10, 100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int min = 0;
			int max = money.bankMap.size() - 1;

			int randomNumber = money.getRandomIntegerBetweenRange(min, max);


			for (Entry<String, Integer> entry : money.customerMap.entrySet()) {
				if (this.customerName.equalsIgnoreCase(entry.getKey())) {
					customerMoney = entry.getValue();
					if(!flag)
					this.firstBalance = customerMoney;

				}
			}

			int min1 = 1;
			int max1 = 50;

			if (customerMoney < 50) {
				max1 = customerMoney;
			}

			this.askedMoney = money.getRandomIntegerBetweenRange(min1, max1);
			
			bank bank = money.bankThreadList.get(randomNumber);
			String bankName = bank.bankName;
			
			while(deniedBanks.contains(bankName)){
				randomNumber = money.getRandomIntegerBetweenRange(min, max);

				bank = money.bankThreadList.get(randomNumber);
				bankName = bank.bankName;
				
				if(deniedBanks.size() == money.bankMap.size()){
					flag1 = false;
					break;
				}
			}
			
			if(!flag1)
				break;
			
			System.out.println(this.customerName + " requests a loan of "
					+ this.askedMoney + " dollar(s) from " + bankName);

			int bankBalance = 0;
			for (Entry<String, Integer> entry : money.bankMap.entrySet()) {
				if (bankName.equalsIgnoreCase(entry.getKey())) {
					bankBalance = entry.getValue();
				}
			}

			if (bankBalance >= askedMoney) {
				String approvedReply = bankName + " approves a loan of " + this.askedMoney + " dollar(s) from " + this.customerName;
				money.printMessage(approvedReply);
				
				customerMoney = customerMoney - this.askedMoney;
				money.customerMap.put(this.customerName, customerMoney);

				bankBalance = bankBalance - this.askedMoney;
				money.bankMap.put(bankName, bankBalance);
				
			} else {
				String deniedReply = bankName + " denies loan of " + this.askedMoney+ " dollar(s) from " + this.customerName;
				this.deniedBanks.add(bankName);
				money.printMessage(deniedReply);
			}
			flag = true;
		}
		
		money.numberOfCostomerCompleted++;
		if(customerMoney == 0){
			money.resultMessages.add( this.customerName + " has reached the objective of " + this.firstBalance + " dollar(s). Woo Hoo !!!!!");
		}
		else{
			money.resultMessages.add( this.customerName + " was only able to borrow " + (this.firstBalance - customerMoney) + ". Boo Hoo !!!!!");
		}
		
		if(money.numberOfCostomerCompleted == money.customerMap.size()){
			
			for (Entry<String, Integer> entry : money.bankMap.entrySet()) {
					money.resultMessages.add(entry.getKey() + " has " + entry.getValue() + " dollar(s) remaining. ");
			}
			
			for (int i = 0; i < money.resultMessages.size(); i++) {
				money.printMessage(money.resultMessages.get(i));
			}
		}
	}
}
