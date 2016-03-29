package org.microapp.Diary.model.enums;

/**
 * Units for various activities.
 * @author Zdenda
 *
 */
public enum ActivityUnit {

	/**
	 * No unit.
	 */
	NULL(1,""),
	
	/**
	 * Millimeters.
	 */
	MM(1,"mm"),
	
	/**
	 * Centimeters.
	 */
	CM(10,"cm"),
	
	/**
	 * Meters.
	 */
	M(1000,"m"),
	
	/**
	 * Kilometers
	 */
	KM(1000000,"km");
	
	/**
	 * Ratio of unit to default unit. 
	 */
	private double ratio;
	
	/**
	 * Text representation.
	 */
	private String textRepresentation;

	
	/**
	 * Constructor of unit with ratio and text representation parameter.
	 * @param ratio If the ratio parameter is 1, unit is considered as default.
	 * @param textRepresentation Text representation of unit.   
	 */
	private ActivityUnit(double ratio, String textRepresentation) {
		this.ratio = ratio;
		this.textRepresentation = textRepresentation;
	}
	
	/**
	 * Constructor of unit with ratio parameter.
	 * @param ratio If the ratio parameter is 1, unit is considered as default.   
	 */
	private ActivityUnit(double ratio) {
		this(ratio, null);
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public String getTextRepresentation() {
		return textRepresentation == null ? name() : textRepresentation;
	}
	
	
	//for UI
	public String getKey() {
		return name();
	}
	
	public String getValue() {
		return getTextRepresentation();
	}
	
	
}
