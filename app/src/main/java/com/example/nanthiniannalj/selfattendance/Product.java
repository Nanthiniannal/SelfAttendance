package com.example.nanthiniannalj.selfattendance;

public class Product {
    public String dates;
    public String times;

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public Product(String dates, String times) {
        this.dates = dates;
        this.times = times;
    }
}
