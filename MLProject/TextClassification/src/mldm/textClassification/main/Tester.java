package mldm.textClassification.main;

import java.util.ArrayList;
import java.util.List;

import mldm.textClassification.IO.TextFileReader;
import mldm.textClassification.algorithm.BayesianClassifier;
import mldm.textClassification.document.DocumentHolder;
import mldm.textClassification.document.TestDocumentHolder;

/*
 * Yunus Can Bilge
 * 3439349
 * Machine Learning and Data Mining Project
 * June 2013
 */


/*
 * Tester class
 */
public class Tester {

	/*
	 * following method removes , . ! ? : \ ( ) etc from word if word includes
	 * it
	 */
	private String removeSomeMarks(String word) {
		String updatedWord;
		// System.out.println("before update = " + word);
		updatedWord = word.replace(",", "");
		updatedWord = updatedWord.replace(".", "");

		updatedWord = updatedWord.replace("!", "");
		updatedWord = updatedWord.replace("?", "");
		updatedWord = updatedWord.replace(":", "");
		updatedWord = updatedWord.replace("\"", "");
		updatedWord = updatedWord.replace("(", "");
		updatedWord = updatedWord.replace(")", "");
		updatedWord = updatedWord.replace("-", "");
		updatedWord = updatedWord.replace("|", "");
		updatedWord = updatedWord.replace(";", "");
		updatedWord = updatedWord.replace("*", "");
		updatedWord = updatedWord.replace(">", "");
		updatedWord = updatedWord.replace(">>", "");
		updatedWord = updatedWord.replace(" ", "");
		// System.out.println("after update = " + updatedWord);
		return updatedWord;

	}

	/*
	 * following method converts all words into lowercase except "I" It is done
	 * because HELLO and hello is the same word and shouldn't be calculated
	 * seperately
	 */

	private String makeAllWordsLowercase(String removedMark) {
		if (!removedMark.equals("I"))
			return removedMark.toLowerCase();
		else
			return removedMark;
	}

	/*
	 * reads file content and puts into DocumentHolder object
	 */
	public void readAndPutToHolderClass(DocumentHolder documentHolder,
			TextFileReader textFileReader) {
		Boolean firstLineBool = true;
		String lineWords;
		String classValue = null;
		List<String> words = new ArrayList<String>();
		String removedMark = null;
		while ((lineWords = textFileReader.readALine()) != null) {
			if (firstLineBool) {
				classValue = lineWords;
				firstLineBool = false;
			} else {

				String[] rows = lineWords.split(" ");
				for (int i = 0; i < rows.length; i++) {
					// System.out.println("before update = " + rows[i]);
					removedMark = removeSomeMarks(rows[i]);
					removedMark = makeAllWordsLowercase(removedMark);
					// System.out.println("after update = " + removedMark);
					if (!removedMark.equals(""))
						words.add(removedMark);
					// words.add(rows[i]);
				}
			}
		}
		documentHolder.setClassName(classValue);
		documentHolder.setWords(words);
		textFileReader.closeDataInputStream();
		// System.out.println(documentHolder);

	}

	/*
	 * reads file content and puts into TestDocumentHolder object
	 */
	public void readAndPutToTestHolderClass(TestDocumentHolder documentHolder,
			TextFileReader textFileReader) {
		Boolean firstLineBool = true;
		String lineWords;
		String classValue = null;
		String removedMark = null;
		List<String> words = new ArrayList<String>();
		while ((lineWords = textFileReader.readALine()) != null) {
			if (firstLineBool) {
				// classValue = lineWords;
				firstLineBool = false;
			} else {
				String[] rows = lineWords.split(" ");
				for (int i = 0; i < rows.length; i++) {
					// System.out.println("before update = " + rows[i]);
					removedMark = removeSomeMarks(rows[i]);
					removedMark = makeAllWordsLowercase(removedMark);
					// System.out.println("after update = " + removedMark);
					// System.out.println("word = " + removedMark);
					if (!removedMark.equals(""))
						words.add(removedMark);
					// words.add(rows[i]);
				}
			}
		}
		documentHolder.setClassName(classValue);
		documentHolder.setWords(words);
		textFileReader.closeDataInputStream();
		// System.out.println(documentHolder);
	}

