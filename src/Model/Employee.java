package Model;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

enum PreferenceChoice{
	WORKSOONER, WORKLATER, WORKSAME, HOMEWORK
};



public class Employee implements Synchornable, Preference, Serializable {
	protected static DecimalFormat df2 = new DecimalFormat("#.##");
	protected String name;
	protected int startHour;
	protected int endHour;
	protected int prefStart;
	protected int prefEnd;
	protected Role role;
	protected Department department;
	protected double efficiency;
	protected PreferenceChoice choice;
	protected boolean isSync;


	public Employee(String name,Role role,Department department){
		this.name = name;
		this.choice = PreferenceChoice.valueOf("WORKSAME");
		this.role = role;
		this.department = department;
		this.prefStart = 8;
		this.prefEnd = 17;
		this.startHour = 8;
		this.endHour = 17;
	}

	public int getPrefStart() {
		return prefStart;
	}
	//calculating efficiency.
	public double efficiency() {
		this.efficiency = 0.0;
		switch(choice) {
		case HOMEWORK:
			if(!department.getIsSync() && !role.getIsSync())
				this.efficiency = 0.9; // 9 hours times 0.1
			else if(department.getIsSync()) {
				if(department.getChoice() == PreferenceChoice.HOMEWORK)
					this.efficiency = 0.9;
				else if(department.getChoice() == PreferenceChoice.WORKSAME)
					this.efficiency = 0.0;
				else if(department.getChoice() == PreferenceChoice.WORKLATER)
					this.efficiency = (17 -this.endHour) * 0.2;
				else if(role.getChoice() == PreferenceChoice.WORKSOONER) {
					this.efficiency = (this.startHour - 8) * 0.2;
				}
			}
			else if(role.getIsSync()) {
				if(role.getChoice() == PreferenceChoice.HOMEWORK)
					this.efficiency = 0.9;
				else if(role.getChoice() == PreferenceChoice.WORKSAME)
					this.efficiency = 0.0;
				else if(role.getChoice() == PreferenceChoice.WORKLATER)
					this.efficiency = (17 -this.endHour) * 0.2;
				else if(role.getChoice() == PreferenceChoice.WORKSOONER) {
					this.efficiency = (this.startHour - 8) * 0.2;
				}
			}
			break;
		case WORKLATER:
			if(!department.getIsSync() && !role.getIsSync())
				this.efficiency	= (this.endHour - 17) * 0.2;
			else if(department.getIsSync()) {
				if(department.getChoice() == PreferenceChoice.HOMEWORK)
					this.efficiency = 0.9;
				else if(department.getChoice() == PreferenceChoice.WORKSAME)
					this.efficiency = 0.0;
				else if(department.getChoice() == PreferenceChoice.WORKLATER)
					if(this.endHour > this.prefEnd )
						this.efficiency = ((this.prefEnd - this.endHour) + (this.endHour - 17)) * 0.2;
					else
						this.efficiency = (this.endHour - 17) * 0.2;
				else if(department.getChoice() == PreferenceChoice.WORKSOONER) {
					this.efficiency = (this.startHour - 8) * 0.2;
				}		
			}
			else if(role.getIsSync()) {
				if(role.getChoice() == PreferenceChoice.HOMEWORK)
					this.efficiency = 0.9;
				else if(role.getChoice() == PreferenceChoice.WORKSAME)
					this.efficiency = 0.0;
				else if(role.getChoice() == PreferenceChoice.WORKLATER)
					if(this.endHour > this.prefEnd )
						this.efficiency = ((this.prefEnd - this.endHour) + (this.endHour - 17)) * 0.2;
					else
						this.efficiency = (this.endHour - 17) * 0.2;
				else if(role.getChoice() == PreferenceChoice.WORKSOONER) {
					this.efficiency = (this.startHour - 8) * 0.2;
				}
			}
			break;
		case WORKSAME:
			if(!department.getIsSync() && !role.getIsSync())
				this.efficiency = 0.0;
			else if(department.getIsSync()) {
				if(department.getChoice() == PreferenceChoice.HOMEWORK)
					this.efficiency = 0.9;
				else if(department.getChoice() == PreferenceChoice.WORKSAME)
					this.efficiency = 0.0;
				else if(department.getChoice() == PreferenceChoice.WORKLATER)
					this.efficiency = (17 - this.endHour) * 0.2;
				else if(department.getChoice() == PreferenceChoice.WORKSOONER) {
					this.efficiency = (this.startHour - 8) * 0.2;
				}	
			}
			else if(role.getIsSync()) {
				if(role.getChoice() == PreferenceChoice.HOMEWORK)
					this.efficiency = 0.9;
				else if(role.getChoice() == PreferenceChoice.WORKSAME)
					this.efficiency = 0.0;
				else if(role.getChoice() == PreferenceChoice.WORKLATER)
					this.efficiency = (17 - this.endHour) * 0.2;
				else if(role.getChoice() == PreferenceChoice.WORKSOONER) {
					this.efficiency = (this.startHour - 8) * 0.2;
				}		
			}
			break;
		case WORKSOONER:
			if(!department.getIsSync() && !role.getIsSync())
				this.efficiency	= (8 - this.startHour) * 0.2;
			else if(department.getIsSync()) {
				if(department.getChoice() == PreferenceChoice.HOMEWORK)
					this.efficiency = 0.9;
				else if(department.getChoice() == PreferenceChoice.WORKSAME)
					this.efficiency = 0;
				else if(department.getChoice() == PreferenceChoice.WORKLATER)
					this.efficiency = (17 - this.endHour) * 0.2;
				else if(department.getChoice() == PreferenceChoice.WORKSOONER) {
					if(this.prefStart > this.startHour) {
						this.efficiency = ((this.startHour - this.prefStart) + (8 - this.prefStart)) * 0.2;
						System.out.println(this.efficiency + "," + this.startHour + "" + this.prefStart);
					}
					else 
						this.efficiency = (8 - this.startHour) * 0.2;
				}		
			}
			else if(role.getIsSync()) {
				if(role.getChoice() == PreferenceChoice.HOMEWORK)
					this.efficiency = 0.9;
				else if(role.getChoice() == PreferenceChoice.WORKSAME)
					this.efficiency = 0.0;
				else if(role.getChoice() == PreferenceChoice.WORKLATER)
					this.efficiency = (17 - this.endHour) * 0.2;
				else if(role.getChoice() == PreferenceChoice.WORKSOONER) {
					if(this.prefStart > this.startHour)
						this.efficiency = ((this.startHour - this.prefStart) + (8 - this.prefStart)) * 0.2;
					else 
						this.efficiency = (8 - this.startHour) * 0.2;
				}		
			}

			break;
		}
		return this.efficiency;    	
	}

