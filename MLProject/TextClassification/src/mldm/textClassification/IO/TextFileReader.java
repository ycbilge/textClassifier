package mldm.textClassification.IO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * Yunus Can Bilge
 * 3439349
 * Machine Learning and Data Mining Project
 * June 2013
 */


//DO NOT FORGET TO CLOSE FILE READER

/*
 * Every file corresponds to a document what it means is first line includes it's class name and 
 * the words at the rest
 * If the file is for the training, then it must include it's class name in it's first line
 * and the rest of it must consists of the words
 * If the file is for the test purposes than it's first line must be left empty and the other lines must
 * consists of the words. The reason I chose to left it empty because I was planning to put the class name 
 * at the first line after determining the class name.
 */
/*
 * reads the file content
 */
public class TextFileReader {

	FileInputStream fstream;
	DataInputStream in;
	BufferedReader br;

	public TextFileReader(String fileName) {

		try {
			fstream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + e.getMessage());
		}
		in = new DataInputStream(fstream);
		br = new BufferedReader(new InputStreamReader(in));

	}

	/*
	 * reads a line from a file
	 */
	public String readALine() {
		String strLine = null;

		try {
			strLine = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strLine;
	}

	public void closeDataInputStream() {
		try {
			in.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

}
