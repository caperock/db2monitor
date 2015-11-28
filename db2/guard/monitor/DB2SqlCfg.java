package db2.guard.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.Document;
import org.dom4j.Element;

import db2.guard.tools.IPTransfer;
import db2.guard.tools.SQLHelper;

public class DB2SqlCfg {
// NUM_EXECUTIONS, AVERAGE_EXECUTION_TIME_S, STMT_SORTS,   SORTS_PER_EXECUTION, STMT_TEXT
	public Document db2SqlInfo(Connection oCon,Document doc){
		
		  try {
			  
			  ResultSet oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_TopTimeSql);
			  Element root = doc.getRootElement();
				 
			  Element elemDB2AvgSql = root.addElement("SqlAvgSInfo");
			  elemDB2AvgSql.addElement("title").addText("SQL监控与优化:运行最慢的SQL语句");
			while(oRS.next()){
			  Element elemValue = elemDB2AvgSql.addElement("topAvgSSql");
			  elemValue.addElement("topAvgSecond").addText(Long.toString(oRS.getLong("AVERAGE_EXECUTION_TIME_S")));
			  elemValue.addElement("topNum").addText(Long.toString(oRS.getLong("NUM_EXECUTIONS")) );			  
			  elemValue.addElement("topSort").addText(Long.toString(oRS.getLong("STMT_SORTS") ));			 
			  elemValue.addElement("topSortPer").addText(Long.toString(oRS.getLong("SORTS_PER_EXECUTION") ));
			  elemValue.addElement("topSql").addText(oRS.getString("STMT_TEXT"));	
			}
			oRS.close();
			
			 oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_TopExesNumSql);
			 
			
			  Element elemDB2SET = root.addElement("SqlExesInfo");
			  elemDB2SET.addElement("title").addText("SQL监控与优化：执行次数最多的SQL语句");
			while(oRS.next()){
			  Element elemValue = elemDB2SET.addElement("topExesSql");
			  elemValue.addElement("topNum").addText(Long.toString(oRS.getLong("NUM_EXECUTIONS")) );
			  elemValue.addElement("topAvgSecond").addText(Long.toString(oRS.getLong("AVERAGE_EXECUTION_TIME_S")));
			  elemValue.addElement("topSort").addText(Long.toString(oRS.getLong("STMT_SORTS") ));			 
			  elemValue.addElement("topSortPer").addText(Long.toString(oRS.getLong("SORTS_PER_EXECUTION") ));
			  elemValue.addElement("topSql").addText(oRS.getString("STMT_TEXT"));	
			}
			oRS.close();
			
			
			 oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_TopSortSql);
			 
			 
			  Element elemDB2SortSql = root.addElement("SqlSortInfo");
			  elemDB2SortSql.addElement("title").addText("SQL监控与优化:最多排序的SQL语句");
			while(oRS.next()){
			  Element elemValue = elemDB2SortSql.addElement("topSortSql");
			  elemValue.addElement("topSort").addText(Long.toString(oRS.getLong("STMT_SORTS") ));			 
			  elemValue.addElement("topSortPer").addText(Long.toString(oRS.getLong("SORTS_PER_EXECUTION") ));
			  elemValue.addElement("topNum").addText(Long.toString(oRS.getLong("NUM_EXECUTIONS")) );
			  elemValue.addElement("topAvgSecond").addText(Long.toString(oRS.getLong("AVERAGE_EXECUTION_TIME_S")));			  
			  elemValue.addElement("topSql").addText(oRS.getString("STMT_TEXT"));	
			}
			oRS.close();
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return doc;
	  }

	
}
