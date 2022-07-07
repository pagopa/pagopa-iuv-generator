package it.gov.pagopa.iuvgenerator.producer;

import java.text.DecimalFormat;

import it.gov.pagopa.iuvgenerator.exception.IuvGeneratorException;

/**
 * IUV code generation algorithm based on <code>auxDigit</code> = 3
 */
public class IuvAlghoritmAuxDigit extends IuvAlghoritm {

    private int auxDigit = 3;

    /**
     * Protected constructor
     */
    protected IuvAlghoritmAuxDigit(int auxDigit) {
        this.auxDigit = auxDigit;
    }

    /**
     * Generate the IUV Code.<br/>
     * IUV (17 digits) = &lt;codice segregazione (2n)&gt;&lt;IUV base (max
     * 13n)&gt;&lt;IUV check digit (2n)&gt;
     * @throws IuvGeneratorException 
     */
    @Override
    public String generate(Integer segregationCode) throws IuvGeneratorException {
        String segregationCodeString = new DecimalFormat("00").format(segregationCode);
        String iuvBase13Digits = generateIuv13Digits();
        String checkDigit = generateCheckDigit(String.valueOf(auxDigit) + segregationCodeString + iuvBase13Digits);
        return segregationCodeString + iuvBase13Digits + checkDigit;
    }
    
}