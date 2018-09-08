/**
 * 
 */
package com.sfsu.searchengines.model;

/**
 * @author supritha
 *
 */
public class TermDocumentFrequency {
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.documentTerm.hashCode();
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
		TermDocumentFrequency other = (TermDocumentFrequency) obj;
		if (documentFrequency != other.documentFrequency)
			return false;
		if (documentTerm == null) {
			if (other.documentTerm != null)
				return false;
		} else if (!documentTerm.equals(other.documentTerm))
			return false;
		return true;
	}
	/**
	 * @param documentTerm
	 * @param documentFrequency
	 */
	public TermDocumentFrequency(String documentTerm, int documentFrequency) {
		// TODO Auto-generated constructor stub
		this.documentTerm = documentTerm;
		this.documentFrequency = documentFrequency;
	}
	private String documentTerm;
	private int documentFrequency;
	/**
	 * @return the documentTerm
	 */
	public String getDocumentTerm() {
		return documentTerm;
	}
	/**
	 * @param documentTerm the documentTerm to set
	 */
	public void setDocumentTerm(String documentTerm) {
		this.documentTerm = documentTerm;
	}
	/**
	 * @return the documentFrequency
	 */
	public int getDocumentFrequency() {
		return documentFrequency;
	}
	/**
	 * @param documentFrequency the documentFrequency to set
	 */
	public void setDocumentFrequency(int documentFrequency) {
		this.documentFrequency = documentFrequency;
	}
}
