package ch.ivyteam.ivy.addons.filemanager.database.sql;

public class ThumbnailSQLQueries {
	
	public static final String THUMBNAIL_TABLENAMESPACE_PLACEHOLDER = "THUMBNAIL_TABLENAMESPACE";
	public static final String FILE_IDS_PLACEHOLDER = "FILE_IDS";
	
	/**
	 * SELECT * FROM --THUMBNAIL_TABLENAMESPACE_PLACEHOLDER-- WHERE org_file_id = ?
	 */
	public static String SELECT_THUMBNAIL_BY_FILE_ID ="SELECT * FROM "+THUMBNAIL_TABLENAMESPACE_PLACEHOLDER+" WHERE org_file_id = ?";
	
	/**
	 * INSERT INTO --THUMBNAIL_TABLENAMESPACE_PLACEHOLDER-- (org_file_id, creation_date, creation_time, use_default, org_modificationDate, org_modificationTime) VALUES (?,?,?,?,?,?)
	 */
	public static String INSERT_THUMBNAIL_WITHOUT_CONTENT ="INSERT INTO "+ THUMBNAIL_TABLENAMESPACE_PLACEHOLDER
			+ " (org_file_id, creation_date, creation_time, use_default, org_modificationDate, org_modificationTime) VALUES (?,?,?,?,?,?)";
	
	/**
	 * INSERT INTO --THUMBNAIL_TABLENAMESPACE_PLACEHOLDER-- (org_file_id, thumb_content, creation_date, creation_time, use_default, org_modificationDate, org_modificationTime) VALUES (?,?,?,?,?,?,?)
	 */
	public static String INSERT_THUMBNAIL_WITH_CONTENT ="INSERT INTO "+ THUMBNAIL_TABLENAMESPACE_PLACEHOLDER
			+ " (org_file_id, thumb_content, creation_date, creation_time, use_default, org_modificationDate, org_modificationTime) VALUES (?,?,?,?,?,?,?)";
	
	/**
	 * DELETE FROM --THUMBNAIL_TABLENAMESPACE_PLACEHOLDER-- WHERE org_file_id = ?
	 */
	public static String DELETE_TUMBNAIL_BY_FILE_ID = "DELETE FROM " + THUMBNAIL_TABLENAMESPACE_PLACEHOLDER+ " WHERE org_file_id = ?";
	
	/**
	 * SELECT * FROM --THUMBNAIL_TABLENAMESPACE_PLACEHOLDER-- WHERE org_file_id in ( --FILE_IDS_PLACEHOLDER-- )
	 */
	public static String SELECT_THUMBNAILS_LIST_BY_FILE_IDS = "SELECT * FROM "+ THUMBNAIL_TABLENAMESPACE_PLACEHOLDER+ " WHERE org_file_id in ("+FILE_IDS_PLACEHOLDER+")";
}
