package View;

import Listeners.GuiListener;

public interface AbstractSimView {
	void registerListener(GuiListener listener);
	void departmentAddedToGui(String name);
	public void msg(String msg);
	void confirmeDepSync(String depName, boolean isSync);
	void roleAddedToGui(String name, int depIndex);
	void confirmRoleSync(String name, boolean isSync, boolean depIsSync);
	void employeeAddedToGui(String name, int depIndex, int roleIndex);
	void toStringToGui(String toString);
	void setCompanyField(String name);
}
