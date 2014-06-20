/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.fileaction;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
public class FileActionHistoryController extends AbstractFileActionHistoryController implements Serializable {

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

	public FileActionHistoryController() {
		this.config = new FileActionConfiguration();

	}

	public FileActionHistoryController(FileActionConfiguration fileActionConfiguration) {
		if(fileActionConfiguration==null) {
			this.config = new FileActionConfiguration();
		}else {
			this.config = fileActionConfiguration;
		}
	}

	@Override
	public void createNewActionHistory(long fileid, short actionType, String username, String actionInfos) throws Exception {
		if(fileid<=0) {
			throw new IllegalArgumentException("Invalid fileid parameter in FileActionHistoryController.createNewActionHistory");
		}
		if(!actionTypeExists(actionType)) {
			throw new IllegalArgumentException("Invalid actionType parameter in FileActionHistoryController.createNewActionHistory. This actionType does not exist. ActionType:"+actionType);
		}

		String sql = "INSERT INTO "+this.config.getFileActionHistoryTableNameSpace()+" (file_id,actiontype,usern,uname,ddate,ttime,adesc) VALUES (?,?,?,?,?,?,?)";
		if(username==null || username.trim().length()==0) {
			username=Ivy.session().getSessionUserName();
		}
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try {
				stmt = jdbcConnection.prepareStatement(sql);
				stmt.setLong(1, fileid);
				stmt.setShort(2, actionType);
				stmt.setString(3, username.trim());
				String uname = "";
				try {
					uname = Ivy.session().getSecurityContext().findUser(username).getFullName();
				}catch(Exception ex) {
					//do nothing
				}
				stmt.setString(4, uname==null?"":uname);
				stmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
				stmt.setTime(6, new java.sql.Time(new java.util.Date().getTime()));
				if(actionInfos==null || actionInfos.trim().length()==0) {
					actionInfos="";
				}
				if(actionInfos.trim().length()>1600) {
					stmt.setString(7, actionInfos.trim().substring(0, 1600));
				}else {
					stmt.setString(7, actionInfos.trim());
				}
				stmt.executeUpdate();

			}finally {
				if(stmt!=null) {
					try {
						stmt.close();
					} catch( SQLException ex) {
						Ivy.log().error("PreparedStatement cannot be closed in File Action.",ex);
					}
				}
			}
		} finally {
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}
	
