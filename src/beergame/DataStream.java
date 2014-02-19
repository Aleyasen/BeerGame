/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beergame;

import java.util.ArrayList;
import java.util.List;
import sun.awt.image.SunWritableRaster;

/**
 *
 * @author aleyase2-admin
 */
public class DataStream {

    List<Double> data;

    public DataStream() {
        data = new ArrayList<>();
    }

    public void push(Double element) {
        data.add(element);
    }

    public Double getCurrent() {
        return get(data.size() - 1);
    }

    public Double get(int index) {
        return data.get(index);
    }

    public List<Double> get() {
        return data;
    }

}
