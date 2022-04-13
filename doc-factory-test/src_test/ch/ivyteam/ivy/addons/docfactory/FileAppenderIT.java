package ch.ivyteam.ivy.addons.docfactory;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions;

public class FileAppenderIT extends DocFactoryTest {

  @Test
  public void isPdf_true_for_pdf() {
    assertTrue(FileAppender.isPdf(new File("aPdf.PDF")));
    assertTrue(FileAppender.isPdf(new File("aPdf.pdf")));
    assertTrue(FileAppender.isPdf(new File("aPdf.pDf")));
  }

  @Test
  public void isPdf_false_for_non_pdf() {
    assertFalse(FileAppender.isPdf(new File("aDoc.DOC")));
    assertFalse(FileAppender.isPdf(new File("aDoc.doc")));
    assertFalse(FileAppender.isPdf(new File("aDirectory")));
  }

  @Test
  public void isOffice_true_for_office_file() {
    assertTrue(FileAppender.isOfficeWord(new File("aDoc.DOC")));
    assertTrue(FileAppender.isOfficeWord(new File("aDoc.doc")));
    assertTrue(FileAppender.isOfficeWord(new File("aDocx.DOCX")));
    assertTrue(FileAppender.isOfficeWord(new File("aDocx.docx")));
    assertTrue(FileAppender.isOfficeWord(new File("aOdt.ODT")));
    assertTrue(FileAppender.isOfficeWord(new File("aOdt.odt")));
  }

  @Test
  public void isOffice_false_for_non_office() {
    assertFalse(FileAppender.isOfficeWord(new File("aTxt.txt")));
    assertFalse(FileAppender.isOfficeWord(new File("aPdf.pdf")));
    assertFalse(FileAppender.isOfficeWord(new File("anHTML.html")));
    assertFalse(FileAppender.isOfficeWord(new File("aDirectory")));
  }

  @Test
  public void filterPdf() {
    List<File> filesToFilter = Arrays.asList(new File("aTxt.txt"), new File("aPdf.pdf"),
            new File("aDocx.docx"),
            new File("aDirectory"), new File("anotherPdf.pDf"), new File("pdf1.pDf"));

    List<File> pdfs = FileAppender.filterPdfFiles(filesToFilter);

    assertThat(pdfs, contains(new File("aPdf.pdf"), new File("anotherPdf.pDf"), new File("pdf1.pDf")));
  }

  @Test
  public void filterOffice() {
    List<File> filesToFilter = Arrays.asList(new File("aTxt.txt"), new File("anOdt.ODT"),
            new File("aDocx.docx"),
            new File("aDirectory"), new File("anotherDoc.doc"), new File("pdf1.pDf"));

    List<File> pdfs = FileAppender.filterOfficeWordFiles(filesToFilter);

    assertThat(pdfs, contains(new File("anOdt.ODT"), new File("aDocx.docx"), new File("anotherDoc.doc")));
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

    assertThat(result.isFile(), is(true));
    assertThat(result.getName(), is("differentDocTypesAppended.pdf"));
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

    assertThat(result.isFile(), is(true));
    assertThat(result.getName(), is("documentRestartingPageNumbering.docx"));
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

    assertThat(result.isFile(), is(true));
    assertThat(result.getName(), is("documentUsingHeadersFootersFromLeadingPage.docx"));
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

    assertThat(result.isFile(), is(true));
    assertThat(result.getName(), is("appendingPdfTogether.pdf"));
  }

}
