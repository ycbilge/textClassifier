package mldm.textClassification.document;

import java.util.List;

/*
 * Yunus Can Bilge
 * 3439349
 * Machine Learning and Data Mining Project
 * June 2013
 */


/*
 * TestDocumentHolder class keeps the content which is read from a test file
 * className(String) keeps the content's class which is currently null 
 * because haven't been calculated yet.
 * words(String list) keeps the tokens that is read from a file
 */public class TestDocumentHolder {
	private String className;
	private List<String> words;

	// private Map<String, Float> probabilitiesOfEachClass;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public String toString() {
		return new String("Class name = " + this.className + " Words = "
				+ this.words);
	}

}
