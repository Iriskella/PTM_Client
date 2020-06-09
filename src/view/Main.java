package view;

import command.DisconnectCommand;
import command.Interpreter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.MainModel;
import viewModel.ViewModel;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Flight Simulator");
        primaryStage.setScene(new Scene(root));

        MainModel mainModel = new MainModel();
        MainWindowController view = loader.getController();
        ViewModel viewModel=new ViewModel(view, mainModel);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        mainModel.addObserver(viewModel);
        view.addObserver(viewModel);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
