package org.microapp.ui.base.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.sql.Date;

import org.apache.wicket.util.convert.ConversionException;

/**
 * Converter for java.sql.Date.
 * @author Zdenda
 *
 */
public class SqlDateConverter extends org.apache.wicket.util.convert.converter.SqlDateConverter{

	@Override
	public Date convertToObject(String date, Locale locale)
			throws ConversionException {
		return super.convertToObject(date, locale);
	}

	/**
	 * Returns the date in dd. MM. yyyy format.
	 */
	@Override
	public String convertToString(Date date, Locale locale) {
		DateFormat df = new SimpleDateFormat("dd. MM. yyyy", locale);
		
		return df.format(date);
	}

}
