package base;

import com.automate.driver.DeviceManager;
import com.automate.driver.DriverManager;
import com.automate.driver.PlatformManager;
import com.automate.enums.MobilePlatformName;
import com.automate.factories.DriverFactory;
import com.automate.utils.AppiumServerUtils;
import com.automate.utils.screenrecording.ScreenRecordingService;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.util.Objects;

public class BaseTest {

    protected BaseTest() {
    }

    @BeforeSuite(alwaysRun = true)
    protected void beforeSuite() {
        AppiumServerUtils.startAppiumServer();
    }

    @Parameters({"platformName", "udid", "deviceName", "systemPort", "chromeDriverPort", "emulator", "wdaLocalPort",
            "webkitDebugProxyPort"})
    @BeforeMethod
    protected void setUp(String platformName, String udid, String deviceName, @Optional("androidOnly") String systemPort,
                         @Optional("androidOnly") String chromeDriverPort, @Optional("androidOnly") String emulator,
                         @Optional("iOSOnly") String wdaLocalPort, @Optional("iOSOnly") String webkitDebugProxyPort) {
        PlatformManager.setPlatformName(platformName);
        DeviceManager.setDeviceName(deviceName);
        if (Objects.isNull(DriverManager.getDriver())) {
            DriverFactory.initializeDriver(MobilePlatformName.valueOf(platformName.toUpperCase()), deviceName, udid, Integer.parseInt(systemPort), emulator);
        }
        ScreenRecordingService.startRecording();
    }

    @AfterMethod
    protected void tearDown(ITestResult result) {
        ScreenRecordingService.stopRecording(result.getName());
        DriverFactory.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    protected void afterSuite() {
        AppiumServerUtils.stopAppiumServer();
    }
}
