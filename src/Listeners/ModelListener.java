package Listeners;

public interface ModelListener {
	void fireDepartmentAdded(String name);
	void fireDepSync(String depName, boolean isSync);
	void fireRoleAdded(String name,int depIndex);
	void fireRoleSync(String name, boolean depIsSync, boolean isSync);
	void fireEmployeeAdded(String name, int depIndex, int roleIndex);
	void fireToString(String toString);
	void saveLoadToGui(String msg);
}
