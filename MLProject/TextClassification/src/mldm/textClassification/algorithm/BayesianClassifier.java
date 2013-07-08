package mldm.textClassification.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mldm.textClassification.document.DocumentHolder;
import mldm.textClassification.document.TestDocumentHolder;

/*
 * Yunus Can Bilge
 * 3439349
 * Machine Learning and Data Mining Project
 * June 2013
 */


/*
 * class that includes Bayesian Classifier algorithm
 */
public class BayesianClassifier {
	/*
	 * list that holds documents (read from the file)
	 */
	private ArrayList<DocumentHolder> documentHolderList;
	/*
	 * map that holds the class prior probability of the class pairs. prior
	 * probabilities of each class
	 */
	private Map<String, Float> priorProbabilities;
	/*
	 * map that holds class word pairs which holds class and all words that it
	 * includes. includes all words of the same class in one map from different
	 * documents(files) each document is in different file so it collects the
	 * one class' words from different documents
	 */
	private Map<String, List<String>> wordsInEachClass;
	/*
	 * map that holds the class and it's words and corresponding number of words
	 * For example; Class A includes the word hello 5 times Class A includes the
	 * word world 3 times Class B includes the word welcome 10 times
	 */
	private Map<String, Map<String, Integer>> wordCountInEachClass;
	/*
	 * stores the number of distinct words from all documents
	 */
	private int vocabValue;
	/*
	 * set that includes distinct words from all documents
	 */
	private Set<String> distinctWords;
	/*
	 * list that includes all words from all documents
	 */
	private List<String> totalWordList;

	/*
	 * map that contains how many document each class have
	 */
	private Map<String, Integer> classValues;
	/*
	 * map that contains the number of words each class have For example; class
	 * A has 13 words; map<A, 13>
	 */
	private Map<String, Integer> totalWordCountInEachClass;
	/*
	 * map that includes posterior probabilities For example; hello from class A
	 * has posterior probability of 0.33 map<A, map<hello, 0.33>, map<world,
	 * 0.11> ...> class, word, probability
	 */
	private Map<String, Map<String, Float>> posteriorProbabilities;
	/*
	 * 
	 */
	private Map<String, Float> testDocClassProb;

	/*
	 * constructor
	 */
	public BayesianClassifier(List<DocumentHolder> docs) {
		testDocClassProb = new HashMap<String, Float>();
		vocabValue = 0;
		distinctWords = new HashSet<String>();
		priorProbabilities = new HashMap<String, Float>();
		classValues = new HashMap<String, Integer>();
		wordsInEachClass = new HashMap<String, List<String>>();
		wordCountInEachClass = new HashMap<String, Map<String, Integer>>();
		documentHolderList = (ArrayList<DocumentHolder>) docs;
		totalWordList = new ArrayList<String>();
		totalWordCountInEachClass = new HashMap<String, Integer>();
		posteriorProbabilities = new HashMap<String, Map<String, Float>>();
	}

	/*
	 * getter and setter methods
	 */
	public ArrayList<DocumentHolder> getDocumentHolderList() {
		return documentHolderList;
	}

	public Map<String, Float> getPriorProbabilities() {
		return priorProbabilities;
	}

	private void setDocumentHolderList(
			ArrayList<DocumentHolder> documentHolderList) {
		this.documentHolderList = documentHolderList;
	}

	public BayesianClassifier(ArrayList<DocumentHolder> documentHolderList) {
		this.setDocumentHolderList(documentHolderList);

	}

	/*
	 * returns the number of documents in a list
	 */
	private int countNumberOfDocuments(List<DocumentHolder> documentHolders) {
		return documentHolders.size();
	}

	/*
	 * calculates the prior probabilities and puts into priorprobabilities map
	 */
	public void calculatePriorProbability(List<DocumentHolder> documentHolders) {
		int sizeOfDocuments = countNumberOfDocuments(documentHolders);

		for (DocumentHolder documentHolder : documentHolders) {
			if (classValues.containsKey(documentHolder.getClassName())) {
				classValues.put(documentHolder.getClassName(),
						classValues.get(documentHolder.getClassName()) + 1);
			} else {
				classValues.put(documentHolder.getClassName(), 1);
			}
		}
		float value;
		for (String key : classValues.keySet()) {
			value = (float) classValues.get(key) / (float) sizeOfDocuments;
			priorProbabilities.put(key, value);
		}
		// System.out.println("Prior probabilities = " + priorProbabilities);
	}

