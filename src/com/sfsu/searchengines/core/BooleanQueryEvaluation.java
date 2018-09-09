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
 *
 */
public class BooleanQueryEvaluation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> firstTerm = new ArrayList<>(Arrays.asList(1, 5, 6, 7, 8, 2, 3, 4));
		List<Integer> secondTerm = new ArrayList<>(Arrays.asList(6, 7, 10, 15, 8, 2));
		BooleanQueryEvaluation booleanQueryEvaluation = new BooleanQueryEvaluation();
		booleanQueryEvaluation.intersect(firstTerm, secondTerm);
	}
	
	private void intersect(List<Integer> firstTerm, List<Integer> secondTerm) {
		firstTerm = new ArrayList<>(Arrays.asList(1, 5, 6, 7, 8, 2, 3, 4));
		secondTerm = new ArrayList<>(Arrays.asList(6, 7, 10, 15, 8, 2));
		Collections.sort(firstTerm);
		Collections.sort(secondTerm);
		termsIntersection(firstTerm, secondTerm);
	}

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
		System.out.println(intersectedTerm);
		return intersectedTerm;
	}
}
