package db2.guard.monitor;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.*;
import org.dom4j.io.*;

import db2.guard.tools.DatabaseBean;


public class DB2InstLink {

	
	
	
	public Document linkInfo(ResultSet oRS, DatabaseBean oDB,Document doc){
		
		
		DB2LinkPolicy oLP = new DB2LinkPolicy();
	
		InfoStatus oISDB2 =oLP.analyDB2Conn(oDB);
		
		
		String strCurTime = oDB.getCurTime();
		String strElapsedTime =Float.toString(oDB.getElapsedTime());
		Element root=null;
		Element elemLinkInfo =null; 
		
		try {
			
			
			root = doc.addElement("report");
			
				 elemLinkInfo = root.addElement("linkInfo");
				Element elemInstInfo = elemLinkInfo.addElement("instInfo");
				InfoStatus oISInst ;
				while(oRS.next()){					
									elemInstInfo.addElement("title").addText("DB2实例的版本信息");
					elemInstInfo.addElement("instName").addText(oRS.getString("INST_NAME"));				
					elemInstInfo.addElement("instVersion").addText(oRS.getString("SERVICE_LEVEL"));
					oISInst =oLP.analyIntance(oRS);
					elemInstInfo.addElement("status").addText(Integer.toString(oISInst.getiStatus()));
					elemInstInfo.addElement("desc").addText(oISInst.getStrDesc());
				}

				oRS.close();
				
				
				Element elem =  elemLinkInfo.addElement("dbInfo");
				elem.addElement("title").addText("本次巡检信息");
				elem.addElement("dbName").addText(oDB.getDBName());
				elem.addElement("dbIpaddr").addText(oDB.getIPAddr());
							
				elem.addElement("scanTime").addText(strCurTime);
				elem.addElement("connSeconds").addText(strElapsedTime);

				elem.addElement("status").addText(Integer.toString(oISDB2.getiStatus()));
				elem.addElement("lkDesc").addText(oISDB2.getStrDesc());
				
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
			
		}
			return doc;
	}

	public void demoWrite(){
		Document doc = DocumentFactory.getInstance()
				.createDocument();
			Element root = doc.addElement("report");
			Element elem = root.addElement("linkInfo");
		
			
			elem.addElement("dbVerion");
			elem.addElement("dbIpaddr");
			elem.addElement("dbScore");
			
			elem.addElement("scanTime");
			elem.addElement("connTime");
					
			//elem.addText("sample");
			
			try {
				FileOutputStream fos = new FileOutputStream("simple.xml");
				OutputFormat format = OutputFormat.createPrettyPrint();
				XMLWriter writer = new XMLWriter(fos, format);
				writer.write(doc);
				writer.flush();
			} catch (Exception e) {
				// TODO: handle exception
			}


	}
}