	// OK WORKS WELL
	/*
	 * test1 which includes ; training data : c1 = class c, words: Chienese
	 * Chinese Shangai c2 = class c, words: Chinese Macao c3 = class c, words:
	 * Chinese Beijing Chinese j1 = class j, words: Tokyo Japan Chinese and
	 * test data : Chinese Chinese Chinese Tokyo Japan
	 * 
	 * Test 1 should return class c as a result
	 */
	public void createTest1() {
		DocumentHolder documentHolder1 = new DocumentHolder();
		TextFileReader textfileReader1 = new TextFileReader(
				"test1DataSets/c1.txt");
		DocumentHolder documentHolder2 = new DocumentHolder();
		TextFileReader textfileReader2 = new TextFileReader(
				"test1DataSets/c2.txt");
		DocumentHolder documentHolder3 = new DocumentHolder();
		TextFileReader textfileReader3 = new TextFileReader(
				"test1DataSets/c3.txt");
		DocumentHolder documentHolder4 = new DocumentHolder();
		TextFileReader textfileReader4 = new TextFileReader(
				"test1DataSets/j1.txt");

		TestDocumentHolder testDocumentHolder = new TestDocumentHolder();
		TextFileReader textfileReader5 = new TextFileReader(
				"test1DataSets/test.txt");

		readAndPutToHolderClass(documentHolder1, textfileReader1);
		readAndPutToHolderClass(documentHolder2, textfileReader2);
		readAndPutToHolderClass(documentHolder3, textfileReader3);
		readAndPutToHolderClass(documentHolder4, textfileReader4);
		readAndPutToTestHolderClass(testDocumentHolder, textfileReader5);
		System.out.println(documentHolder1);
		System.out.println(documentHolder2);
		System.out.println(documentHolder3);
		System.out.println(documentHolder4);
		System.out.println(testDocumentHolder);
		List<DocumentHolder> documentHolderList = new ArrayList<DocumentHolder>();
		documentHolderList.add(documentHolder1);
		documentHolderList.add(documentHolder2);
		documentHolderList.add(documentHolder3);
		documentHolderList.add(documentHolder4);

		BayesianClassifier bayesianClassifier = new BayesianClassifier(
				documentHolderList);
		bayesianClassifier.BayesianClassifierAlgorithm(testDocumentHolder);
//		 bayesianClassifier
//		 .BayesianClassifierAlgorithmForBigDocs(testDocumentHolder);

	}

