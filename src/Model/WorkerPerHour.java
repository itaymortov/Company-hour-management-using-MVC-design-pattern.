package Model;

import java.util.Currency;

//an employee who get paid by the hour. (getting per hour earnings  from the user gui)
public class WorkerPerHour extends Employee{
	private int perHour;
	
	public WorkerPerHour(String name, Role role, Department department, int perHour) {
		super(name,role,department);
		this.perHour = perHour;
		
	}

	public String toString() {
		return  super.toString() + this.perHour  + " Nis per hour.\n";
	}
}
