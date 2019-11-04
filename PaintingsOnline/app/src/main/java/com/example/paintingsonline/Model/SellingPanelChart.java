package com.example.paintingsonline.Model;

public class SellingPanelChart
{
    private String Sum;
    private String Month;

    public SellingPanelChart(String sum, String month) {
        Sum = sum;
        Month = month;
    }

    public String getSum() {
        return Sum;
    }

    public void setSum(String sum) {
        Sum = sum;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }
}
