package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * ExtentReports manager for test reporting
 */
public class ExtentManager {

    private static ExtentReports extent;
    private static ExtentTest test;

    public static void initReports() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("target/extent-reports/ExtentReport.html");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("API Test Report");
            spark.config().setReportName("REST Assured Test Results");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Environment", System.getProperty("environment", "dev"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
        }
    }

    public static void createTest(String testName, String description) {
        if (extent == null) {
            initReports();
        }
        test = extent.createTest(testName, description);
    }

    public static ExtentTest getTest() {
        return test;
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
