/**
 * 
 */
package com.sfsu.searchengines.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemurproject.kstem.KrovetzStemmer;

import com.sfsu.searchengines.model.SearchDocument;
import com.sfsu.searchengines.model.TermDocumentFrequency;

/**
 * @author Supritha Amudhu
 *
 */
public class InvertedIndex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		InvertedIndex invertedIndex = new InvertedIndex();
//		Map<Integer, String> documentsMap = invertedIndex.parseSearchDocument();
//		List<SearchDocument> wordList = new ArrayList<>();
//		Set<SearchDocument> setForStemmer = new HashSet<>();
//		wordList = invertedIndex.formStringTokenizer(wordList, documentsMap);
//		setForStemmer = invertedIndex.removeDuplicates(wordList);
//		setForStemmer = invertedIndex.stemWordList(setForStemmer);
//		Map<TermDocumentFrequency, List<Integer>> finalDocumentsList = invertedIndex.removeStopWords(setForStemmer);
	}
	
	private Map<TermDocumentFrequency, List<Integer>> updateTermDocumentFrequency(Map<TermDocumentFrequency, List<Integer>> finalDictionaryValues) {
//		System.out.println("Final dict: " + finalDictionaryValues);
		Iterator<TermDocumentFrequency> iterator = finalDictionaryValues.keySet().iterator();
		int i =0;
	    while (iterator.hasNext()) {
	    	i++;
	    	TermDocumentFrequency keyValue = (TermDocumentFrequency)iterator.next();
//	    	System.out.println("Update to size: "+finalDictionaryValues.get(keyValue).size());
	    	keyValue.setDocumentFrequency(finalDictionaryValues.get(keyValue).size());
//	        System.out.println(keyValue.getDocumentTerm() + " " + keyValue.getDocumentFrequency() + " = " + finalDictionaryValues.get(keyValue));
	    }
//	    System.out.println(i);
		return finalDictionaryValues;
	}
	
	
	public Map<TermDocumentFrequency, List<Integer>> removeStopWords(Set<SearchDocument> setForStemmer) {
		Map<TermDocumentFrequency, List<Integer>> finalDictionaryValues = new HashMap<>();
		List<String> stopListWords = new ArrayList<>(Arrays.asList("the", "is", "at", "of", "on", "and", "a"));
		TermDocumentFrequency dictionaryKey;
		ArrayList<Integer> dictionaryValue;
		for(SearchDocument value : setForStemmer){
			if(stopListWords.contains(value.getSearchDocumentWord())){
//				System.out.println(">>>>>>> Stop word detected! " +value.getSearchDocumentWord());
				continue;
			}
			if(finalDictionaryValues.containsKey(new TermDocumentFrequency(value.getSearchDocumentWord(), 0))){
				finalDictionaryValues.get(new TermDocumentFrequency(value.getSearchDocumentWord(), 0)).add(value.getSearchDocumentNumber());
				continue;
			}
			dictionaryKey = new TermDocumentFrequency(value.getSearchDocumentWord(), 0);
			dictionaryValue = new ArrayList<Integer>(Arrays.asList(value.getSearchDocumentNumber()));
//			System.out.println("Dictionary Value: "+dictionaryValue);
			finalDictionaryValues.put(dictionaryKey, dictionaryValue);
		}
//		Iterator<TermDocumentFrequency> iterator = finalDictionaryValues.keySet().iterator();
//		int i =0;
//	    while (iterator.hasNext()) {
//	    	i++;
//	    	TermDocumentFrequency keyValue = (TermDocumentFrequency)iterator.next();
//	        System.out.println(keyValue.getDocumentTerm() + " = " + finalDictionaryValues.get(keyValue));
//	        iterator.remove(); // avoids a ConcurrentModificationException
//	    }
//	    System.out.println(i);
		finalDictionaryValues = updateTermDocumentFrequency(finalDictionaryValues);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>" +finalDictionaryValues);
		return finalDictionaryValues;
	}
	
	public Set<SearchDocument> stemWordList(Set<SearchDocument> setForStemmer) {
		Set<SearchDocument> stemmedWordList = new HashSet<>();
		KrovetzStemmer stemmer = new KrovetzStemmer();
		String stemmedWord;
		for(SearchDocument value : setForStemmer){
//			System.out.println("Stemmed value: " +stemmer.stem(value.getSearchDocumentWord()));
			stemmedWord = stemmer.stem(value.getSearchDocumentWord());
			stemmedWordList.add(new SearchDocument(stemmedWord, value.getSearchDocumentNumber()));
		}
//		System.out.println("Count of non-stemmed words: "+setForStemmer.size());
//		System.out.println("Count of stemmed words: "+stemmedWordList.size());
		return stemmedWordList;
	}
	
	public Set<SearchDocument> removeDuplicates(List<SearchDocument> wordList) {
		Set<SearchDocument> noDuplicateSet = new HashSet<>();
		for(int index=0;index<wordList.size();index++){
			noDuplicateSet.add(wordList.get(index));
//			System.out.println(wordList.get(i).getSearchDocumentNumber() + " -----> " + wordList.get(i).getSearchDocumentWord());
		}
		for(SearchDocument value : noDuplicateSet){
//            System.out.println(value.getSearchDocumentWord() + " -----> " + value.getSearchDocumentNumber());
		}
		return noDuplicateSet;
	}
	
	private String removeNonAlphaNumerics(String wordToBeChecked) {
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher matcher;
		matcher = pattern.matcher(wordToBeChecked);
		String alphaNumeric = null;
		if(matcher.find()){
//			System.out.println("Thief word: " + wordToBeChecked);
			alphaNumeric = wordToBeChecked.replaceAll("[^A-Z^a-z^0-9]","");
//			System.out.println("Corrected word: " + alphaNumeric);
		}
		return alphaNumeric;
	}
	
	private String convertToLowerCase(String wordToBeConverted) {
		return wordToBeConverted.toLowerCase();
	}
	
	public List<SearchDocument> formStringTokenizer(List<SearchDocument> wordList, Map<Integer, String> documentsMap) {
		Iterator<Entry<Integer, String>> iterator = documentsMap.entrySet().iterator();
		List<String> splitString = new ArrayList<String>();
		String individualSplitWord = null;
		
		while(iterator.hasNext()) {
			Map.Entry<Integer, String> keyValue = (Map.Entry<Integer, String>)iterator.next();
//			System.out.println("Key: " +keyValue.getKey());
//			System.out.println("Value: " +keyValue.getValue());
			splitString = Arrays.asList(keyValue.getValue().split(" |\\'|\\\"|\\-|\\(|\\)|\\[|\\]|\\{|\\}|\\+|\\=|\\_|\\:|\\;|\\<|\\>|\\?|\\/|\\\n|\\\t"));
//			System.out.println("Split String: " + splitString);
			for(int index=0;index<splitString.size();index++){
				individualSplitWord = splitString.get(index);
//				System.out.println("Word being considered: " +individualSplitWord);
				individualSplitWord = removeNonAlphaNumerics(individualSplitWord);
				if(individualSplitWord == null || individualSplitWord.isEmpty()){
					continue;
				}
				individualSplitWord = convertToLowerCase(individualSplitWord);
				wordList.add(new SearchDocument(individualSplitWord, keyValue.getKey()));
			}
		}
		iterator.remove();
		Collections.sort(wordList, new CustomComparator());
//		System.out.println("Wordlist: ");
		for(int index=0;index<wordList.size();index++){
//			System.out.println(wordList.get(index).getSearchDocumentNumber() + " -----> " + wordList.get(index).getSearchDocumentWord());
		}
		System.out.println("Count: "+wordList.size());
		return wordList;
	}
	
	public Map<Integer, String> parseSearchDocument() {
		Scanner scanner = null;
		Map<Integer, String> documents = new HashMap<Integer, String>();
		String line = null;
		int documentCounter = 1;
		try {
			scanner = new Scanner(new FileReader("documents.txt"));
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (scanner.hasNextLine()) {
			String documentContents = "";
			String nextLine;
			line = scanner.nextLine();
			if(line.startsWith("<DOC ")){
				while(!(nextLine = scanner.nextLine()).equals("</DOC>")) {
					if(nextLine != "</DOC>") {
//						System.out.println("nextLine: "+nextLine);
						if(nextLine.equals("")){
							documentContents += " ";
						}
						documentContents += nextLine;
//						System.out.println("documentContents: "+documentContents);
					}	
					else {
						break;
					}
				}
				documents.put(documentCounter, documentContents);
				documentCounter++;
			}
			else {
				continue;
			}
		}
	    try {
	    	scanner.close();
		} 
	    catch (Exception e) {
			e.printStackTrace();
		}
//	    System.out.println(documents);
		return documents;	
	}

}

class CustomComparator implements Comparator<SearchDocument> {
    @Override
    public int compare(SearchDocument object1, SearchDocument object2) {
        return object1.getSearchDocumentWord().compareTo(object2.getSearchDocumentWord());
    }
}

