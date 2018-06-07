package ch.ivyteam.ivy.addons.filemanager.database.filetype;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.addons.filemanager.FileType;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PersistenceConnectionManagerFactory.class})
public class FileTypesControllerFileTypeListFilter {

	@Mock
	BasicConfigurationController config;
	
	@Mock
	FileTypeSQLPersistence mockedFileTypePersistence;
	
	
	@Before
	public void setUp() throws Exception {
		config = mock(BasicConfigurationController.class);
		
		mockedFileTypePersistence = mock(FileTypeSQLPersistence.class);
		when(mockedFileTypePersistence.getAllFileTypes()).thenReturn(makeFileTypes());
		
		mockStatic(PersistenceConnectionManagerFactory.class);
		when(PersistenceConnectionManagerFactory.getIFileTypePersistenceInstance(any(BasicConfigurationController.class))).thenReturn(mockedFileTypePersistence);
	}

	@Test
	public void getAllFileTypes_configHasNoFileTypeListFilter_listNotFiltered() throws Exception {
		when(config.getFileTypeListFilter()).thenReturn(null);
		
		FileTypesController ftc = new FileTypesController(config);
		
		assertTrue(ftc.getAllFileTypes().containsAll(makeFileTypes()));
	}
	
	@Test
	public void getAllFileTypes_fileTypeListFilterSetInConfig_listFiltered() throws Exception {
		when(config.getFileTypeListFilter()).thenReturn(new myFileTypeListFilter());
		
		FileTypesController ftc = new FileTypesController(config);
		
		for(FileType ft: ftc.getAllFileTypes()) {
			assertTrue(ft.getApplicationName().equalsIgnoreCase(myFileTypeListFilter.ACCEPTED_APPLICATION_NAME));
		}
	}
	
	private java.util.List<FileType> makeFileTypes() {
		java.util.List<FileType> types = new ArrayList<>();
		FileType ft = new FileType();
		ft.setApplicationName(myFileTypeListFilter.ACCEPTED_APPLICATION_NAME);
		ft.setId((long) 1234);
		ft.setFileTypeName("letter");
		types.add(ft);
		
		ft = new FileType();
		ft.setId((long) 134);
		ft.setFileTypeName("document");
		types.add(ft);
		
		ft = new FileType();
		ft.setApplicationName(myFileTypeListFilter.ACCEPTED_APPLICATION_NAME);
		ft.setId((long) 124534);
		ft.setFileTypeName("bill");
		types.add(ft);
		
		ft = new FileType();
		ft.setApplicationName("XREC");
		ft.setId((long) 19234);
		ft.setFileTypeName("letter");
		types.add(ft);
		
		return types;
	}
	
	
	
	private class myFileTypeListFilter implements FileTypeListFilter {
		
		static final String ACCEPTED_APPLICATION_NAME = "XFIN";

		@Override
		public boolean accept(FileType fileType) {
			return fileType.getApplicationName()!=null && fileType.getApplicationName().equalsIgnoreCase(ACCEPTED_APPLICATION_NAME);
		}
		
	}

}
