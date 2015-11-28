package db2.guard.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.Document;
import org.dom4j.Element;

import db2.guard.tools.IPTransfer;
import db2.guard.tools.SQLHelper;

public class DB2TabInfo {
	//"select TABSCHEMA||'.'||TABNAME as TAB,CARD,COLCOUNT,STATS_TIME,LASTUSED,TBSPACE,INDEX_TBSPACE,LONG_TBSPACE,CREATE_TIME,ALTER_TIME,COMPRESSION from syscat.tables WHERE OWNERTYPE='U' AND TYPE='T' order by LASTUSED asc,CARD desc COLCOUNT desc fetch first 50 rows only with ur ";

	public Document db2TabInfo(Connection oCon,Document doc){
			
			  try {
				ResultSet oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strTableRunStatsInfo);
				 
				 Element root = doc.getRootElement();
				//"select A.TABSCHEMA||'.'||A.TABNAME as TAB,DATA_OBJECT_PAGES,INDEX_OBJECT_PAGES,ROWS_READ,ROWS_WRITTEN,
					//CARD,STATS_TIME,TBSPACE,INDEX_TBSPACE,TAB_TYPE,COMPRESSION FROM SYSIBMADM.SNAPTAB A,syscat.tables B where OWNERTYPE='U' AND TYPE='T' and A.TABSCHEMA=B.TABSCHEMA AND A.TABNAME = B.TABNAME order by (2.5* ROWS_WRITTEN + ROWS_READ) desc fetch first 50 rows only with ur";
					oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_HotTableInfo);
					 Element elemHotTable = root.addElement("tabHotInfo");
					  elemHotTable.addElement("title").addText("表的监控:读写最频繁的热表");
					while(oRS.next()){
					  Element elemValue = elemHotTable.addElement("table");
					  elemValue.addElement("tabName").addText(oRS.getString("TAB") );
					  elemValue.addElement("tabDatPage").addText(oRS.getString("DATA_OBJECT_PAGES") );
					  elemValue.addElement("tabIdxPage").addText((oRS.getString("INDEX_OBJECT_PAGES")==null) ? "0": oRS.getString("INDEX_OBJECT_PAGES"));
					  elemValue.addElement("tabRead").addText(Long.toString(oRS.getLong("ROWS_READ")) );
					  elemValue.addElement("tabWrite").addText(Long.toString(oRS.getLong("ROWS_WRITTEN") ));				  
					  elemValue.addElement("tabTotalRow").addText(oRS.getString("CARD"));
					  elemValue.addElement("tabType").addText(oRS.getString("TAB_TYPE"));
					  elemValue.addElement("tabStatsTime").addText((oRS.getString("STATS_TIME")==null) ? "'0001-01-01'":oRS.getString("STATS_TIME"));
					  elemValue.addElement("tabAccessTime").addText(oRS.getString("LASTUSED"));
					  elemValue.addElement("tabTabsp").addText(oRS.getString("TBSPACE"));
					  elemValue.addElement("tabIdxTabsp").addText((oRS.getString("INDEX_TBSPACE") == null) ? oRS.getString("TBSPACE"): oRS.getString("INDEX_TBSPACE") );
					  elemValue.addElement("tabCreatTime").addText(oRS.getString("CREATE_TIME"));
					  elemValue.addElement("tabAlertTime").addText(oRS.getString("ALTER_TIME"));
					  elemValue.addElement("tabZip").addText(oRS.getString("COMPRESSION"));
					}
					oRS.close();
					
					
					 
							
						oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_LostIdxInfo);
						 Element elemIdxLost = root.addElement("tabLostIdxInfo");
						  elemIdxLost.addElement("title").addText("表的监控:大量表扫描需要建立索引的表");
						while(oRS.next()){
						  Element elemValue = elemIdxLost.addElement("index");
						  elemValue.addElement("tabName").addText(oRS.getString("TAB") );
						  elemValue.addElement("tabType").addText(oRS.getString("TAB_TYPE"));					 				
						  elemValue.addElement("tabRead").addText(Long.toString(oRS.getLong("ROWS_READ")) );
						  elemValue.addElement("tabScan").addText(oRS.getString("table_scans"));					}
						oRS.close();
					 
					oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strTableOrphanInfo);
					  Element elemTABOrphan = root.addElement("tabOrphanInfo");
					  elemTABOrphan.addElement("title").addText("表的监控:长时间不用的垃圾表");
					while(oRS.next()){
					  Element elemValue = elemTABOrphan.addElement("table");
					  elemValue.addElement("tabName").addText(oRS.getString("TAB") );
					  elemValue.addElement("tabTotalRow").addText(oRS.getString("CARD"));
					  elemValue.addElement("tabTotalColumn").addText(oRS.getString("COLCOUNT"));
					  elemValue.addElement("tabStatsTime").addText((oRS.getString("STATS_TIME")==null) ? "'0001-01-01'":oRS.getString("STATS_TIME"));
					  elemValue.addElement("tabAccessTime").addText(oRS.getString("LASTUSED"));
					  elemValue.addElement("tabTabsp").addText(oRS.getString("TBSPACE"));
					  elemValue.addElement("tabIdxTabsp").addText((oRS.getString("INDEX_TBSPACE") == null) ? oRS.getString("TBSPACE"): oRS.getString("INDEX_TBSPACE") );
					  elemValue.addElement("tabCreatTime").addText(oRS.getString("CREATE_TIME"));
					  elemValue.addElement("tabAlertTime").addText(oRS.getString("ALTER_TIME"));
					  elemValue.addElement("tabZip").addText(oRS.getString("COMPRESSION"));
					}
					oRS.close();
					 oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strTableRunStatsInfo);
				  Element elemDB2SET = root.addElement("tabStatsInfo");
				  elemDB2SET.addElement("title").addText("表的监控:长时间未刷新统计信息的表");
				while(oRS.next()){
				  Element elemValue = elemDB2SET.addElement("table");
				  elemValue.addElement("tabName").addText(oRS.getString("TAB") );
				  elemValue.addElement("tabTotalRow").addText(oRS.getString("CARD"));
				  elemValue.addElement("tabTotalColumn").addText(oRS.getString("COLCOUNT"));
				  elemValue.addElement("tabStatsTime").addText((oRS.getString("STATS_TIME")==null) ? "'0001-01-01'":oRS.getString("STATS_TIME"));
				  elemValue.addElement("tabAccessTime").addText(oRS.getString("LASTUSED"));
				  elemValue.addElement("tabTabsp").addText(oRS.getString("TBSPACE"));
				  elemValue.addElement("tabIdxTabsp").addText((oRS.getString("INDEX_TBSPACE") == null) ? oRS.getString("TBSPACE"): oRS.getString("INDEX_TBSPACE") );				  
				  elemValue.addElement("tabCreatTime").addText(oRS.getString("CREATE_TIME"));
				  elemValue.addElement("tabAlertTime").addText(oRS.getString("ALTER_TIME"));
				  elemValue.addElement("tabZip").addText(oRS.getString("COMPRESSION"));
				  
				}
				oRS.close();
				
				 oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strTableMaxRowsInfo);
				  Element elemTABMaxRow = root.addElement("tabMaxRowInfo");
				  elemTABMaxRow.addElement("title").addText("表的监控:记录数最多的表");
				while(oRS.next()){
				  Element elemValue = elemTABMaxRow.addElement("table");
				  elemValue.addElement("tabName").addText(oRS.getString("TAB") );
				  elemValue.addElement("tabTotalRow").addText(oRS.getString("CARD"));
				  elemValue.addElement("tabTotalColumn").addText(oRS.getString("COLCOUNT"));
				  elemValue.addElement("tabStatsTime").addText((oRS.getString("STATS_TIME")==null) ? "'0001-01-01'":oRS.getString("STATS_TIME"));
				  elemValue.addElement("tabAccessTime").addText(oRS.getString("LASTUSED"));
				  elemValue.addElement("tabTabsp").addText(oRS.getString("TBSPACE"));
				  elemValue.addElement("tabIdxTabsp").addText((oRS.getString("INDEX_TBSPACE") == null) ? oRS.getString("TBSPACE"): oRS.getString("INDEX_TBSPACE") );
				  elemValue.addElement("tabCreatTime").addText(oRS.getString("CREATE_TIME"));
				  elemValue.addElement("tabAlertTime").addText(oRS.getString("ALTER_TIME"));
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
