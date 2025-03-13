package fr.keycloak.oidc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtilities {
	
	/**
	 * Loading properties
	 * 
	 * @param filePath
	 * @return
	 */
	public Properties loadPropertiesFile(String filePath) {

		Properties prop = new Properties();

		try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
			prop.load(resourceAsStream);
		} catch (IOException e) {
			System.err.println("Unable to load properties file : " + filePath);
		}

		return prop;

	}
}
