package ch.ivyteam.ivy.addons.filemanager.database.sql;

public class FileActionSQLQueries {

	public static final String TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER = "FILEACTIONS_TABLENAMESPACE";
	public static final String TABLENAMESPACE_FOR_ACTIONTYPE_PLACEHOLDER = "ACTIONTYPE_TABLENAMESPACE";

	public static final String INSERT_FILEACTION = "INSERT INTO " + TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER + " (file_id,actiontype,usern,uname,ddate,ttime,adesc) VALUES (?,?,?,?,?,?,?)";

	public static final String GET_FILEACTIONTYPE_ID_BY_TYPE_NUMBER = "SELECT id FROM " + TABLENAMESPACE_FOR_ACTIONTYPE_PLACEHOLDER + " WHERE atype = ?";

	public static final String GET_FILEACTION_BY_ID = "SELECT * FROM " + 
			TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER + " FA, " + 
			TABLENAMESPACE_FOR_ACTIONTYPE_PLACEHOLDER + " FAT " +
			"WHERE FA.id=? AND FA.actiontype = FAT.atype ";
	
	public static final String GET_FILEACTIONS_BY_FILEID = "SELECT * FROM " + 
			TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER + " FA, " + 
			TABLENAMESPACE_FOR_ACTIONTYPE_PLACEHOLDER + " FAT " +
			"WHERE FA.file_id=? AND FA.actiontype = FAT.atype "+
			" ORDER BY FA.id DESC";

	public static final String UPDATE_FILEACTION = "UPDATE " + TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER + " SET actiontype = ?, usern = ?, uname = ?, ddate =?, ttime = ?, adesc = ? WHERE id = ?";

	public static final String GET_ALL_FILEACTIONTYPES = "SELECT * FROM " + TABLENAMESPACE_FOR_ACTIONTYPE_PLACEHOLDER;
	
	public static final String DELETE_FILEACTION_BYID = "DELETE FROM " + TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER + " WHERE id = ?";
	
	public static final String DELETE_FILEACTIONS_BY_FILEID = "DELETE FROM " + TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER + " WHERE file_id = ?";

}
