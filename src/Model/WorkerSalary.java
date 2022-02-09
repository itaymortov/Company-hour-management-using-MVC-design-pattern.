package Model;

import java.util.Currency;

//an employee who get a fixed salary. (getting salary from the user gui)
public class WorkerSalary extends Employee{
	protected int salary;
	
	public WorkerSalary(String name, Role role, Department department,int salary) {
		super(name, role, department);
		this.salary = salary;
	}

	public String toString() {
		return super.toString() + this.salary + " Nis a month\n";
	}
	
	
}
