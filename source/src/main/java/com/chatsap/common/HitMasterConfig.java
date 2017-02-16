package com.chatsap.common;

import java.io.InputStream;
import java.util.Properties;

public class HitMasterConfig {
	public static final String GECKO_WEBDRIVER_LOCATION = "hitmaster.webdriver.location";
	public static final String TOR_BROWSER_LOCATION = "hitmaster.tor.location";
	public static final String TOR_BROWSER_PROFILE_LOCATION = "hitmaster.tor.profile.location";
	
	public static final String MAX_HITS = "hitmaster.website.maxHits";
	public static final String TARGET_URI = "hitmaster.target.URI";
	public static final String INTER_ROUND_WAIT_TIME = "hitmaster.round.inter_round_wait_times";
	public static final String ON_PAGE_WAIT_TIME = "hitmaster.round.on_page_wait_times";
	

	private static final String HIT_MASTER_CONFIG_FILE = "hit_master_app.properties";
	private static Properties prop = null;

	static {
		load();
	}

	public static String getProperty(String propName) {
		return prop==null? null: prop.getProperty(propName);
	}

	private static void load() {
		try {
			prop = new Properties();
			InputStream input = null;
			try {
				input = HitMasterConfig.class.getClassLoader().getResourceAsStream(HIT_MASTER_CONFIG_FILE);
				if(input == null){
					throw new RuntimeException("Error loading " + HIT_MASTER_CONFIG_FILE + " from classpath.");
				}
				prop.load(input);
			} finally {
				if(input!=null){
					input.close();
				}
			}
		} catch (Throwable th) {
			throw new RuntimeException("Error loading " + HIT_MASTER_CONFIG_FILE + " from classpath.", th);
		} finally {

		}
	}

}
