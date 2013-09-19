/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import ch.ivyteam.db.jdbc.DatabaseUtil;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Record;

/**
 * @author ec
 *
 */
public class SqlPersistenceHelper {
	
	/**
	 * This method is a helper method to build a list of Record from a sql ResultSet.<br>
	 * All the values from the records are set as String.
	 * @param rst the ResulSet resulting from a previous sql query result
	 * @return a ch.ivyteam.ivy.scripting.objects.List<ch.ivyteam.ivy.scripting.objects.Record>
	 * @throws Exception
	 */
	public static List<Record> getRecordsListFromResulSet(ResultSet rst) throws Exception{
		if(rst == null){
			throw(new SQLException("Invalid ResultSet","ResultSet Null"));
		}

		List<Record> recordList= (List<Record>) List.create(Record.class);
		try{
			ResultSetMetaData rsmd = rst.getMetaData();
			int numCols = rsmd.getColumnCount();
			List<String> colNames= List.create(String.class);
			for(int i=1; i<=numCols; i++){
				colNames.add(rsmd.getColumnName(i));
			}
			while(rst.next()){
				List<Object> values = List.create(numCols);
				for(int i=1; i<=numCols; i++){

					if(rst.getString(i)==null)
						values.add(" ");
					else values.add(rst.getString(i));
				}
				Record rec = new Record(colNames,values);
				recordList.add(rec);
			}
		}catch(Exception ex){
			Ivy.log().error(ex.getMessage(), ex);
		}finally
		{
			DatabaseUtil.close(rst);
		}
		return recordList;
	}

}
