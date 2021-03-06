package it.gov.pagopa.iuvgenerator.producer;

import it.gov.pagopa.iuvgenerator.exception.IuvGeneratorException;
import lombok.experimental.UtilityClass;

/**
 * Business logic class
 */
@UtilityClass
public class IuvCodeBusiness {

    /**
     * Generates a iuv <code>iuv</code>
     * 
     * @param segregationCode
     * @return the <code>iuv</code>
     * @throws IuvGeneratorException 
     * 
     */
    public static String generateIUV(Long segregationCode, Long auxDigit) {
    	IIuvAlghoritmGenerator iuvGenerator = 
    			IuvAlghoritmAuxDigit.builder().auxDigit(auxDigit).segregationCode(segregationCode).build();
        return iuvGenerator.generate();
    }
    
}
