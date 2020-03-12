package com.example.msm;

import java.util.ArrayList;

//쿠키용도
public class Admin_cooki {
    public static String password;
    public static ArrayList<Admin_Info2> arrayList = new ArrayList<>();
    public static int window_size_x;
    public static int window_size_y;

    public static ArrayList<Admin_Info2> getArrayList() {
        return arrayList;
    }

    public static void setArrayList(ArrayList<Admin_Info2> arrayList) {
        Admin_cooki.arrayList = arrayList;
    }

    public static int getWindow_size_x() {
        return window_size_x;
    }

    public static void setWindow_size_x(int window_size_x) {
        Admin_cooki.window_size_x = window_size_x;
    }

    public static int getWindow_size_y() {
        return window_size_y;
    }

    public static void setWindow_size_y(int window_size_y) {
        Admin_cooki.window_size_y = window_size_y;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Admin_cooki.password = password;
    }

    public static void clearCookie(){
        password = "";
        arrayList = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        Admin_Info2 temp = (Admin_Info2) obj;
        if(temp.equals(Admin_cooki.getArrayList())){
            return true;
        }
        else
            return false;
    }
}
