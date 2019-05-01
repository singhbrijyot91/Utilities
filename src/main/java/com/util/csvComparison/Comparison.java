package com.util.csvComparison;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.CSVReader;

public class Comparison extends ReadCsvPath{
	static int rowCounter = 0;
	static String[] header1;
	static String[] header2;
	static List<String[]> allData;

	public static void main(String[] args) {
		try {
			readCSV();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	public static void readCSV() throws IOException {
		Reader reader1 = Files.newBufferedReader(Paths.get(readCsvPaths("actualCSV")));
		Reader reader2 = Files.newBufferedReader(Paths.get(readCsvPaths("expectedCSV")));

		CSVReader csvReader1 = new CSVReader(reader1);
		CSVReader csvReader2 = new CSVReader(reader2);

		rowCounter = 0;

		String[] nextRecordReader1;
		String[] nextRecordReader2;

		// Verify number of rows in both csv
		if (getRowCount()) {
			while ((nextRecordReader1=csvReader1.readNext()) !=null && (nextRecordReader2=csvReader2.readNext()) !=null)
					  {

				// Verify header row in both csv
				if (rowCounter == 0) {
					header1 = nextRecordReader1;
					header2 = nextRecordReader2;
					if(!checkEquality(header1, header2)) {
						break;
					}
					rowCounter += 1;
				} else {
					// Verify remaining rows
					checkEquality(nextRecordReader1, nextRecordReader2);
					rowCounter += 1;
				}
			}
		}

	}
	
	public static boolean getRowCount() throws IOException {
		Reader reader1 = Files.newBufferedReader(Paths.get(readCsvPaths("actualCSV")));
		Reader reader2 = Files.newBufferedReader(Paths.get(readCsvPaths("expectedCSV")));

		CSVReader csvReader1 = new CSVReader(reader1);
		CSVReader csvReader2 = new CSVReader(reader2);
		allData = csvReader1.readAll();
		List<String[]> allData1 = csvReader2.readAll();

		if (allData.size() != allData1.size()) {
			System.out.println(allData.size() + " rows in CSV1 against " + allData1.size()
					+ " rows in CSV2. Please check your CSV.");
			return false;
		} else {
			System.out.println("Number of rows are same in both CSV : " + allData.size());
		}
		return true;
	}

	public static boolean checkEquality(String[] s1, String[] s2) {
		try {
			// Verify header count
			if (s1.length != s2.length) {
				if(rowCounter==0) {
				System.out.println(
						"Headers in CSV1 is " + s1.length + " which is not equal to " + s2.length + " headers in CSV2");
				return false;
				}else {
					System.out.println("There is an extra cell in one of the CSVs");
					return false;
				}
			}
			boolean headerFlag = true;
			for (int i = 0; i < s1.length; i++) {
				// Verify header values
				if (rowCounter == 0) {
					if (!s1[i].trim().equals(s2[i].trim())) {
						System.out.println("CSV1 header " + s1[i] + " is not equal to CSV2 header " + s2[i]);
						headerFlag = false;

					}
					// verify values other then headers
				} else if (!s1[i].trim().equals(s2[i].trim())) {
					System.out.println(header1[i] + " in CSV1 is " + s1[i] + " is not equal to "+header2[i]+" in CSV2 " + s2[i]
							+ " in row number " + rowCounter);
					headerFlag = false;

				}
			}
			if (headerFlag && rowCounter == 0) {
				System.out.println("CSV headers are equal : "+s1.length);

			}
			if (headerFlag && rowCounter != 0) {
				System.out.println("Row number "+rowCounter + " data is equal");
			}
		} catch (NullPointerException e) {
			System.out.println("Row is missing in any of the CSV");
		}
		return true;
	}



}