	/*
	 * test2 which includes ; training data : e1 = class Yes, words: Red Sports
	 * Domestic e2 = class No, words: Red Sports Domestic e3 = class Yes, words:
	 * Red Sports Domestic e4 = class No, words: Yellow Sports Domestic e5 =
	 * class Yes, words: Yellow Sports Imported e6 = class No, words: Yellow SUV
	 * Imported e7 = class Yes, words: Yellow SUV Imported e8 = class No, words:
	 * Yellow SUV Domestic e9 = class No, words: Red SUV Imported e10 = class
	 * Yes, words: Red Sports Imported and test data : Red Domestic SUV
	 * 
	 * Test 2 should return class NO as a result
	 */
	public void createTest2() {
		DocumentHolder documentHolder1 = new DocumentHolder();
		TextFileReader textfileReader1 = new TextFileReader(
				"test2DataSets/e1.txt");
		DocumentHolder documentHolder2 = new DocumentHolder();
		TextFileReader textfileReader2 = new TextFileReader(
				"test2DataSets/e2.txt");
		DocumentHolder documentHolder3 = new DocumentHolder();
		TextFileReader textfileReader3 = new TextFileReader(
				"test2DataSets/e3.txt");
		DocumentHolder documentHolder4 = new DocumentHolder();
		TextFileReader textfileReader4 = new TextFileReader(
				"test2DataSets/e4.txt");
		DocumentHolder documentHolder5 = new DocumentHolder();
		TextFileReader textfileReader5 = new TextFileReader(
				"test2DataSets/e5.txt");
		DocumentHolder documentHolder6 = new DocumentHolder();
		TextFileReader textfileReader6 = new TextFileReader(
				"test2DataSets/e6.txt");
		DocumentHolder documentHolder7 = new DocumentHolder();
		TextFileReader textfileReader7 = new TextFileReader(
				"test2DataSets/e7.txt");
		DocumentHolder documentHolder8 = new DocumentHolder();
		TextFileReader textfileReader8 = new TextFileReader(
				"test2DataSets/e8.txt");
		DocumentHolder documentHolder9 = new DocumentHolder();
		TextFileReader textfileReader9 = new TextFileReader(
				"test2DataSets/e9.txt");
		DocumentHolder documentHolder10 = new DocumentHolder();
		TextFileReader textfileReader10 = new TextFileReader(
				"test2DataSets/e10.txt");

		TestDocumentHolder testDocumentHolder = new TestDocumentHolder();
		TextFileReader test = new TextFileReader("test2DataSets/tt.txt");

		readAndPutToHolderClass(documentHolder1, textfileReader1);
		readAndPutToHolderClass(documentHolder2, textfileReader2);
		readAndPutToHolderClass(documentHolder3, textfileReader3);
		readAndPutToHolderClass(documentHolder4, textfileReader4);
		readAndPutToHolderClass(documentHolder5, textfileReader5);
		readAndPutToHolderClass(documentHolder6, textfileReader6);
		readAndPutToHolderClass(documentHolder7, textfileReader7);
		readAndPutToHolderClass(documentHolder8, textfileReader8);
		readAndPutToHolderClass(documentHolder9, textfileReader9);
		readAndPutToHolderClass(documentHolder10, textfileReader10);

		readAndPutToTestHolderClass(testDocumentHolder, test);
		List<DocumentHolder> documentHolderList = new ArrayList<DocumentHolder>();
		documentHolderList.add(documentHolder1);
		documentHolderList.add(documentHolder2);
		documentHolderList.add(documentHolder3);
		documentHolderList.add(documentHolder4);
		documentHolderList.add(documentHolder5);
		documentHolderList.add(documentHolder6);
		documentHolderList.add(documentHolder7);
		documentHolderList.add(documentHolder8);
		documentHolderList.add(documentHolder9);
		documentHolderList.add(documentHolder10);

		System.out.println("Input list : " + documentHolderList);

		BayesianClassifier bayesianClassifier = new BayesianClassifier(
				documentHolderList);
		bayesianClassifier.BayesianClassifierAlgorithm(testDocumentHolder);
//		 bayesianClassifier.BayesianClassifierAlgorithmForBigDocs(testDocumentHolder);

	}
//
//	private List<DocumentHolder> putForTest3(String name1, String name2,
//			String name3, String name4) {
//		DocumentHolder documentHolder1 = new DocumentHolder();
//		TextFileReader textfileReader1 = new TextFileReader(name1);
//		DocumentHolder documentHolder2 = new DocumentHolder();
//		TextFileReader textfileReader2 = new TextFileReader(name2);
//
//		DocumentHolder documentHolder3 = new DocumentHolder();
//		TextFileReader textfileReader3 = new TextFileReader(name3);
//		DocumentHolder documentHolder4 = new DocumentHolder();
//		TextFileReader textfileReader4 = new TextFileReader(name4);
//
//		readAndPutToHolderClass(documentHolder1, textfileReader1);
//		readAndPutToHolderClass(documentHolder2, textfileReader2);
//		readAndPutToHolderClass(documentHolder3, textfileReader3);
//		readAndPutToHolderClass(documentHolder4, textfileReader4);
//
//		List<DocumentHolder> documentHolderList = new ArrayList<DocumentHolder>();
//		documentHolderList.add(documentHolder1);
//		documentHolderList.add(documentHolder2);
//		documentHolderList.add(documentHolder3);
//		documentHolderList.add(documentHolder4);
//		return documentHolderList;
//	}

