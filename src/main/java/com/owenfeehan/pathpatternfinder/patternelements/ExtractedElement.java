package com.owenfeehan.pathpatternfinder.patternelements;

/**
 * The result of partition a string into two components, one left (prefix) and one right (suffix)
 * 
 * @author owen
 *
 */
public class ExtractedElement {

	private String extracted;
	private String remainder;

	/**
	 * Constructor
	 * 
	 * @param extracted left-most part of string
	 * @param remainder right-most part of string
	 */
	public ExtractedElement(String extracted, String remainder) {
		super();
		this.extracted = extracted;
		this.remainder = remainder;
	}
	
	/**
	 * Alternative constructor where a string is passed, and then split into two by an index
	 * @param str string to split
	 * @param indexSecondPart the starting in index of the second part, so that extracted=[0,indexSecondPart-1] and remainder=[indexSecondPart..]
	 */
	public ExtractedElement(String str, int indexSecondPart) {
		this(
			str.substring(0, indexSecondPart),
			str.substring(indexSecondPart)				
		);
	}

	public String getExtracted() {
		return extracted;
	}

	public String getRemainder() {
		return remainder;
	}

}
