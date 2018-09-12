/**
 * 
 */
package com.sfsu.searchengines.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author supritha
 * This class performs Query Evaluation.
 */
public class BooleanQueryEvaluation {
	
	/**
	 * This method returns the intersection of the two given lists. The lists are sorted before 
	 * performing intersection.
	 * @param firstTerm
	 * @param secondTerm
	 * @return intersection of two lists.
	 */
	public List<Integer> intersect(List<Integer> firstTerm, List<Integer> secondTerm) {
		Collections.sort(firstTerm);
		Collections.sort(secondTerm);
		return termsIntersection(firstTerm, secondTerm);
	}

	/**
	 * This method performs the intersection of two sorted lists.
	 * @param firstTerm - document list for first search term
	 * @param secondTerm - document list for second search term
	 * @return Intersection of the two lists.
	 */
	private List<Integer> termsIntersection(List<Integer> firstTerm, List<Integer> secondTerm) {
		List<Integer> intersectedTerm = new ArrayList<>();
		int firstIndex=0;
		int secondIndex=0;
		if(firstTerm.isEmpty() || secondTerm.isEmpty()){
			return null;
		}
		while(firstIndex < firstTerm.size() && secondIndex < secondTerm.size()){
			if(firstTerm.get(firstIndex).equals(secondTerm.get(secondIndex))) {
				intersectedTerm.add(firstTerm.get(firstIndex));
				firstIndex++;
				secondIndex++;
			}
			else if(firstTerm.get(firstIndex) < secondTerm.get(secondIndex)) {
				firstIndex++;
			}
			else{
				secondIndex++;
			}
		}
		return intersectedTerm;
	}
}
