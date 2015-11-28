package db2.guard.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import db2.guard.tools.IPTransfer;
import db2.guard.tools.SQLHelper;

public class DB2ProblemMsg {
	//SELECT timestamp,       instancename,       dbname,       appl_id,       msg  FROM SYSIBMADM.PDLOGMSGS_LAST24HOURS  ORDER BY TIMESTAMP DESC

	public Document db2ErrorInfo(Connection oCon,Document doc){
			
			  try {
				ResultSet oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_Problems);
				 
				 Element root = doc.getRootElement();
				  Element elemDB2SET = root.addElement("problemInfo");
				  elemDB2SET.addElement("title").addText("���ݿ������Ϣ");
				  int i=0;
				  
				  
				while(oRS.next()){
					i=i+1;
				  Element elemValue = elemDB2SET.addElement("problem");
				  elemValue.addElement("pmTimestamp").addText(oRS.getString("timestamp") );
				  elemValue.addElement("pmInst").addText(oRS.getString("instancename"));
				  elemValue.addElement("pmDBName").addText( (oRS.getString("dbname")==null) ? " ": oRS.getString("dbname") );				  			 
				 try{
				  elemValue.addElement("pmMsg").addText(oRS.getString("msg"));		
				 }catch(SQLException e) {
					 e.printStackTrace();
				 }
				}
				oRS.close();
				 Node oNode = doc.selectSingleNode("/report/linkInfo/dbInfo/lkDesc");
			      String strDesc="";
			      int db2Value=0;
				  if(i==0){					  
					  strDesc ="���ι��ϼ���з���24Сʱ��δ���ֹ��ϱ���.\n"; 					  										
				  }else{
					  Node oValue= doc.selectSingleNode("/report/linkInfo/dbInfo/status");
					  db2Value =Integer.parseInt(oValue.getText()) -7;
					  oValue.setText(Integer.toString(db2Value));
									  
					  strDesc ="���ι��ϼ���з���24Сʱ�ڴ���" + Integer.toString(i) + "�����ϱ���.\n"
						                                  + "���ϱ�����ԭ��ǳ��ĸ��ӣ���Ҫ�����Ų����⣬���п��ܽ�����������ߵĲ����汾";
				  }
				  oNode.getParent().addElement("erDesc").addText(strDesc);				   
				  oNode.getParent().addElement("erValue").addText(Integer.toString(i));
				  
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
			  return doc;
		  }

	 

}
