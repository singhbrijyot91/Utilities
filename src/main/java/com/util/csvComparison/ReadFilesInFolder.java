package com.util.csvComparison;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadFilesInFolder {
	static List<String> csvPath=new ArrayList();
	public static void main(String[] args) {
		readFiles(new File("C:\\Users\\Ashmeet\\Documents\\Brijyot\\CSVFolder"));

	}
	
	public static void readFiles(File folder) {
		for(final File fileEntry : folder.listFiles()) {
			if(fileEntry.isDirectory()) {
				readFiles(fileEntry);
			}else {
				if(fileEntry.getName().endsWith(".csv")) {
					System.out.print(fileEntry.getName());
					System.out.print(" --> " +fileEntry.getAbsolutePath());
					System.out.println();
					csvPath.add(fileEntry.getAbsolutePath());
				}

			}
		}
	}

}