	/*
	 * test 3 includes some the data from 20news dataset files are updated in
	 * order to harmonize with my implementation
	 */
//	public void createTest3() {
//		TestDocumentHolder testDocumentHolder = new TestDocumentHolder();
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/atest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/btest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/crtest");
//
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/ctest");
//		 TextFileReader test = new
//		 TextFileReader("test3DataSets/testset/etest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/ftest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/gtest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/gutest");
////		 TextFileReader test = new TextFileReader(
////		 "test3DataSets/testset/medtest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/metest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/mhtest");
////		 TextFileReader test = new TextFileReader(
////		 "test3DataSets/testset/motortest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/rtest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/stest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/wmtest");
////		 TextFileReader test = new
////		 TextFileReader("test3DataSets/testset/wxtest");
////		TextFileReader test = new TextFileReader("test3DataSets/testset/autest");
////		TextFileReader test = new TextFileReader("test3DataSets/testset/htest");
////		TextFileReader test = new TextFileReader("test3DataSets/testset/poltest");
////		TextFileReader test = new TextFileReader("test3DataSets/testset/pchtest");
//		readAndPutToTestHolderClass(testDocumentHolder, test);
//		List<DocumentHolder> documentHolderList1 = putForTest3(
//				"test3DataSets/autos/au1", "test3DataSets/autos/au2",
//				"test3DataSets/autos/au3", "test3DataSets/autos/au4");
//
//		List<DocumentHolder> documentHolderList2 = putForTest3(
//				"test3DataSets/atheism/a1", "test3DataSets/atheism/a2",
//				"test3DataSets/atheism/a3", "test3DataSets/atheism/a4");
//
//		List<DocumentHolder> documentHolderList3 = putForTest3(
//				"test3DataSets/baseball/b1", "test3DataSets/baseball/b2",
//				"test3DataSets/baseball/b3", "test3DataSets/baseball/b4");
//
//		List<DocumentHolder> documentHolderList4 = putForTest3(
//				"test3DataSets/christian/c1", "test3DataSets/christian/c2",
//				"test3DataSets/christian/c3", "test3DataSets/christian/c4");
//
//		List<DocumentHolder> documentHolderList5 = putForTest3(
//				"test3DataSets/crypt/cr1", "test3DataSets/crypt/cr2",
//				"test3DataSets/crypt/cr3", "test3DataSets/crypt/cr4");
//
//		List<DocumentHolder> documentHolderList6 = putForTest3(
//				"test3DataSets/electronics/e1", "test3DataSets/electronics/e2",
//				"test3DataSets/electronics/e3", "test3DataSets/electronics/e4");
//
//		List<DocumentHolder> documentHolderList7 = putForTest3(
//				"test3DataSets/forsale/f1", "test3DataSets/forsale/f2",
//				"test3DataSets/forsale/f3", "test3DataSets/forsale/f4");
//
//		List<DocumentHolder> documentHolderList8 = putForTest3(
//				"test3DataSets/graphics/g1", "test3DataSets/graphics/g2",
//				"test3DataSets/graphics/g3", "test3DataSets/graphics/g4");
//
//		List<DocumentHolder> documentHolderList9 = putForTest3(
//				"test3DataSets/guns/gu1", "test3DataSets/guns/gu2",
//				"test3DataSets/guns/gu3", "test3DataSets/guns/gu4");
//		List<DocumentHolder> documentHolderList10 = putForTest3(
//				"test3DataSets/hockey/h1", "test3DataSets/hockey/h2",
//				"test3DataSets/hockey/h3", "test3DataSets/hockey/h4");
//
//		List<DocumentHolder> documentHolderList11 = putForTest3(
//				"test3DataSets/machardware/mh1",
//				"test3DataSets/machardware/mh2",
//				"test3DataSets/machardware/mh3",
//				"test3DataSets/machardware/mh4");
//
//		List<DocumentHolder> documentHolderList12 = putForTest3(
//				"test3DataSets/med/med1", "test3DataSets/med/med2",
//				"test3DataSets/med/med3", "test3DataSets/med/med4");
//
//		List<DocumentHolder> documentHolderList13 = putForTest3(
//				"test3DataSets/mideast/me1", "test3DataSets/mideast/me2",
//				"test3DataSets/mideast/me3", "test3DataSets/mideast/me4");
//
//		List<DocumentHolder> documentHolderList14 = putForTest3(
//				"test3DataSets/motorcycles/motor1",
//				"test3DataSets/motorcycles/motor2",
//				"test3DataSets/motorcycles/motor3",
//				"test3DataSets/motorcycles/motor4");
//
//		List<DocumentHolder> documentHolderList15 = putForTest3(
//				"test3DataSets/pchardware/pch1",
//				"test3DataSets/pchardware/pch2",
//				"test3DataSets/pchardware/pch3",
//				"test3DataSets/pchardware/pch4");
//
//		List<DocumentHolder> documentHolderList16 = putForTest3(
//				"test3DataSets/politics/pol1", "test3DataSets/politics/pol2",
//				"test3DataSets/politics/pol3", "test3DataSets/politics/pol4");
//		List<DocumentHolder> documentHolderList17 = putForTest3(
//				"test3DataSets/religion/r1", "test3DataSets/religion/r2",
//				"test3DataSets/religion/r3", "test3DataSets/religion/r4");
//
//		List<DocumentHolder> documentHolderList18 = putForTest3(
//				"test3DataSets/space/s1", "test3DataSets/space/s2",
//				"test3DataSets/space/s3", "test3DataSets/space/s4");
//
//		List<DocumentHolder> documentHolderList19 = putForTest3(
//				"test3DataSets/windowsmisc/wm1",
//				"test3DataSets/windowsmisc/wm2",
//				"test3DataSets/windowsmisc/wm3",
//				"test3DataSets/windowsmisc/wm4");
//		List<DocumentHolder> documentHolderList20 = putForTest3(
//				"test3DataSets/windowsx/wx1", "test3DataSets/windowsx/wx2",
//				"test3DataSets/windowsx/wx3", "test3DataSets/windowsx/wx4");
//
//		List<DocumentHolder> documentHolderTotal = new ArrayList<DocumentHolder>();
//		documentHolderTotal.addAll(documentHolderList1);
//		documentHolderTotal.addAll(documentHolderList2);
//		documentHolderTotal.addAll(documentHolderList3);
//		documentHolderTotal.addAll(documentHolderList4);
//		documentHolderTotal.addAll(documentHolderList5);
//		documentHolderTotal.addAll(documentHolderList6);
//		documentHolderTotal.addAll(documentHolderList7);
//		documentHolderTotal.addAll(documentHolderList8);
//		documentHolderTotal.addAll(documentHolderList9);
//		documentHolderTotal.addAll(documentHolderList10);
//		documentHolderTotal.addAll(documentHolderList11);
//		documentHolderTotal.addAll(documentHolderList12);
//		documentHolderTotal.addAll(documentHolderList13);
//		documentHolderTotal.addAll(documentHolderList14);
//		documentHolderTotal.addAll(documentHolderList15);
//		documentHolderTotal.addAll(documentHolderList16);
//		documentHolderTotal.addAll(documentHolderList17);
//		documentHolderTotal.addAll(documentHolderList18);
//		documentHolderTotal.addAll(documentHolderList19);
//		documentHolderTotal.addAll(documentHolderList20);
//
//		// documentHolderTotal.addAll(documentHolderList9);
//		System.out.println("Input list : " + documentHolderTotal);
//
//		System.out.println("Test data : " + testDocumentHolder.toString());
//		BayesianClassifier bayesianClassifier = new BayesianClassifier(
//				documentHolderTotal);
//		 bayesianClassifier
//		 .BayesianClassifierAlgorithmForBigDocs(testDocumentHolder);
////		bayesianClassifier.BayesianClassifierAlgorithm(testDocumentHolder);
//
//	}

