package db2.guard.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.Document;
import org.dom4j.Element;

import db2.guard.tools.IPTransfer;
import db2.guard.tools.SQLHelper;

public class DB2IdxInfo {
	//select TABSCHEMA||'.'||TABNAME as TAB, INDNAME,COLNAMES,COLCOUNT,INDEXTYPE,STATS_TIME,LASTUSED,fullkeycard,INDCARD,COMPRESSION,CREATE_TIME 

	public Document db2IdxInfo(Connection oCon,Document doc){
			
			  try {
					 Element root = doc.getRootElement();
				ResultSet oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_IdxOrphanInfo);
					  Element elemTABOrphan = root.addElement("idxOrphanInfo");
					  elemTABOrphan.addElement("title").addText("表的监控:长时间不用的垃圾索引");
					while(oRS.next()){
					  Element elemValue = elemTABOrphan.addElement("index");
					  elemValue.addElement("idxTabName").addText(oRS.getString("TAB") );
					  elemValue.addElement("idxName").addText(oRS.getString("INDNAME"));
					  elemValue.addElement("idxType").addText(oRS.getString("INDEXTYPE")) ;
					  elemValue.addElement("tabStatsTime").addText((oRS.getString("STATS_TIME")==null) ? "'0001-01-01'":oRS.getString("STATS_TIME"));
					  elemValue.addElement("tabAccessTime").addText(oRS.getString("LASTUSED"));
					  elemValue.addElement("idxCards").addText(oRS.getString("fullkeycard"));
					  elemValue.addElement("idxRows").addText(oRS.getString("INDCARD"));
					  elemValue.addElement("idxColNum").addText(oRS.getString("COLCOUNT"));
					  elemValue.addElement("idxColName").addText(oRS.getString("COLNAMES"));
					  elemValue.addElement("tabCreatTime").addText(oRS.getString("CREATE_TIME"));
					  elemValue.addElement("tabZip").addText(oRS.getString("COMPRESSION"));
					}
					oRS.close();
					
//"select TABSCHEMA||'.'||TABNAME as TAB, INDNAME,COLNAMES,COLCOUNT,INDEXTYPE,STATS_TIME,LASTUSED,
					//fullkeycard,INDCARD,COMPRESSION,CREATE_TIME 
					 oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_IdxStatsInfo);
				  Element elemDB2SET = root.addElement("idxStatsInfo");
				  elemDB2SET.addElement("title").addText("索引的监控:长时间未刷新统计信息的索引");
				while(oRS.next()){
					  Element elemValue = elemDB2SET.addElement("index");
					  elemValue.addElement("idxTabName").addText(oRS.getString("TAB") );
					  elemValue.addElement("idxName").addText(oRS.getString("INDNAME"));
					  elemValue.addElement("idxType").addText(oRS.getString("INDEXTYPE")) ;
					  elemValue.addElement("tabStatsTime").addText((oRS.getString("STATS_TIME")==null) ? "'0001-01-01'":oRS.getString("STATS_TIME"));
					  elemValue.addElement("tabAccessTime").addText(oRS.getString("LASTUSED"));
					  elemValue.addElement("idxCards").addText(oRS.getString("fullkeycard"));
					  elemValue.addElement("idxRows").addText(oRS.getString("INDCARD"));
					  elemValue.addElement("idxColNum").addText(oRS.getString("COLCOUNT"));
					  elemValue.addElement("idxColName").addText(oRS.getString("COLNAMES"));
					  elemValue.addElement("tabCreatTime").addText(oRS.getString("CREATE_TIME"));
					  elemValue.addElement("tabZip").addText(oRS.getString("COMPRESSION"));
				  
				}
				oRS.close();
		
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
			  return doc;
		  }

	 

}
