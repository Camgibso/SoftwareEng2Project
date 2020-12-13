
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.*;




public class BankSystem {

  //scanner object to get user input
  Scanner userInput = new Scanner(System.in);

  //list of current account objects
  public static volatile ArrayList<Account> accounts = new ArrayList<Account>();


  //constructor populates account arrays with given initial data
  BankSystem() {
    //add checking accounts
    accounts.add(new CheckingAccount(1, 5678, 123456789, 300));
		accounts.add(new CheckingAccount(2, 4567, 234567890, 4000));
		accounts.add(new CheckingAccount(3, 3456, 345678901, 70));
		accounts.add(new CheckingAccount(4, 2345, 456789012, 5000));
		
		//adding savings accounts 
		accounts.add(new SavingsAccount(10001, 1234, 567890123, 700));
		accounts.add(new SavingsAccount(10002, 0123, 234567890, 2000));
		accounts.add(new SavingsAccount(10003, 6789, 789012345, 9000));
		accounts.add(new SavingsAccount(10004, 7890, 890123456, 1000));
  }

  //periodically add interest to accounts
  public static ScheduledExecutorService x = Executors.newSingleThreadScheduledExecutor();

  static {
    x.scheduleAtFixedRate(new Runnable() {
      public void run() {
        for (int i = 0; i < BankSystem.accounts.size(); i++) {

          //object is of type SavingsAccount
          if (BankSystem.accounts.get(i) instanceof SavingsAccount) {
            double old_bal = BankSystem.accounts.get(i).getBalance();

            BankSystem.accounts.get(i).setBalance(
              BankSystem.accounts.get(i).getBalance() + (BankSystem.accounts.get(i).getBalance())*0.05
            );

            if (old_bal != 0.0) {
              BankSystem.accounts.get(i).addHistory(
                BankSystem.accounts.get(i).getBalance() - old_bal
              );
            }
          }
        }
      }
    }, 30, 30, TimeUnit.SECONDS);
  }


  //removes account from accounts list given account number
  //returns true for successful remove, false otherwise
  public boolean close_account(int acc_num) {

    for(int i = 0; i < accounts.size(); i++) {

      //if the account number is found
      if(acc_num == this.accounts.get(i).getAccountNumber()) {
        this.accounts.get(i).setBalance(0.0);
        this.accounts.remove(i);
        System.out.println("Account " + acc_num + " closed.");
        return true;
      }
    }
    //return false if acc num not found
    return false;
  }


  //adds account to accounts list given appropriate constructor info
  //returns true for successful add, false otherwise 
  public boolean open_account(int pin, int ssn, char acc_type) {

    //pin invalid
    if ((pin < 0000) || (pin > 9999)) {
      this.clearScreen();
      System.out.println("Invalid PIN.");
      return false;
    }

    //account num invalid
    else if ((ssn < 000000000) || (pin > 999999999)) {
      this.clearScreen();
      System.out.println("Invalid SSN.");
      return false;
    }

    //account type invalid
    else if ((acc_type != 'c') && (acc_type != 's')) {
      this.clearScreen();
      System.out.println("Invalid account type.");
      return false;
    }


    //everything checks out
    //generate new account number without clashing with current acc numbers
    int max = 10000;
    for (int i = 0; i < accounts.size(); i++) {
      if (accounts.get(i).getAccountNumber() > max) {
        max = accounts.get(i).getAccountNumber();
      }
    }

    int new_acc_num = max + 1;

    if (acc_type == 'c') {
      Account new_acc = new CheckingAccount(new_acc_num, pin, ssn, 0.0);
      this.accounts.add(new_acc);
      System.out.println("Opened new checking account with number " + new_acc_num);
      return true;
    }

    if (acc_type == 's') {
      Account new_acc = new SavingsAccount(new_acc_num, pin, ssn, 0.0);
      System.out.println("Opened new savings account with number " + new_acc_num);
      this.accounts.add(new_acc);
      return true;
    }
    return false;
  }


