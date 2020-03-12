package com.example.msm;

public class Admin_Info2 {
    int month,year;
    int day;
    int hour, minute;
    int colors;
    int checking;
    String context,week;
    boolean select;

    public Admin_Info2() {
    }

    public Admin_Info2(int year, int month,String week, int day, int hour, int minute, String context, int colors, int checking) {
        this.week = week;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.context = context;
        this.colors = colors;
        this.year = year;
        this.month = month;
        this.checking = checking;
    }

    public int getChecking() {
        return checking;
    }

    public void setChecking(int checking) {
        this.checking = checking;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getColors() {
        return colors;
    }

    public void setColors(int colors) {
        this.colors = colors;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
