package com.util.csvComparison;

import java.util.List;
import java.util.Properties;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ReadCsvPath {

	
	public static String readCsvPaths(String csvName) throws IOException {
		String csvPath;
		Properties configFile=new Properties();
		FileInputStream file=new FileInputStream("./config.properties");
		configFile.load(file);
		csvPath=configFile.getProperty(csvName);
		return csvPath;
		
	}
	
	public static String getJarPath() {
		File jarPath=new File(java.lang.Object.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String propertiesPath=jarPath.getAbsolutePath();
        System.out.println(" propertiesPath-"+propertiesPath);
        return propertiesPath;
	}

}
