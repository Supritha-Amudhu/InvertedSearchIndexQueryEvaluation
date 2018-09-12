/**
 * 
 */
package com.sfsu.searchengines.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.sfsu.searchengines.model.SearchWord;
import com.sfsu.searchengines.model.TermDocumentFrequency;

/**
 * @author supritha This class runs the search engine program.
 */
public class SearchEngine {

	/**
	 * The main driver program for the search engine.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SearchEngine searchEngine = new SearchEngine();

		Map<Integer, String> documentsMap = searchEngine.parseSearchDocument("documents.txt");
		Map<TermDocumentFrequency, List<Integer>> searchDocumentIndexMap = searchEngine.searchForTerms(documentsMap,
				"documentTermFrequency.txt");

		Map<Integer, String> queriesMap = searchEngine.parseQueries(searchEngine.parseSearchQueries());

		BooleanQueryEvaluation booleanQueryEvaluation = new BooleanQueryEvaluation();
		FileWriter writer = new FileWriter("searchResults.txt");
		for (Integer inputId : queriesMap.keySet()) {
			Map<Integer, String> searchQueryMap = new HashMap<>();
			writer.write("Input Query: " + queriesMap.get(inputId) + "\n");
			System.out.print("Input Query: " + queriesMap.get(inputId) + "\n");
			searchQueryMap.put(inputId, queriesMap.get(inputId).replaceAll(AND, ""));

			Map<TermDocumentFrequency, List<Integer>> searchQueriesIndexMap = searchEngine
					.searchForTerms(searchQueryMap, "input_" + inputId + ".txt");
			if (searchQueriesIndexMap.size() > 1) {
				Object[] terms = searchQueriesIndexMap.keySet().toArray();
				TermDocumentFrequency firstTerm = (TermDocumentFrequency) terms[0];
				writer.write(firstTerm.getDocumentTerm() + "->"
						+ searchEngine.stringifyDocList(searchDocumentIndexMap.get(firstTerm)) + "\n");
				System.out.print(firstTerm.getDocumentTerm() + "->"
						+ searchEngine.stringifyDocList(searchDocumentIndexMap.get(firstTerm)) + "\n");
				TermDocumentFrequency secondTerm = (TermDocumentFrequency) terms[1];
				writer.write(secondTerm.getDocumentTerm() + "->"
						+ searchEngine.stringifyDocList(searchDocumentIndexMap.get(secondTerm)) + "\n");
				System.out.print(secondTerm.getDocumentTerm() + "->"
						+ searchEngine.stringifyDocList(searchDocumentIndexMap.get(secondTerm)) + "\n");
				List<Integer> intersection = booleanQueryEvaluation.intersect(searchDocumentIndexMap.get(firstTerm),
						searchDocumentIndexMap.get(secondTerm));
				writer.write("Intersection List: " + searchEngine.stringifyDocList(intersection) + "\n\n");
				System.out.print("Intersection List: " + searchEngine.stringifyDocList(intersection) + "\n\n");
			}
		}
		writer.close();
	}

	/**
	 * This method takes the list of document numbers and prints the list as
	 * [DOC1, DOC2, DOC3]
	 * 
	 * @param docList
	 * @return string formatted list of document names.
	 */
	private String stringifyDocList(List<Integer> docList) {
		if (docList.size() == 1) {
			return "[DOC" + docList.get(0) + "]";
		}

		StringBuffer result = new StringBuffer("[");
		for (int i = 0; i < docList.size() - 1; i++) {
			result.append("DOC" + docList.get(i) + ", ");
		}
		result.append("DOC" + docList.get(docList.size() - 1));
		result.append("]");
		return result.toString();
	}

	/**
	 * This method returns a static list of search queries.
	 * 
	 * @return list of search queries.
	 */
	private List<String> parseSearchQueries() {
		List<String> queries = new ArrayList<>();
		queries.add("asus AND google");
		queries.add("screen AND bad");
		queries.add("great AND tablet");
		return queries;
	}

	/**
	 * This method parses the search queries and constructs a map of Query
	 * Number to the actual query String.
	 * 
	 * @param queries
	 * @return Map of Query Number and Query String.
	 */
	private Map<Integer, String> parseQueries(List<String> queries) {
		Map<Integer, String> documents = new HashMap<Integer, String>();
		for (int index = 0; index < queries.size(); index++) {
			documents.put(index + 1, queries.get(index));
		}
		return documents;
	}

	/**
	 * This method takes as input a documentsMap and performs 1. Tokenization 2.
	 * Case Normalization 3. Stemming 4. Stop word Removal and returns the
	 * Inverted Index as a Map. Also writes the invertedIndex to given output
	 * file.
	 * 
	 * @param documentsMap
	 * @param dictionaryOutputFileame
	 * @return InvertedIndex Map
	 */
	private Map<TermDocumentFrequency, List<Integer>> searchForTerms(Map<Integer, String> documentsMap,
			String dictionaryOutputFileame) {

		InvertedIndex invertedIndex = new InvertedIndex();
		List<SearchWord> wordList = new ArrayList<>();
		Set<SearchWord> setForStemmer = new HashSet<>();

		wordList = invertedIndex.formStringTokenizer(documentsMap);
		setForStemmer = invertedIndex.removeDuplicates(wordList);
		setForStemmer = invertedIndex.stemWordList(setForStemmer);
		Map<TermDocumentFrequency, List<Integer>> finalDocumentsList = invertedIndex.removeStopWords(setForStemmer);
		invertedIndex.writeDictionary(dictionaryOutputFileame);
		return finalDocumentsList;
	}

	/**
	 * This method reads the contents of the given file as separate documents
	 * delimited by <DOC></DOC> tags and returns a Map of Document Number and
	 * Document content
	 * 
	 * @return Map of Doc number and Document content
	 */
	public Map<Integer, String> parseSearchDocument(String filename) {
		Scanner scanner = null;
		Map<Integer, String> documents = new HashMap<Integer, String>();
		String line = null;
		int documentCounter = 1;
		try {
			scanner = new Scanner(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (scanner.hasNextLine()) {
			String documentContents = "";
			String nextLine;
			line = scanner.nextLine();
			if (line.startsWith("<DOC ")) {
				while (!(nextLine = scanner.nextLine()).equals("</DOC>")) {
					if (nextLine != "</DOC>") {
						if (nextLine.equals("")) {
							documentContents += " ";
						}
						documentContents += nextLine;
					} else {
						break;
					}
				}
				documents.put(documentCounter, documentContents);
				documentCounter++;
			} else {
				continue;
			}
		}
		try {
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documents;
	}

	/** constant AND used for separating query params. */
	private static final String AND = "AND";
}
