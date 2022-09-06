package ch.ivyteam.ivy.addons.docfactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions;

public class FileAppenderIT {

  @Test
  public void isPdf_true_for_pdf() {
    assertThat(FileAppender.isPdf(new File("aPdf.PDF"))).isTrue();
    assertThat(FileAppender.isPdf(new File("aPdf.pdf"))).isTrue();
    assertThat(FileAppender.isPdf(new File("aPdf.pDf"))).isTrue();
  }
  
  @Test
  public void isPdf_false_for_non_pdf() {
    assertThat(FileAppender.isPdf(new File("aDoc.DOC"))).isFalse();
    assertThat(FileAppender.isPdf(new File("aDoc.doc"))).isFalse();
    assertThat(FileAppender.isPdf(new File("aDirectory"))).isFalse();
  }
  
  @Test
  public void isOffice_true_for_office_file() {
    assertThat(FileAppender.isOfficeWord(new File("aDoc.DOC"))).isTrue();
    assertThat(FileAppender.isOfficeWord(new File("aDoc.doc"))).isTrue();
    assertThat(FileAppender.isOfficeWord(new File("aDocx.DOCX"))).isTrue();
    assertThat(FileAppender.isOfficeWord(new File("aDocx.docx"))).isTrue();
    assertThat(FileAppender.isOfficeWord(new File("aOdt.ODT"))).isTrue();
    assertThat(FileAppender.isOfficeWord(new File("aOdt.odt"))).isTrue();
  }

  @Test
  public void isOffice_false_for_non_office() {
    assertThat(FileAppender.isOfficeWord(new File("aTxt.txt"))).isFalse();
    assertThat(FileAppender.isOfficeWord(new File("aPdf.pdf"))).isFalse();
    assertThat(FileAppender.isOfficeWord(new File("anHTML.html"))).isFalse();
    assertThat(FileAppender.isOfficeWord(new File("aDirectory"))).isFalse();
  }

  @Test
  public void filterPdf() {
    List<File> filesToFilter = Arrays.asList(new File("aTxt.txt"), new File("aPdf.pdf"),
            new File("aDocx.docx"),
            new File("aDirectory"), new File("anotherPdf.pDf"), new File("pdf1.pDf"));

    List<File> pdfs = FileAppender.filterPdfFiles(filesToFilter);
    assertThat(pdfs).contains(new File("aPdf.pdf"), new File("anotherPdf.pDf"), new File("pdf1.pDf"));
  }

  @Test
  public void filterOffice() {
    List<File> filesToFilter = Arrays.asList(new File("aTxt.txt"), new File("anOdt.ODT"),
            new File("aDocx.docx"),
            new File("aDirectory"), new File("anotherDoc.doc"), new File("pdf1.pDf"));

    List<File> pdfs = FileAppender.filterOfficeWordFiles(filesToFilter);
    assertThat(pdfs).contains(new File("anOdt.ODT"), new File("aDocx.docx"), new File("anotherDoc.doc"));
  }

  @Test
  public void appendOfficeFiles() throws Exception {
    List<File> filesToAppend = new ArrayList<>();
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document1.doc").toURI().getPath()));
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document2.docx").toURI().getPath()));
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document3.pdf").toURI().getPath())); // will
                                                                                                            // be
                                                                                                            // ignored
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/another_doc.doc").toURI().getPath()));
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/another_odt.odt").toURI().getPath()));

    File result = FileAppender.getInstance().appendOfficeWordFiles(
            filesToAppend,
            FileAppenderOptions.getInstance()
                    .withAppendedFileName("differentDocTypesAppended")
                    .withAppendedFileParentDirectoryPath("test/fileAppender"));

    assertThat(result.isFile()).isTrue();
    assertThat(result.getName()).isEqualTo("differentDocTypesAppended.pdf");
  }

  @Test
  public void appendOfficeFiles_FileAppenderOptions_restartPageNumbering() throws Exception {
    List<File> filesToAppend = new ArrayList<>();
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document1.doc").toURI().getPath()));
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document2.docx").toURI().getPath()));
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document3.pdf").toURI().getPath())); // will
                                                                                                            // be
                                                                                                            // ignored
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/another_doc.doc").toURI().getPath()));
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/another_odt.odt").toURI().getPath()));

    File result = FileAppender.getInstance().appendOfficeWordFiles(filesToAppend,
            FileAppenderOptions.getInstance()
                    .withAppendedFileName("documentRestartingPageNumbering")
                    .withAppendedFileOutputFormat(DocFactoryConstants.DOCX_FORMAT)
                    .withAppendedFileParentDirectoryPath("test/fileAppender")
                    .restartPageNumbering(true));

    assertThat(result.isFile()).isTrue();
    assertThat(result.getName()).isEqualTo("documentRestartingPageNumbering.docx");
  }

  @Test
  public void appendOfficeFiles_FileAppenderOptions_useHeadersFootersFromLeadingPage() throws Exception {
    List<File> filesToAppend = new ArrayList<>();
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document1.doc").toURI().getPath()));
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document2.docx").toURI().getPath()));
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document3.pdf").toURI().getPath())); // will
                                                                                                            // be
                                                                                                            // ignored
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/another_doc.doc").toURI().getPath()));
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/another_odt.odt").toURI().getPath()));

    File result = FileAppender.getInstance().appendOfficeWordFiles(filesToAppend,
            FileAppenderOptions.getInstance()
                    .withAppendedFileName("documentUsingHeadersFootersFromLeadingPage")
                    .withAppendedFileOutputFormat(DocFactoryConstants.DOCX_FORMAT)
                    .withAppendedFileParentDirectoryPath("test/fileAppender")
                    .useHeadersFootersFromLeadingPage(true));

    assertThat(result.isFile()).isTrue();
    assertThat(result.getName()).isEqualTo("documentUsingHeadersFootersFromLeadingPage.docx");
  }

  @Test
  public void appendPdfFiles() throws Exception {
    List<File> filesToAppend = new ArrayList<>();
    filesToAppend
            .add(new File(FileAppenderIT.class.getResource("resources/files/pdf1.pdf").toURI().getPath()));
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document2.docx").toURI().getPath())); // will
                                                                                                             // be
                                                                                                             // ignored
    filesToAppend.add(
            new File(FileAppenderIT.class.getResource("resources/files/document3.pdf").toURI().getPath()));
    filesToAppend
            .add(new File(FileAppenderIT.class.getResource("resources/files/pdf2.pdf").toURI().getPath()));

    File result = FileAppender.getInstance().appendPdfFiles(filesToAppend,
            FileAppenderOptions.getInstance()
                    .withAppendedFileName("appendingPdfTogether")
                    .withAppendedFileParentDirectoryPath("fileAppender")
    // .withAppendedFileOutputFormat(DocFactoryConstants.DOCX_FORMAT) setting
    // the format has no effect, only pdf output will be done
    // .useHeadersFootersFromLeadingPage(true) or restartPageNumbering(true) has
    // no effect
    );

    assertThat(result.isFile()).isTrue();
    assertThat(result.getName()).isEqualTo("appendingPdfTogether.pdf");
  }
}
