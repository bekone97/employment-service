package com.godeltech.mastery.employeeservice.utils;

public class TransactionUtil {
    public static boolean isUpdated(int numberOfChanges) {
        return numberOfChanges > 0;
    }
}
