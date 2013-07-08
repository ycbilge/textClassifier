package mldm.textClassification.document;

import java.util.List;

/*
 * Yunus Can Bilge
 * 3439349
 * Machine Learning and Data Mining Project
 * June 2013
 */

/*
 * DocumentHolder class keeps the content which is read from a file
 * className(String) keeps the content's class
 * words(String list) keeps the tokens that is read from a file
 */

public class DocumentHolder {
	private String className;
	private List<String> words;

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
		return new String("Class name = " + this.className + " Words = " +this.words);
	}
}
 