	/*
	 * test4 which includes ; training data : s1 = class ham, words : good e2 =
	 * class ham, words: very good e3 = class spam, words: bad e4 = class spam,
	 * words: very bad e5 = class spam, words: very bad very bad and test data :
	 * good bad very bad
	 * 
	 * Test 4 should return class spam as a result
	 */
	public void createTest4() {
		DocumentHolder documentHolder1 = new DocumentHolder();
		TextFileReader textfileReader1 = new TextFileReader(
				"test4DataSets/s1.txt");
		DocumentHolder documentHolder2 = new DocumentHolder();
		TextFileReader textfileReader2 = new TextFileReader(
				"test4DataSets/s2.txt");
		DocumentHolder documentHolder3 = new DocumentHolder();
		TextFileReader textfileReader3 = new TextFileReader(
				"test4DataSets/s3.txt");
		DocumentHolder documentHolder4 = new DocumentHolder();
		TextFileReader textfileReader4 = new TextFileReader(
				"test4DataSets/s4.txt");
		DocumentHolder documentHolder5 = new DocumentHolder();
		TextFileReader textfileReader5 = new TextFileReader(
				"test4DataSets/s5.txt");

		TestDocumentHolder testDocumentHolder = new TestDocumentHolder();
		TextFileReader test = new TextFileReader("test4DataSets/ss.txt");

		readAndPutToHolderClass(documentHolder1, textfileReader1);
		readAndPutToHolderClass(documentHolder2, textfileReader2);
		readAndPutToHolderClass(documentHolder3, textfileReader3);
		readAndPutToHolderClass(documentHolder4, textfileReader4);
		readAndPutToHolderClass(documentHolder5, textfileReader5);

		readAndPutToTestHolderClass(testDocumentHolder, test);
		List<DocumentHolder> documentHolderList = new ArrayList<DocumentHolder>();
		documentHolderList.add(documentHolder1);
		documentHolderList.add(documentHolder2);
		documentHolderList.add(documentHolder3);
		documentHolderList.add(documentHolder4);
		documentHolderList.add(documentHolder5);

		System.out.println("Input list : " + documentHolderList);
		BayesianClassifier bayesianClassifier = new BayesianClassifier(
				documentHolderList);
		bayesianClassifier.BayesianClassifierAlgorithm(testDocumentHolder);
//		 bayesianClassifier
//		 .BayesianClassifierAlgorithmForBigDocs(testDocumentHolder);

	}

