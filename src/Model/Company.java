package Model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import Listeners.ModelListener;


public class Company implements Serializable {
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	private String name;
	private ArrayList<Department> departments;
	private double eff;

	private ArrayList<ModelListener> listeners;


	public Company(String name) {
		this.listeners = new ArrayList<ModelListener>();
		this.name = name;
		this.departments = new ArrayList<Department>();
	}

	//adding listeners
	public void registerListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addDepartment(String name) throws Exception {
		Department d = new Department(name.toLowerCase());
		if (!checkIfExist(departments,d)) {
			departments.add(d);
			for (ModelListener l : listeners) {
				l.fireDepartmentAdded(name);
				l.fireToString(toString());
			}
		} 
		else
			throw new Exception("Department already exist");
	}


	//changing dep sync pref and changing employees hours accordingly 
	public void syncDepartment(int depIndex) {
		departments.get(depIndex).syncChange();
		for (ModelListener l : listeners) {
			l.fireDepSync(departments.get(depIndex).getName(),departments.get(depIndex).getIsSync());
			l.fireToString(toString());
		}
	}

	//calling department method for ableing or not ableing employees to change pref
	public void LetDepChangePref(int depIndex) {
		departments.get(depIndex).canChangePref();
	}

	//changing dep Pref hours depends on the pref they chose in gui
	public void changeDepHours(String pref, int depIndex, String hourChoice) throws NumberFormatException, Exception {
		if(pref == "soon") {
			departments.get(depIndex).startSooner(Integer.parseInt(hourChoice));
			departments.get(depIndex).changeHours();
		}
		else if(pref == "late") {
			departments.get(depIndex).startLater(Integer.parseInt(hourChoice));
			departments.get(depIndex).changeHours();
		}
		else if(pref == "same") {
			departments.get(depIndex).startSame();
			departments.get(depIndex).changeHours();
		}
		else {
			departments.get(depIndex).Homework(Integer.parseInt(hourChoice));
			departments.get(depIndex).changeHours();
		}
	}

	public void addRole(String roleName,int depIndex) throws Exception {
		Role r = new Role(roleName.toLowerCase());
		if (!checkIfExist(departments.get(depIndex).getRoles(),r)) {
			Role rExist = new Role(r,departments.get(depIndex));
			departments.get(depIndex).addRole(rExist);
			for (ModelListener l : listeners) {
				l.fireRoleAdded(roleName,depIndex);
				l.fireToString(toString());
			}
		} 
		else
			throw new Exception("Role already exist");

	}

	// General method for checking if a object is in the array
	public boolean checkIfExist(ArrayList<?> list, Object object) {
		boolean exist = false;
		for (Object l : list) {
			if (l.equals(object)) {
				return true;
			}
		}
		return exist;
	}

	//changing role Pref hours depends on the pref they chose in gui
	public void changeRoleHours(String pref, int roleIndex, int depIndex, String hourChoice) throws NumberFormatException, Exception {
		if(pref == "soon") {
			departments.get(depIndex).getRoles().get(roleIndex).startSooner(Integer.parseInt(hourChoice));
			departments.get(depIndex).getRoles().get(roleIndex).changeHours();
		}
		else if(pref == "late") {
			departments.get(depIndex).getRoles().get(roleIndex).startLater(Integer.parseInt(hourChoice));
			departments.get(depIndex).getRoles().get(roleIndex).changeHours();
		}
		else if(pref == "same") {
			departments.get(depIndex).getRoles().get(roleIndex).startSame();
			departments.get(depIndex).getRoles().get(roleIndex).changeHours();
		}
		else {
			departments.get(depIndex).getRoles().get(roleIndex).Homework(Integer.parseInt(hourChoice));
			departments.get(depIndex).getRoles().get(roleIndex).changeHours();
		}
	}

	//changing role sync pref and changing employees hours accordingly 
	public void syncRole(int roleIndex, int depIndex) {
		departments.get(depIndex).getRoles().get(roleIndex).syncChange();
		for (ModelListener l : listeners) {
			l.fireRoleSync(departments.get(depIndex).getRoles().get(roleIndex).getRoleName(),departments.get(depIndex).getIsSync(),departments.get(depIndex).getRoles().get(roleIndex).getIsSync());
			l.fireToString(toString());
		}

	}

	public void addEmployee(String name, int depIndex, int roleIndex, String workerType, int income, int bonus) throws Exception {
		Employee worker;
		if(workerType == "hourly") {
			worker = new WorkerPerHour(name.toLowerCase(),departments.get(depIndex).getRoles().get(roleIndex), departments.get(depIndex), income);
		}
		else if(workerType == "salary") {
			worker = new WorkerSalary(name.toLowerCase(),departments.get(depIndex).getRoles().get(roleIndex), departments.get(depIndex), income);
		}
		else {
			worker = new WorkerWithBonus(name.toLowerCase(),departments.get(depIndex).getRoles().get(roleIndex), departments.get(depIndex), income, bonus);
		}
		if (!checkIfExist(departments.get(depIndex).getRoles().get(roleIndex).getAllEmployees(),worker)) {
			departments.get(depIndex).getRoles().get(roleIndex).addEmployee(worker);
			for (ModelListener l : listeners) {
				l.fireEmployeeAdded(name,depIndex,roleIndex);
				l.fireToString(toString());
			}
		} 
		else
			throw new Exception("Employee already exist");

	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("The company " + this.name + " have " + this.departments.size() + " departments and the departments are:\n");
		for (Department department : departments) {
			sb.append((departments.indexOf(department)+1) + ") ");
			sb.append(department.toString());
		}
		return sb.toString();
	}

