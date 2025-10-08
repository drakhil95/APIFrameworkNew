package utils;

import com.aventstack.extentreports.ExtentTest;

/**
 * ThreadLocal utility to provide thread-safe ExtentReports, ExtentTest, and DBUtils instances.
 */
public class ThreadLocalClass {
    // ThreadLocal for ExtentTest
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    // ThreadLocal for DBUtills
    private static final ThreadLocal<DBUtills> dbUtilsThreadLocal = new ThreadLocal<>();

    // Setters
    public static void setExtentTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }

    public static void setDBUtills(DBUtills dbUtils) {
        dbUtilsThreadLocal.set(dbUtils); 
    }

    // Getters
    public static ExtentTest getExtentTest() {
        return extentTestThreadLocal.get();
    }

    public static DBUtills getDBUtills() {
        return dbUtilsThreadLocal.get();
    }

    // Remove (cleanup)
    public static void removeExtentTest() {
        extentTestThreadLocal.remove();
    }

    public static void removeDBUtills() {
        dbUtilsThreadLocal.remove();
    }
}
