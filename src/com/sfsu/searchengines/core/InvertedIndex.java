/**
 * 
 */
package com.sfsu.searchengines.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemurproject.kstem.KrovetzStemmer;

import com.sfsu.searchengines.model.SearchWord;
import com.sfsu.searchengines.model.TermDocumentFrequency;

/**
 * @author Supritha Amudhu
 * To build an InvertedIndex for a given set of documents.
 */
public class InvertedIndex {
	
	/** The term frequency dictionary.*/
	private Map<TermDocumentFrequency, List<Integer>> finalDictionaryValues;
	
	public InvertedIndex()
	{
		this.finalDictionaryValues = new HashMap<>();
	}
	
	/**
	 * This method writes the contents of the finalDictionaryValues variable to
	 * the given file path.
	 * @param filepath
	 */
	public void writeDictionary(String filepath)
	{
		try {
			FileWriter writer = new FileWriter(new File(filepath));
			writer.write("Term {doc_freq}\t -> [document_list]\n");
			for(TermDocumentFrequency key: this.finalDictionaryValues.keySet())
			{
				writer.write(key.getDocumentTerm() + " {" + key.getDocumentFrequency() + "}" + "\t\t -> " + this.finalDictionaryValues.get(key) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error writing dictionary.");
			e.printStackTrace();
		}
	}
	
	/**
	 * This method runs through the finalDictionaryValues objects and updates the documentFrequency for each key (TermDocumentFrequency) 
	 * @param finalDictionaryValues
	 * @return the updated finalDictionaryValue object.
	 */
	private Map<TermDocumentFrequency, List<Integer>> updateTermDocumentFrequency(Map<TermDocumentFrequency, List<Integer>> finalDictionaryValues) {
		Iterator<TermDocumentFrequency> iterator = finalDictionaryValues.keySet().iterator();
	    while (iterator.hasNext()) {
	    	TermDocumentFrequency keyValue = (TermDocumentFrequency)iterator.next();
	    	keyValue.setDocumentFrequency(finalDictionaryValues.get(keyValue).size());
	    }
		return finalDictionaryValues;
	}
	
	/**
	 * This method removes all stop words from the searchWords set.
	 * @param setForStemmer
	 * @return Map of TermDocumentFrequency object and document list.
	 */
	public Map<TermDocumentFrequency, List<Integer>> removeStopWords(Set<SearchWord> setForStemmer) {
		List<String> stopListWords = new ArrayList<>(Arrays.asList("the", "is", "at", "of", "on", "and", "a"));
		TermDocumentFrequency dictionaryKey;
		ArrayList<Integer> dictionaryValue;
		for(SearchWord value : setForStemmer){
			if(stopListWords.contains(value.getSearchDocumentWord())){
				continue;
			}
			if(finalDictionaryValues.containsKey(new TermDocumentFrequency(value.getSearchDocumentWord(), 0))){
				finalDictionaryValues.get(new TermDocumentFrequency(value.getSearchDocumentWord(), 0)).add(value.getSearchDocumentNumber());
				continue;
			}
			dictionaryKey = new TermDocumentFrequency(value.getSearchDocumentWord(), 0);
			dictionaryValue = new ArrayList<Integer>(Arrays.asList(value.getSearchDocumentNumber()));
			finalDictionaryValues.put(dictionaryKey, dictionaryValue);
		}
		finalDictionaryValues = updateTermDocumentFrequency(finalDictionaryValues);
		return finalDictionaryValues;
	}
	
	/**
	 * This method performed stemming on the search words set.
	 * @param setForStemmer
	 * @return Set of search words after stemming.
	 */
	public Set<SearchWord> stemWordList(Set<SearchWord> setForStemmer) {
		Set<SearchWord> stemmedWordList = new HashSet<>();
		KrovetzStemmer stemmer = new KrovetzStemmer();
		String stemmedWord;
		for(SearchWord value : setForStemmer){
			stemmedWord = stemmer.stem(value.getSearchDocumentWord());
			stemmedWordList.add(new SearchWord(stemmedWord, value.getSearchDocumentNumber()));
		}
		return stemmedWordList;
	}
	
	/**
	 * This method takes a list of search words and removes all duplicates.
	 * @param wordList
	 * @return Set of search words.
	 */
	public Set<SearchWord> removeDuplicates(List<SearchWord> wordList) {
		return new HashSet<>(wordList);
	}
	
	/**
	 * This method removes all non-alphanumeric characters from a word.
	 * @param wordToBeChecked
	 * @return word with non alphanumeric characters removed.
	 */
	private String removeNonAlphaNumerics(String wordToBeChecked) {
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher matcher;
		matcher = pattern.matcher(wordToBeChecked);
		String alphaNumeric = null;
		if(matcher.find()){
			alphaNumeric = wordToBeChecked.replaceAll("[^A-Z^a-z^0-9]","");
		}
		return alphaNumeric;
	}
	
	/**
	 * Converts the given word to lowercase
	 * @param wordToBeConverted
	 * @return input word in lowercase
	 */
	private String convertToLowerCase(String wordToBeConverted) {
		return wordToBeConverted.toLowerCase();
	}
	
	/**
	 * This method takes document Map as input and returns a list of words
	 * after tokenization.
	 * @param documentsMap
	 * @return list of words from all the documents.
	 */
	public List<SearchWord> formStringTokenizer(Map<Integer, String> documentsMap) {
		Iterator<Entry<Integer, String>> iterator = documentsMap.entrySet().iterator();
		List<String> splitString = new ArrayList<String>();
		String individualSplitWord = null;
		List<SearchWord> wordList = new ArrayList<>();
		
		while(iterator.hasNext()) {
			Map.Entry<Integer, String> keyValue = (Map.Entry<Integer, String>)iterator.next();
			splitString = Arrays.asList(keyValue.getValue().split(" |\\'|\\\"|\\-|\\(|\\)|\\[|\\]|\\{|\\}|\\+|\\=|\\_|\\:|\\;|\\<|\\>|\\?|\\/|\\\n|\\\t"));
			for(int index=0;index<splitString.size();index++){
				individualSplitWord = splitString.get(index);
				individualSplitWord = removeNonAlphaNumerics(individualSplitWord);
				if(individualSplitWord == null || individualSplitWord.isEmpty()){
					continue;
				}
				individualSplitWord = convertToLowerCase(individualSplitWord);
				wordList.add(new SearchWord(individualSplitWord, keyValue.getKey()));
			}
		}
		iterator.remove();
		Collections.sort(wordList, new CustomComparator());
		return wordList;
	}
}

/**
 * 
 * @author supritha
 * To compares two searchWord objects.
 */
class CustomComparator implements Comparator<SearchWord> {
    @Override
    public int compare(SearchWord object1, SearchWord object2) {
        return object1.getSearchDocumentWord().compareTo(object2.getSearchDocumentWord());
    }
}

