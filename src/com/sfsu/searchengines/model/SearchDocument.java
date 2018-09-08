/**
 * 
 */
package com.sfsu.searchengines.model;

import java.util.Comparator;
import java.util.List;

/**
 * @author Supritha Amudhu
 *
 */

public class SearchDocument {
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + searchDocumentNumber;
		result = prime * result + ((searchDocumentWord == null) ? 0 : searchDocumentWord.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchDocument other = (SearchDocument) obj;
		if (searchDocumentNumber != other.searchDocumentNumber)
			return false;
		if (searchDocumentWord == null) {
			if (other.searchDocumentWord != null)
				return false;
		} else if (!searchDocumentWord.equals(other.searchDocumentWord))
			return false;
		return true;
	}

	private String searchDocumentWord;
	private int searchDocumentNumber;
	/**
	 * Constructor to create a SearchDocument object
	 * @param blogURL
	 */
	public SearchDocument(String searchDocumentWord, int searchDocumentNumber) {
		this.searchDocumentWord = searchDocumentWord; 
		this.setSearchDocumentNumber(searchDocumentNumber);
	}
		
	/**
	 * Getters and Setters for the Blog object parameters
	 * @return
	 */
	public String getSearchDocumentWord() {
		return searchDocumentWord;
	}
	public void setSearchDocumentWord(String searchDocumentWord) {
		this.searchDocumentWord = searchDocumentWord;
	}

	public int getSearchDocumentNumber() {
		return searchDocumentNumber;
	}

	public void setSearchDocumentNumber(int searchDocumentNumber) {
		this.searchDocumentNumber = searchDocumentNumber;
	}
}
