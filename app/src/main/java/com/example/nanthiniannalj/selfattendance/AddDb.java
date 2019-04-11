package com.example.nanthiniannalj.selfattendance;

public class AddDb {
    String useremail;
    String dates;
    String statuses;
    String latitude;
    String longitude;
    String times;

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getStatuses() {
        return statuses;
    }

    public void setStatuses(String statuses) {
        this.statuses = statuses;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public AddDb(String useremail, String dates, String statuses, String latitude, String longitude, String times) {

        this.useremail = useremail;
        this.dates = dates;
        this.statuses = statuses;
        this.latitude = latitude;
        this.longitude = longitude;
        this.times = times;
    }

    public AddDb()
    {

    }


}
