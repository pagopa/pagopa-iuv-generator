package it.gov.pagopa.iuvgenerator.producer;

import java.text.DecimalFormat;

import lombok.NoArgsConstructor;

/**
 * IUV code generation algorithm based on <code>auxDigit</code> (default = 3)
 */
@lombok.Builder
@NoArgsConstructor
public class IuvAlghoritmAuxDigit extends IuvAlghoritm {

	@lombok.Builder.Default
    private long auxDigit = 3;

    /**
     * Protected constructor
     */
    protected IuvAlghoritmAuxDigit(long auxDigit) {
        this.auxDigit = auxDigit;
    }

    /**
     * Generate the IUV Code.<br/>
     * IUV (17 digits) = &lt;codice segregazione (2n)&gt;&lt;IUV base (max
     * 13n)&gt;&lt;IUV check digit (2n)&gt; 
     */
    @Override
    public String generate(Long segregationCode) {
        String segregationCodeString = new DecimalFormat("00").format(segregationCode);
        String iuvBase13Digits = generateIuv13Digits();
        String checkDigit = generateCheckDigit(String.valueOf(auxDigit) + segregationCodeString + iuvBase13Digits);
        return segregationCodeString + iuvBase13Digits + checkDigit;
    }
    
}