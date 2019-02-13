package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.aspose.words.DataRow;
import com.aspose.words.DataSet;
import com.aspose.words.DataTable;
import com.aspose.words.Document;

import ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker;

public class DocumentWorkerInjectingDataSetIT extends DocFactoryTest {
	
	File template;
	
	@Before
	public void setUp() throws URISyntaxException {
		template = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
	}
	
	@Test
    public void produceDocument_with_DataSet_in_DocumentWorker() throws Exception {
        DocumentTemplate documentTemplate = DocumentTemplate.
                withTemplate(template).
                putDataAsSourceForSimpleMailMerge(makePerson()).
                withDocumentWorker(new dataInjector()).
                useLocale(Locale.GERMAN);
        FileOperationMessage result = null;
        File resultFile = new File("test/documentWorker/injectDataSet.pdf");
        try {
            if(resultFile.isFile()) {
                resultFile.delete();
            }
            result = documentTemplate.produceDocument(resultFile);
        } catch (Exception ex) {
            System.out.println("Exception : " + ex.toString());
        }
        assertTrue(result.isSuccess());
        assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
    }
	
	class dataInjector implements DocumentWorker {

		@Override
		public void prepare(Document document) {
			DataSet dataset = new DataSet();
			dataset.getTables().add(makeDataTable());
			try {
				document.getMailMerge().executeWithRegions(dataset);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		private DataTable makeDataTable() {
			//The name of the DataTable must be the same as the name of the merge field region (TableStart:itemPrices)
			com.aspose.words.DataTable data = new DataTable("itemPrices");
			// add the columns which names are the same as the merge fields in the data table region
			data.getColumns().add("Item", String.class);
			data.getColumns().add("Price", Number.class);
			data.getColumns().add("Currency", String.class);
			// add the rows
			DataRow dr = data.newRow();
			dr.set("Item", "T-Shirt");
			dr.set("Price", 22.56);
			dr.set("Currency", "$");
			data.getRows().add(dr);
			dr = data.newRow();
			dr.set("Item", "Porsche");
			dr.set("Price", 435345);
			dr.set("Currency", "CHF");
			data.getRows().add(dr);
			dr = data.newRow();
			dr.set("Item", "EM-Ticket");
			dr.set("Price", 45);
			dr.set("Currency", "CHF");
			data.getRows().add(dr);
			dr = data.newRow();
			dr.set("Item", "Super PC");
			dr.set("Price", 1500.00);
			dr.set("Currency", "Euro");
			data.getRows().add(dr);

			return data;
	    }
		
	}

}
