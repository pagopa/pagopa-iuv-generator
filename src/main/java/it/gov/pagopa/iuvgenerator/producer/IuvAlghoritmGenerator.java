package it.gov.pagopa.iuvgenerator.producer;

import it.gov.pagopa.iuvgenerator.exception.IuvGeneratorException;

/**
 * IUV code generator with IUV alghoritm interface
 */
public interface IuvAlghoritmGenerator {

    /**
     * Initialization of <code>IuvAlghoritmGenerator</code> class
     */
    public static class Builder {

        /**
         * Build the IuvAlghoritmGenerator based on <code>auxDigit</code>
         * 
         * @return a new instance of <code>IuvAlghoritmGenerator</code>
         */
        public IuvAlghoritmGenerator build(long auxDigit) {
            return new IuvAlghoritmAuxDigit(auxDigit);
        }
    }
    
    /**
     * Generates the IUV Code
     * 
     * @param segregationCode
     *            the segregation code
     * @return the IUV Code
     * @throws IuvGeneratorException 
     */
    String generate(Long segregationCode) throws IuvGeneratorException;
}