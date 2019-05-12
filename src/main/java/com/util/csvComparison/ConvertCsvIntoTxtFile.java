package com.util.csvComparison;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConvertCsvIntoTxtFile {

	public static void main(String[] args) throws IOException {
		String file = "C:\\Users\\Ashmeet\\Documents\\Brijyot\\CSVFolder1\\Book2_Orders.csv",
				txtFile = "C:\\Users\\Ashmeet\\Documents\\Brijyot\\CSVFolder1\\Book2_Orders.txt";
		BufferedReader br = null;// For Read CSV File
		BufferedWriter br2 = null;// For Write a file in which you want to write
		String words = "";
		String separator = ",";// Here you can use , or space according to your csv file

		try {

			br = new BufferedReader(new FileReader(file));
			br2 = new BufferedWriter(new FileWriter(txtFile));
			while ((words = br.readLine()) != null) {
				String[] code = words.split(separator);
				for (int i = 0; i < code.length; i++) {
					br2.write(code[i]);
					br2.write(",");

					System.out.print(code[i]+",");
				}
				System.out.println();
				br2.write("\n");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			br.close();
			br2.close();
		}

	}
}
