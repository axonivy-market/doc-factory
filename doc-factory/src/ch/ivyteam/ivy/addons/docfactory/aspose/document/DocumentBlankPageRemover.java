package ch.ivyteam.ivy.addons.docfactory.aspose.document;

import org.apache.commons.lang3.StringUtils;

import com.aspose.words.Document;
import com.aspose.words.LayoutCollector;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.ParagraphCollection;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.aspose.words.SectionCollection;

public class DocumentBlankPageRemover {

  public static void removesBlankPage(Document document) throws Exception {
    LayoutCollector lc = new LayoutCollector(document);
    int pages = lc.getStartPageIndex(document.getLastSection().getBody()
            .getLastParagraph());
    SectionCollection sections = document.getSections();

    for (int pageNumber = pages; pageNumber >= 0; pageNumber--) {
      if (isPageEmpty(pageNumber, document)) {
        for (Section section : sections) {
          removeSectionFromPage(section, pageNumber, lc);
        }
      }
    }
  }

  private static void removeSectionFromPage(
          Section section, int pageNumber, LayoutCollector lc) throws Exception {
    ParagraphCollection pc = section.getBody().getParagraphs();
    for (Paragraph p : pc) {
      if (lc.getStartPageIndex(p) == pageNumber) {
        p.remove();
      }
    }
  }

  private static boolean isPageEmpty(int page, Document document) throws Exception {
    SectionCollection sections = document.getSections();
    LayoutCollector lc = new LayoutCollector(document);
    String pageText = "";
    for (Section section : sections) {
      ParagraphCollection pc = section.getBody().getParagraphs();
      for (Paragraph p : pc) {
        if (lc.getStartPageIndex(p) == page) {
          NodeCollection<?> nodes = p.getChildNodes(NodeType.PARAGRAPH, false);
          for (Node node : nodes) {
            pageText += node.toString(SaveFormat.TEXT).trim();
          }
        }
      }
    }

    return StringUtils.isBlank(pageText);
  }

}
