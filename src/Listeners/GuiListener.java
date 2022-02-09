package Listeners;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface GuiListener {
	void setCompanyNameFromGui(String name);
	void addDepartmentFromGui(String name);
	void syncDepartment(int depIndex);
	void LetChangePref(int depIndex);
	void changeDepHours(String pref, int depIndex, String hourChoice) throws NumberFormatException, Exception;
	void addRoleFromGui(String name, int depIndex);
	void changeRoleHours(String pref, int roleIndex, int depIndex, String hourChoice) throws NumberFormatException, Exception;
	void syncRole(int roleIndex, int depIndex);
	void letRoleChangePref(int roleIndex, int depIndex);
	void addEmployeeFromGui(String text, int depIndex, int roleIndex, String workerType, int intIncome, int bonus);
	void getToString();
	void changeEmployeeHours(String pref, int roleIndex, int depIndex, int employeeIndex, String hourChoice) throws NumberFormatException, Exception;
	String roleEff(int depIndex, int roleIndex);
	String depEff(int depIndex);
	String employeeEff(int depIndex, int roleIndex, int employeeIndex);
	String companyEff();
	void saveData() throws FileNotFoundException, IOException;
	void loadData();
	boolean getDepSync(int depIndex);
	boolean getDepChange(int depIndex);
	boolean getRoleSync(int depIndex, int roleIndex);
	boolean getRoleChange(int depIndex, int roleIndex);
}
