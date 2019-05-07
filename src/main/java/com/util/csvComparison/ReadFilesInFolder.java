package com.util.csvComparison;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.opencsv.CSVReader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ReadFilesInFolder {

	static List<String> csvPathActual;
	static List<String> csvPathExpected;
	static int rowCounter = 0;
	static String[] header1;
	static String[] header2;
	static List<String[]> allData;
	static ExtentReports extent;
	static ExtentTest logger;
	// static List<String> csvPathActual;
	// static List<String> csvPathExpected;

	public static void main(String[] args) {
		// Reporting
		extent = new ExtentReports("./Report.html", true);
		extent.loadConfig(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\extent-config.xml"));
		logger = extent.startTest("Comparing CSVs");
		csvPathExpected = readFiles(new File("C:\\Users\\Ashmeet\\Documents\\Brijyot\\CSVFolder1"));
		csvPathActual = readFiles(new File("C:\\Users\\Ashmeet\\Documents\\Brijyot\\CSVFolder2"));

		Collections.sort(csvPathExpected);
		Collections.sort(csvPathActual);

		if (csvPathExpected.size() == csvPathActual.size()) {

			for (int i = 0; i < csvPathExpected.size(); i++) {

				if (csvPathExpected.get(i).substring(csvPathExpected.get(i).lastIndexOf("_")+1)
						.equals(csvPathExpected.get(i).substring(csvPathActual.get(i).lastIndexOf("_")+1))) {
					try {
						readCSV(csvPathExpected.get(i), csvPathActual.get(i));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("Expected CSV does not match " + csvPathExpected.get(i) + " Actual csv path is "
							+ csvPathActual.get(i));
				}
			}
		} else {
			System.out.println("Number of csv files does not match in expected and actual folder");
		}
	}

	public static List<String> readFiles(File folder) {
		List<String> csvPath = new ArrayList();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				readFiles(fileEntry);
			} else {
				if (fileEntry.getName().endsWith(".csv")) {
					System.out.print(fileEntry.getName());
					System.out.print(" --> " + fileEntry.getAbsolutePath());
					System.out.println();
					csvPath.add(fileEntry.getAbsolutePath());
				}

			}
		}
		return csvPath;
	}

	public static void readCSV(String path1, String path2) throws IOException {

		Reader reader1 = Files.newBufferedReader(Paths.get(path1));
		Reader reader2 = Files.newBufferedReader(Paths.get(path2));
		// Logging CSV Paths
		logger.log(LogStatus.INFO, "CSV1 path is " + path1);
		logger.log(LogStatus.INFO, "CSV2 path is " + path2);
		if (path1 == null || path2 == null) {
			logger.log(LogStatus.FAIL, "CSV path not found.");
		}
		CSVReader csvReader1 = new CSVReader(reader1);
		CSVReader csvReader2 = new CSVReader(reader2);

		rowCounter = 0;

		String[] nextRecordReader1;
		String[] nextRecordReader2;

		// Verify number of rows in both csv
		if (getRowCount(path1, path2)) {
			while ((nextRecordReader1 = csvReader1.readNext()) != null
					&& (nextRecordReader2 = csvReader2.readNext()) != null) {

				// Verify header row in both csv
				if (rowCounter == 0) {
					logger.log(LogStatus.INFO, "Checking Header row");
					header1 = nextRecordReader1;
					header2 = nextRecordReader2;
					if (!checkEquality(header1, header2)) {
						break;
					}

					rowCounter += 1;
				} else {
					// Verify remaining rows
					logger.log(LogStatus.INFO, "Checking row number " + rowCounter);
					checkEquality(nextRecordReader1, nextRecordReader2);
					rowCounter += 1;
				}
			}
		}

	}

	public static boolean getRowCount(String path1, String path2) throws IOException {

		Reader reader1 = Files.newBufferedReader(Paths.get(path1));
		Reader reader2 = Files.newBufferedReader(Paths.get(path2));

		CSVReader csvReader1 = new CSVReader(reader1);
		CSVReader csvReader2 = new CSVReader(reader2);
		allData = csvReader1.readAll();
		List<String[]> allData1 = csvReader2.readAll();

		if (allData.size() != allData1.size()) {
			System.out.println(allData.size() + " rows in CSV1 against " + allData1.size()
					+ " rows in CSV2. Please check your CSV.");
			logger.log(LogStatus.FAIL, allData.size() + " rows in CSV1 against " + allData1.size()
					+ " rows in CSV2. Please check your CSV.");
			return false;
		} else {
			System.out.println("Number of rows are same in both CSV : " + allData.size());
			logger.log(LogStatus.PASS, "Number of rows are same in both CSV : " + allData.size());
		}
		return true;
	}

	public static boolean checkEquality(String[] s1, String[] s2) {
		try {
			// Verify header count
			if (s1.length != s2.length) {
				if (rowCounter == 0) {
					System.out.println("Headers in CSV1 is " + s1.length + " which is not equal to " + s2.length
							+ " headers in CSV2");
					logger.log(LogStatus.FAIL, "Headers in CSV1 is " + s1.length + " which is not equal to " + s2.length
							+ " headers in CSV2");
					return false;
				} else {
					System.out.println("There is an extra cell in one of the CSVs");
					logger.log(LogStatus.FAIL, "There is an extra cell in one of the CSVs");
					return false;
				}
			}
			boolean headerFlag = true;
			for (int i = 0; i < s1.length; i++) {
				// Verify header values
				if (rowCounter == 0) {
					if (!s1[i].trim().equalsIgnoreCase(s2[i].trim())) {
						System.out.println("CSV1 header " + s1[i] + " is not equal to CSV2 header " + s2[i]);
						logger.log(LogStatus.FAIL, "CSV1 header " + s1[i] + " is not equal to CSV2 header " + s2[i]);
						headerFlag = false;

					}
					// verify values other then headers
				} else if (!s1[i].trim().equalsIgnoreCase(s2[i].trim())) {
					System.out.println(header1[i] + " in CSV1 is " + s1[i] + " is not equal to " + header2[i]
							+ " in CSV2 " + s2[i] + " in row number " + rowCounter);
					logger.log(LogStatus.FAIL, header1[i] + " : " + s1[i] + " in CSV1 " + " is not equal to "
							+ header2[i] + ": " + s2[i] + " in CSV2 " + " in row number " + rowCounter);
					headerFlag = false;

				}
			}
			if (headerFlag && rowCounter == 0) {
				System.out.println("CSV headers are equal : " + s1.length);
				logger.log(LogStatus.PASS, "CSV headers are equal : " + s1.length);

			}
			if (headerFlag && rowCounter != 0) {
				System.out.println("Row number " + rowCounter + " data is equal");
				logger.log(LogStatus.PASS, "Row number " + rowCounter + " data is equal");
			}
		} catch (NullPointerException e) {
			System.out.println("Row is missing in any of the CSV");
			logger.log(LogStatus.INFO, "Row is missing in any of the CSV");
		}

		return true;
	}

}
