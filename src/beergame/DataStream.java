/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beergame;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aleyase2-admin
 */
public class DataStream {

    List<Double> data;

    public DataStream() {
        data = new ArrayList<>();
    }

    DataStream(double d) {
        this();
        data.add(d);
    }

    public void push(Double element) {
        data.add(element);
    }

    public Double getCurrent() {
        return get(data.size() - 1);
    }

    public Double get(int index) {
//        if (index > 0 && index < data.size()) {
        return data.get(index);
//        } else {
//            System.out.println("error in dataStream.get(index)! index=" + index + " size=" + data.size());
//            return 0.0;
//        }

    }

    public List<Double> getStream() {
        return data;
    }

    @Override
    public String toString() {
        return data + "";
    }

}
