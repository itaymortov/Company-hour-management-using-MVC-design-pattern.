package Model;

import java.util.Currency;

//an employee who get a fixed salary plus bonus.(all getting from the user gui)
public class WorkerWithBonus extends WorkerSalary{
	private int bonus;
	
	public WorkerWithBonus(String name, Role role, Department department, int salary, int bonus) {
		super(name, role, department, salary);
		this.bonus = bonus;
	}
	
	public String toString() {
		return super.toString() + "\t\t and " + this.bonus + " Nis in bonus\n";
	}
	
}
