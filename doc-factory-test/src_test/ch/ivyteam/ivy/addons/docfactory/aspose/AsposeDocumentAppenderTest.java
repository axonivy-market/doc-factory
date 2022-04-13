package ch.ivyteam.ivy.addons.docfactory.aspose;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.aspose.words.Document;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentAppendingStart;
import ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions;

public class AsposeDocumentAppenderTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private List<Document> documents;
  private FileAppenderOptions fileAppenderOptions;
  private int totalDocumentPages;

  @Before
  public void setup() throws Exception {
    documents = new ArrayList<>();
    Document doc1 = new Document(
            new File(this.getClass().getResource("../resources/files/document1.doc").toURI())
                    .getAbsolutePath());
    Document doc2 = new Document(
            new File(this.getClass().getResource("../resources/files/document2.docx").toURI())
                    .getAbsolutePath());
    Document doc3 = new Document(
            new File(this.getClass().getResource("../resources/files/another_doc.doc").toURI())
                    .getAbsolutePath());
    documents.add(doc1);
    documents.add(doc2);
    documents.add(doc3);

    for (Document doc : documents) {
      totalDocumentPages += doc.getPageCount();
    }

    // In this test we append all the documents as new page. Allowing us to
    // count the result page count easily.
    fileAppenderOptions = FileAppenderOptions.getInstance()
            .withDocumentAppendingStart(DocumentAppendingStart.NEW_PAGE);
  }

  @Test
  public void appendDocuments_throws_IAE_if_documentsList_null() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    AsposeDocumentAppender.appendDocuments(null, fileAppenderOptions);
  }

  @Test
  public void appendDocuments_throws_IAE_if_documentsList_isEmpty() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    AsposeDocumentAppender.appendDocuments(new ArrayList<>(), fileAppenderOptions);
  }

  @Test
  public void appendDocuments_throws_IAE_if_FileAppenderOptions_isNull() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    AsposeDocumentAppender.appendDocuments(documents, null);
  }

  @Test
  public void appendDocuments_returns_document_with_appended_documents() throws Exception {
    Document result = AsposeDocumentAppender.appendDocuments(documents, fileAppenderOptions);

    assertThat(result.getPageCount(), is(totalDocumentPages));
  }

  @Test
  public void appendDocuments_returns_document_which_is_not_equal_to_given_documents() throws Exception {
    Document result = AsposeDocumentAppender.appendDocuments(documents, fileAppenderOptions);

    for (Document doc : documents) {
      assertThat(result, not(doc));
    }
  }

  @Test
  public void appendDocuments_givenOnlyOneDocument_returns_document_with_same_content() throws Exception {
    List<Document> listWithOnlyOneDocument = documents.subList(0, 1);
    assertThat(listWithOnlyOneDocument.size(), is(1));

    Document result = AsposeDocumentAppender.appendDocuments(listWithOnlyOneDocument, fileAppenderOptions);

    assertThat(result.getPageCount(), is(listWithOnlyOneDocument.get(0).getPageCount()));
    assertThat(result.getText(), is(listWithOnlyOneDocument.get(0).getText()));
  }

  @Test
  public void appendDocuments_givenOnlyOneDocument_returns_document_which_is_not_equal_to_given_document()
          throws Exception {
    List<Document> listWithOnlyOneDocument = documents.subList(0, 1);
    assertThat(listWithOnlyOneDocument.size(), is(1));

    Document result = AsposeDocumentAppender.appendDocuments(listWithOnlyOneDocument, fileAppenderOptions);

    assertThat(result, not(listWithOnlyOneDocument.get(0)));
  }

  @Test
  public void appendDoc_throws_IAE_if_leadingDoc_is_null() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    AsposeDocumentAppender.appendDoc(null, documents.get(1));
  }

  @Test
  public void appendDoc_throws_IAE_if_docToAppend_is_null() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    AsposeDocumentAppender.appendDoc(documents.get(0), null);
  }

  @Test
  public void after_appendDoc_leadingDoc_sectionsCount_has_appendedDoc_sectionsCount() throws Exception {
    int expectedSectionNumbers = documents.get(0).getSections().getCount()
            + documents.get(1).getSections().getCount();

    assertThat(documents.get(0).getSections().getCount(), not(expectedSectionNumbers));

    AsposeDocumentAppender.appendDoc(documents.get(0), documents.get(1));

    assertThat(documents.get(0).getSections().getCount(), is(expectedSectionNumbers));
  }

}