	/*
	 * 
	 */

	// 10 correct classfy out of 12
	public void createTest5() {
		DocumentHolder documentHolder1 = new DocumentHolder();
		TextFileReader textfileReader1 = new TextFileReader(
				"test5DataSets/c1.txt");
		DocumentHolder documentHolder2 = new DocumentHolder();
		TextFileReader textfileReader2 = new TextFileReader(
				"test5DataSets/c2.txt");
		DocumentHolder documentHolder3 = new DocumentHolder();
		TextFileReader textfileReader3 = new TextFileReader(
				"test5DataSets/c3.txt");
		DocumentHolder documentHolder4 = new DocumentHolder();
		TextFileReader textfileReader4 = new TextFileReader(
				"test5DataSets/c4.txt");
		DocumentHolder documentHolder5 = new DocumentHolder();
		TextFileReader textfileReader5 = new TextFileReader(
				"test5DataSets/c5.txt");
		DocumentHolder documentHolder6 = new DocumentHolder();
		TextFileReader textfileReader6 = new TextFileReader(
				"test5DataSets/c6.txt");
		DocumentHolder documentHolder7 = new DocumentHolder();
		TextFileReader textfileReader7 = new TextFileReader(
				"test5DataSets/c7.txt");
		DocumentHolder documentHolder8 = new DocumentHolder();
		TextFileReader textfileReader8 = new TextFileReader(
				"test5DataSets/c8.txt");
		DocumentHolder documentHolder9 = new DocumentHolder();
		TextFileReader textfileReader9 = new TextFileReader(
				"test5DataSets/c9.txt");
		/*
		 * returned cat true bigDocsalg returned cat true
		 */
		TestDocumentHolder testDocumentHolder = new TestDocumentHolder();
//		 TextFileReader test = new TextFileReader("test5DataSets/t1.txt");

		/*
		 * returned cat true bigDocsalg returned cat true
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t2.txt");
		/*
		 * returned cat true bigDocsalg returned cat true
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t3.txt");

		/*
		 * returned cat true bigDocsalg returned cat true
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t4.txt");

		/*
		 * returned dog false true classificatigon is cat bigDocsalg returned
		 * dog false
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t5.txt");

		/*
		 * returned cat true bigDocsalg returned cat true
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t6.txt");

		/*
		 * returned cat true bigDocsalg returned cat true
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t7.txt");
		/*
		 * returned dog true bigDocsalg returned dog true
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t8.txt");

		/*
		 * returned dog true bigDocsalg returned dog true
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t9.txt");

		/*
		 * returned dog true bigDocsalg returned dog true
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t10.txt");

		/*
		 * returned dog true bigDocsalg returned dog true
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t11.txt");

		/*
		 * returned cat false should be classified as dog returns false stop
		 * wordler olmadigi icin maybe and the other one as well bigDocsalg
		 * returned cat false
		 */
//		 TextFileReader test = new TextFileReader("test5DataSets/t12.txt");
		/*
		 * returned dog true bigDocsalg returned dog true
		 */
		 TextFileReader test = new TextFileReader("test5DataSets/t13.txt");
		/*
		 * returned dog true bigDocsalg returned dog true probabilities are so
		 * close
		 */
//		TextFileReader test = new TextFileReader("test5DataSets/t14.txt");
		readAndPutToHolderClass(documentHolder1, textfileReader1);
		readAndPutToHolderClass(documentHolder2, textfileReader2);
		readAndPutToHolderClass(documentHolder3, textfileReader3);
		readAndPutToHolderClass(documentHolder4, textfileReader4);
		readAndPutToHolderClass(documentHolder5, textfileReader5);
		readAndPutToHolderClass(documentHolder6, textfileReader6);
		readAndPutToHolderClass(documentHolder7, textfileReader7);
		readAndPutToHolderClass(documentHolder8, textfileReader8);
		readAndPutToHolderClass(documentHolder9, textfileReader9);

		readAndPutToTestHolderClass(testDocumentHolder, test);
		List<DocumentHolder> documentHolderList = new ArrayList<DocumentHolder>();
		documentHolderList.add(documentHolder1);
		documentHolderList.add(documentHolder2);
		documentHolderList.add(documentHolder3);
		documentHolderList.add(documentHolder4);
		documentHolderList.add(documentHolder5);
		documentHolderList.add(documentHolder6);
		documentHolderList.add(documentHolder7);
		documentHolderList.add(documentHolder8);
		documentHolderList.add(documentHolder9);
		System.out.println("Test doc = " + testDocumentHolder.toString());
		System.out.println("Input list : " + documentHolderList);
		BayesianClassifier bayesianClassifier = new BayesianClassifier(
				documentHolderList);
		bayesianClassifier.BayesianClassifierAlgorithm(testDocumentHolder);
//		 bayesianClassifier.BayesianClassifierAlgorithmForBigDocs(testDocumentHolder);

	}

	public static void main(String[] args) {
		Tester tester = new Tester();
		/*
		 * ok for normal alg ok for bigDocsalg
		 */
//		 tester.createTest1();
		/*
		 * ok for normal alg ok for bigDocsalg
		 */
//		 tester.createTest2(); //ok
		/*
		 * not ok for normal alg
		 * ok for big docs alg
		 */
//		tester.createTest3(); // 
		/*
		 * ok for normal alg not ok for bigDocsalg
		 */
//		 tester.createTest4(); //ok
		/*
		 * 12 true classification out of 14 tests for normal alg so ok 12 true
		 * classification out of 14 tests for bigDocs alg so ok
		 */
		 tester.createTest5();

	}
}
