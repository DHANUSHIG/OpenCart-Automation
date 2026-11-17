package utilities;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

/*
 * This class is responsible for generating Extent Reports.
 * It implements ITestListener, which means it listens to TestNG test events.
 * Whenever a test starts, passes, fails, or skips, this class will react.
 */

public class ExtentReportManager implements ITestListener {

    // This creates the HTML report file
    public ExtentSparkReporter sparkReporter;

    // Main class which controls the reporting
    public ExtentReports extent;

    // Represents each individual test inside the report (ThreadLocal for parallel thread safety)
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // Report file name
    String repName;

    // =================== WHEN TEST SUITE STARTS ===================

    /*
     * This method runs BEFORE any test method starts.
     * It is used to configure the report.
     */
    public void onStart(ITestContext testContext) {

        // Create unique report name using date & time
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        repName = "Test-Report-" + timeStamp + ".html";

        // Create report inside "reports" folder
        sparkReporter = new ExtentSparkReporter("./reports/" + repName);

        // Set report UI properties
        sparkReporter.config().setDocumentTitle("opencart Automation Report");
        sparkReporter.config().setReportName("opencart Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK);

        // Attach reporter to main ExtentReports object
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Add system information to report
        extent.setSystemInfo("Application", "opencart");
        extent.setSystemInfo("Environment", "QA");

        // Get parameters from testng.xml file
        String os = testContext.getCurrentXmlTest().getParameter("os");
        extent.setSystemInfo("Operating System", os);

        String browser = testContext.getCurrentXmlTest().getParameter("browser");
        extent.setSystemInfo("Browser", browser);

        // If groups are used in TestNG, capture them
        List<String> groups = testContext.getCurrentXmlTest().getIncludedGroups();
        if (!groups.isEmpty()) {
            extent.setSystemInfo("Groups", groups.toString());
        }
    }

    // =================== WHEN EACH TEST STARTS ===================

    /*
     * This runs before every @Test method.
     * It creates a new test entry in the report.
     */
    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    // =================== WHEN TEST PASSES ===================

    /*
     * If test method executes successfully,
     * this method logs PASS status.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test Passed");
    }

    // =================== WHEN TEST FAILS ===================

    /*
     * If test method fails,
     * this method logs failure and attaches screenshot.
     */
    @Override
    public void onTestFailure(ITestResult result) {

        extentTest.get().log(Status.FAIL, "Test Failed");

        // Log actual exception message
        extentTest.get().log(Status.INFO, result.getThrowable());

        try {
            // Capture screenshot from BaseClass
            String imgPath = ((BaseClass) result.getInstance()).captureScreen(result.getName());

            // Attach screenshot to report
            extentTest.get().addScreenCaptureFromPath(imgPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =================== WHEN TEST IS SKIPPED ===================

    /*
     * If test is skipped (dependsOnMethods failure etc.),
     * mark it as SKIP in report.
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "Test Skipped");
        extentTest.get().log(Status.INFO, result.getThrowable());
    }

    // =================== WHEN TEST SUITE FINISHES ===================

    /*
     * This runs after all tests are completed.
     * It finalizes the report and opens it automatically if in a supported desktop environment.
     */
    @Override
    public void onFinish(ITestContext testContext) {

        // Write everything into report file
        extent.flush();

        // Automatically open report in default browser if desktop is supported and not in headless mode
        String path = System.getProperty("user.dir") + "/reports/" + repName;
        File reportFile = new File(path);

        try {
            if (!GraphicsEnvironment.isHeadless() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(reportFile.toURI());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}