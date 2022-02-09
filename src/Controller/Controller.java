package Controller;



import java.io.FileNotFoundException;
import java.io.IOException;
import Listeners.GuiListener;
import Listeners.ModelListener;
import Model.Company;
import View.AbstractSimView;

public class Controller implements GuiListener, ModelListener {
	private Company company;
	private AbstractSimView view;

	public Controller(Company company, AbstractSimView view) {
		this.company = company;
		this.view = view;

		view.registerListener(this);
		company.registerListener(this);
	}

	@Override
	public void setCompanyNameFromGui(String name) {
		company.setName(name);
	}

	@Override
	public void addDepartmentFromGui(String name) {
		try {
			company.addDepartment(name);
		} catch (Exception e) {
			view.msg(e.getMessage());
		}
	}

	@Override
	public void fireDepartmentAdded(String name) {
		view.departmentAddedToGui(name);
	}

	@Override
	public void syncDepartment(int depIndex) {
		company.syncDepartment(depIndex);

	}

	@Override
	public void fireDepSync(String depName, boolean isSync) {
		view.confirmeDepSync(depName,isSync);

	}

	public void LetChangePref(int depIndex) {
		company.LetDepChangePref(depIndex);
	}

	@Override
	public void changeDepHours(String pref,int depIndex,String hourChoice) throws NumberFormatException, Exception {
		company.changeDepHours(pref,depIndex,hourChoice);

	}

	@Override
	public void addRoleFromGui(String name,int depIndex) {
		try {
			company.addRole(name,depIndex);
		} catch (Exception e) {
			view.msg(e.getMessage());
		}

	}

	public void fireRoleAdded(String name,int depIndex) {
		view.roleAddedToGui(name,depIndex);
	}

	@Override
	public void changeRoleHours(String pref, int roleIndex, int depIndex, String hourChoice) throws NumberFormatException, Exception {
		company.changeRoleHours(pref,roleIndex,depIndex,hourChoice);

	}

	@Override
	public void syncRole(int roleIndex, int depIndex) {
		company.syncRole(roleIndex,depIndex);

	}

	@Override
	public void letRoleChangePref(int roleIndex, int depIndex) {
		company.letRoleChangePref(roleIndex,depIndex);

	}

	@Override
	public void fireRoleSync(String name, boolean depIsSync,boolean isSync) {
		view.confirmRoleSync(name,depIsSync,isSync);

	}

	@Override
	public void addEmployeeFromGui(String name, int depIndex, int roleIndex, String workerType,int income,int bonus) {
		try {
			company.addEmployee(name,depIndex,roleIndex,workerType,income,bonus);
		}
		catch (Exception e) {
			view.msg(e.getMessage());
		}
	}

	@Override
	public void fireEmployeeAdded(String name, int depIndex, int roleIndex) {
		view.employeeAddedToGui(name, depIndex, roleIndex);

	}

	@Override
	public void fireToString(String toString) {
		view.toStringToGui(toString);
	}

	@Override
	public void getToString() {
		company.getToString();
	}

	@Override
	public void changeEmployeeHours(String pref, int roleIndex, int depIndex,int employeeIndex, String hourChoice) throws NumberFormatException, Exception {
		company.changeEmployeeHours(pref,roleIndex,depIndex,employeeIndex,hourChoice);
		
	}

	@Override
	public String roleEff(int depIndex, int roleIndex) {
		return company.roleEff(depIndex,roleIndex);
		
	}

	@Override
	public String depEff(int depIndex) {
		return company.depEff(depIndex);
	}

	@Override
	public String employeeEff(int depIndex, int roleIndex, int employeeIndex) {
		return company.empEff(depIndex,roleIndex,employeeIndex);
	}

	@Override
	public String companyEff() {
		return company.efficiencyToString();
	}

	@Override
	public void saveLoadToGui(String msg) {
		view.msg(msg);
		view.setCompanyField(company.getName());
	}

	@Override
	public void saveData() throws FileNotFoundException, IOException {
		company.saveData();
		
	}

	@Override
	public void loadData() {
		try {
			company.loadData();
		} catch (Exception e) {
			view.msg(e.getMessage());
		}
		
	}

	@Override
	public boolean getDepSync(int depIndex) {
		return company.getDepSync(depIndex);
	}

	@Override
	public boolean getDepChange(int depIndex) {
		return company.getDepChange(depIndex);
	}

	@Override
	public boolean getRoleSync(int depIndex, int roleIndex) {
		return company.getRoleSync(depIndex,roleIndex);
	}

	@Override
	public boolean getRoleChange(int depIndex, int roleIndex) {
		return company.getRoleChange(depIndex,roleIndex);
	}
		

}
