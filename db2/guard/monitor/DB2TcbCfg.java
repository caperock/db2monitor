package db2.guard.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import db2.guard.tools.IPTransfer;
import db2.guard.tools.SQLHelper;

public class DB2TcbCfg {
	
	public Document db2TcbInfo(Connection oCon,Document doc){
		
		  try {
			ResultSet oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_Tcb);
			 
			  Element root = doc.getRootElement();
			  Element elemDB2SET = root.addElement("tcbInfo");
			  elemDB2SET.addElement("title").addText("��ռ�ļ����Ϣ");
			  float x=0.0f,y=0.0f;
			  String strY="";
		      String strMaxResult=null;
			while(oRS.next()){
			  Element elemValue = elemDB2SET.addElement("tablespace");
			  elemValue.addElement("tcbID").addText(Long.toString(oRS.getLong("TBSP_ID") ));
			  elemValue.addElement("tcbNAME").addText(oRS.getString("TBSP_NAME") );
			  
			  elemValue.addElement("tcbState").addText(oRS.getString("TBSP_STATE"));
			  elemValue.addElement("tcbType").addText(oRS.getString("TBSP_TYPE"));
			  elemValue.addElement("tcbContentType").addText(oRS.getString("TBSP_CONTENT_TYPE"));
			  elemValue.addElement("tcbPagesize").addText(Long.toString(oRS.getLong("TBSP_PAGE_SIZE") ));
			
			  if(oRS.getString("TBSP_TYPE").indexOf("SMS") !=0 ){
			  elemValue.addElement("tcbUsableMB").addText(Long.toString(oRS.getLong("TBSP_USABLE_PAGES") * oRS.getLong("TBSP_PAGE_SIZE") /1024));
			  elemValue.addElement("tcbUsedMB").addText(Long.toString(oRS.getLong("TBSP_USED_PAGES") * oRS.getLong("TBSP_PAGE_SIZE") /1024));
			  elemValue.addElement("tcbFreeMB").addText(Long.toString(oRS.getLong("TBSP_FREE_PAGES") * oRS.getLong("TBSP_PAGE_SIZE") /1024));
              DecimalFormat format = new DecimalFormat("0.00%");
              x= (float)oRS.getLong("TBSP_USED_PAGES")/oRS.getLong("TBSP_USABLE_PAGES");

              
		      String result = format.format( x  );

              if(x >y){
            	  y=x;
            	  strY=oRS.getString("TBSP_NAME") + ",ʹ����" + result;
            	  strMaxResult =result.substring(0, result.length()-1);
              }
			  elemValue.addElement("tcbUsedRatio").addText(result);              
			  }else{
				  elemValue.addElement("tcbUsableMB").addText("AUTOMATIC");
				  elemValue.addElement("tcbUsedMB").addText("AUTOMATIC");
				  elemValue.addElement("tcbFreeMB").addText("AUTOMATIC");
	              elemValue.addElement("tcbUsedRatio").addText("AUTOMATIC");			  
			  }
			  elemValue.addElement("tcbBPName").addText(oRS.getString("BPNAME"));
			  elemValue.addElement("tcbNumCont").addText(Long.toString(oRS.getLong("TBSP_NUM_CONTAINERS") ));
			}
			oRS.close();
			
			  Node oValue= doc.selectSingleNode("/report/linkInfo/dbInfo/status");
			  int db2Value =Integer.parseInt(oValue.getText());
			  if(y>.95f){
				  db2Value =db2Value -40;
			  }
			  if(y>0.9 && y<=0.95){
				  db2Value =db2Value -30;
			  }
			  
			  if(y>0.85 && y<=0.9){
				  db2Value =db2Value -20;
			  }
			  
			   oValue.setText(Integer.toString(db2Value));
			
			Node oNode = doc.selectSingleNode("/report/linkInfo/dbInfo/lkDesc");
			String strDesc="��ռ�ļ���з���ʹ������ߵı�ռ�Ϊ" + strY + ".\n"
					                               + "��ռ��ʹ���ʿ�����90%���£����б�Ҫ����������������������չ��ռ�������������ռ�������ľ�";
			 oNode.getParent().addElement("tbDesc").addText(strDesc);
			 oNode.getParent().addElement("tbValue").addText(strMaxResult);
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return doc;
	  }


	public Document db2ContainerInfo(Connection oCon,Document doc){
		
		  try {
			ResultSet oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_Tcb);
			 
			 Element root = doc.getRootElement();
			  Element elemDB2SET = root.addElement("tcbInfo");
			  elemDB2SET.addElement("title").addText("���ݿ������ļ����Ϣ");
			while(oRS.next()){
			  Element elemValue = elemDB2SET.addElement("container");
			  elemValue.addElement("ctnName").addText(Long.toString(oRS.getLong("CONTAINER_NAME") ));
			  elemValue.addElement("ctnTcbNAME").addText(oRS.getString("TBSP_NAME") );
			  
			  elemValue.addElement("ctnState").addText(oRS.getString("CONTAINER_TYPE"));
			  elemValue.addElement("tcbType").addText(oRS.getString("TBSP_TYPE"));
			  elemValue.addElement("tcbContentType").addText(oRS.getString("TBSP_CONTENT_TYPE"));
			  elemValue.addElement("tcbPagesize").addText(Long.toString(oRS.getLong("TBSP_PAGE_SIZE") ));
			
			  if(oRS.getString("TBSP_TYPE").indexOf("SMS") !=0 ){
			  elemValue.addElement("tcbUsableMB").addText(Long.toString(oRS.getLong("TBSP_USABLE_PAGES") * oRS.getLong("TBSP_PAGE_SIZE") /1024));
			  elemValue.addElement("tcbFreeMB").addText(Long.toString(oRS.getLong("TBSP_FREE_PAGES") * oRS.getLong("TBSP_PAGE_SIZE") /1024));
            elemValue.addElement("tcbUsedRatio").addText(Float.toString(oRS.getLong("TBSP_USED_PAGES")/oRS.getLong("TBSP_USABLE_PAGES")  ));			  
			  }else{
				  elemValue.addElement("tcbUsableMB").addText("AUTOMATIC");
				  elemValue.addElement("tcbFreeMB").addText("AUTOMATIC");
	              elemValue.addElement("tcbUsedRatio").addText("AUTOMATIC");			  
			  }
			  elemValue.addElement("tcbBPName").addText(oRS.getString("BPNAME"));
			  elemValue.addElement("tcbNumCont").addText(Long.toString(oRS.getLong("TBSP_NUM_CONTAINERS") ));
			}
			oRS.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return doc;
	  }




}
