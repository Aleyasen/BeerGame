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
public abstract class Agent {

    Stock stock;
    Incoming incoming;
    Outgoing outgoing;
    static Double DEFAULT_OUTGOING = 4.0;

    public Agent() {
        stock = new Stock();
        incoming = new Incoming();
        outgoing = new Outgoing();
    }

    public void incoming(Double val) {
        incoming.push(val);
    }

    public Double getOutgoing(int week) {
        if (week < 0) {
            return DEFAULT_OUTGOING;
        }
        return outgoing.get(week);
    }

    public abstract void play();

}
