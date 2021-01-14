package com.example.databaseproject;

public class myGlobalData {
    private static String employeeName;
    private static int employeeId = 20000529;
    private static int branchId;
    private static String branchName;
    private static String branchManagerName;
    private static int sessionKey;
    private static String level;

    public static void setEmployeeName(String data) {
        employeeName = data;
    }

    public static void setEmployeeId(int data) {
        employeeId = data;
    }

    public static void setBranchId(int data) {
        branchId = data;
    }

    public static void setBranchName(String data) {
        branchName = data;
    }

    public static void setBranchManagerName(String data) {
        branchManagerName = data;
    }

    public static void setSessionKey(int data) {
        sessionKey = data;
    }

    public static void setLevel(String data) {
        level = data;
    }

    public static String getEmployeeName() {
        return employeeName;
    }

    public static int getEmployeeId() {
        return employeeId;
    }

    public static int getBranchId() {
        return branchId;
    }

    public static String getBranchName() {
        return branchName;
    }

    public static String getBranchManagerName() {
        return branchManagerName;
    }

    public static int getSessionKey() {
        return sessionKey;
    }

    public static String getLevel() {
        return level;
    }

}
