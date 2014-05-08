/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beergame;

import beergame.ui.BeerGameUIController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author aleyase2-admin
 */
public class BeerGame extends Application {

    public static BeerGameUIController appController;

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/BeerGameUI.fxml"));
            Parent root = (Parent) loader.load();
            //root.getStylesheets().add("style-default.css");
            appController = (BeerGameUIController) loader.getController();
            appController.setStageAndSetupListeners(stage);
            SupplyChain sch = new SupplyChain();
            sch.start();
            appController.setSupplyChain(sch);

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Beer Game Simulation");
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(BeerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