	/*
	 * calculates the prior probabilities for documents has lots of words and
	 * puts logarithms of it into prior probabilities map the difference between
	 * calculatePriorProbabilityForBigDocs method and calculatePriorProbability
	 * method is calculatePriorProbabilityForBigDocs method put's logarithm of
	 * the probability value into prior probabilities map
	 */
	public void calculatePriorProbabilityForBigDocs(
			List<DocumentHolder> documentHolders) {
		int sizeOfDocuments = countNumberOfDocuments(documentHolders);

		for (DocumentHolder documentHolder : documentHolders) {
			if (classValues.containsKey(documentHolder.getClassName())) {
				classValues.put(documentHolder.getClassName(),
						classValues.get(documentHolder.getClassName()) + 1);
			} else {
				classValues.put(documentHolder.getClassName(), 1);
			}
		}
		float value;
		for (String key : classValues.keySet()) {
			value = (float) classValues.get(key) / (float) sizeOfDocuments;
			value = (float) Math.log(value);
			priorProbabilities.put(key, value);
		}
		// System.out.println("Prior probabilities = " + priorProbabilities);
	}

	/*
	 * the method that calculates the number of each word occurrences given list
	 */
	private Map<String, Integer> calculateNumberOfWordOccurrencesInEachClass(
			List<String> words) {
		Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
		for (String word : words) {
			if (wordCountMap.containsKey(word)) {
				wordCountMap.put(word, wordCountMap.get(word) + 1);
			} else {
				wordCountMap.put(word, 1);

			}
		}
		// System.out.println("Word count map = " + wordCountMap);
		return wordCountMap;
	}

	/*
	 * method that puts each class into their corresponding class from documents
	 */

	private void divideWordsAccordingToTheirClass() {

		for (DocumentHolder documentHolder : documentHolderList) {
			if (wordsInEachClass.containsKey(documentHolder.getClassName())) {
				ArrayList<String> val = (ArrayList<String>) wordsInEachClass
						.get(documentHolder.getClassName());
				val.addAll(documentHolder.getWords());
				wordsInEachClass.put(documentHolder.getClassName(), val);
			} else {
				wordsInEachClass.put(documentHolder.getClassName(),
						documentHolder.getWords());
			}
		}
		// System.out.println("Words in each class = " + wordsInEachClass);
	}

	// calculates number of each word occurrences for every class
	private void calculateNumberOfAllWordsInEachClass() {
		for (String key : wordsInEachClass.keySet()) {
			ArrayList<String> wordList = (ArrayList<String>) wordsInEachClass
					.get(key);
			Map<String, Integer> wordCountMap = calculateNumberOfWordOccurrencesInEachClass(wordList);

			wordCountInEachClass.put(key, wordCountMap);
		}
		// System.out.println("Word count in class c = "
		// + calculateNumberOfWordsInClass("c"));
		// System.out.println("Word count in class j = "
		// + calculateNumberOfWordsInClass("j"));
	}

	/*
	 * puts all words into totalWordList
	 */
	private void putAllWordsInAList() {

		for (String key : wordsInEachClass.keySet()) {
			ArrayList<String> wordList = (ArrayList<String>) wordsInEachClass
					.get(key);
			totalWordList.addAll(wordList);
		}

	}

	/*
	 * calculates number of distinct words from totalWordList
	 */
	private void calculateVocabularyValue() {
		for (String word : totalWordList) {
			distinctWords.add(word);
		}
		vocabValue = distinctWords.size();
	}

	/*
	 * following method calculates the number of distinct words in the all of
	 * the documents. Following method calculates the value V in the
	 * corresponding formula; likelihood of a given class: P(w|c) = (count(w,c)+
	 * 1)/(count(c) + V)
	 */
	private void calculateVocabularyAlgorithm() {
		putAllWordsInAList();
		calculateVocabularyValue();
	}

	/*
	 * finds the sum of integer values given a map<String, Integer>
	 */
	private int calculateTotalSizeOfMap(Map<String, Integer> map) {
		int total = 0;
		for (String key : map.keySet()) {
			total += map.get(key);
		}
		return total;

	}

	/*
	 * gets words from each class and puts into totalWordCountInEachClass map.
	 * Therefore it collects words from different documents and puts the ones
	 * that has same class together
	 */
	private void calculateNumberOfWordsInAClass() {
		for (String key : wordCountInEachClass.keySet()) {
			int count = calculateTotalSizeOfMap(wordCountInEachClass.get(key));
			totalWordCountInEachClass.put(key, count);
		}
	}

	/*
	 * precalculations for the calculation of likelihood/posterior probabilities
	 */
	private void preCalculationsForLikelihood() {
		divideWordsAccordingToTheirClass();
		// System.out.println("Words are divided with respect to their class : "
		// + wordsInEachClass);
		calculateNumberOfAllWordsInEachClass();
		System.out.println("Each word count in each class = "
				+ wordCountInEachClass);
		calculateVocabularyAlgorithm();
		System.out.println("Vocabulary value = " + vocabValue);
		calculateNumberOfWordsInAClass();
		System.out.println("Word count in each class = "
				+ totalWordCountInEachClass);
	}

