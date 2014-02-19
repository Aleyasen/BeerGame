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
    private static int MAX_WEEK = 60;
    private static int DELAY = 2; //number of weeks takes to prepare orders

    public SupplyChain() {
        agents = new ArrayList<Agent>();
        requestStream = getRequests();
        Agent retailer = new Agent() {
            @Override
            public void play() {
            }
        };
        Agent wholesaler = new Agent() {
            @Override
            public void play() {
            }
        };
        Agent distributer = new Agent() {
            @Override
            public void play() {
            }
        };
        Agent producer = new Agent() {
            @Override
            public void play() {
            }
        };
        agents.addAll(Arrays.asList(retailer, wholesaler, distributer, producer));
    }

    public void start() {
        while (week < MAX_WEEK) {
            agents.get(0).incoming(requestStream.get(week));
            for (int i = 1; i < agents.size(); i++) {
                Double previousOutgoing = agents.get(i - 1).getOutgoing(week - DELAY);
                agents.get(i).incoming(previousOutgoing);
            }
            for (int i = 0; i < agents.size(); i++) {
                agents.get(i).play();
            }
            week++;
        }
    }

    private DataStream getRequests() {
        DataStream requests = new DataStream();
        requests.get().addAll(Arrays.asList(10.0, 10.0, 15.0, 15.0, 15.0));
        return requests;
    }

}