	@Override
	public void createNewActionHistory(long fileid, short actionType, String username, String actionInfos, Connection con) throws Exception {
		if(fileid<=0) {
			throw new IllegalArgumentException("Invalid fileid parameter in FileActionHistoryController.createNewActionHistory");
		}
		if(!actionTypeExists(actionType, con)) {
			throw new IllegalArgumentException("Invalid actionType parameter in FileActionHistoryController.createNewActionHistory. This actionType does not exist.");
		}

		String sql = "INSERT INTO "+this.config.getFileActionHistoryTableNameSpace()+" (file_id,actiontype,usern,uname,ddate,ttime,adesc) VALUES (?,?,?,?,?,?,?)";
		if(username==null || username.trim().length()==0) {
			username=Ivy.session().getSessionUserName();
		}
		
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, fileid);
			stmt.setShort(2, actionType);
			stmt.setString(3, username.trim());
			String uname = "";
			try {
				uname = Ivy.session().getSecurityContext().findUser(username).getFullName();
			}catch(Exception ex) {
				//do nothing
			}
			stmt.setString(4, uname==null?"":uname);
			stmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
			stmt.setTime(6, new java.sql.Time(new java.util.Date().getTime()));
			if(actionInfos==null || actionInfos.trim().length()==0) {
				actionInfos="";
			}
			if(actionInfos.trim().length()>1600) {
				stmt.setString(7, actionInfos.trim().substring(0, 1600));
			}else {
				stmt.setString(7, actionInfos.trim());
			}
			stmt.executeUpdate();
			Ivy.log().debug("Write history "+this.config.getFileActionHistoryTableNameSpace());

		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in File Action.",ex);
				}
			}
		}
		 
	}

	@Override
	public boolean actionTypeExists(short actionType) throws Exception {
		boolean r = false;
		String sql = "SELECT id FROM "+this.config.getFileActionTypeNameSpace()+" WHERE atype=?";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			ResultSet rst = null;
			try {
				stmt = jdbcConnection.prepareStatement(sql);
				stmt.setShort(1, actionType);
				rst = stmt.executeQuery();
				r = rst.next();
				rst.close();
			}finally {
				if(rst!=null) {
					DatabaseUtil.close(rst);
				}
				if(stmt!=null) {
					try {
						stmt.close();
					} catch( SQLException ex) {
						Ivy.log().error("PreparedStatement cannot be closed in File Action.",ex);
					}
				}
			}
		} finally {
			if(connection!=null ) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return r;
	}
	
	@Override
	public boolean actionTypeExists(short actionType, Connection con) throws Exception {
		boolean r = false;
		String sql = "SELECT id FROM "+this.config.getFileActionTypeNameSpace()+" WHERE atype=?";
		
		PreparedStatement stmt = null;
		ResultSet rst = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setShort(1, actionType);
			rst = stmt.executeQuery();
			r = rst.next();
			rst.close();
		} finally {
			if(rst!=null) {
				DatabaseUtil.close(rst);
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in File Action.",ex);
				}
			}
		}

		return r;
	}

	@Override
	public List<FileActionType> getAllFileActionTypes(String lang) throws Exception {
		if(lang == null || lang.trim().length()==0) {
			lang="en";
		}
		List<FileActionType> atypes = new ArrayList<FileActionType>();
		String sql = "SELECT * FROM "+this.config.getFileActionTypeNameSpace();
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			ResultSet rst = null;
			try {
				stmt = jdbcConnection.prepareStatement(sql);
				rst = stmt.executeQuery();
				while(rst.next()) {
					FileActionType fa = new FileActionType();
					fa.setId(rst.getInt("id"));
					fa.setType(rst.getInt("atype"));
					try {
						fa.setDesc(rst.getString(lang.trim().toLowerCase()));
					}catch(Exception ex) {// if the desired language column does not exist, we switch to english per default
						fa.setDesc(rst.getString("en"));
					}
					atypes.add(fa);
				}
				rst.close();
			}finally {
				if(rst!=null) {
					DatabaseUtil.close(rst);
				}
				if(stmt!=null) {
					try {
						stmt.close();
					} catch( SQLException ex) {
						Ivy.log().error("PreparedStatement cannot be closed in File Action.",ex);
					}
				}
			}
		} finally {
			if(connection!=null ) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return atypes;
	}

	@Override
	public List<FileAction> getFileActions(long fileid, String lang) throws Exception {
		if(fileid <=0) {
			throw new IllegalArgumentException("Invalid fileid parameter in FileActionHistoryController.getFileActions");
		}
		if(lang == null || lang.trim().length()==0) {
			lang="en";
		}
		List<FileAction> actions = new ArrayList<FileAction>();
		String sql = "SELECT * FROM "+this.config.getFileActionHistoryTableNameSpace()+", "+this.config.getFileActionTypeNameSpace()+" WHERE file_id = ? AND actiontype = atype ORDER BY "+this.config.getFileActionHistoryTableNameSpace()+".id DESC";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			ResultSet rst = null;
			try{
				Ivy.log().debug(sql);
				stmt = jdbcConnection.prepareStatement(sql);
				stmt.setLong(1, fileid);

				rst = stmt.executeQuery();
				while(rst.next()) {
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
				rst.close();
			}finally {
				if(rst!=null) {
					DatabaseUtil.close(rst);
				}
				if(stmt!=null) {
					try {
						stmt.close();
					} catch( SQLException ex) {
						Ivy.log().error("PreparedStatement cannot be closed in File Action.",ex);
					}
				}
			}
		} finally {
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return actions;

	}

	@Override
	public boolean fileActionTypeTranslationExist(String lang) throws Exception{
		if(lang==null || lang.trim().length()==0) {
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
			ResultSet rst = null;
			try {
				stmt = jdbcConnection.prepareStatement(sql);
				rst = stmt.executeQuery();
				ResultSetMetaData rsmd = rst.getMetaData();
				int nb = rsmd.getColumnCount();
				for(int i=0; i<nb; i++) {
					if(lang.equals(rsmd.getColumnName(i))) {
						b=true;
						break;
					}
				}
				rst.close();
			}finally {
				if(rst!=null) {
					DatabaseUtil.close(rst);
				}
				if(stmt!=null) {
					try {
						stmt.close();
					} catch( SQLException ex) {
						Ivy.log().error("PreparedStatement cannot be closed in File Action.",ex);
					}
				}
			}
		} finally {
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
		if(config==null) {
			this.config = new FileActionConfiguration();
		} else {
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
	private IExternalDatabase getDatabase() throws Exception {
		if(this.database==null) {
			final String _nameOfTheDatabaseConnection = this.config.getIvyDBConnectionName();
			this.database = Ivy.session().getSecurityContext().executeAsSystemUser(new Callable<IExternalDatabase>() {
				public IExternalDatabase call() throws Exception {
					IExternalDatabaseApplicationContext context = (IExternalDatabaseApplicationContext)Ivy.wf().getApplication().getAdapter(IExternalDatabaseApplicationContext.class);
					return context.getExternalDatabase(_nameOfTheDatabaseConnection);
				}
			});
		}
		return this.database;	
	}

}
