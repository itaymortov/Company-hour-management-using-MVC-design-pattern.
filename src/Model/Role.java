package Model;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class Role implements Preference, Synchornable, Serializable {
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	private String roleName;
	private ArrayList<Employee> allEmployees;
	private int startHour;
	private int endHour;
	private int prefStart;
	private int prefEnd;
	private Department department;
	private boolean isSync;
	private boolean isChange;
	private double eff;
	private PreferenceChoice choice;


	public Role(String name){
		this.roleName = name;
		this.allEmployees = new ArrayList<Employee>();
		this.choice = PreferenceChoice.valueOf("WORKSAME");
		this.startHour = 8;
		this.endHour = 17;
		this.prefStart = 8;
		this.prefEnd = 17;

	}

	public Role(Role r, Department d) {
		this(r.getRoleName());
		this.department = d;
	}

	public String getRoleName() {
		return this.roleName;
	}
	//Role method for ableing or not ableing employees to change pref
	@Override
	public boolean canChangePref() {
		this.isChange = !this.isChange;
		if(this.isChange) {
			for (Employee employees : this.allEmployees) {
				employees.changeHours();
			}
		}
		return this.isChange;
	}

	public boolean getIsSync() {
		return this.isSync;
	}

	public int getStartHour() {
		return startHour;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	//Role method for ableing or not ableing employees to sync hours
	@Override
	public void syncChange() {
		this.isSync = !(isSync);
		if(!(department.getIsSync()))
			changeHours();
	}
	//if sync is true changing all employees hours. else returnig hours to employee choice
	@Override
	public void changeHours() {
		if(department.getIsSync()) {
			for (Employee employee : allEmployees) {
				employee.setStartHour(this.startHour);
				employee.setEndHour(this.endHour);
			}
		}
		else {
			if(this.getIsSync()) {
				for(Employee employee : allEmployees){
					employee.setStartHour(this.prefStart);
					employee.setEndHour(this.prefEnd);
				}
			}
				else {
					for(Employee employee : allEmployees){
						employee.setStartHour(employee.getPrefStart());
						employee.setEndHour(employee.getPrefStart() + 9);
					}
				}
			}
	}
	//4 methods for changing role pref and hours according to the start hour we got from gui.
	@Override
	public void startSooner(int startHour) throws Exception  {
		this.choice = PreferenceChoice.valueOf("WORKSOONER");
		if(startHour < 0 || startHour > 7) {
			throw new Exception("Start hour can't be Later than 7AM, you asked to work sooner,Please enter start hour again");
		}
		this.prefStart = startHour;
		this.prefEnd = startHour + 9;
		if(this.prefEnd > 24)
			this.prefEnd =-24;
		ChangeAccHours();
	}

	@Override
	public void startSame() {
		this.choice = PreferenceChoice.valueOf("WORKSAME");
		this.prefStart = 8;
		this.prefEnd = 17;
		ChangeAccHours();
	}

	@Override
	public void startLater(int startHour) throws Exception {
		this.choice = PreferenceChoice.valueOf("WORKLATER");
		if(startHour > 15)
			throw new Exception("Start hour can't be higher than 3PM (work hours excced the day),Please enter start hour again");
		else if(startHour < 9)
			throw new Exception("Start hour can't be lower than 9AM,you asked to start later,please enter start hour again");
		this.prefStart = startHour;
		this.prefEnd = startHour + 9;
		ChangeAccHours();
	}

	@Override
	public void Homework(int startHour) throws Exception {
		this.choice = PreferenceChoice.valueOf("HOMEWORK");
		if(startHour > 15 || startHour < 0)
			throw new Exception("start hour can't be negative or higher than 3PM (work hours excced the day),Please enter start hour again");
		this.prefStart = startHour;
		this.prefEnd = startHour + 9;
		ChangeAccHours();

	}

	public boolean getIsChange() {
		return isChange;
	}

	public PreferenceChoice getChoice() {	
		return choice;
	}

	//setting department to the role.
	public void setDepartment (Department d) {
		this.department = d;
	}
	
	//override to equals method
	public boolean equals(Object r) {
		if (this.roleName.equalsIgnoreCase(((Role)r).roleName)) {
			return true;
		} else
			return false;
	}

	// if dep letting to change hours and not forcing a sync changing role hours according to it pref.
	public void ChangeAccHours() {
		if(!this.department.getIsChange() && !this.department.getIsSync()) {
			this.startHour = this.prefStart;
			this.endHour = this.prefEnd;
		}
		else if(this.department.getIsSync()) {
			this.startHour = this.department.getPrefStart();
			this.endHour = this.department.getPrefEnd();
		}
	}

	public void addEmployee(Employee worker) {
		this.allEmployees.add(worker);
	}

	public ArrayList<Employee> getAllEmployees(){
		return this.allEmployees;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(!this.isSync) {
			sb.append(this.roleName + " that have " + this.allEmployees.size() + " emplyoees" + " does not want synchronization" + " \n\tthe emplpyees with that role are:\n");
			for (Employee employee : allEmployees) {
				sb.append("\t\t" + (allEmployees.indexOf(employee) + 1) + ") ");
				sb.append(employee.toString());
			}
		}
		else {
			sb.append(this.roleName + " that have " + this.allEmployees.size() + " emplyoees" + " want synchronization from " + this.prefStart + " \n\tthe emplpyees with that role are:\n");
			for (Employee employee : allEmployees) {
				sb.append("\t\t" + (allEmployees.indexOf(employee) + 1) + ") ");
				sb.append(employee.toString());
			}
		}
		return sb.toString();
	}
	//converting efficiency to string for display.
	public String efficiencyToString() {
		df2.setRoundingMode(RoundingMode.DOWN);
		StringBuffer sb = new StringBuffer(this.roleName + " efficiency is: "); 
		sb.append(df2.format(this.efficiency()));

		return sb.toString();
	}
	//calculating efficiency
	public double efficiency() {
		this.eff = 0.0;
		for (Employee employee : allEmployees) {
			this.eff = this.eff + employee.efficiency();
		}
		return this.eff;
	}
}
