package webBrowsers;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import reporting.ExtentManager;
import reporting.ExtentTestManager;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

    public class BrowserDriver {

        public static WebDriver driver = null;
        public static String browserName = System.getProperty("browserName", "chrome");
        public static final String url = System.getProperty("url", "https://www.zappos.com/");
        public static String os = System.getProperty("os", "mac");
        public static String cloudPlatformName = System.getProperty("cloudPlatformName", "browserstack");
        public static final String AUTOMATE_USERNAME = System.getProperty("AUTOMATE_USERNAME","xxxwinsxxxx");
        public static final String AUTOMATE_ACCESS_KEY = System.getProperty("AUTOMATE_ACCESS_KEY", "abcds");
        public static String platform = System.getProperty("platform", "local");

        /**
         * **************************************************
         * ********** Start Of Reporting Utilities **********
         * **************************************************
         * */

        //ExtentReport
        public static ExtentReports extent;
        @BeforeSuite
        public void extentSetup(ITestContext context) {
            ExtentManager.setOutputDirectory(context);
            extent = ExtentManager.getInstance();
        }
        @BeforeMethod
        public void startExtent(Method method) {
            String className = method.getDeclaringClass().getSimpleName();
            String methodName = method.getName().toLowerCase();
            ExtentTestManager.startTest(method.getName());
            ExtentTestManager.getTest().assignCategory(className);
        }
        protected String getStackTrace(Throwable t) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            return sw.toString();
        }
        @AfterMethod
        public void afterEachTestMethod(ITestResult result) {
            ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis()));
            ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis()));

            for (String group : result.getMethod().getGroups()) {
                ExtentTestManager.getTest().assignCategory(group);
            }

            if (result.getStatus() == 1) {
                ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
            } else if (result.getStatus() == 2) {
                ExtentTestManager.getTest().log(LogStatus.FAIL, getStackTrace(result.getThrowable()));
            } else if (result.getStatus() == 3) {
                ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
            }
            ExtentTestManager.endTest();
            extent.flush();
            if (result.getStatus() == ITestResult.FAILURE) {
                captureScreenshot(driver, result.getName());
            }
            driver.quit();
        }
        public static void captureScreenshot(WebDriver driver, String screenshotName){

            DateFormat df = new SimpleDateFormat("(MM.dd.yyyy-HH:mma)");
            Date date = new Date();
            df.format(date);

            File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(file, new File(System.getProperty("user.dir")+ "/screenshots/"+screenshotName+" "+df.format(date)+".png"));
                System.out.println("Screenshot captured");
            } catch (Exception e) {
                System.out.println("Exception while taking screenshot "+e.getMessage());;
            }

        }
        @AfterSuite
        public void generateReport() {
            extent.close();
        }
        private Date getTime(long millis) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            return calendar.getTime();
        }

        /**
         * **********************************************
         * **********End Of Reporting Utilities**********
         * **********************************************
         * */

        @BeforeMethod
        public void setUp() throws MalformedURLException {
            /**
             *
             * Here we will setup the driver
             *
             * */

            if(platform.equals("local")){
                if(browserName.equals("chrome")){
                    getChromeDriver();
                }
                else if(browserName.equals("fireFox")){
                    getFireFoxDriver();
                }
            }
            else if(platform.equals("cloud")) {
                if(cloudPlatformName.equals("browserstack")) {
                    getDriverForBrowserStack();
                }
                else if(cloudPlatformName.equals("saucelab")){
                    getDriverForSauceLab();
                }
            }

            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(45, TimeUnit.SECONDS);
            // Open Browser with the Home Page
            driver.get(url);

        }


        public static WebDriver getChromeDriver(){

            /**
             * Provide power or additional capabilities to Chrome Driver
             *
             * */
            /**
             * Command Line Arguments
             * https://peter.sh/experiments/chromium-command-line-switches/
             * */
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            options.addArguments("--start-maximized");

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            if(os.equals("mac")) {
                System.setProperty("webdriver.chrome.driver", "../Utilities/src/main/drivers/mac/chromedriver");
                driver = new ChromeDriver(options);
            }
            else if(os.equals("windows")){
                System.setProperty("webdriver.chrome.driver", "../Utilities/src/main/drivers/mac/chromedriver.exe");
                driver = new ChromeDriver(options);
            }

            return driver;
        }

        public static WebDriver getFireFoxDriver(){

            /**
             * https://chercher.tech/java/chrome-firefox-options-selenium-webdriver
             *
             * */
            FirefoxOptions options = new FirefoxOptions();
            //options.setHeadless(true);
            options.addArguments("--start-maximized");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--private");
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);

            if(os.equals("mac")) {
                System.setProperty("webdriver.gecko.driver", "../utilities/drivers/mac/geckodriver");
                driver = new FirefoxDriver(options);
            }
            else if(os.equals("windows")) {
                System.setProperty("webdriver.gecko.driver", "../utilities/drivers/windows/geckodriver.exe");
                driver = new FirefoxDriver(options);
            }

            return driver;
        }

        public WebDriver getDriverForBrowserStack() throws MalformedURLException {

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("os_version", "10");
            caps.setCapability("resolution", "2048x1536");
            caps.setCapability("browser", "Chrome");
            caps.setCapability("browser_version", "latest-beta");
            caps.setCapability("os", "Windows");

            driver = new RemoteWebDriver(new URL("https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub"),caps );

            return driver;
        }

        public WebDriver getDriverForSauceLab() throws MalformedURLException {

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("os_version", "10");
            caps.setCapability("browser", "Chrome");
            caps.setCapability("browser_version", "latest-beta");
            caps.setCapability("os", "Windows");

            driver = new RemoteWebDriver(new URL("http://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub"), caps);

            return driver;
        }

        @AfterMethod
        public void cleanUp(){
            driver.quit();
        }
}
