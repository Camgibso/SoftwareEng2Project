import java.util.ArrayList;
import java.util.Scanner;

public class Account 
{
	//account vars
	protected int accountNumber, pin, SSN;
	protected double balance;
	protected ArrayList<Double> history;

	Account()
	{
		accountNumber = 00000;
		pin = 0000;
		SSN = 999999999;
		balance = 0.00;
		history = new ArrayList<Double>();
	}


	Account(int aN, int pn, int sn, double bal)
	{
		accountNumber = aN;
		pin = pn;
		SSN = sn;
		balance = bal;
		history = new ArrayList<Double>();
	}

	public boolean validPin(int pn)
	{
		if(pn == pin)
		{
			return true;
		}
		return false;
	}
	
	
	//Simple gets and sets
	public double getBalance()
	{
		return this.balance;
	}


	public void setBalance(double bal)
	{
		this.balance = bal;
	}


	public int getAccountNumber()
	{
		return this.accountNumber;
	}


	public void setAccountNumber(int an)
	{
		this.accountNumber = an;
	}


 	public int getPin()
	{
		return this.pin;
	}


	public void setPin(int pn)
	{
		if (pn > 1000 && pn < 10000)
		{
			this.pin = pn;
		}

		else
		{
			System.out.println("Pin invalid length");
		}
	}


	public int getSSN()
	{
		return SSN;
	}


	public void setSSN(int sn)
	{
		if(sn > 100000000 && sn < 1000000000)
		{
			SSN = sn;
		}

		else
		{
			System.out.println("SSN invalid length");
		}
	}


	public void addHistory(double amount)
	{
		this.history.add(amount);
	}


  /*returns an account type so that however the other account is altered, the main will have an account to replace the 
		 * one in the main with, with the correct balance*/
	public Account transferFunds(Account other, double amount)
	{
		//verify if the balance is too high to transfer
		if(other.getBalance() < amount)
		{
			System.out.println("The amount was greater than the balance of the account");
			return other;
		}

		//if the amount is negative than the balance will be taken out of this account 
		//so we need to verify this account has the correct balance
		if((amount < 0) && (amount*-1) > this.balance)
		{
			System.out.println("The amount was greater than the balance of the account");
      //UPDATE: nothing was returned here prevously
      //I updated it so it returns the 'other' account obj 
      //with no data altered
      return other;
		}

    //else, go through with transfer
		this.balance += amount;
		other.setBalance(other.getBalance() - amount);
		this.addHistory(amount);
		other.addHistory(amount*-1);
		return other;
	}


	public String getHistory()
	{
		//outputString
		String out = "";
		//if there is no history
		if(history.size() < 1)
		{
			return "No History";
		}
		//loops through history, giving the associated action with the amount
		for(int i = 0; i < history.size(); i++)
		{
			if(history.get(i) > 0)
			{
				out += "Deposit :: $" + history.get(i) + "\n";
			}
			else
			{
				out += "Withdraw :: $" + (history.get(i)*-1) + "\n";
			}
		}
		return out;
	}


  //String representation of object
	public String toString()
	{
		String out = "";
		out += "The Account Number :: " + accountNumber + "\n";
		out += "The Social is 	   :: " + SSN + "\n";
		out += "The Balance is 	   :: " + balance + "\n";
		out += "History Below\n";
		out += getHistory();
		return out;
	}


	//test methods
	public static void main(String[] args) 
	{
		//vars
		ArrayList<Account> accts = new ArrayList<Account>();
		int choice = 999999;
		Scanner board = new Scanner(System.in);
		double tempBal;
		
		
		//Account(int aN, int pn, int sn, double bal, boolean sav)
		
		//adding checking accounts
		accts.add(new CheckingAccount(1, 5678, 123456789, 300));
		accts.add(new CheckingAccount(2, 4567, 234567890, 4000));
		accts.add(new CheckingAccount(3, 3456, 345678901, 70));
		accts.add(new CheckingAccount(4, 2345, 456789012, 5000));
		
		//adding savings accounts 
		accts.add(new SavingsAccount(10001, 1234, 567890123, 700));
		accts.add(new SavingsAccount(10002, 0123, 234567890, 2000));
		accts.add(new SavingsAccount(10003, 6789, 789012345, 9000));
		accts.add(new SavingsAccount(10004, 7890, 890123456, 1000));
		
		//loginprocess, and user options
		
		do
		{
			
			System.out.println("Enter Your account Number ::");
			choice = board.nextInt();
			//loop through the accounts looking for the 
			for(int i = 0; i < accts.size(); i++)
			{
				//if the account number is found
				if(choice == accts.get(i).getAccountNumber())
				{
					//prompt user for pin, if invalid the user will have 
					//to reenter their account number again
					System.out.println("Enter your Pin :: ");
					choice = board.nextInt();

					if(accts.get(i).validPin(choice))
					{
						//go to user options 
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
							choice = board.nextInt();

							//switch to chose opperation 
							switch(choice)
							{

							case 1:
							{
								//print balance
								System.out.println("Your balance is :: $" + accts.get(i).getBalance());
								break;
							}

							case 2:
							{
								//deposit funds
								//prompt the user for the amount 
								System.out.println("Enter the amount you are depositing ::");
								tempBal = board.nextDouble();
								
								//changes the account balance, by the amount prompted for								
								accts.get(i).setBalance(accts.get(i).getBalance() + tempBal);
								//adds transaction to history
								accts.get(i).addHistory(tempBal);
								
								//reset the choice to not mess with switch
								break;
							}

							case 3:
							{
								//withdraw funds
								
								//Print the users balance so they do not overdraft
								System.out.println("Your current balance is :: $" + accts.get(i).getBalance());
								
								//set the var very high 
								tempBal = Double.MAX_VALUE;

								//prompt the user for an amount, and countinue to prompt the user until the amount 
								//is less than the currnent balance of the account
								do
								{
									System.out.println("We do NOT allow over drafts\nPlease enter the amount you would like to withdraw :: ");
									tempBal = board.nextDouble();

								}while(tempBal <= accts.get(i).getBalance());
								
								//when a accectable balance is given it will be subtracked from the users account
								accts.get(i).setBalance(accts.get(i).getBalance()-tempBal);
								accts.get(i).addHistory(tempBal*-1);
								break;
							}

							case 4:
							{
								//transfer funds
								//var to keep track of the other account
								int j = 0;
								
							
								//prompt user for account number
								System.out.println("Enter the other accounts number :: ");
								choice = board.nextInt();
								while (choice != accts.get(j).getAccountNumber() && j < accts.size())
								{
									j++;
								}

								if(j == accts.size())
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
									tempBal = board.nextDouble();
									
									//resetting the other changer account, the other account will change itself in the function
									accts.set(j, accts.get(i).transferFunds(accts.get(j), tempBal));
								}
								//reset the choice var & break switch
								choice = 4;
								break;
							}

							case 5:
							{
								//view account history
								System.out.println("Your balance history is below\n" + accts.get(i).getHistory());
								break;
							}

							case 6:
							{
								//print all account information
								System.out.println("All account information below\n" + accts.get(i));
								break;
							}

							case 0:
							{
								//logout case,
								System.out.println("GoodBye\n\n\n\n\n\n");
								break;
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
						System.out.println("Invalid Pin, try again.");
					}
				}
			}
		}while(choice > -1);
		
		//close scanner 
		board.close();
	}
}