  //validates pin based on current accound data
  //return value of false means customer entered data incorrectly 3 times
  public boolean validate_pin(Account acc) {

    System.out.println("Please enter a PIN:");
    int pin = this.userInput.nextInt();
    
    int attempts = 1;

    while (!valid_pin(pin, acc)) {
      System.out.println("Invalid PIN and account combination. " +(3-attempts) + " attempts remaining.");

      System.out.println("Please re-enter a valid PIN number:");
      pin = this.userInput.nextInt();

      attempts++;
      //incorrect pin entered 3 times
      if (attempts == 3) {
        return false;
      }
    }
    return true;
  }


  //helper for validate_pin
  //return true if pin and account combination is valid
  //else returns false
  public boolean valid_pin(int pin, Account acc) {
    if (acc.getPin() == pin) {
      return true;
    }
    return false;
  }


  //helper to run
  //clears console to avoid repeatedly printing menu
  //https://stackoverflow.com/questions/2979383/java-clear-the-console
  public void clearScreen() {  
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
  }  


  //UPDATE: clears console after various options for cleaner interface
  public void customer_menu() {
    
    int choice;
    double tempBal;

    do
		{
			
			System.out.println("Enter a valid account Number ::");
			choice = userInput.nextInt();
			//loop through the accounts looking for the 
			for(int i = 0; i < this.accounts.size(); i++)
			{
				//if the account number is found
				if(choice == this.accounts.get(i).getAccountNumber())
				{

					if(validate_pin(this.accounts.get(i)))
					{
						//go to user options
            this.clearScreen();
						System.out.println("Login Successful!\n\n");
						do
						{
							//user options, then will prompt the user 
							System.out.println("\n\nUser Options");
							System.out.println("Enter the corresponding number to chose \nthe opperation it corresponds with.");
							System.out.println("1 :: View Account Balance");
							System.out.println("2 :: Deposit funds");
							System.out.println("3 :: Withdraw funds" );
							System.out.println("4 :: Transfer funds");
							System.out.println("5 :: View Account History");
							System.out.println("6 :: View all account history");
							System.out.println("0 :: to log out");
							System.out.print("Enter Your choice :: ");
							choice = userInput.nextInt();

							//switch to chose opperation 
							switch(choice)
							{

							case 1:
							{
								//print balance
                this.clearScreen();
								System.out.println("Your balance is :: $" + this.accounts.get(i).getBalance());
								break;
							}

							case 2:
							{
								//deposit funds
								//prompt the user for the amount 
                this.clearScreen();
								System.out.println("Enter the amount you are depositing ::");
								tempBal = userInput.nextDouble();
								
								//changes the account balance, by the amount prompted for								
								this.accounts.get(i).setBalance(this.accounts.get(i).getBalance() + tempBal);
								//adds transaction to history
								this.accounts.get(i).addHistory(tempBal);
								
								//reset the choice to not mess with switch
								break;
							}

							case 3:
							{
								//withdraw funds
								
								//Print the users balance so they do not overdraft
                this.clearScreen();
								System.out.println("Your current balance is :: $" + this.accounts.get(i).getBalance());
								
								//set the var very high 
								tempBal = Double.MAX_VALUE;

								//prompt the user for an amount, and countinue to prompt the user until the amount 
								//is less than the currnent balance of the account
								do
								{
                  this.clearScreen();
									System.out.println("We do NOT allow over drafts\nPlease enter the amount you would like to withdraw :: ");
									tempBal = userInput.nextDouble();

								}while(!(tempBal <= this.accounts.get(i).getBalance()) || (tempBal <= 0));
								
                
								//when a accectable balance is given it will be subtracked from the users account
								this.accounts.get(i).setBalance(this.accounts.get(i).getBalance()-tempBal);
								this.accounts.get(i).addHistory(tempBal*-1);
                System.out.println("Withdrawal successful!");
                System.out.println("Your new balance is $" + this.accounts.get(i).getBalance());
								break;
							}

							case 4:
							{
								//transfer funds
								//var to keep track of the other account
								int j = 0;
								
                this.clearScreen();
								//prompt user for account number
								System.out.println("Enter the other accounts number :: ");
								choice = userInput.nextInt();
								while (choice != this.accounts.get(j).getAccountNumber() && j < this.accounts.size())
								{
									j++;
								}

								if(j == this.accounts.size())
								{
									//if an invalid account number is given, break
									System.out.println("Invalid Account number given");
								}

								else
								{
									//prompt the user for the amount, will check for overdraft int the transfer function 
									System.out.println("This Number will be added to your account and subtracted from theirs");
									System.out.println("If you would like to send money to their account enter the number as a negative number.");
									System.out.println("Enter how much you would like to transfer :: ");
									tempBal = userInput.nextDouble();
									
									//resetting the other changer account, the other account will change itself in the function
									this.accounts.set(j, this.accounts.get(i).transferFunds(this.accounts.get(j), tempBal));
								}
								//reset the choice var & break switch
								choice = 4;
								break;
							}

							case 5:
							{
                this.clearScreen();
								//view account history
								System.out.println("Your balance history is below\n" + this.accounts.get(i).getHistory());
								break;
							}

							case 6:
							{
                this.clearScreen();
								//print all account information
								System.out.println("All account information below\n" + this.accounts.get(i));
								break;
							}

							case 0:
							{
                this.clearScreen();
								//logout case
                //return to get back to main menu
								System.out.println("GoodBye\n\n\n\n\n\n");
								return;
							}

							default:
							{
								System.out.println("Invalid input, try again");
								break;
							}
							
							}//end of switch
						//0 is the kill condition
						}while(choice != 0);
					}

					else
					{
						//when pin is ivalid 
				
            while (true) {
              this.clearScreen();
              System.out.println("Maxmium incorrect PIN attempts reached.");
              System.out.println("1 :: Retry\n2 :: Exit");

              int c = userInput.nextInt();

              if (c == 1) {break;}
              if (c == 2) {this.clearScreen();return;}
            }
					}
				}
			}
		}while(choice > -1);
		
		//close scanner 
		userInput.close();
  }

