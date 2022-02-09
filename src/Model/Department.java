package Model;

import java.io.Serializable;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class Department implements Preference, Synchornable, Serializable {
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	private String name;
	private boolean isSync;
	private boolean isChange;
	private int prefStart;
	private int prefEnd;
	private ArrayList<Role> roles;
	private PreferenceChoice choice;
	private double eff;

	public Department(String name) {
		this.choice = PreferenceChoice.valueOf("WORKSAME");
		this.name = name;
		this.isSync = false;
		this.roles = new ArrayList<Role>();
		this.prefStart = 8;
		this.prefEnd = 17;
	}

	//department method for ableing or not ableing employees to change pref
	@Override
	public boolean canChangePref() {
		this.isChange = !this.isChange;
		if(this.isChange) {
			for (Role role : roles) {
				for (Employee employees : role.getAllEmployees()) {
					employees.changeHours();
				}
			}
		}
		
		return this.isChange;
	}
	//department method for ableing or not ableing employees to sync hours
	@Override
	public void syncChange() {
		this.isSync = !(isSync);
			changeHours();	
	}
	//if sync is true changing all roles and employees hours. else returnig hours to role/employee choice
	public void changeHours(){
		for (Role role : roles) {
			role.setStartHour(this.prefStart);
			role.setEndHour(this.prefEnd);
			role.changeHours();
		} 
			
	}

	public boolean getIsSync() {
		return isSync;
	}

	//4 methods for changing dep pref and hours according to the start hour we got from gui.
	@Override
	public void startSooner(int startHour) throws Exception  {
		this.choice = PreferenceChoice.valueOf("WORKSOONER");
		if(startHour < 0 || startHour > 7) {
			throw new Exception("Start hour can't be Later than 7AM, you asked to work sooner,Please enter start hour again");
		}
		this.prefStart = startHour;
		this.prefEnd = startHour + 9;
	}

	@Override
	public void startSame() {
		this.choice = PreferenceChoice.valueOf("WORKSAME");
		this.prefStart = 8;
		this.prefEnd = 17;

	}

	@Override
	public void startLater(int startHour) throws Exception {
		this.choice = PreferenceChoice.valueOf("WORKLATER");
		if(startHour > 15)
			throw new Exception("Start hour can't be higher than 3PM (work hours excced the day),Please enter start hour again");
		else if(startHour < 9)
			throw new Exception("Start hour can't be lower than 8AM,you asked to start later,please enter start hour again");
		this.prefStart = startHour;
		this.prefEnd = startHour + 9;

	}

	@Override
	public void Homework(int startHour) throws Exception {
		this.choice = PreferenceChoice.valueOf("HOMEWORK");
		if(startHour > 15 || startHour < 0)
			throw new Exception("start hour can't be negative or higher than 3PM (work hours excced the day),Please enter start hour again");
		this.prefStart = startHour;
		this.prefEnd = startHour + 9;
	}

	public int getPrefStart() {
		return prefStart;
	}

	public int getPrefEnd() {
		return prefEnd;
	}

	public boolean getIsChange() {
		return isChange;
	}

	public PreferenceChoice getChoice() {	
		return choice;
	}

	//override to equals method
	public boolean equals(Object d) {
		if (this.name.equalsIgnoreCase(((Department)d).name)) {
			return true;
		} else
			return false;
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<Role> getRoles() {
		return roles;
	}
	//adding a role to dep
	public void addRole(Role r) {
		roles.add(r);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(!this.isSync) {
			sb.append("the department " + this.name + " that have " + this.roles.size() + " roles" +  " does not want synchronization " + "\nthe roles in the departments are:\n");
			for (Role role: roles) {
				sb.append("\t" + (roles.indexOf(role)+1) + ") ");
				sb.append(role.toString());
			}
		}
		else {
			sb.append("the department " + this.name + " that have " + this.roles.size() + " want synchronization at " + this.prefStart + "\nthe roles in the departments are:\n");
			for (Role role: roles) {
				sb.append(role.toString());
			}
		}
			
		return sb.toString();
	}
	//calculating efficiency
	public double efficiency() {
		this.eff = 0.0;
		for (Role roles : roles) {
			this.eff = this.eff + roles.efficiency();
		}
		return this.eff;
	}
	//converting efficiency to string for display.
	public String efficiencyToString() {
		this.eff = 0;
		df2.setRoundingMode(RoundingMode.DOWN);
		StringBuffer sb = new StringBuffer(this.name + " efficiency is: "); 
		sb.append(df2.format(efficiency()));
		return sb.toString();
	}
}