	//fire toString to the view
	public void getToString() {
		for (ModelListener l : listeners) {
			l.fireToString(toString());
		}
	}

	//changing employee hours according to his pref and hour chosen 
	public void changeEmployeeHours(String pref, int roleIndex, int depIndex, int employeeIndex, String hourChoice) throws NumberFormatException, Exception {
		if(pref == "soon")
			departments.get(depIndex).getRoles().get(roleIndex).getAllEmployees().get(employeeIndex).startSooner(Integer.parseInt(hourChoice));
		else if(pref == "late")
			departments.get(depIndex).getRoles().get(roleIndex).getAllEmployees().get(employeeIndex).startLater(Integer.parseInt(hourChoice));
		else if(pref == "same")
			departments.get(depIndex).getRoles().get(roleIndex).getAllEmployees().get(employeeIndex).startSame();
		else
			departments.get(depIndex).getRoles().get(roleIndex).getAllEmployees().get(employeeIndex).Homework(Integer.parseInt(hourChoice));


	}

	//Role ableing or not ableing employees to change pref (dep have priority)
	public void letRoleChangePref(int roleIndex, int depIndex) {
		departments.get(depIndex).getRoles().get(roleIndex).canChangePref();
	} 

	//calculate company eff and converting to String.
	public String efficiencyToString() {
		this.eff = 0;
		df2.setRoundingMode(RoundingMode.DOWN);
		StringBuffer sb = new StringBuffer("the company: " + this.name + " efficiency is: "); 
		for (Department departments : departments) {
			this.eff = this.eff + departments.efficiency();
		}
		sb.append(df2.format(this.eff));
		return sb.toString();
	}

	// calling for specific role calculating efficiency method
	public String roleEff(int depIndex,int roleIndex) {
		return departments.get(depIndex).getRoles().get(roleIndex).efficiencyToString();
	}
	// calling for specific dep calculating efficiency method
	public String depEff(int depIndex) {
		return departments.get(depIndex).efficiencyToString();
	}
	// calling for specific employee calculating efficiency method
	public String empEff(int depIndex, int roleIndex, int employeeIndex) {
		return departments.get(depIndex).getRoles().get(roleIndex).getAllEmployees().get(employeeIndex).efficiencyToString();
	}

	public void saveData() throws FileNotFoundException, IOException {
		if(this.name != "") {
			ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream("Company.dat"));
			outFile.writeObject(name);
			outFile.close();
			outFile = new ObjectOutputStream(new FileOutputStream("Departments.dat"));
			outFile.writeObject(departments);
			outFile.close();
			fireSaveLoadFile("Simulation saved");
		}
	}

	private void fireSaveLoadFile(String msg) {
		for (ModelListener l : listeners) {
			l.saveLoadToGui(msg + "\n");
		}
	}


	@SuppressWarnings("unchecked")
	public void loadData() throws Exception {
		try {
			ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("Company.dat"));
			name = (String)inFile.readObject();
			inFile = new ObjectInputStream(new FileInputStream("Departments.dat"));
			departments = (ArrayList<Department>)inFile.readObject();
			fireSaveLoadFile("Simulation loaded");
			//Send data to view
			setName(name);
			for (ModelListener l : listeners) {
				l.fireToString(toString());
				for (int i = 0; i < departments.size(); i++) {
					l.fireDepartmentAdded(departments.get(i).getName());
					for (int j = 0; j < departments.get(i).getRoles().size(); j++) {
						l.fireRoleAdded(departments.get(i).getRoles().get(j).getRoleName(), i);
						for (int k = 0; k < departments.get(i).getRoles().get(j).getAllEmployees().size(); k++) {
							l.fireEmployeeAdded(departments.get(i).getRoles().get(j).getAllEmployees().get(k).getName(), i, j);
						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			throw new Exception("Can't find all data");
		}
		catch (ClassNotFoundException e) {
			throw new Exception("Can't find all data");
		}

	}


	public String getName() {
		return this.name;
	}

	public boolean getDepSync(int depIndex) {
		return departments.get(depIndex).getIsSync();
	}

	public boolean getDepChange(int depIndex) {
		return departments.get(depIndex).getIsChange();
	}

	public boolean getRoleSync(int depIndex, int roleIndex) {
		return departments.get(depIndex).getRoles().get(roleIndex).getIsSync();
	}

	public boolean getRoleChange(int depIndex, int roleIndex) {
		return departments.get(depIndex).getRoles().get(roleIndex).getIsChange();
	}


}
