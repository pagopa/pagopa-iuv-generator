package it.gov.pagopa.debtposition.iuv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IuvGenerationModel {
	private Integer segregationCode;
	private Integer auxDigit;
}
