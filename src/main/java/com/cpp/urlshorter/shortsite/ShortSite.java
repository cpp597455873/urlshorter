package com.cpp.urlshorter.shortsite;

import java.util.Random;
import java.util.TreeSet;

public class ShortSite {
    public final static String charSet = "12345mnABCD6wxyzEFGHIJKLMNOPjkl7980abcdefghiqrstuvopQRSTUVWXYZ";


    public static String getOneSite() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = new Random().nextInt(charSet.length());
            sb.append(charSet.charAt(index));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        TreeSet<String> treeSet = new TreeSet<String>();
        String oneSite;
        int i1 = 500000;
        for (int j = 0; j < 100; j++) {
            treeSet.clear();
            for (int i = 0; i < i1; i++) {
                oneSite = getOneSite();
                if (treeSet.contains(oneSite)) {
                    System.out.println("重复了" + treeSet.size()+"  "+oneSite);
                }else {
                    treeSet.add(oneSite);
                }
            }
            System.out.println("-------------"+j+"-------------");
        }
        System.out.println(i1-treeSet.size());
    }
}
