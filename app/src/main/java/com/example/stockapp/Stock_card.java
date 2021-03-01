package com.example.stockapp;

public class Stock_card {
        String card_ticker;
        String card_desc;
        String card_last;
        String card_change;
        String trend;
        String SectionFlag;

    public Stock_card(String card_ticker, String card_last,
                      String card_desc, String card_change, String SectionFlag) {
        this.card_ticker=card_ticker;
        this.card_change=card_change;
        this.card_last=card_last;
        this.card_desc=card_desc;
        this.SectionFlag=SectionFlag;

    }


    public String getTicker() {
            return card_ticker;
        }

    public void setTicker(String card_ticker) {
            this.card_ticker = card_ticker;
        }

    public String getDesc() {
        return card_desc;
    }

    public void setDesc(String card_desc) {
        this.card_desc= card_desc;
    }

    public String getLast() {
        return card_last;
    }

    public void setLast(String card_last) {
        this.card_last = card_last;
    }

    public String getChange() {
        return card_change;
    }

    public void setChange(String card_change) {
        this.card_change = card_change;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }
    public String getSectionFlag() {
        return SectionFlag;
    }

    public void setSectionFlag(String sectionFlag) {
        SectionFlag = sectionFlag;
    }


}
