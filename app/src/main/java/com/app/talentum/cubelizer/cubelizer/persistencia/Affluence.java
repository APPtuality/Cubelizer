package com.app.talentum.cubelizer.cubelizer.persistencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Astrid on 29/11/2016.
 */

public class Affluence {

    private List<List<Integer>> affluenceFlow = new ArrayList<List<Integer>>();

    public List<List<Integer>> getAffluenceFlow() {
        return affluenceFlow;
    }

    public void setAffluenceFlow(List<List<Integer>> affluenceFlow) {
        this.affluenceFlow = affluenceFlow;
    }
}
