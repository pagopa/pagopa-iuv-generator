package it.gov.pagopa.iuvgenerator.producer;

import it.gov.pagopa.iuvgenerator.exception.IuvGeneratorException;

/**
 * IUV code generator with IUV alghoritm interface
 */
public interface IuvAlghoritmGenerator {

    /**
     * Generates the IUV Code
     * 
     * @param segregationCode
     *            the segregation code
     * @return the IUV Code
     * @throws IuvGeneratorException 
     */
    String generate(Long segregationCode);
}