  //allow employees to call open_account, close_account
  //and add_interest functions
  public void employee_menu() {
	   
	  while (true){	
			System.out.println("1 :: Open Account");
			System.out.println("2 :: Close Account");
			System.out.println("3 :: Go Back to Main Menu");
			int option = this.userInput.nextInt();
			if(option == 1){
				//open account with int pin, int ssn, char acc_type
				System.out.println("Please enter a pin(Between 0000 and 9999: ");
				int pin = this.userInput.nextInt();
				System.out.println("Please enter a SSN (Between 000000000 and 999999999): ");
				int ssn = this.userInput.nextInt();
				//was giving me trouble scanning "checkings" so made it scan a char instead
				System.out.println("For Checkings type 'c', for Savings type 's': ");
				char acc_type = this.userInput.next().charAt(0);

				this.open_account(pin, ssn, acc_type);
			}
			if(option == 2){
				//close account
				System.out.println("Enter Account Number to Close: ");
				int acc_num = this.userInput.nextInt();
				this.close_account(acc_num);
			}
    
			if(option == 3){	//close
				this.clearScreen();
				return;
			}
		}
  }


  //Menu to choose customer or bank staff
  public void run() {
	   
	  while (true){
		  System.out.println("1 :: Customer");
		  System.out.println("2 :: Bank Staff");
		  System.out.println("3 :: Exit");
		   
		  int choice = this.userInput.nextInt();
		   
		  if (choice == 1){
				this.clearScreen();
				this.customer_menu();
			}
		  if (choice == 2){
				this.clearScreen();
				this.employee_menu();
			}
		  if (choice == 3){   
				this.clearScreen();
				System.out.println("Goodbye!");
        this.userInput.close();
        break;
			}

       else {
        continue;
      }
	  }
  }
}
