public class SavingsAccount extends Account
{
	SavingsAccount()
	{
		super();
	}
	//Account(int aN, int pn, int sn, double bal, boolean sav)
	SavingsAccount(int aN, int pn, int sn, double bal)
	{
		super(aN, pn, sn, bal);
	}
	
  
	public String toString()
	{
		String out = "";
		out += "The Savings Account Number :: " + accountNumber + "\n";
		out += "The Social is 	   :: " + SSN + "\n";
		out += "The Balance is 	   :: " + balance + "\n";
		out += "History Below";
		out += getHistory();
		return out;
	}
}