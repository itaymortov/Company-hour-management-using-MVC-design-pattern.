package application;
	
import Controller.Controller;
import Model.Company;
import View.AbstractSimView;
import View.SimulationView;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Company company = new Company("");
			AbstractSimView view = new SimulationView(primaryStage);
			Controller controll = new Controller(company, view);
			controll.loadData();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
