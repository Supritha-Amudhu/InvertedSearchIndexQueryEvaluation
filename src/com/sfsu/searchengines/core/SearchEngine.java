/**
 * 
 */
package com.sfsu.searchengines.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sfsu.searchengines.model.SearchDocument;
import com.sfsu.searchengines.model.TermDocumentFrequency;

/**
 * @author supritha
 *
 */
public class SearchEngine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SearchEngine searchEngine = new SearchEngine();
		int invertedIndex = 0;
		int processQueries = 1;
		Map<TermDocumentFrequency, List<Integer>> invertedIndexMap = searchEngine.searchForTerms(invertedIndex);
		Map<TermDocumentFrequency, List<Integer>> processQueriesMap = searchEngine.searchForTerms(processQueries);
		BooleanQueryEvaluation booleanQueryEvaluation = new BooleanQueryEvaluation();
		booleanQueryEvaluation.intersect(processQueriesMap.get(new TermDocumentFrequency("asus", 0)), processQueriesMap.get(new TermDocumentFrequency("google", 0)));
	}

	
	private List<String> parseSearchQueries() {
		List<String> queries = new ArrayList<>();
		queries.add("asus AND google");
		queries.add("screen AND bad");
		queries.add("great AND tablet");
		return queries;
	}
	
	private Map<Integer, String> parseQueries(List<String> queries) {
		Map<Integer, String> documents = new HashMap<Integer, String>();
		for(int index=0;index<queries.size();index++){
			documents.put(index+1, queries.get(index));
		}
		return documents;
	}
	
	private Map<TermDocumentFrequency, List<Integer>> searchForTerms(int method) {
		InvertedIndex invertedIndex = new InvertedIndex();
		List<SearchDocument> wordList = new ArrayList<>();
		Set<SearchDocument> setForStemmer = new HashSet<>();
		Map<Integer, String> documentsMap;
		if(method == 0) {
			documentsMap = invertedIndex.parseSearchDocument();
		}
		else {
			documentsMap = parseQueries(parseSearchQueries());
		}
		
		wordList = invertedIndex.formStringTokenizer(wordList, documentsMap);
		setForStemmer = invertedIndex.removeDuplicates(wordList);
		setForStemmer = invertedIndex.stemWordList(setForStemmer);
		Map<TermDocumentFrequency, List<Integer>> finalDocumentsList = invertedIndex.removeStopWords(setForStemmer);
		return finalDocumentsList;
	}

}
