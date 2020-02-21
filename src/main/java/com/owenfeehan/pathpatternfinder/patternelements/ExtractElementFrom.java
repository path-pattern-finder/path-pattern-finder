package com.owenfeehan.pathpatternfinder.patternelements;

import org.apache.commons.io.IOCase;

public class ExtractElementFrom {

	private ExtractElementFrom() {
		// NOTHING TO DO
	}
	
	
	/**
	 * Extracts a string (trims it from the left-side) if possible
	 * 
	 * @param strToExtract string to extract
	 * @param strToSearch string to search from
	 * @param ioCase whether to be case-sensitive or not
	 * @return the extracted-string (and the remainder) or null if the left-most-side doesn't match
	 */
	public static ExtractedElement extractStrIfPossible( String strToExtract, String strToSearch, IOCase ioCase ) {
		
		if (ioCase.checkStartsWith(strToSearch, strToExtract)) {
			return new ExtractedElement(
				strToSearch.substring(0, strToExtract.length()),
				strToSearch.substring(strToExtract.length())
			);
		}
		return null;
	}
}
