package ch.traiding.model;
/**
* @author  Joshua	Blöchliger
* @version 1.1
*/
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TradingService test = new TradingService();
		User u = test.login("admin", "Lernende123");
		for(int i=0;i<u.getAktien().size();i++){
			System.out.println(u.getAktien().get(i).getName());
		}
	}

}