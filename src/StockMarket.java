import javax.swing.*;
import java.util.Random;

public class StockMarket {
	private double balance = 10000;
	private double price = 100;
	private int shares;
	private double speed = 2.5;
	private final JLabel balanceL = new JLabel();
	private final JLabel owned = new JLabel();
	private final JLabel netWorth = new JLabel();
	private final JLabel pricing = new JLabel();
	private final JFrame frame = new JFrame("Stock Market");
	private final Object waiting = new Object();




	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void guiCreate(){

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(500,300);
			frame.setLocationRelativeTo(null);


			balanceL.setBounds(25,20,500,30);
			frame.add(balanceL);
			owned.setBounds(25,40,500,30);
			frame.add(owned);
			netWorth.setBounds(25,60,500,30);
			frame.add(netWorth);
			pricing.setBounds(25,80,500,30);
			frame.add(pricing);
			update();


			JTextField buyField = new JTextField("");
			buyField.setBounds(95 ,150, 200,30);
			JButton buyButton = new JButton("Confirm");
			buyButton.setBounds(295, 150, 100, 30);
			buyButton.addActionListener(e -> {
				try {
					double num = Double.parseDouble(buyField.getText());
					withdrawal(num * price);
					shares += num;
				} catch (Exception ignored){}
				buyField.setText("");
				update();
			});
			JLabel buyText = new JLabel("Buy");
			buyText.setBounds(70,150,100,30);
			frame.add(buyField);
			frame.add(buyButton);
			frame.add(buyText);



			JTextField sellField = new JTextField("");
			sellField.setBounds(95,175, 200,30);
			JButton sellButton = new JButton("Confirm");
			sellButton.setBounds(295, 175, 100, 30);
			sellButton.addActionListener(e -> {
				try {
					double num = Double.parseDouble(sellField.getText());
					if(num > shares){throw new RuntimeException("The sell number cannot be greater than " +
																"the number of shares owned");}
					deposit(num * price);
					shares -= num;
				} catch (Exception ignored){}
				sellField.setText("");
				update();
			});

			JLabel sellText = new JLabel("Sell");
			sellText.setBounds(70,175,100,30);
			frame.add(sellField);
			frame.add(sellButton);
			frame.add(sellText);

			frame.setLayout(null);
			frame.setVisible(true);
		}

		private void update(){
			owned.setText("Shares Owned: " + shares);
			if(10 * balance % 1 == 0){
				balanceL.setText("Balance: $" + balance + "0");
			}else{balanceL.setText("Balance: $" + balance);}

			if(10 * price % 1 == 0){
				pricing.setText("Stock Price: $" + price + "0");
			}else{pricing.setText("Stock Price: $" + price);}

			double net = (price * shares) + balance;
			net = round(net);

			if(10 * net % 1 == 0){
				netWorth.setText("Net Worth: $" + net + "0");
			}else{netWorth.setText("Net Worth: $" + net);}

		}

		private void deposit(double money) {
			money = round(money);
			balance += money;
			balance = round(balance);

	}

		private void withdrawal(double money) {
			money = round(money);
			if (money <= balance) {
				balance -= money;
				balance = round(balance);
			} else {
				throw new RuntimeException("The amount withdrawn cannot be greater than balance");
			}
		}

		public void startMarket() throws InterruptedException {
			synchronized (waiting) {

				while (true) {
					waiting.wait((long) (speed * 1000L));
					price += (new Random().nextInt(1000) - 500) / 100.0;
					price = round(price);
					if(price < 0){price = 0;}
					update();
				}
			}
		}


		private double round(double num){
			num = Math.floor(num * 100.0) / 100.0;
			return num;
		}

}
