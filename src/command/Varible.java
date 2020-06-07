
package command;

import model.SimulatorConnection;

public class Varible {
	//Data
	private double value;
	private String bind;
	
	//Ctor
	public Varible(double value) {
		this.value = value;
		bind = null;
	}
	
	//Getters
	public String getBind() {return bind;}
	public double getValue() {return value;}
	
	//Setters
	public void setBind(String bind) {this.bind = bind;}
	public void setValue(double value) {
		this.value = value;
		if(bind != null) 
			try{
				String[] outToServer ={
						"set "+bind+" "+Double.toString(value)
				};
				SimulatorConnection.getConnection("127.0.0.1",5402).sendData(outToServer);
			}catch (Exception e) {}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Double.toString(this.value);
	}
	
}
