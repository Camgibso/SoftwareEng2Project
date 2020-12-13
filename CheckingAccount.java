public class CheckingAccount extends Account
{
	CheckingAccount()
	{
		super();
	}
	//Account(int aN, int pn, int sn, double bal, boolean sav)
	CheckingAccount(int aN, int pn, int sn, double bal)
	{
		super(aN, pn, sn, bal);
	}
	public String toString()
	{
		String out = "";
		out += "The Checking Account Number :: " + accountNumber + "\n";
		out += "The Social is 	   :: " + SSN + "\n";
		out += "The Balance is 	   :: " + balance + "\n";
		out += "History Below";
		out += getHistory();
		return out;
	}
}