	/*
	 * calculates the value(float) of posterior probability the formula is;
	 * P(w|c) = (count(w,c)+ 1)/(count(c) + V) count(w,c) corresponds
	 * wordCountGivenClass value, count(c) corresponds to
	 * totalNumberOfWordsInClass value V value corresponds to vocabValue as
	 * described earlier in the page.
	 */
	private float calculatePosteriorProbValues(int wordCountGivenClass,
			int totalNumberOfWordsInAllClass) {
		float result = (float) (wordCountGivenClass + 1)
				/ (float) (totalNumberOfWordsInAllClass + vocabValue);
		return result;
	}

	/*
	 * method that calculates likelihood/posterior probabilities for every word
	 */
	private void calculateLikehood() {
		Map<String, Integer> wordMap;
		Map<String, Float> map;
		int currentwordCountValueGivenClass;
		int wordCountInClass;
		for (String className : wordCountInEachClass.keySet()) {
			wordMap = wordCountInEachClass.get(className);
			map = new HashMap<String, Float>();
			wordCountInClass = totalWordCountInEachClass.get(className);// count(c)
			for (String word : wordMap.keySet()) {
				currentwordCountValueGivenClass = wordMap.get(word);// count(w,c)
				float posteriorProb = calculatePosteriorProbValues(
						currentwordCountValueGivenClass, wordCountInClass);

				map.put(word, posteriorProb);
				posteriorProbabilities.put(className, map);
			}
		}
	}

	/*
	 * method that calculates likelihood/posterior probabilities for every word
	 * for big docs it just stores natural logarithm of the probability
	 */
	private void calculateLikehoodForBigDocs() {
		Map<String, Integer> wordMap;
		Map<String, Float> map;
		int currentwordCountValueGivenClass;
		int wordCountInClass;
		for (String className : wordCountInEachClass.keySet()) {
			wordMap = wordCountInEachClass.get(className);
			map = new HashMap<String, Float>();
			wordCountInClass = totalWordCountInEachClass.get(className);// count(c)
			for (String word : wordMap.keySet()) {
				currentwordCountValueGivenClass = wordMap.get(word);// count(w,c)
				float posteriorProb = calculatePosteriorProbValues(
						currentwordCountValueGivenClass, wordCountInClass);
//				System.out.println("Posterior prob  of " + word + " in class "
//						+ className + " = " + posteriorProb);
				posteriorProb = (float) Math.log(posteriorProb);
//				System.out.println("After log Posterior prob  of " + word
//						+ " in class " + className + " = " + posteriorProb);
				map.put(word, posteriorProb);
				posteriorProbabilities.put(className, map);
			}
		}
	}

	/*
	 * following method is an algorithm for calculation of prior and posterior
	 */
	private void calculatePriorAndPosteriorProbabilitiesAlgorithm() {
		calculatePriorProbability(documentHolderList);
		System.out.println("Prior probability calculated for each class : "
				+ priorProbabilities);
		preCalculationsForLikelihood();
		calculateLikehood();
		System.out.println("Posterior prob = " + posteriorProbabilities);
	}

	/*
	 * following method is an algorithm for calculation of prior and posterior
	 * this method stores the logarithm of probabilities because of the lots of
	 * words
	 */
	private void calculatePriorAndPosteriorProbabilitiesAlgorithmForBigDocs() {
		calculatePriorProbabilityForBigDocs(documentHolderList);
		// System.out.println("Prior probability calculated for each class : "
		// + priorProbabilities);
		preCalculationsForLikelihood();
		calculateLikehoodForBigDocs();
		// System.out.println("Posterior prob = " + posteriorProbabilities);
	}

	/*
	 * following method determines classifier by selecting maximum probability
	 * value from calculated probability values.
	 */
	private void tellTheClassifier() {
		// float max = Float.MIN_VALUE;
		float max = -999999999f;
		String ourDecision = null;
		float prob;
		// System.out.println("testDoc class prob = " + testDocClassProb);
		for (String key : testDocClassProb.keySet()) {
			prob = testDocClassProb.get(key);
			System.out.println("prob = " + prob);
			// System.out.println("max = " + max);
			if (prob > max) {
				max = prob;
				ourDecision = key;

			}
		}

		// System.out.println("max = " + max);
		if (ourDecision != null) {
			System.out.println("We think your text belongs to class "
					+ ourDecision.toUpperCase() + " with probabilitiy of "
					+ max);
		} else {
			System.out.println("Something went wrong!!");
		}
	}

