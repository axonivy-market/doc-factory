package ch.ivyteam.ivy.addons.docfactory.aspose;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aspose.words.Document;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentAppendingStart;
import ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions;

public class AsposeDocumentAppenderTest {

  private List<Document> documents;
  private FileAppenderOptions fileAppenderOptions;
  private int totalDocumentPages;

  @BeforeEach
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
    assertThatThrownBy(() -> AsposeDocumentAppender.appendDocuments(null, fileAppenderOptions)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void appendDocuments_throws_IAE_if_documentsList_isEmpty() throws Exception {
    assertThatThrownBy(() -> AsposeDocumentAppender.appendDocuments(new ArrayList<>(), fileAppenderOptions)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void appendDocuments_throws_IAE_if_FileAppenderOptions_isNull() throws Exception {
    assertThatThrownBy(() -> AsposeDocumentAppender.appendDocuments(documents, null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void appendDocuments_returns_document_with_appended_documents() throws Exception {
    var result = AsposeDocumentAppender.appendDocuments(documents, fileAppenderOptions);
    assertThat(result.getPageCount()).isEqualTo(totalDocumentPages);
  }

  @Test
  public void appendDocuments_returns_document_which_is_not_equal_to_given_documents() throws Exception {
    var result = AsposeDocumentAppender.appendDocuments(documents, fileAppenderOptions);
    for (Document doc : documents) {
      assertThat(result).isNotEqualTo(doc);
    }
  }

  @Test
  public void appendDocuments_givenOnlyOneDocument_returns_document_with_same_content() throws Exception {
    List<Document> listWithOnlyOneDocument = documents.subList(0, 1);
    assertThat(listWithOnlyOneDocument).hasSize(1);

    var result = AsposeDocumentAppender.appendDocuments(listWithOnlyOneDocument, fileAppenderOptions);
    assertThat(result.getPageCount()).isEqualTo(listWithOnlyOneDocument.get(0).getPageCount());
    assertThat(result.getText()).isEqualTo(listWithOnlyOneDocument.get(0).getText());
  }

  @Test
  public void appendDocuments_givenOnlyOneDocument_returns_document_which_is_not_equal_to_given_document() throws Exception {
    List<Document> listWithOnlyOneDocument = documents.subList(0, 1);
    assertThat(listWithOnlyOneDocument).hasSize(1);

    Document result = AsposeDocumentAppender.appendDocuments(listWithOnlyOneDocument, fileAppenderOptions);
    assertThat(result).isNotEqualTo(listWithOnlyOneDocument.get(0));
  }

  @Test
  public void appendDoc_throws_IAE_if_leadingDoc_is_null() throws Exception {
    assertThatThrownBy(() -> AsposeDocumentAppender.appendDoc(null, documents.get(1))).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void appendDoc_throws_IAE_if_docToAppend_is_null() throws Exception {
    assertThatThrownBy(() -> AsposeDocumentAppender.appendDoc(documents.get(0), null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void after_appendDoc_leadingDoc_sectionsCount_has_appendedDoc_sectionsCount() throws Exception {
    int expectedSectionNumbers = documents.get(0).getSections().getCount() + documents.get(1).getSections().getCount();
    assertThat(documents.get(0).getSections().getCount()).isNotEqualTo(expectedSectionNumbers);

    AsposeDocumentAppender.appendDoc(documents.get(0), documents.get(1));
    assertThat(documents.get(0).getSections().getCount()).isEqualTo(expectedSectionNumbers);
  }
}
