/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beergame.ui;

import beergame.Agent;
import beergame.DataStream;
import beergame.SupplyChain;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author aleyase2-admin
 */
public class BeerGameUIController implements Initializable {
    
    private SupplyChain sch;
    @FXML
    private LineChart<String, Number> chartOutgoing;
    @FXML
    private LineChart<String, Number> chartStock;
    @FXML
    private LineChart<String, Number> chartBalance;
    @FXML
    private LineChart<String, Number> chartIncoming;
    private Stage stage;
    @FXML
    private Label caption;
    
    public SupplyChain getSupplyChain() {
        return sch;
    }
    
    public void setSupplyChain(SupplyChain sch) {
        this.sch = sch;
        initializeCharts();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private void initializeCharts() {
        //caption.setTextFill(Color.DARKORANGE);
        caption.setVisible(false);
        caption.setStyle("-fx-font-size: 16px;\n"
                + "-fx-background-color: linen;\n"
                + "    -fx-border-color: darkorchid;\n"
                + "    -fx-border-radius: 3;\n"
                + "    -fx-padding: 2 10 2 10;");
        initialChart("Incoming", sch.getAllOrders(), sch.getAgents(), chartIncoming, false);
        initialChart("Outgoing", sch.getAllDeliveries(), sch.getAgents(), chartOutgoing, false);
        initialChart("Stock", sch.getAllInventoriesPlusBackorders(), sch.getAgents(), chartStock, false);
        initialChart("Balance", sch.getAllBalances(), sch.getAgents(), chartBalance, true);
        
    }
    
    private void initialChart(String title, List<DataStream> datalist, List<Agent> agents, LineChart<String, Number> chart, boolean totalSeries) {
        int index = 0;
        chart.setTitle(title);
        chart.setAnimated(true);
        chart.getXAxis().setAnimated(true);
        chart.getYAxis().setAnimated(true);
        for (DataStream data : datalist) {
            XYChart.Series series = new XYChart.Series();
            series.setName(agents.get(index).getName());
            int week = 1;
            for (Double val : data.getStream()) {
                final XYChart.Data node = new XYChart.Data(week + "", val);
                series.getData().add(node);
                
                week++;
            }
            chart.getData().add(series);
            index++;
        }
        if (totalSeries) {
            List<Double> sumOfStreams = new ArrayList<>();
            for (int i = 0; i < datalist.get(0).getStream().size(); i++) {
                double sum = 0;
                for (int j = 0; j < datalist.size(); j++) {
                    sum += datalist.get(j).getStream().get(i);
                }
                sumOfStreams.add(sum);
            }
            XYChart.Series series = new XYChart.Series();
            series.setName("All");
            int week = 1;
            for (Double val : sumOfStreams) {
                series.getData().add(new XYChart.Data(week + "", val));
                week++;
            }
            chart.getData().add(series);
        }
        for (final XYChart.Series series : chart.getData()) {
            for (Object dataRaw : series.getData()) {
                final XYChart.Data data = (XYChart.Data) dataRaw;
                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
//                                System.out.println("mouse enter for node=" + String.valueOf(data.getXValue()) + ": " + String.valueOf(data.getYValue()));
                                caption.setVisible(true);
                                caption.setLayoutX(e.getSceneX() + 15);
                                caption.setLayoutY(e.getSceneY() + 15);
                                String text = "week " + String.valueOf(data.getXValue()) + "\n"
                                + series.getName() + ": " + String.valueOf(data.getYValue());
                                caption.setText(text);
                            }
                        });
                data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                caption.setVisible(false);
                            }
                        });
            }
        }
    }
    
    public void setStageAndSetupListeners(Stage stage) {
        this.stage = stage;
    }
    
}