	private void tellTheClassifierForBigDocs() {
		// float max = Float.MIN_VALUE;
		float max = -999999999f;
		String ourDecision = null;
		float prob;
		 System.out.println("testDoc class prob = " + testDocClassProb);
		for (String key : testDocClassProb.keySet()) {
			System.out.println("key = " + key);
			prob = testDocClassProb.get(key);
//			System.out.println("prob = " + prob);
			// System.out.println("max = " + max);
			
			
			if (prob > max) {
				max = prob;
				ourDecision = key;

			}
		}

		// System.out.println("max = " + max);
		if (ourDecision != null) {
			System.out.println("We think your text belongs to class "
					+ ourDecision.toUpperCase() + " with probabilitiy of "
					+ max);
		} else {
			System.out.println("Something went wrong!!");
		}
	}

	/*
	 * multiplies prior and posterior probabilities and puts into map of
	 * testDocWords which includes class and it's probability calculated from
	 * multiplying prior and posterior probabilities
	 */
	private void calculateResult(TestDocumentHolder testDocument) {
		float prob;
		float fval;
		List<String> testDocWords = testDocument.getWords();
		for (String key : wordCountInEachClass.keySet()) {
			prob = 1;
			prob *= priorProbabilities.get(key);// get priorProbability and
												// multiply
			// System.out.println("Prior prob. of class " + key + " is " +
			// prob);
			Map<String, Float> map = posteriorProbabilities.get(key);// get
																		// posteriorProb
			// System.out.println(map);
			for (String word : testDocWords) {
				// System.out.println("word = " + word + " map = " + map);
				if (!map.containsKey(word)) {
					fval = ((float) 1 / (float) (totalWordCountInEachClass
							.get(key) + vocabValue));
					// System.out.println("fval = " + fval);
					prob *= fval;/*
								 * if current class does not include that word
								 * that O for that word count
								 */

				} else {
					fval = map.get(word);
					// System.out.println("val = " + fval);
					prob *= fval;// multiply prior probability and
					// System.out.println("prob of class " + key + " prob = "
					// + prob); // posterior probability
				}
				// System.out.println("prob of class " + key + " prob = " +
				// prob);
			}
			testDocClassProb.put(key, prob);
		}
		System.out.println(testDocClassProb);
		tellTheClassifier();
	}

	/*
	 * add prior and posterior probabilities and puts into map of testDocWords
	 * which includes class and it's probability calculated from adding prior
	 * and posterior probabilities For big documents which has lots of words
	 * instead of multiplying I am adding the probabilities
	 */
	private void calculateResultForBigDocs(TestDocumentHolder testDocument) {
		float prob;
		float fval;

		List<String> testDocWords = testDocument.getWords();
		for (String key : wordCountInEachClass.keySet()) {
			prob = 0;
//			System.out.println("prob = " +prob);
			prob += priorProbabilities.get(key);
//			System.out.println("prob = " +prob + " priorProb = "+ priorProbabilities.get(key) 	 +" class = " + key);
			Map<String, Float> map = posteriorProbabilities.get(key);
			for (String word : testDocWords) {

				if (!map.containsKey(word)) {
					fval = ((float) 1 / (float) (totalWordCountInEachClass
							.get(key) + vocabValue));
					fval = (float) Math.log(fval);
					prob += fval;
					/*
					 * if current class does not include that word that O for
					 * that word count
					 */
//					System.out.println("word from 1 = "+ word + "prob = " + fval);
				} else {
					fval = map.get(word);
//					System.out.println("word from 2 = "+ word);
					prob += fval;
				}

//				System.out.println("class = " + key + "word = " + word + " Fval = " + fval
//						+ " prob  = " + prob);
			}
			testDocClassProb.put(key, prob);
		}
//		System.out.println(testDocClassProb);
		tellTheClassifierForBigDocs();
	}

	/*
	 * an algorithm that calculates prior and posterior probabilities and
	 * calculates result with respect to them. following method will be used
	 * relatively small sized documents
	 */

	public void BayesianClassifierAlgorithm(TestDocumentHolder testDocument) {

		calculatePriorAndPosteriorProbabilitiesAlgorithm();
		calculateResult(testDocument);

	}

	/*
	 * an algorithm that calculates prior and posterior probabilities and
	 * calculates result with respect to them. Following method will be used
	 * relatively big sized documents
	 */

	public void BayesianClassifierAlgorithmForBigDocs(
			TestDocumentHolder testDocument) {

		calculatePriorAndPosteriorProbabilitiesAlgorithmForBigDocs();
		calculateResultForBigDocs(testDocument);

	}
}
