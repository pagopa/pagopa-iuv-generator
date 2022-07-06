package it.gov.pagopa.debtposition.iuv.generator;

import it.gov.pagopa.debtposition.iuv.exception.IuvGeneratorException;
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
    public static String generateIUV(Integer segregationCode, Integer auxDigit) throws IuvGeneratorException {
        IuvAlghoritmGenerator iuvGenerator = new IuvAlghoritmGenerator.Builder().build(auxDigit);
        return iuvGenerator.generate(segregationCode);
    }
    
}
