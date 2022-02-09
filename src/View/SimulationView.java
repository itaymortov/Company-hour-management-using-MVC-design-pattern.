package View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.JOptionPane;
import Listeners.GuiListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class SimulationView implements AbstractSimView {
	private ArrayList<GuiListener> allListeners = new ArrayList<GuiListener>();
	private ArrayList<ComboBox<String>> rolesByDep = new ArrayList<ComboBox<String>>();
	private ArrayList<ArrayList<ComboBox<String>>> employeesByrole = new ArrayList<ArrayList<ComboBox<String>>>();

	int intIncome;
	int bonus;

	//efficiency buttons
	Button calcEmployeeEfficiency = new Button("Calculate single employee efficiency");
	Button calcRoleEfficiency = new Button("calculate single role efficiency");
	Button calcDepartmentEfficiency = new Button("calculate single department efficiency");
	Button calcCompanyEfficiency = new Button("calculate entire company efficiency");

	//combo boxes:
	private ComboBox<String> depList;
	private ComboBox<String> roleList;
	private ComboBox<String> employeeList;

	private Label lblString = new Label("");
	private Label lblEff = new Label("");

	// to show "eff".
	ScrollPane scrollEff = new ScrollPane(lblEff);
	GridPane companyPane;
	GridPane depPane;
	GridPane rolePane;
	GridPane employeePane;

	//company pane
	Label lblCompanyName = new Label("Enter company name:");
	TextField fieldCompanyName = new TextField("");
	Button btnCompanyName = new Button("Set company name");

	public String dialogWithText(String header,String title) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		//	dialog.setContentText("Please enter your hours:");
		Optional<String> result = dialog.showAndWait();
		if(result.toString() == Optional.empty().toString())
			return "";
		return result.get();
	}

	public SimulationView(Stage stage) {
		depList = new ComboBox<String>();
		roleList = new ComboBox<String>();
		employeeList = new ComboBox<String>();
		GridPane gridPane = new GridPane();


		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		companyPane = getCompanyPane();
		depPane = getDepartmentPane();
		rolePane = getRolePane();
		employeePane = getEmployeePane();

		depPane.setDisable(true);
		rolePane.setDisable(true);
		employeePane.setDisable(true);

		calcEmployeeEfficiency.setDisable(true);
		calcRoleEfficiency.setDisable(true);
		calcDepartmentEfficiency.setDisable(true);
		calcCompanyEfficiency.setDisable(true);

		scrollEff.setPrefHeight(300);
		scrollEff.setMaxWidth(9000);

		gridPane.add(companyPane, 0, 0);
		gridPane.add(depPane, 1, 0);
		gridPane.add(rolePane, 2, 0);
		gridPane.add(employeePane, 3, 0);
		gridPane.add(calcEmployeeEfficiency,4,1);
		gridPane.add(calcRoleEfficiency,4,2);
		gridPane.add(calcDepartmentEfficiency,4,3);
		gridPane.add(calcCompanyEfficiency,4,4);
		gridPane.add(scrollEff, 4,0);


		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent action) {
				for (GuiListener l : allListeners) {
					try {
						l.saveData();
					} catch (FileNotFoundException e) {
						msg(e.getMessage());
					} catch (IOException e) {
						msg(e.getMessage());
					}
				}		
			}
		});
		stage.setScene(new Scene(gridPane, 1700, 600));
		stage.show();


	}

	public GridPane getCompanyPane() {
		GridPane companyPane = new GridPane();
		companyPane.setPadding(new Insets(10));
		companyPane.setHgap(10);
		companyPane.setVgap(10);

		//text field to show "toStrings".
		ScrollPane scrollString = new ScrollPane(lblString);
		scrollString.setPrefHeight(300);
		scrollString.setMaxWidth(9000);

		companyPane.add(lblCompanyName, 0, 0);
		companyPane.add(fieldCompanyName, 0, 1);
		companyPane.add(btnCompanyName, 0, 2);
		companyPane.add(scrollString, 0, 3);


		btnCompanyName.setOnAction(new EventHandler<ActionEvent>() { // Event for set name of company
			@Override
			public void handle(ActionEvent action) {
				if (!fieldCompanyName.getText().isEmpty()) {
					fieldCompanyName.setDisable(true);
					lblCompanyName.setDisable(true);
					btnCompanyName.setDisable(true);
					for (GuiListener l : allListeners) {
						l.setCompanyNameFromGui(fieldCompanyName.getText());
					}
					depPane.setDisable(false);
				} else
					msg("Company must have a name");

			}
		}); // Event end


		calcCompanyEfficiency.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent action) {
				for (GuiListener l : allListeners) {
					effToGui(l.companyEff());	
				}
			}
		});

		return companyPane;

	}

	public GridPane getRolePane() {
		GridPane rolePane = new GridPane();
		rolePane.setPadding(new Insets(10));
		rolePane.setHgap(10);
		rolePane.setVgap(10);

		RadioButton rolePrefSoon = new RadioButton("Start earlier");
		rolePrefSoon.setUserData("soon");
		RadioButton rolePrefLater = new RadioButton("Start later");
		rolePrefLater.setUserData("late");
		RadioButton rolePrefHome = new RadioButton("Work from home");
		rolePrefHome.setUserData("home");
		RadioButton rolePrefSame = new RadioButton("Work as usual");
		rolePrefSame.setUserData("same");

		ToggleGroup rolePrefGroup = new ToggleGroup();
		rolePrefSoon.setToggleGroup(rolePrefGroup);
		rolePrefLater.setToggleGroup(rolePrefGroup);
		rolePrefHome.setToggleGroup(rolePrefGroup);
		rolePrefSame.setToggleGroup(rolePrefGroup);

		rolePrefSoon.setDisable(true);
		rolePrefLater.setDisable(true);
		rolePrefHome.setDisable(true);
		rolePrefSame.setDisable(true);


		Label lblRoleName = new Label("Enter new role:");
		TextField fieldRoleName = new TextField("");
		Button btnRoleCreate = new Button("Create role");
		CheckBox syncCheckRole = new CheckBox("Syncronize entire role");	
		CheckBox canChangeRole = new CheckBox("Let employees change preferences");

		syncCheckRole.setDisable(true);
		canChangeRole.setDisable(true);

		rolePane.add(lblRoleName, 0, 0);
		rolePane.add(fieldRoleName, 0, 1);
		rolePane.add(btnRoleCreate, 0, 2);
		rolePane.add(roleList, 0, 3);
		rolePane.add(rolePrefSoon, 0, 4);
		rolePane.add(rolePrefLater, 0, 5);
		rolePane.add(rolePrefHome, 0, 6);
		rolePane.add(rolePrefSame, 0, 7);
		rolePane.add(syncCheckRole, 0, 8);
		rolePane.add(canChangeRole, 0, 9);

		btnRoleCreate.setOnAction(new EventHandler<ActionEvent>() { //Event for set a department
			@Override
			public void handle(ActionEvent action) {
				int depIndex = depList.getSelectionModel().getSelectedIndex();
				for (GuiListener l : allListeners) {
					if (!fieldRoleName.getText().isEmpty() && depIndex != -1) {
						l.addRoleFromGui(fieldRoleName.getText(), depIndex);
						roleList.getItems().setAll(rolesByDep.get(depIndex).getItems());
						employeeList.getSelectionModel().clearSelection();
						fieldRoleName.clear();
					} else
						msg("Role must have a name and a department must be picked");	
				}
			}
		}); //Event end

		roleList.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				boolean roleSync = false;
				boolean roleChange = false;
				employeePane.setDisable(false);
				int depIndex = depList.getSelectionModel().getSelectedIndex();
				int roleIndex = roleList.getSelectionModel().getSelectedIndex();
				if(roleIndex != -1) {
					employeeList.setItems(employeesByrole.get(depIndex).get(roleIndex).getItems());
				}
				if(roleIndex != -1 && depIndex != -1) {
					for (GuiListener l : allListeners) {
						roleSync = l.getRoleSync(depIndex,roleIndex);
						roleChange = l.getRoleChange(depIndex,roleIndex);
					}
				}
				rolePrefSoon.setDisable(false);
				rolePrefLater.setDisable(false);
				rolePrefHome.setDisable(false);
				rolePrefSame.setDisable(false);
				syncCheckRole.setDisable(false);
				canChangeRole.setDisable(false);
				syncCheckRole.setSelected(roleSync);
				canChangeRole.setSelected(roleChange);

			}
		});

		rolePrefSoon.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						if(roleList.getSelectionModel().getSelectedIndex() != -1) {
							hourChoice = dialogWithText("Choose hours between 0-7AM","You choose to start earlier");
							try {
								l.changeRoleHours(rolePrefSoon.getUserData().toString(),roleList.getSelectionModel().getSelectedIndex(),depList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							} catch (NumberFormatException e) {
								msg("You must enter an integer");
							} catch (Exception e) {
								msg(e.getMessage());
							}
						}
						else {
							succ = true;
							rolePrefSoon.setSelected(false);
						}
					}
					succ = false;
				}
			}

		});

		rolePrefLater.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						if(roleList.getSelectionModel().getSelectedIndex() != -1) {
							hourChoice = dialogWithText("Choose hours between 9-15","You choose to start later");
							try {
								l.changeRoleHours(rolePrefLater.getUserData().toString(),roleList.getSelectionModel().getSelectedIndex(),depList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							} catch (NumberFormatException e) {
								msg("You must enter an integer");
							} catch (Exception e) {
								msg(e.getMessage());
							}
						}
						else {
							succ = true;
							rolePrefLater.setSelected(false);
						}

					}
					succ = false;
				}
			}

		});

		rolePrefSame.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						if(roleList.getSelectionModel().getSelectedIndex() != -1) {
							msg("You chose to work the same");
							try {

								l.changeRoleHours(rolePrefSame.getUserData().toString(),roleList.getSelectionModel().getSelectedIndex(),depList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							} catch (NumberFormatException e) {
								msg("You must enter an integer");
							} catch (Exception e) {
								msg(e.getMessage());
							}
						}
						else {
							succ = true;
							rolePrefSame.setSelected(false);
						}
					}
					succ = false;
				}
			}

		});

		rolePrefHome.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						if(roleList.getSelectionModel().getSelectedIndex() != -1) {
							hourChoice = dialogWithText("Choose hours to start working","You choose to work from home");
							try {
								l.changeRoleHours(rolePrefHome.getUserData().toString(),roleList.getSelectionModel().getSelectedIndex(),depList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							} catch (NumberFormatException e) {
								msg("You must enter an integer");
							} catch (Exception e) {
								msg(e.getMessage());
							}
						}
						else {
							succ = true;
							rolePrefHome.setSelected(false);
						}
					}
					succ = false;
				}
			}
		});

		syncCheckRole.setOnAction(new EventHandler<ActionEvent>() { //Event for synchronizing the department
			@Override
			public void handle(ActionEvent action) {
				int roleIndex = roleList.getSelectionModel().getSelectedIndex(); 
				if(!depList.getSelectionModel().isSelected(-1)) {
					for (GuiListener l : allListeners) {
						if(roleIndex != -1)
							l.syncRole(roleList.getSelectionModel().getSelectedIndex(),depList.getSelectionModel().getSelectedIndex());
						else
							syncCheckRole.setSelected(false);
					}
				}
			}
		}); //Event end

		canChangeRole.setOnAction(new EventHandler<ActionEvent>() { //Event for synchronizing the department
			@Override
			public void handle(ActionEvent action) {
				int roleIndex = roleList.getSelectionModel().getSelectedIndex(); 
				if(!depList.getSelectionModel().isSelected(-1)) {
					for (GuiListener l : allListeners) {
						if(roleIndex != -1) {
							l.letRoleChangePref(roleList.getSelectionModel().getSelectedIndex(),depList.getSelectionModel().getSelectedIndex());
							l.getToString();
						}
						else
							canChangeRole.setSelected(false);
					}
				}
			}
		}); //Event end

		calcRoleEfficiency.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent action) {
				int depIndex = depList.getSelectionModel().getSelectedIndex();
				int roleIndex = roleList.getSelectionModel().getSelectedIndex();
				for (GuiListener l : allListeners) {
					if (depIndex != -1 && roleIndex != -1) {
						effToGui(l.roleEff(depIndex, roleIndex));	
					}
					else
						msg("pick a role");
				}
			}
		});

		return rolePane;
	}

	public GridPane getDepartmentPane() {
		GridPane departmentPane = new GridPane();
		departmentPane.setPadding(new Insets(10));
		departmentPane.setHgap(10);
		departmentPane.setVgap(10);

		//Toggle group for pref
		RadioButton prefSoon = new RadioButton("Start earlier");
		prefSoon.setUserData("soon");
		RadioButton prefLater = new RadioButton("Start later");
		prefLater.setUserData("late");
		RadioButton prefHome = new RadioButton("Work from home");
		prefHome.setUserData("home");
		RadioButton prefSame = new RadioButton("Work as usual");
		prefSame.setUserData("same");

		final ToggleGroup prefGroup = new ToggleGroup();
		prefSoon.setToggleGroup(prefGroup);
		prefLater.setToggleGroup(prefGroup);
		prefHome.setToggleGroup(prefGroup);
		prefSame.setToggleGroup(prefGroup);

		prefSoon.setDisable(true);
		prefLater.setDisable(true);
		prefHome.setDisable(true);
		prefSame.setDisable(true);



		Label lblDepartmentName = new Label("Enter new department:");
		TextField fieldDepartmentName = new TextField("");
		Button btnDepartmentCreate = new Button("Create department");
		CheckBox syncCheck = new CheckBox("Syncronize entire deparment");	
		CheckBox canChange = new CheckBox("Let employees change preferences");	

		syncCheck.setDisable(true);
		canChange.setDisable(true);

		departmentPane.add(lblDepartmentName, 0, 0);
		departmentPane.add(fieldDepartmentName, 0, 1);
		departmentPane.add(btnDepartmentCreate, 0, 2);
		departmentPane.add(depList, 0, 3);
		departmentPane.add(prefSoon, 0, 4);
		departmentPane.add(prefLater, 0, 5);
		departmentPane.add(prefHome, 0, 6);
		departmentPane.add(prefSame, 0, 7);
		departmentPane.add(syncCheck, 0, 8);
		departmentPane.add(canChange, 0, 9);


		btnDepartmentCreate.setOnAction(new EventHandler<ActionEvent>() { //Event for set a department
			@Override
			public void handle(ActionEvent action) {
				for (GuiListener l : allListeners) {
					if (!fieldDepartmentName.getText().isEmpty()) {
						l.addDepartmentFromGui(fieldDepartmentName.getText());
						fieldDepartmentName.clear();
					} else
						msg("Department must have a name");	
				}
			}
		}); //Event end

		syncCheck.setOnAction(new EventHandler<ActionEvent>() { //Event for synchronizing the department
			@Override
			public void handle(ActionEvent action) {
				if(!depList.getSelectionModel().isSelected(-1)) {
					for (GuiListener l : allListeners) {
						l.syncDepartment(depList.getSelectionModel().getSelectedIndex());
					}
				}
				else
					syncCheck.setSelected(false);
			}
		}); //Event end

		canChange.setOnAction(new EventHandler<ActionEvent>() { //Event for synchronizing the department
			@Override
			public void handle(ActionEvent action) {
				if(!depList.getSelectionModel().isSelected(-1)) {
					for (GuiListener l : allListeners) {
						l.LetChangePref(depList.getSelectionModel().getSelectedIndex());
						l.getToString();
					}
				}
				else
					canChange.setSelected(false);
			}
		}); //Event end

		prefSoon.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						hourChoice = dialogWithText("Choose hours between 0-7AM","You choose to start earlier");
						try {
							if(depList.getSelectionModel().getSelectedIndex() != -1) {
								l.changeDepHours(prefSoon.getUserData().toString(),depList.getSelectionModel().getSelectedIndex(),hourChoice);
								l.getToString();
								succ = true;
							}
						} catch (NumberFormatException e) {
							msg("You must enter an integer");
						} catch (Exception e) {
							msg(e.getMessage());
						}
					}
					succ = false;
				}
			}

		});

		prefLater.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						hourChoice = dialogWithText("Choose hours between 9-15","You choose to start later");
						try {
							if(depList.getSelectionModel().getSelectedIndex() != -1) {
								l.changeDepHours(prefLater.getUserData().toString(),depList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							}
						} catch (NumberFormatException e) {
							msg("You must enter an integer");
						} catch (Exception e) {
							msg(e.getMessage());
						}
					}
					succ = false;
				}
			}

		});

		prefSame.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						msg("You chose to work the same");
						try {
							if(depList.getSelectionModel().getSelectedIndex() != -1) {
								l.changeDepHours(prefSame.getUserData().toString(),depList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							}
						} catch (NumberFormatException e) {
							msg("You must enter an integer");
						} catch (Exception e) {
							msg(e.getMessage());
						}
					}
					succ = false;
				}
			}

		});

		prefHome.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						hourChoice = dialogWithText("Choose hours to start working","You choose to work from home");
						try {
							if(depList.getSelectionModel().getSelectedIndex() != -1) {
								l.changeDepHours(prefHome.getUserData().toString(),depList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							}
						} catch (NumberFormatException e) {
							msg("You must enter an integer");
						} catch (Exception e) {
							msg(e.getMessage());
						}
					}
					succ = false;
				}
			}

		});

		depList.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				employeeList.getSelectionModel().clearSelection();
				boolean depSync = false;
				boolean depChange = false;
				rolePane.setDisable(false);
				roleList.getSelectionModel().clearSelection();
				employeePane.setDisable(true);
				int depIndex = depList.getSelectionModel().getSelectedIndex();
				if(depIndex != -1) {
					for (GuiListener l : allListeners) {
						depSync = l.getDepSync(depIndex);
						depChange = l.getDepChange(depIndex);
					}
				}
				roleList.getItems().setAll(rolesByDep.get(depIndex).getItems());
				prefSoon.setDisable(false);
				prefLater.setDisable(false);
				prefHome.setDisable(false);
				prefSame.setDisable(false);
				syncCheck.setDisable(false);
				canChange.setDisable(false);
				syncCheck.setSelected(depSync);
				canChange.setSelected(depChange);

			}
		});

		calcDepartmentEfficiency.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent action) {
				int depIndex = depList.getSelectionModel().getSelectedIndex();
				for (GuiListener l : allListeners) {
					if(depIndex != -1 )
						effToGui(l.depEff(depIndex));	
					else
						msg("pick a department");
				}
			}
		});


		return departmentPane;		
	}

	public GridPane getEmployeePane() {
		GridPane employeePane = new GridPane();
		employeePane.setPadding(new Insets(10));
		employeePane.setHgap(10);
		employeePane.setVgap(10);

		//Toggle group for pref
		RadioButton empoloyeePrefSoon = new RadioButton("Start earlier");
		empoloyeePrefSoon.setUserData("soon");
		RadioButton empoloyeePrefLater = new RadioButton("Start later");
		empoloyeePrefLater.setUserData("late");
		RadioButton empoloyeePrefHome = new RadioButton("Work from home");
		empoloyeePrefHome.setUserData("home");
		RadioButton empoloyeePrefSame = new RadioButton("Work as usual");
		empoloyeePrefSame.setUserData("same");

		ToggleGroup prefGroup = new ToggleGroup();
		empoloyeePrefSoon.setToggleGroup(prefGroup);
		empoloyeePrefLater.setToggleGroup(prefGroup);
		empoloyeePrefHome.setToggleGroup(prefGroup);
		empoloyeePrefSame.setToggleGroup(prefGroup);

		empoloyeePrefSoon.setDisable(true);
		empoloyeePrefLater.setDisable(true);
		empoloyeePrefHome.setDisable(true);
		empoloyeePrefSame.setDisable(true);

		//toggle group for type of employee
		RadioButton employeeByHour = new RadioButton("Payed by the hour");
		employeeByHour.setUserData("hourly");
		RadioButton employeeBySalary = new RadioButton("Payed fixed salary");
		employeeBySalary.setUserData("salary");
		RadioButton employeeByBonus = new RadioButton("Payed fixed salary + bonus");
		employeeByBonus.setUserData("bonus");


		ToggleGroup feeGroup = new ToggleGroup();
		employeeByHour.setToggleGroup(feeGroup);
		employeeBySalary.setToggleGroup(feeGroup);
		employeeByBonus.setToggleGroup(feeGroup);


		Label lblEmployeeName = new Label("Enter new employee:");
		TextField fieldEmployeeName = new TextField("");
		Button btnEmployeeCreate = new Button("Create employee");

		employeePane.add(lblEmployeeName, 0, 0);
		employeePane.add(fieldEmployeeName, 0, 1);
		employeePane.add(employeeByHour, 0, 2);
		employeePane.add(employeeBySalary, 0, 3);
		employeePane.add(employeeByBonus, 0, 4);
		employeePane.add(btnEmployeeCreate, 0, 5);
		employeePane.add(employeeList, 0, 6);
		employeePane.add(empoloyeePrefSoon, 0, 7);
		employeePane.add(empoloyeePrefLater, 0, 8);
		employeePane.add(empoloyeePrefHome, 0, 9);
		employeePane.add(empoloyeePrefSame, 0, 10);

		btnEmployeeCreate.setOnAction(new EventHandler<ActionEvent>() { //Event for set a department
			@Override
			public void handle(ActionEvent action) {
				int depIndex = depList.getSelectionModel().getSelectedIndex();
				int roleIndex = roleList.getSelectionModel().getSelectedIndex();
				employeeList.setItems(employeesByrole.get(depIndex).get(roleIndex).getItems());
				if(feeGroup.getSelectedToggle() != null) {
					for (GuiListener l : allListeners) {
						if (!fieldEmployeeName.getText().isEmpty() && depIndex != -1 ) {
							l.addEmployeeFromGui(fieldEmployeeName.getText(), depIndex,roleIndex,feeGroup.getSelectedToggle().getUserData().toString(),intIncome,bonus);
							fieldEmployeeName.clear();
						} else
							msg("Employee must have a name and a department and role must be picked");	
					}
				}
				else
					msg("Please select worker type"); 
				employeeByHour.setSelected(false);
				employeeByBonus.setSelected(false);
				employeeBySalary.setSelected(false);

			}
		}); //Event end

		employeeByHour.setOnAction(new EventHandler<ActionEvent>() {
			String income = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				while(!succ) {
					income = dialogWithText("Enter your hourly income","You chose to work by the hour");
					try {
						intIncome = Integer.parseInt(income);
						if(intIncome <= 0)
							throw new Exception("worker must get paid a positive amount.");
						succ = true;
					}catch (NumberFormatException e) {
						msg("You must enter an integer");
					}catch (Exception e){
						msg(e.toString());
					}
				}
				succ = false;
			}
		});

		employeeBySalary.setOnAction(new EventHandler<ActionEvent>() {
			String income = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				while(!succ) {
					income = dialogWithText("Enter your salary","You chose base salary work");
					try {
						intIncome = Integer.parseInt(income);
						if(intIncome <= 0)
							throw new Exception("worker must get paid a positive amount.");
						succ = true;
					} catch (NumberFormatException e) {
						msg("You must enter an integer");
					}catch (Exception e){
						msg(e.toString());
					}
				}
				succ = false;
			}
		});

		employeeByBonus.setOnAction(new EventHandler<ActionEvent>() {
			String income = "";
			String stringBonus = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				while(!succ) {
					income = dialogWithText("Enter your salary","You chose base salary work + bonus");
					try {
						intIncome = Integer.parseInt(income);
						if(intIncome <= 0)
							throw new Exception("worker must get paid a positive amount.");
					} catch (NumberFormatException e) {
						msg("You must enter an integer");
					}catch (Exception e){
						msg(e.toString());
					}
					stringBonus = dialogWithText("Enter your bonus","You chose base salary work + bonus");
					try {
						bonus = Integer.parseInt(stringBonus);
						if(bonus <= 0)
							throw new Exception("worker bonus must be a positive amount.");
						succ = true;
					}catch(NumberFormatException e) {
						msg("You must enter an integer");
					}catch (Exception e){
						msg(e.toString());
					}
				}
				succ = false;
			}
		});

		employeeList.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//feeGroup.getSelectedToggle().setSelected(false);
				empoloyeePrefSoon.setDisable(false);
				empoloyeePrefLater.setDisable(false);
				empoloyeePrefHome.setDisable(false);
				empoloyeePrefSame.setDisable(false);
				empoloyeePrefSoon.setSelected(false);
				empoloyeePrefLater.setSelected(false);
				empoloyeePrefHome.setSelected(false);
				empoloyeePrefSame.setSelected(false);
				employeeByHour.setSelected(false);
				employeeByBonus.setSelected(false);
				employeeBySalary.setSelected(false);
				calcEmployeeEfficiency.setDisable(false);
				calcEmployeeEfficiency.setDisable(false);
				calcRoleEfficiency.setDisable(false);
				calcDepartmentEfficiency.setDisable(false);
				calcCompanyEfficiency.setDisable(false);

			}
		});

		empoloyeePrefSoon.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						if(employeeList.getSelectionModel().getSelectedIndex() != -1) {
							hourChoice = dialogWithText("Choose hours between 0-7AM","You choose to start earlier");
							try {
								l.changeEmployeeHours(empoloyeePrefSoon.getUserData().toString(),roleList.getSelectionModel().getSelectedIndex(),depList.getSelectionModel().getSelectedIndex(),employeeList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							} catch (NumberFormatException e) {
								msg("You must enter an integer");
							} catch (Exception e) {
								msg(e.getMessage());
							}
						}
						else
							succ = true;

					}
					succ = false;
				}
			}

		});

		empoloyeePrefLater.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						if(employeeList.getSelectionModel().getSelectedIndex() != -1) {
							hourChoice = dialogWithText("Choose hours between 9-15","You choose to start later");
							try {
								l.changeEmployeeHours(empoloyeePrefLater.getUserData().toString(),roleList.getSelectionModel().getSelectedIndex(),depList.getSelectionModel().getSelectedIndex(),employeeList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							}catch (NumberFormatException e) {
								msg("You must enter an integer");
							} catch (Exception e) {
								msg(e.getMessage());
							}
						}
						else
							succ = true;
					}
					succ = false;
				}
			}

		});

		empoloyeePrefHome.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						if(employeeList.getSelectionModel().getSelectedIndex() != -1) {
							hourChoice = dialogWithText("Choose hours between 0-15","You choose to work from home");
							try {

								l.changeEmployeeHours(empoloyeePrefHome.getUserData().toString(),roleList.getSelectionModel().getSelectedIndex(),depList.getSelectionModel().getSelectedIndex(),employeeList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							} catch (NumberFormatException e) {
								msg("You must enter an integer");
							} catch (Exception e) {
								msg(e.getMessage());
							}
						}
						else
							succ = true;
					}

					succ = false;
				}
			}

		});

		empoloyeePrefSame.setOnAction(new EventHandler<ActionEvent>() {
			String hourChoice = "";
			boolean succ = false;
			@Override
			public void handle(ActionEvent arg0) {
				for (GuiListener l : allListeners) {
					while(!succ) {
						if(employeeList.getSelectionModel().getSelectedIndex() != -1) {
							msg("You chose to work the same");
							try {

								l.changeEmployeeHours(empoloyeePrefSame.getUserData().toString(),roleList.getSelectionModel().getSelectedIndex(),depList.getSelectionModel().getSelectedIndex(),employeeList.getSelectionModel().getSelectedIndex(),hourChoice);
								succ = true;
								l.getToString();
							} catch (NumberFormatException e) {
								msg("You must enter an integer");
							} catch (Exception e) {
								msg(e.getMessage());
							}
						}
						else
							succ = true;
					}
					succ = false;
				}
			}

		});

		calcEmployeeEfficiency.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent action) {
				int depIndex = depList.getSelectionModel().getSelectedIndex();
				int roleIndex = roleList.getSelectionModel().getSelectedIndex();
				int employeeIndex = employeeList.getSelectionModel().getSelectedIndex();
				for (GuiListener l : allListeners) {
					if(depIndex != -1 && roleIndex != -1 && employeeIndex != -1)
						effToGui(l.employeeEff(depIndex,roleIndex,employeeIndex));	
					else
						msg("pick an employee");
				}
			}
		});

		return employeePane;
	}

	@Override
	public void registerListener(GuiListener listener) {
		allListeners.add(listener);
	}

	public void msg(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

	@Override
	public void departmentAddedToGui(String name) {
		depList.getItems().add(name);
		msg("Department " + name + " was added to the company.");
		rolesByDep.add(new ComboBox<String>());
		employeesByrole.add(new ArrayList<ComboBox<String>>());
	}

	@Override
	public void confirmeDepSync(String depName, boolean isSync) {
		if(isSync)
			msg(depName + " has been syncronize");
		else
			msg("department " + depName + " removed syncronization");
	}

	@Override
	public void roleAddedToGui(String name, int depIndex) {
		rolesByDep.get(depIndex).getItems().add(name);
		msg("role " + name + " was added to the company");
		employeesByrole.get(depIndex).add(new ComboBox<String>());
	}
	@Override
	public void confirmRoleSync(String name, boolean depIsSync, boolean isSync) {
		if(depIsSync && isSync)
			msg("Can't sync role, department have priority");
		else if(isSync)
			msg(name + " has been syncronize");
		else
			msg("the role " + name + " removed syncronization");
	}
	@Override
	public void employeeAddedToGui(String name, int depIndex, int roleIndex) {
		employeesByrole.get(depIndex).get(roleIndex).getItems().add(name);
		msg("Employee " + name + " was added to the company");
	}
	@Override
	public void toStringToGui(String toString) {
		lblString.setText(toString);

	}
	public void effToGui(String effString) {
		lblEff.setText(effString);
	}

	@Override
	public void setCompanyField(String name) {
		fieldCompanyName.setText(name);
		fieldCompanyName.setDisable(true);
		lblCompanyName.setDisable(true);
		btnCompanyName.setDisable(true);
		depPane.setDisable(false);
	}
}
