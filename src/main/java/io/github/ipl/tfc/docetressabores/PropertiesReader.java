package io.github.ipl.tfc.docetressabores;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
	private Properties properties;

    public PropertiesReader(String propertyFileName) throws IOException {
		InputStream is = getClass()
			.getClassLoader()
            .getResourceAsStream(propertyFileName);
        this.properties = new Properties();
		this.properties.load(is);

		System.out.println(properties.toString());
    }

    public String getProperty(String propertyName) {
		System.out.println(this.properties.getProperty(propertyName));
        return this.properties.getProperty(propertyName);
    }
}
