package ch.ivyteam.ivy.addons.docfactory.pdf;

/**
 * Defines the different PDA/A types that the docFactory supports for PDF/A generation.
 * See also https://en.wikipedia.org/wiki/PDF/A#Conformance_levels_and_versions <br />
 * <b>Important note:</b><br />
 * It seems that the underlying library used for this operation has some bugs with the PDF/A 2A, 3A, 2U and 3U formats.<br />
 * We recommend the {@link PdfAType#PDF_A_1A}, {@link PdfAType#PDF_A_1B}, {@link PdfAType#PDF_A_2B} and {@link PdfAType#PDF_A_3B}
 */
public enum PdfAType {
	PDF_A_1A, PDF_A_1B, PDF_A_2A, PDF_A_2B, PDF_A_3A, PDF_A_3B, PDF_A_2U, PDF_A_3U;
}
