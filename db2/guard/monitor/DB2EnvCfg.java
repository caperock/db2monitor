package db2.guard.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.Document;
import org.dom4j.Element;

import db2.guard.tools.SQLHelper;

public class DB2EnvCfg {
	
	
	  public Document db2setInfo(Connection oCon,Document doc){
		  try {
			ResultSet oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2Set);
			  //REG_VAR_NAME        REG_VAR_VALUE 
			 Element root = doc.getRootElement();
			  Element elemDB2SET = root.addElement("db2setInfo");
			  elemDB2SET.addElement("title").addText("db2set配置参数");
			while(oRS.next()){
			  //System.out.println(oRS.getString("REG_VAR_NAME") + "=" +oRS.getString("REG_VAR_VALUE") + "*");
			  
			 
			  Element elemValue = elemDB2SET.addElement("db2set");
			  elemValue.addElement("regName").addText(oRS.getString("REG_VAR_NAME") );
			  elemValue.addElement("regValue").addText(oRS.getString("REG_VAR_VALUE"));
			  
			  
			  
			}
			oRS.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return doc;
	  }
	
	public Document db2DBMInfo(Connection oCon, Document doc) {
		try {
			ResultSet oRS = SQLHelper.getResultSet(oCon,
					DB2V97ESE.strDB2_DBM_CFG);
			// REG_VAR_NAME REG_VAR_VALUE
			Element root = doc.getRootElement();
			Element elemDB2SET = root.addElement("dbmInfo");
			elemDB2SET.addElement("title").addText("数据库实例配置参数");
			while (oRS.next()) {
               if(oRS.getString("VALUE").length()==0) {
            	   //System.out.println("#" + oRS.getString("NAME") +"#");
            	   //System.out.println("#" + oRS.getString("VALUE") +"#");
            	   continue;
            	   
               }
				Element elemValue = elemDB2SET.addElement("db2dbm");
				elemValue.addElement("dbmName").addText(oRS.getString("NAME"));

				// Re-organize display message
				if (oRS.getString("VALUE_FLAGS").indexOf("AUTOMATIC") == 0) {

					elemValue.addElement("dbmValue").addText(
							"AUTOMATIC(" + oRS.getString("VALUE") + ")");

				} else {
					elemValue.addElement("dbmValue").addText(
							oRS.getString("VALUE"));
				}

			}
			oRS.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return doc;
	}
	
	public Document db2DBInfo(Connection oCon, Document doc) {
		try {
			ResultSet oRS = SQLHelper.getResultSet(oCon,
					DB2V97ESE.strDB2_DB_CFG);
			// REG_VAR_NAME REG_VAR_VALUE
			Element root = doc.getRootElement();
			Element elemDB2SET = root.addElement("dbInfo");
			elemDB2SET.addElement("title").addText("数据库配置参数");
			while (oRS.next()) {
				   if(oRS.getString("VALUE").length()==0) {
	            	   continue;
	               }
				Element elemValue = elemDB2SET.addElement("db2db");
				elemValue.addElement("dbName").addText(oRS.getString("NAME"));

				// Re-organize display message
				if (oRS.getString("VALUE_FLAGS").indexOf("AUTOMATIC") == 0) {

					elemValue.addElement("dbValue").addText(
							"AUTOMATIC(" + oRS.getString("VALUE") + ")");

				} else {
					elemValue.addElement("dbValue").addText(
							oRS.getString("VALUE"));
				}
			
			}
			oRS.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return doc;
	}
	
	

}
