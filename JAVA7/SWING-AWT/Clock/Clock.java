import java.util.ArrayList;
import java.util.Calendar;

public class Clock implements Observable{

	private Calendar cal;
	private String hour = "";
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();

	public void run(){
		while(true)
		{
			this.cal = Calendar.getInstance();
			this.hour = this.cal.get(Calendar.HOUR_OF_DAY) + " : "
			+ (this.cal.get(Calendar.MINUTE) < 10 ? "0"
			+ this.cal.get(Calendar.MINUTE)
			: this.cal.get(Calendar.MINUTE)
			)
			+ " : "
			+
			(
			(this.cal.get(Calendar.SECOND)< 10)
			? "0"+this.cal.get(Calendar.SECOND)
			: this.cal.get(Calendar.SECOND)
			);
			this.updateObserver();

			try{
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void addObserver(Observer obs){
		this.listObserver.add(obs);
	}

	public void delObserver(){
		this.listObserver = new ArrayList<Observer>();
	}

	public void updateObserver(){
		for(Observer obs : this.listObserver )
			obs.update(this.hour);
	}
}
