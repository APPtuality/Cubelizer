package com.app.talentum.cubelizer.cubelizer.persistencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Astrid on 29/11/2016.
 */

public class Affluence {

    private int longitud;
    private String uas_flow2;
    private ArrayList<String> lista2 = new ArrayList<>();
    private String[] lista1;// = uas_flow2.split("]");
    private int cont1, posX, posY, posXX, posYY;
    private int listaArrays[][];
    private String[] palabra3;
    private int listaUasFlow_a[][];// = new int[cont1][cont1];

    public int[][] getListaUasFlow_a() {
        return listaUasFlow_a;
    }

    public void setListaUasFlow_a(int[][] listaUasFlow_a) {
        this.listaUasFlow_a = listaUasFlow_a;
    }

    public void getArrayFlow(String uas) {

        int longitud = uas.length();
        String resultado2 = uas.substring(2, longitud - 2);
        //resultado = resultado.trim();
        System.out.println(resultado2);
        ArrayList<String> lista2 = new ArrayList<>();
        String[] lista1 = resultado2.split("]");

        int i = 0;
        for (String palabra : lista1) {
            if (i == 0) {
                lista2.add(palabra);
                System.out.println("ARRAYFLOW con la lista2, " + palabra);

                i = i + 1;
                //System.out.println(i);
            } else {
                palabra = palabra.substring(3);
                lista2.add(palabra);
                System.out.println(palabra);
                i = i + 1;
                //System.out.println(i);
            }
        }
        System.out.println(i);

        int x = 0;
        int y = 0;
        String lista3[][] = new String[i][i];
        //ArrayList<String> lista3 = new ArrayList<>();
        for (String palabra2 : lista2) {
            String[] palabra3 = palabra2.split(",");
            for (String palabra4 : palabra3) {
                lista3[x][y] = palabra4.trim();
                System.out.println("****ARRAY_FLOW con la lista3 [x]= " + x + "[y]= " + y + ", " + lista3[x][y]);
                //arrayFlechasSector(0);
                //   int[] arraySectorFlow;
                //  arraySectorFlow = Integer.parseInt(lista3[i][y]);
                //System.out.println(lista3[x][y]);

                y = y + 1;
            }
            y = 0;
            x = x + 1;
        }

        int xx = 0;
        int yy = 0;
        int listaUasFlow[][] = new int[i][i];
        for (String palabra5[] : lista3) {
            for (String palabra6 : palabra5) {
                listaUasFlow[xx][yy] = Integer.parseInt(palabra6);
                //  listaUasFlow_a = listaUasFlow;
                yy = yy + 1;
                System.out.println(palabra6);
            }
            yy = 0;
            xx = xx + 1;
        }
        System.out.println("LISTA_uasflow, "+String.valueOf(listaUasFlow[2][1]).toString());
        listaUasFlow_a = listaUasFlow;
        System.out.println("LISTA_uasflow_a, "+String.valueOf(listaUasFlow_a[2][1]).toString());
        arrayFlechasSector(4);
    }

    public ArrayList<Integer> arrayFlechasSector (int n){
        ArrayList<Integer> arrayFlujos = new ArrayList<Integer>();
        for (int i=0; i<listaUasFlow_a.length; i++) {
            arrayFlujos.add(listaUasFlow_a[i][n]);
            System.out.println("ARRAY_de_flechas, segun sector FLUJOS, " + String.valueOf(arrayFlujos));
        }
        return arrayFlujos;
    }


}
