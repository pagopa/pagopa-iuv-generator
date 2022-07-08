package it.gov.pagopa.iuvgenerator.producer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.regex.Pattern;

/**
 * IUV alghoritm abstract class
 */
public abstract class IuvAlghoritm implements IuvAlghoritmGenerator {
	
	public static final String UNEXPECTED_GENERATED_VALUE_ERROR = "Unexpected generated value: ";

    private static final String DIGIT_OF_2 = "%02d";
    private static final String DIGIT_OF_13 = "\\d{13}";

    private static Pattern pattern = Pattern.compile(DIGIT_OF_13);

    /**
     * Calculates the check digit of IUV code
     * 
     * @param checkDigitComponent
     *            check digit component
     * @return the generated check digit
     */
    protected String generateCheckDigit(String checkDigitComponent) {
        return String.format(DIGIT_OF_2,
                (new BigDecimal(checkDigitComponent).remainder(new BigDecimal(93))).intValue());
    }

    /**
     * Generates 13 digits IUV
     * 
     * @return the IUV base
     * @throws IllegalArgumentException 
     */
    protected String generateIuv13Digits() throws IllegalArgumentException {

    	long timeStampMillis = Instant.now().toEpochMilli();	

    	String sequence=Long.toString(timeStampMillis);

    	if (!pattern.matcher(sequence).matches()) {
    		throw new IllegalArgumentException(UNEXPECTED_GENERATED_VALUE_ERROR + sequence);
    	}

    	return sequence;
    }
}