	//setting employee sync.
	@Override
	public void syncChange() {
		if(department.getIsSync() || role.getIsChange())
			this.isSync = true;
		else 
			this.isSync = false;
	}
	
	//changing employee hours
	@Override
	public void changeHours() {
		if(this.canChangePref()) {
			this.startHour = prefStart;
			this.endHour = prefEnd;
		}
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public PreferenceChoice getChoice() {
		return this.choice;
	}

	//4 methods for changing employee pref and hours according to the start hour we got from Gui.
	@Override
	public void startSooner(int startHour) throws Exception  {
		this.choice = PreferenceChoice.valueOf("WORKSOONER");
		if(startHour < 0 || startHour > 8) {
			throw new Exception("Start hour can't be Later than 8AM, you asked to work sooner,Please enter start hour again");
			//startHour = (getting the start hour again(through GUI)
		}
		this.prefStart = startHour;
		this.prefEnd = startHour + 9;
		if(this.prefEnd > 24)
			this.prefEnd =-24;
		if((!(department.getIsSync()) && !(role.getIsSync())))
			changeHours();
	}

	@Override
	public void startSame() {
		this.choice = PreferenceChoice.valueOf("WORKSAME");
		this.prefStart = 8;
		this.prefEnd = 17;
		if((!(department.getIsSync()) && !(role.getIsSync())))
			changeHours();
	}

	@Override
	public void startLater(int startHour) throws Exception {
		this.choice = PreferenceChoice.valueOf("WORKLATER");
		if(startHour > 15 || startHour < 8 ) {
			if(startHour > 15)
				throw new Exception("Start hour can't be higher than 3PM (work hours excced the day),Please enter start hour again");
			else
				throw new Exception("Start hour can't be lower than 8AM,you asked to start later,please enter start hour again");
		}
		this.prefStart = startHour;
		this.prefEnd = startHour + 9;
		if(this.prefEnd > 24)
			this.prefEnd =-24;
		if((!(department.getIsSync()) && !(role.getIsSync())) )
			changeHours();
	}

	@Override
	public void Homework(int startHour) throws Exception {
		this.choice = PreferenceChoice.valueOf("HOMEWORK");
		if(startHour > 15 || startHour < 0) {
			throw new Exception("Start hour can't be higher than 3PM (work hours excced the day),Please enter start hour again");
		}
		this.prefStart = startHour;
		this.prefEnd = startHour + 9;
		if((!(department.getIsSync()) && !(role.getIsSync())))
			changeHours();


	}

	public boolean equals(Object e) {
		if (this.name.equalsIgnoreCase(((Employee)e).name)) {
			return true;
		} else
			return false;
	}

	//returning if employee can change to his pref hours
	@Override
	public boolean canChangePref() {
		return (this.department.getIsChange() && this.role.getIsChange());
	}

	public String toString() {
		return "Employee " + this.name + ": would like to start working" + getPrefString() + " from " + this.prefStart + " , actuall start:" + this.startHour + "\n\t\t" + " and they are making ";
	}

	//method for changing the pref choice to string for the toString method.
	public String getPrefString() {
		if(this.choice == PreferenceChoice.HOMEWORK)
			return " at home";
		else if(this.choice == PreferenceChoice.WORKLATER)
			return " later";
		else if(this.choice == PreferenceChoice.WORKSOONER)
			return " sooner";
		else
			return " like before";

	}

	//converting eff to string
	public String efficiencyToString() {
		df2.setRoundingMode(RoundingMode.DOWN);
		this.efficiency = 0;
		return (this.name + " efficiency is: " + df2.format(this.efficiency())); 
	}

	public String getName() {
		return this.name;
	}



}
