package com.chatsap.hitmaster;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.chatsap.common.HitMasterConfig;

public class WebsiteHitMaster {

	public String targetURI = null;
	public int maxHits;
	public int[] interRoundWaitTime = null;
	public int[] onPageWaitTime = null;
	public String geckoWebDriverLocation = null;
	public String torBrowserLocation = null;
	public String torBrowserProfileLocation = null;

	public WebsiteHitMaster() {
		setMaxHits();
		setTargetURI();
		setInterRoundWaitTime();
		setOnPageWaitTime();

		setGeckoWebDriver();
		setTorBrowserLocation();
		setTorBrowserProfileLocation();
	}

	private void setTorBrowserProfileLocation() {
		String propVal = HitMasterConfig.getProperty(HitMasterConfig.TOR_BROWSER_PROFILE_LOCATION);
		if (propVal == null) {
			throw new RuntimeException("TOR_BROWSER_PROFILE_LOCATION not found in property file.");
		}
		this.torBrowserProfileLocation = propVal;
	}

	private void setTorBrowserLocation() {
		String propVal = HitMasterConfig.getProperty(HitMasterConfig.TOR_BROWSER_LOCATION);
		if (propVal == null) {
			throw new RuntimeException("TOR_BROWSER_LOCATION not found in property file.");
		}
		this.torBrowserLocation = propVal;
	}

	private void setGeckoWebDriver() {
		String propVal = HitMasterConfig.getProperty(HitMasterConfig.GECKO_WEBDRIVER_LOCATION);
		if (propVal == null) {
			throw new RuntimeException("GECKO_WEBDRIVER_LOCATION not found in property file.");
		}
		this.geckoWebDriverLocation = propVal;
		System.setProperty("webdriver.gecko.driver", this.geckoWebDriverLocation);
	}

	private void setMaxHits() {
		String propVal = HitMasterConfig.getProperty(HitMasterConfig.MAX_HITS);
		if (propVal == null) {
			throw new RuntimeException("MAX_HITS not found in property file.");
		}
		this.maxHits = Integer.parseInt(propVal);
	}

	private void setTargetURI() {
		String propVal = HitMasterConfig.getProperty(HitMasterConfig.TARGET_URI);
		if (propVal == null) {
			throw new RuntimeException("TARGET_URI not found in property file.");
		}
		this.targetURI = propVal;
	}

	private void setInterRoundWaitTime() {
		String propVal = HitMasterConfig.getProperty(HitMasterConfig.INTER_ROUND_WAIT_TIME);
		if (propVal == null) {
			throw new RuntimeException("INTER_ROUND_WAIT_TIME not found in property file.");
		} else {
			String[] waitTimeArr = propVal.trim().split(",");
			this.interRoundWaitTime = new int[waitTimeArr.length];
			for (int i = 0; i < waitTimeArr.length; i++) {
				interRoundWaitTime[i] = Integer.parseInt(waitTimeArr[i].trim());
			}

		}
	}

	private void setOnPageWaitTime() {
		String propVal = HitMasterConfig.getProperty(HitMasterConfig.ON_PAGE_WAIT_TIME);
		if (propVal == null) {
			throw new RuntimeException("ON_PAGE_WAIT_TIME not found in property file.");
		} else {
			String[] waitTimeArr = propVal.trim().split(",");
			this.onPageWaitTime = new int[waitTimeArr.length];
			for (int i = 0; i < waitTimeArr.length; i++) {
				onPageWaitTime[i] = Integer.parseInt(waitTimeArr[i].trim());
			}

		}
	}

	public void run() throws Exception {
		for (int i = 0; i < maxHits; i++) {
			startRound(i);

		}

	}

	private long getInterRoundWaitTime(int roundNumber) {
		int index = roundNumber % 10;
		int sleepDuration = this.interRoundWaitTime[index];
		return sleepDuration * 1000;
	}

	private long getVideoWatchTime(int roundNumber) {
		int index = roundNumber % 10;
		int sleepDuration = this.onPageWaitTime[index];
		return sleepDuration * 1000;
	}

	private void startRound(int roundNumber) throws Exception {
		System.out.println("************** Starting Round " + (roundNumber + 1) + " ************************");
		
		openLinkInTor(targetURI, roundNumber);
		
		long interRoundWaitTime = getInterRoundWaitTime(roundNumber);
		System.out.println("==========> Round " + (roundNumber + 1) + " is over. Let's wait for " + interRoundWaitTime
				+ " ms before starting next round...");
		Thread.sleep(interRoundWaitTime);
	}

	private void openLinkInTor(String targetURL, int roundNumber) throws Exception {

		try {
			FirefoxProfile profile = new FirefoxProfile(new File(this.torBrowserProfileLocation));
			FirefoxBinary binary = new FirefoxBinary(new File(this.torBrowserLocation));
			FirefoxDriver driver = new FirefoxDriver(binary, profile);
			driver.get(targetURL);

			// WebDriver driver = new ChromeDriver();
			// WebDriver driver = new FirefoxDriver();

			driver.get(targetURL);
			long watchTime = getVideoWatchTime(roundNumber);
			System.out.println("==========> Round:"+(roundNumber+1)+" - Browser is launched. Let's watch video for " + watchTime + " ms.");
			Thread.sleep(watchTime);
			
			// WebElement searchBox = driver.findElement(By.name("q"));
			// searchBox.sendKeys("ChromeDriver");
			// searchBox.submit();
			// Thread.sleep(5000); // Let the user actually see something!
			driver.quit();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

}
