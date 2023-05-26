package com.edengardensigiriya.edengarden.dto;

import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);
        int x=10;
        int y=10;
        int var=input.nextInt();
        String v=compare(x,y,var);
        System.out.println(v);
    }

    private static String compare(int x, int y, int var) {
        int z=0;
        if(x>var){
            z=x+y;
        }else if(y>var){
            z=x-y;
        }
        return String.valueOf(z);
    }
}
