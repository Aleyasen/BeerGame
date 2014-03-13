/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beergame;

/**
 *
 * @author aleyase2-admin
 */
public class Agent {

    String name;
    DataStream inventory;
    DataStream incomingDelivery, outgoingDelivery;
    DataStream incomingOrder, outgoingOrder;
    DataStream balance;
    DataStream backorder;

    static Double DEFAULT_ORDER = 0.0;

    public Agent(String name) {
        inventory = new DataStream(0.0);
        incomingDelivery = new DataStream(0);
        outgoingDelivery = new DataStream(0);
        incomingOrder = new DataStream(0);
        outgoingOrder = new DataStream(0);
        backorder = new DataStream(0);
        balance = new DataStream(0);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DataStream getInventoriesPlusBackorder() {
        DataStream invAndBackOrder = new DataStream();
        for (int i = 0; i < inventory.getStream().size(); i++) {
            invAndBackOrder.push(inventory.get(i) - backorder.get(i));
        }
        return invAndBackOrder;
    }

    public void play() {
    }

    @Override
    public String toString() {
        return name + " [DLV:" + incomingDelivery + "][ORD:" + incomingOrder + "][BOR:" + backorder + "][INV:" + inventory + "]\n";
    }

}
