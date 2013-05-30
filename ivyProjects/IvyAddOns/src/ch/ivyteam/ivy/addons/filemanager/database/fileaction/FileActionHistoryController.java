/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.fileaction;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import ch.ivyteam.db.jdbc.DatabaseUtil;
import ch.ivyteam.ivy.addons.filemanager.FileAction;
import ch.ivyteam.ivy.addons.filemanager.FileActionType;
import ch.ivyteam.ivy.db.IExternalDatabase;
import ch.ivyteam.ivy.db.IExternalDatabaseApplicationContext;
import ch.ivyteam.ivy.db.IExternalDatabaseRuntimeConnection;
import ch.ivyteam.ivy.environment.EnvironmentNotAvailableException;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.Time;

/**
 * @author ec
 *
 */
public class FileActionHistoryController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8399180559766999962L;
	
	public static short FILE_CREATED_ACTION = 1;
	public static short FILE_CONTENT_CHANGED_ACTION = 2;
	public static short FILE_DESCRIPTION_CHANGED_ACTION = 3;
	public static short FILE_RENAMED_ACTION = 4;
	public static short FILE_DELETED_ACTION = 5;
	public static short FILE_DOWNLOADED_ACTION = 6;
	public static short FILE_PRINTED_ACTION = 7;
	public static short FILE_OPENED_ACTION = 8;
	public static short FILE_COPY_PASTE_ACTION = 9;
	public static short FILE_MOVED_ACTION = 10;
	public static short FILE_NEW_VERSION_ACTION = 11;
	public static short FILE_VERSION_ROLLBACK_ACTION = 12;

	
	private IExternalDatabase database=null;
	private FileActionConfiguration config = null;

	public FileActionHistoryController()
	{
		this.config = new FileActionConfiguration();

	}

	public FileActionHistoryController(FileActionConfiguration fileActionConfiguration)
	{
		if(fileActionConfiguration==null)
		{
			this.config = new FileActionConfiguration();
		}else
		{
			
			this.config = fileActionConfiguration;
		}

	}

	/**
	 * creates a new file action history record.
	 * @param fileid: the file id on which the action was done
	 * @param actionType: the action type
	 * @param username: the user's name who has performed the action (eg. ec, sk ect..)
	 * @param actionInfos: if any, a description of the action.
	 * @throws Exception
	 */
	public void createNewActionHistory(long fileid, short actionType, String username, String actionInfos) throws Exception
	{
		if(fileid<=0)
		{
			throw new IllegalArgumentException("Invalid fileid parameter in FileActionHistoryController.createNewActionHistory");
		}
		if(!actionTypeExists(actionType))
		{
			throw new IllegalArgumentException("Invalid actionType parameter in FileActionHistoryController.createNewActionHistory. This actionType does not exist.");
		}

		String sql = "INSERT INTO "+this.config.getFileActionHistoryTableNameSpace()+" (file_id,actiontype,usern,uname,ddate,ttime,adesc) VALUES (?,?,?,?,?,?,?)";
		if(username==null || username.trim().length()==0)
		{
			username=Ivy.session().getSessionUserName();
		}
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(sql);
				stmt.setLong(1, fileid);
				stmt.setShort(2, actionType);
				stmt.setString(3, username.trim());
				String uname = "";
				try{
					uname = Ivy.session().getSecurityContext().findUser(username).getFullName();
				}catch(Exception ex)
				{
					//do nothing
				}
				stmt.setString(4, uname==null?"":uname);
				stmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
				stmt.setTime(6, new java.sql.Time(new java.util.Date().getTime()));
				if(actionInfos==null || actionInfos.trim().length()==0)
				{
					actionInfos="";
				}
				if(actionInfos.trim().length()>1600)
				{
					stmt.setString(7, actionInfos.trim().substring(0, 1600));
				}else{
					stmt.setString(7, actionInfos.trim());
				}
				stmt.executeUpdate();

			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}
	
	/**
	 * Creates a new file action history record.<br>
	 * It takes a java.sql.connection as parameter to be able to be called from within other methods <br>
	 * without locking another database connection during its processing.<br>
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * <b>Important: </b>this method does not release the given java.sql.Connection. It is up to the calling method to do that.
	 * @param fileid: the file id on which the action was done
	 * @param actionType: a short number representing the action type. The action types are stored in the "fileactiontype" table.
	 * @param username: the user's name who has performed the action (eg. ec, sk ect..)
	 * @param actionInfos: if any, a description of the action.
	 * @param con a java.sql.connection to the database.
	 * @throws Exception if the file id is not a valid id, if the file action type does not exist, or in case of SQLException.
	 */
	public void createNewActionHistory(long fileid, short actionType, String username, String actionInfos, Connection con) throws Exception
	{
		if(fileid<=0)
		{
			throw new IllegalArgumentException("Invalid fileid parameter in FileActionHistoryController.createNewActionHistory");
		}
		if(!actionTypeExists(actionType, con))
		{
			throw new IllegalArgumentException("Invalid actionType parameter in FileActionHistoryController.createNewActionHistory. This actionType does not exist.");
		}

		String sql = "INSERT INTO "+this.config.getFileActionHistoryTableNameSpace()+" (file_id,actiontype,usern,uname,ddate,ttime,adesc) VALUES (?,?,?,?,?,?,?)";
		if(username==null || username.trim().length()==0)
		{
			username=Ivy.session().getSessionUserName();
		}
		
		PreparedStatement stmt = null;
		try{
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, fileid);
			stmt.setShort(2, actionType);
			stmt.setString(3, username.trim());
			String uname = "";
			try{
				uname = Ivy.session().getSecurityContext().findUser(username).getFullName();
			}catch(Exception ex)
			{
				//do nothing
			}
			stmt.setString(4, uname==null?"":uname);
			stmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
			stmt.setTime(6, new java.sql.Time(new java.util.Date().getTime()));
			if(actionInfos==null || actionInfos.trim().length()==0)
			{
				actionInfos="";
			}
			if(actionInfos.trim().length()>1600)
			{
				stmt.setString(7, actionInfos.trim().substring(0, 1600));
			}else{
				stmt.setString(7, actionInfos.trim());
			}
			stmt.executeUpdate();
			Ivy.log().info("Write history "+this.config.getFileActionHistoryTableNameSpace());

		}finally{
			DatabaseUtil.close(stmt);
		}
		 
	}

	/**
	 * Check if the given action Type exists in the table.
	 * @param actionType a short number representing the action type. The action types are stored in the "fileactiontype" table.
	 * @return true if the action type was found, else false.
	 * @throws Exception
	 */
	public boolean actionTypeExists(short actionType) throws Exception
	{
		boolean r = false;
		String sql = "SELECT id FROM "+this.config.getFileActionTypeNameSpace()+" WHERE atype=?";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(sql);
				stmt.setShort(1, actionType);
				ResultSet rst = stmt.executeQuery();
				r = rst.next();

			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return r;
	}
	
	/**
	 * Check if the given action Type exists in the table.
	 * It takes a java.sql.connection as parameter to be able to be called from within other methods <br>
	 * without locking another database connection during its processing.<br>
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * <b>Important: </b>this method does not release the given java.sql.Connection. It is up to the calling method to do that.
	 * @param actionType a short number representing the action type. The action types are stored in the "fileactiontype" table.
	 * @param con a java.sql.connection to the database.
	 * @return
	 * @throws Exception
	 */
	public boolean actionTypeExists(short actionType, Connection con) throws Exception
	{
		boolean r = false;
		String sql = "SELECT id FROM "+this.config.getFileActionTypeNameSpace()+" WHERE atype=?";
		
		PreparedStatement stmt = null;
		try{
			stmt = con.prepareStatement(sql);
			stmt.setShort(1, actionType);
			ResultSet rst = stmt.executeQuery();
			r = rst.next();
		}finally{
			DatabaseUtil.close(stmt);
		}

		return r;
	}

	/**
	 * returns all the action types with the action description in the right language.<br>
	 * The language is stored in a column which name is the ISO language code in lowercase: "en", "fr", "de"...<br>
	 * If the language column does not exist the English translation will be inserted.
	 * @param lang: the desired language for the description. "en", "fr", "de"....
	 * @return the java.util.List of ch.ivyteam.ivy.addons.filemanager.FileActionType objects
	 * @throws Exception in case of sql exceptions etc...
	 */
	public List<FileActionType> getAllFileActionTypes(String lang) throws Exception
	{
		if(lang == null || lang.trim().length()==0)
		{
			lang="en";
		}
		List<FileActionType> atypes = new ArrayList<FileActionType>();
		String sql = "SELECT * FROM "+this.config.getFileActionTypeNameSpace();
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(sql);
				ResultSet rst = stmt.executeQuery();
				while(rst.next())
				{
					FileActionType fa = new FileActionType();
					fa.setId(rst.getInt("id"));
					fa.setType(rst.getInt("atype"));
					try{
						fa.setDesc(rst.getString(lang.trim().toLowerCase()));
					}catch(Exception ex)
					{// if the desired language column does not exist, we switch to english per default
						fa.setDesc(rst.getString("en"));
					}
					atypes.add(fa);
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return atypes;
	}

	/**
	 * Gets the registered actions on a given file. The action desc are given in the given language if this translation exists.<br>
	 * The language is stored in a column which name is the ISO language code in lowercase: "en", "fr", "de"...<br>
	 * @param fileid : the file id
	 * @return : the desired language for the action description. This description is stored in a language column in the fileactiontype table.<br>
	 * If the language column cannot be found, the english translation will be taken instead.
	 * @throws Exception
	 */
	public List<FileAction> getFileActions(long fileid, String lang) throws Exception {
		if(fileid <=0)
		{
			throw new IllegalArgumentException("Invalid fileid parameter in FileActionHistoryController.getFileActions");
		}
		if(lang == null || lang.trim().length()==0)
		{
			lang="en";
		}
		List<FileAction> actions = new ArrayList<FileAction>();
		String sql = "SELECT * FROM "+this.config.getFileActionHistoryTableNameSpace()+", "+this.config.getFileActionTypeNameSpace()+" WHERE file_id = ? AND actiontype = atype ORDER BY "+this.config.getFileActionHistoryTableNameSpace()+".id DESC";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				Ivy.log().info(sql);
				stmt = jdbcConnection.prepareStatement(sql);
				stmt.setLong(1, fileid);

				ResultSet rst = stmt.executeQuery();
				while(rst.next())
				{
					FileAction fa = new FileAction();
					fa.setId(rst.getLong("id"));
					fa.setFileid(rst.getLong("file_id"));
					fa.setUsername(rst.getString("usern")); 
					fa.setFullUsername(rst.getString("uname"));	
					fa.setInfo(rst.getString("adesc"));
					try{
						fa.setDesc(rst.getString(lang.trim().toLowerCase()));
					}catch(Exception ex)
					{// if the desired language column does not exist, we switch to English per default
						fa.setDesc(rst.getString("en"));
					}
					DateFormat formatter = null;
					if(lang.equalsIgnoreCase("en"))
					{
						formatter = new SimpleDateFormat("yyyy-MM-dd");
					}else{
						formatter = new SimpleDateFormat("dd.MM.yyyy");
					}
					fa.setDate(new Date(formatter.format(rst.getDate("ddate"))));
					fa.setTime(new Time(DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMAN).format(rst.getTime("ttime"))));

					actions.add(fa);
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return actions;

	}

	/**
	 * Checks if the given language code is used as translation in the fileActionTypes table.<br>
	 * Calling fileActionTypeTranslationExist("it") will return true if a column named "it" exists in the fileActionTypes table for the Italian translation.<br>
	 * Per convention the translation column names are spelled in lowercase and represent the ISO Code of the language (en, fr, de ...)
	 * @param lang
	 * @return true if a column representing the given language translation exists
	 * @throws Exception
	 */
	public boolean fileActionTypeTranslationExist(String lang) throws Exception{
		if(lang==null || lang.trim().length()==0)
		{
			throw new IllegalArgumentException("Invalid lang parameter in FileActionHistoryController.fileActionTypeTranslationExist");
		}
		lang = lang.trim().toLowerCase();
		boolean b = false;
		String sql = "SELECT * FROM "+this.config.getFileActionTypeNameSpace();
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(sql);
				ResultSet rst = stmt.executeQuery();
				ResultSetMetaData rsmd = rst.getMetaData();
				int nb = rsmd.getColumnCount();
				for(int i=0; i<nb; i++){
					if(lang.equals(rsmd.getColumnName(i)))
					{
						b=true;
						break;
					}
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return b;
	}
	
	


	/**
	 * @param config the config to set
	 */
	public void setConfig(FileActionConfiguration config) {
		if(config==null)
		{
			this.config = new FileActionConfiguration();
		}else{
			this.config = config;
		}
	}

	/**
	 * @return the config
	 */
	public FileActionConfiguration getConfig() {
		return config;
	}

	/**
	 * used to get Ivy IExternalDatabase object with given user friendly name of Ivy Database configuration
	 * @param _nameOfTheDatabaseConnection: the user friendly name of Ivy Database configuration
	 * @return the IExternalDatabase object
	 * @throws Exception 
	 * @throws EnvironmentNotAvailableException 
	 */
	private IExternalDatabase getDatabase() throws Exception{
		if(this.database==null){
			final String _nameOfTheDatabaseConnection = this.config.getIvyDBConnectionName();
			this.database = Ivy.session().getSecurityContext().executeAsSystemUser(new Callable<IExternalDatabase>(){
				public IExternalDatabase call() throws Exception {
					IExternalDatabaseApplicationContext context = (IExternalDatabaseApplicationContext)Ivy.wf().getApplication().getAdapter(IExternalDatabaseApplicationContext.class);
					return context.getExternalDatabase(_nameOfTheDatabaseConnection);
				}
			});
		}
		return this.database;	
	}

}
