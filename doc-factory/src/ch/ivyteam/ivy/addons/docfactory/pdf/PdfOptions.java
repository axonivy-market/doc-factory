package ch.ivyteam.ivy.addons.docfactory.pdf;

/**
 * Defines a set of options for the PDF generation in the case the docFactory
 * has to produce a PDF document.
 *
 */
@SuppressWarnings("hiding")
public class PdfOptions {

  private PdfAType pdfAType = null;
  /**
   * true if the generated field has to keep fields form editable. Else false.
   * Default is false.
   */
  private boolean keepFormFieldsEditableInPdf;
  /**
   * true if one has to remove the whitespace at the end of editable fields
   * form. Else false. Default is false.
   */
  private boolean removeWhiteSpaceInPdfEditableFields;

  private PdfOptions() {

  }

  /**
   * returns an instance of the PdfOptions with default options:<br />
   * <ul>
   * <li>No {@link PdfAType} is by default defined. If the docFactory has to
   * generate a PDF/A you can set it via {@link #setPdfAType(PdfAType)} or
   * {@link #withPdfAType(PdfAType)}
   * <li>If the template has editable fields, these fields are not editable in
   * the created PDF by default. Set this option via
   * {@link #hasToKeepFormFieldsEditable(boolean)} or
   * {@link #setKeepFormFieldsEditableInPdf(boolean)}
   * <li>If the PDF has some editable fields, the white-spaces in the field are
   * not removed by default. Set this option via
   * {@link #hasToRemoveWhiteSpaceInPdfEditableFields(boolean)} or
   * {@link #setRemoveWhiteSpaceInPdfEditableFields(boolean)}
   * </ul>
   * @return a new instance of the PdfOptions with default options.
   */
  public static PdfOptions getInstance() {
    return new PdfOptions();
  }

  /**
   * Returns the PdfOptions instance which {@link PdfAType} for the PDF
   * generation has been set.
   *
   * @param pdfAType
   */
  public PdfOptions withPdfAType(PdfAType pdfAType) {
    this.pdfAType = pdfAType;
    return this;
  }

  /**
   * Returns the PdfOptions instance which {@link #keepFormFieldsEditableInPdf}
   * option for the PDF generation has been set.
   * @param keepFormFieldsEditableInPdf
   */
  public PdfOptions hasToKeepFormFieldsEditable(boolean keepFormFieldsEditableInPdf) {
    this.keepFormFieldsEditableInPdf = keepFormFieldsEditableInPdf;
    return this;
  }

  /**
   * Returns the PdfOptions instance which
   * {@link #removeWhiteSpaceInPdfEditableFields} option for the PDF generation
   * has been set.
   * @param removeWhiteSpaceInPdfEditableFields
   */
  public PdfOptions hasToRemoveWhiteSpaceInPdfEditableFields(boolean removeWhiteSpaceInPdfEditableFields) {
    this.removeWhiteSpaceInPdfEditableFields = removeWhiteSpaceInPdfEditableFields;
    return this;
  }

  /**
   * Returns the {@link PdfAType} for the PDF creation.<br />
   * If it is null, the docFactory won't convert the created PDF to a PDF/A one.
   * @return the {@link PdfAType} for the PDF creation. Can be null.
   */
  public PdfAType getPdfAType() {
    return pdfAType;
  }

  /**
   * If the generated pdf has to be converted to PDF/A, you can set the wanted
   * {@link PdfAType} here. If you set it to null, the PDF won't be converted to
   * PDF/A
   * @param pdfAType
   */
  public void setPdfAType(PdfAType pdfAType) {
    this.pdfAType = pdfAType;
  }

  /**
   * Returns true if the generated field has to keep fields form editable. Else
   * false.
   * @return true if the generated field has to keep fields form editable. Else
   *         false.
   */
  public boolean isKeepFormFieldsEditableInPdf() {
    return keepFormFieldsEditableInPdf;
  }

  /**
   * If the PDF has to keep its fields form editable, you can set it here.
   * @param keepFormFieldsEditableInPdf Put true if the fields form have to be
   *          editable in the PDF, else put false.
   */
  public void setKeepFormFieldsEditableInPdf(boolean keepFormFieldsEditableInPdf) {
    this.keepFormFieldsEditableInPdf = keepFormFieldsEditableInPdf;
  }

  /**
   * Returns true if one has to remove the whitespace at the end of editable
   * fields form. Else false.
   */
  public boolean isRemoveWhiteSpaceInPdfEditableFields() {
    return removeWhiteSpaceInPdfEditableFields;
  }

  /**
   * If one has to remove the whitespace at the end of editable fields form set
   * it to true. Else false.
   * @param removeWhiteSpaceInPdfEditableFields
   */
  public void setRemoveWhiteSpaceInPdfEditableFields(
          boolean removeWhiteSpaceInPdfEditableFields) {
    this.removeWhiteSpaceInPdfEditableFields = removeWhiteSpaceInPdfEditableFields;
  }

}
