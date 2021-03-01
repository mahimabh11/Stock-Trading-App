package com.example.stockapp;

import java.util.List;

public class section {
    private String sectionName;
    private String netW;
    private String netvalue;
    private List<Stock_card> stockList;

    public section(String sectionName,String netW, String netvalue, List<Stock_card> stockList) {
        this.sectionName = sectionName;
        this.stockList=stockList;
        this.netW=netW;
        this.netvalue=netvalue;
//        System.out.println("section constr");
//        System.out.println(this.sectionName);
//        System.out.println(this.stockList);
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<Stock_card> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock_card> stockList) {
        this.stockList = stockList;
    }

    @Override
    public String toString() {
        return "section{" +
                "sectionName='" + sectionName + '\'' +
                ", stockList=" + stockList +
                '}';
    }

    public String getNetW() {
        return netW;
    }

    public void setNetW(String netW) {
        this.netW = netW;
    }

    public String getNetvalue() {
        return netvalue;
    }

    public void setNetvalue(String netvalue) {
        this.netvalue = netvalue;
    }
}
