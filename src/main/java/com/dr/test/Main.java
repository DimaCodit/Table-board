package com.dr.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Main extends Application {
    public FXMLLoader getfxml(){
        return new FXMLLoader(getClass().getResource("/sample.fxml"));
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
         try{
        FXMLLoader loader = getfxml();

        Parent root = loader.load();

        //root.getStylesheets().add("/sample/styles.css");

        Controller controller = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        controller.init(primaryStage, scene);
        primaryStage.show();

        new EchoServer(controller).start();}

         catch(Exception e) {e.printStackTrace();}

        //controller.getSettingsPanel().setVisible(false);
        int a = 0;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
