package com.example.msm;

import java.util.ArrayList;

public class Admin_Info1 {
    public int month,year;
    public ArrayList<Admin_Info2> arrayList;

    public Admin_Info1() {
    }
    public Admin_Info1(int year, int month) {
        this.month = month;
        this.year = year;
    }
    public Admin_Info1(int month, int year, ArrayList<Admin_Info2> arrayList) {
        this.month = month;
        this.year = year;
        this.arrayList = arrayList;
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

    public ArrayList<Admin_Info2> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Admin_Info2> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public boolean equals(Object obj) {
        Admin_Info1 temp = (Admin_Info1) obj;
        if(temp.year == year && temp.month == month){
            return true;
        }
        else
            return false;
    }
}
