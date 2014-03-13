/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beergame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author aleyase2-admin
 */
public class SupplyChain {

    List<Agent> agents;
    int week;
    DataStream requestStream;
    private static int MAX_WEEK = 52;
    private static int DELAY = 2; //number of weeks takes to prepare orders

    public List<Agent> getAgents() {
        return agents;
    }

    public List<DataStream> getAllDeliveries() {
        List<DataStream> list = new ArrayList<>();
        for (Agent agent : agents) {
            list.add(agent.outgoingDelivery);
        }
        return list;
    }

    public List<DataStream> getAllOrders() {
        List<DataStream> list = new ArrayList<>();
        for (Agent agent : agents) {
            list.add(agent.incomingOrder);
        }
        return list;
    }

    public List<DataStream> getAllInventoriesPlusBackorders() {
        List<DataStream> list = new ArrayList<>();
        for (Agent agent : agents) {
            list.add(agent.getInventoriesPlusBackorder());
        }

        return list;
    }

    public List<DataStream> getAllBalances() {
        List<DataStream> list = new ArrayList<>();
        for (Agent agent : agents) {
            list.add(agent.balance);
        }
        return list;
    }

    public Agent getAgents(int index) {
        return agents.get(index);
    }

    public void calculateOutgoingOrder(Agent agent) {
//        scenario1(agent);
        scenario2(agent);
//        scenario3(agent);
//        scenario4(agent);
//        scenario5(agent);
    }

    public void scenario1(Agent agent) { // Follow incoming order
        final Double incomingOrder = agent.incomingOrder.getCurrent();
        agent.outgoingOrder.push(incomingOrder);
    }

    public void scenario2(Agent agent) { // Follow incoming order + Compensate stock levels
        Double outgoingOrder = 0.0;
        final Double inventoryAndbackorder = agent.inventory.getCurrent() - agent.backorder.getCurrent();
        final Double incomingOrder = agent.incomingOrder.getCurrent();
        if (inventoryAndbackorder > incomingOrder) {
            outgoingOrder = 0.0;
        } else {
            outgoingOrder = incomingOrder - (inventoryAndbackorder);
        }
        agent.outgoingOrder.push(outgoingOrder);
    }

    public void scenario3(Agent agent) { // All know all things!
        agent.outgoingOrder.push(requestStream.get(week));
    }

    public void scenario4(Agent agent) { // All know all things! + Compensate stock levels
        Double outgoingOrder = 0.0;
        final Double inventoryAndbackorder = agent.inventory.getCurrent() - agent.backorder.getCurrent();
        if (inventoryAndbackorder > requestStream.get(week)) {
            outgoingOrder = 0.0;
        } else {
            outgoingOrder = requestStream.get(week) - inventoryAndbackorder;
        }
        agent.outgoingOrder.push(outgoingOrder);
    }

    static double MAX_DRIFT = 4.0;

    public void scenario5(Agent agent) { // All know maximum drift
        Double outgoingOrder = 0.0;
        final Double inventoryAndbackorder = agent.inventory.getCurrent() - agent.backorder.getCurrent();
        final Double incomingOrder = agent.incomingOrder.getCurrent();
        if (inventoryAndbackorder > incomingOrder) {
            outgoingOrder = 0.0;
        } else {
            outgoingOrder = incomingOrder - inventoryAndbackorder;
            final Double previousOutgoingOrder = agent.outgoingOrder.getCurrent();
            final Double diff = outgoingOrder - previousOutgoingOrder;
            if (diff > MAX_DRIFT) {
                outgoingOrder = previousOutgoingOrder + MAX_DRIFT;
            } else if (diff < -MAX_DRIFT) {
                outgoingOrder = previousOutgoingOrder - MAX_DRIFT;
            }
        }
        agent.outgoingOrder.push(outgoingOrder);
    }

    public SupplyChain() {
        agents = new ArrayList<Agent>();
        requestStream = getRequests();
        Agent retailer = new Agent("Retailer") {
            @Override
            public void play() {
                calculateOutgoingOrder(this);
            }
        };
        Agent wholesaler = new Agent("Wholesaler") {
            @Override
            public void play() {
                calculateOutgoingOrder(this);
            }
        };
        Agent distributer = new Agent("Distributer") {
            @Override
            public void play() {
                calculateOutgoingOrder(this);
            }
        };
        Agent producer = new Agent("Producer") {
            @Override
            public void play() {
                calculateOutgoingOrder(this);
            }
        };
        agents.addAll(Arrays.asList(retailer, wholesaler, distributer, producer));
    }

    public void start() {
        week = 0;
        while (week < MAX_WEEK) {
            System.out.println("Starting week " + (week + 1));
            for (Agent agent : agents) {
                System.out.println(agent);
            }
            agents.get(0).incomingOrder.push(requestStream.get(week));
            for (int i = 1; i < agents.size(); i++) {
                Double previousOrder = agents.get(i - 1).outgoingOrder.get(week);
                agents.get(i).incomingOrder.push(previousOrder);
            }
            for (int i = 0; i < agents.size(); i++) {
                Double incomingDelivery;
                if (week - DELAY + 1 >= 0) {
                    if (i == agents.size() - 1) {
                        incomingDelivery = agents.get(i).outgoingOrder.get(week - DELAY + 1);
                    } else {
                        incomingDelivery = agents.get(i + 1).outgoingDelivery.get(week - DELAY + 1);
                    }
                } else {
                    incomingDelivery = 0.0;
                }
                Double incomingOrder = agents.get(i).incomingOrder.getCurrent();
                Double backorder = agents.get(i).backorder.getCurrent();
                Double inventory = agents.get(i).inventory.getCurrent();
                Double balance = agents.get(i).balance.getCurrent();
                Double available = inventory + incomingDelivery;
                Double toShip = backorder + incomingOrder;
                Double outgoingDeliver;
                if (toShip > available) {
                    outgoingDeliver = available;
                } else {
                    outgoingDeliver = toShip;
                }

                Double newBackorder = 0.0;
                if (toShip > available) {
                    newBackorder = toShip - available;
                } else {
                    newBackorder = 0.0;
                }
                Double newInventory = 0.0;
                if (toShip > available) {
                    newInventory = 0.0;
                } else {
                    newInventory = available - toShip;
                }

                agents.get(i).inventory.push(newInventory);
                agents.get(i).backorder.push(newBackorder);
                agents.get(i).incomingDelivery.push(incomingDelivery);
                agents.get(i).outgoingDelivery.push(outgoingDeliver);
                agents.get(i).balance.push(balance - inventory * 1 - backorder * 2 - incomingDelivery * 1 + outgoingDeliver * 4);
                agents.get(i).play();
            }
            week++;
        }
    }

    private DataStream getRequests() {
        DataStream requests = new DataStream();
        requests.getStream().addAll(Arrays.asList(4.0, 4.0, 4.0, 4.0, 4.0, 4.0));
        for (int i = 0; i < MAX_WEEK - 6; i++) {
            requests.getStream().add(8.0);
        }
        return requests;
    }

}
