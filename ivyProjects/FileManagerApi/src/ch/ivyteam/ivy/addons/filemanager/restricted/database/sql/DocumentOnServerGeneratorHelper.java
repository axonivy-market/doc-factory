package ch.ivyteam.ivy.addons.filemanager.restricted.database.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileType;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Record;

/**
 * NOT PUBLIC API
 * @author ec
 *
 */
public class DocumentOnServerGeneratorHelper {
	
	
	/**
	 * NOT PUBLIC API
	 * @param rst
	 * @param configuration
	 * @return
	 * @throws Exception
	 */
	public static DocumentOnServer buildDocumentOnServerWithResulSetRow(ResultSet rst, BasicConfigurationController configuration) throws Exception {
		
		assert(rst !=null):"Invalid ResultSet in buildDocumentOnServerWithResulSetRow method.";
		DocumentOnServer doc = buildDocumentOnServer(rst);
		
		if(configuration!=null && configuration.isActivateFileType()) {
			try {
				long id = rst.getInt("filetypeid");
				if(id>0) {
					FileType ft = new FileType();
					ft.setId(id);
					doc.setFileType(ft);
				}
			}catch(Exception ex) {
				//do nothing the file type is not mandatory
				Ivy.log().warn("WARNING while getting the file type on "+doc.getFilename()+ " "+ex.getMessage(), ex);
			}
		}
		
		return doc;
	}

	/**
	 * NOT PUBLIC API
	 * @param rst
	 * @return
	 * @throws SQLException
	 */
	public static DocumentOnServer buildDocumentOnServer(ResultSet rst)
			throws SQLException {
		DocumentOnServer doc =  new DocumentOnServer();
		doc.setId((long) rst.getInt("FileId"));
		doc.setFileID(String.valueOf(rst.getInt("FileId")));
		doc.setFilename(rst.getString("FileName"));
		doc.setPath(rst.getString("FilePath"));
		doc.setDisplayedPath(doc.getPath());
		doc.setFileSize(rst.getString("FileSize"));
		doc.setUserID(rst.getString("CreationUserId"));
		doc.setCreationDate(rst.getString("CreationDate"));
		doc.setCreationTime(rst.getString("CreationTime"));
		doc.setModificationUserID(rst.getString("ModificationUserId"));
		doc.setModificationDate(rst.getString("ModificationDate"));
		doc.setModificationTime(rst.getString("ModificationTime"));
		doc.setLocked(""+rst.getInt("Locked"));
		doc.setIsLocked(doc.getLocked().compareTo("1")==0);
		doc.setLockingUserID(rst.getString("LockingUserId"));
		doc.setDescription(rst.getString("Description"));
		try {
			int i = rst.getInt("versionnumber") > 0 ? rst.getInt("versionnumber") : 1;
			doc.setVersionnumber(i);
		}catch(Exception ex) {
			doc.setVersionnumber(1);
		}
		return doc;
	}
	
	/**
	 * NOT PUBLIC API
	 * @param recordList
	 * @param configuration
	 * @return
	 */
	public static List<DocumentOnServer> makeDocsWithRecordList(List<Record> recordList, BasicConfigurationController configuration) {
		List<DocumentOnServer>  al = new ArrayList<DocumentOnServer>();
		if(recordList==null || recordList.isEmpty()) {
			return al;
		}

		for(Record rec: recordList) {
			DocumentOnServer doc = new DocumentOnServer();
			doc.setFileID(rec.getField("FileId").toString());
			doc.setId(Long.parseLong(rec.getField("FileId").toString()));
			doc.setFilename(rec.getField("FileName").toString());
			doc.setPath(rec.getField("FilePath").toString());
			doc.setDisplayedPath(doc.getPath());
			doc.setFileSize(rec.getField("FileSize").toString());
			doc.setUserID(rec.getField("CreationUserId").toString());
			doc.setCreationDate(rec.getField("CreationDate").toString());
			doc.setCreationTime(rec.getField("CreationTime").toString());
			doc.setModificationUserID(rec.getField("ModificationUserId").toString());
			doc.setModificationDate(rec.getField("ModificationDate").toString());
			doc.setModificationTime(rec.getField("ModificationTime").toString());
			doc.setLocked(rec.getField("Locked").toString());
			doc.setIsLocked(doc.getLocked().compareTo("1")==0);
			doc.setLockingUserID(rec.getField("LockingUserId").toString());
			doc.setDescription(rec.getField("Description").toString());
			
			try{
				int i = Integer.parseInt(rec.getField("versionnumber").toString());
				if(i<=0) {
					i=1;
				}
				doc.setVersionnumber(i);
			}catch(Exception ex){
				doc.setVersionnumber(1);
			}
			try{
				doc.setExtension(doc.getFilename().substring(doc.getFilename().lastIndexOf(".")+1));
			}catch(Exception ex){
				//Ignore the Exception here
			}
			if(configuration.isActivateFileType()) {
				try{
					String fileTypeId = rec.getField("filetypeid").toString();
					if(!fileTypeId.trim().isEmpty()) {
						long id = Long.parseLong(rec.getField("filetypeid").toString());
						if(id>0) {
							FileType ft = new FileType();
							ft.setId(id);
							doc.setFileType(ft);
						}
					}
				}catch(Exception ex){
					//do nothing the file type is not mandatory
					Ivy.log().warn("WARNING while getting the file type on "+doc.getFilename()+ " "+ex.getMessage(), ex);
				}
			}
			if(configuration!=null && configuration.isActivateDirectoryTranslation()) {
				FolderOnServer fos = new FolderOnServer();
				fos.setPath(PathUtil.getParentDirectoryPath(doc.getPath()));
				
				String path;
				try {
					path = FolderOnServerTranslationHelper.getTranslatedPathForFolderOnServer(fos, configuration);
					doc.setDisplayedPath(path+"/"+doc.getFilename());
				} catch (Exception e) {
					Ivy.log().warn("Cannot get the translated directory path for the following document: "+doc.getPath());
				}
				
			}
			if(configuration.getDocumentListFilter()!=null) {
				if(configuration.getDocumentListFilter().accept(doc)) {
					al.add(doc);
				}
			} else {
				al.add(doc);
			}
		}
		return al;
	}

}
