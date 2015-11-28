package db2.guard.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import db2.guard.tools.IPTransfer;
import db2.guard.tools.SQLHelper;

public class DB2LockCfg {
	//TAB,LOCK_MODE,REQ_APPLICATION_NAME,REQ_APPLICATION_HANDLE,REQ_STMT_TEXT,HLD_CURRENT_STMT_TEXT,LOCK_WAIT_ELAPSED_TIME from sysibmadm.MON_LOCKWAITS order by LOCK_WAIT_ELAPSED_TIME

	public Document db2LockWaitInfo(Connection oCon,Document doc){
			
			  try {
				  Element root = doc.getRootElement();
				//Dead Lock  
				ResultSet oRS =SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_DeadLock);
				 
				
				  Element elemDeadLock = root.addElement("DeadLockInfo");
				  elemDeadLock.addElement("title").addText("�����ļ����Ϣ");
				  int i=0;
				  i = oRS.getRow();
				while(oRS.next()){
				  Element elemValue = elemDeadLock.addElement("DeadLock");
				  elemValue.addElement("lwTabName").addText(oRS.getString("TAB") );
				  elemValue.addElement("lwReqIPAddr").addText(IPTransfer.DB2Addr2IPPort(oRS.getString("IPAddr")));
				  elemValue.addElement("lwReqAppName").addText(oRS.getString("REQ_APPLICATION_NAME") );
				  
				  //REQ_APPLICATION_HANDLE,HLD_APPLICATION_HANDLE,LOCK_MODE
				  elemValue.addElement("lwMode").addText(oRS.getString("LOCK_MODE"));
				  elemValue.addElement("lwReqAppID").addText(Long.toString(oRS.getLong("REQ_APPLICATION_HANDLE") ));
				  elemValue.addElement("lwReqSql").addText(oRS.getString("REQ_STMT_TEXT"));				
				  elemValue.addElement("lwWaitSeconds").addText(Long.toString(oRS.getLong("LOCK_WAIT_ELAPSED_TIME")));				  
				  elemValue.addElement("lwHldAppID").addText(Long.toString(oRS.getLong("HLD_APPLICATION_HANDLE") ));
				}
				  oRS.close();
		             Node oNode = doc.selectSingleNode("/report/linkInfo/dbInfo/lkDesc");
		       
		             String strDesc="";
		             int db2Value =0;
				  if(i==0){
					  
                    strDesc ="���μ����δ��������.\n"; 
				  }else{
					  Node oValue= doc.selectSingleNode("/report/linkInfo/dbInfo/status");
					  db2Value =Integer.parseInt(oValue.getText()) -i/2 *5;
					  oValue.setText(Integer.toString(db2Value));
					  strDesc ="���μ���з���" + Integer.toString(i) + "������.\n"
						                                  + "����������Խ�࣬�������ݿ�ϵͳ���ܵ��£���������ķ���������������������LOCKLIST���߻���أ��޸�SQL���ﵽ����������ĳ��ȡ����ٳ�������ʱ��";
				  }
			      oNode.getParent().addElement("dlDesc").addText(strDesc);
			      oNode.getParent().addElement("dlValue").addText(Integer.toString(db2Value));
			      
			 // Lock Wait			
						oRS =SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_LockWait);
				 
				
				  Element elemDB2SET = root.addElement("lockwaitInfo");
				  elemDB2SET.addElement("title").addText("���ȴ��ļ����Ϣ");
				while(oRS.next()){
				  Element elemValue = elemDB2SET.addElement("lockwait");
				  elemValue.addElement("lwTabName").addText(oRS.getString("TAB") );
				  elemValue.addElement("lwReqIPAddr").addText(IPTransfer.DB2Addr2IPPort(oRS.getString("IPAddr")));
				  elemValue.addElement("lwReqAppName").addText(oRS.getString("REQ_APPLICATION_NAME") );
				  
				  //REQ_APPLICATION_HANDLE,HLD_APPLICATION_HANDLE,LOCK_MODE
				  elemValue.addElement("lwMode").addText(oRS.getString("LOCK_MODE"));
				  elemValue.addElement("lwReqAppID").addText(Long.toString(oRS.getLong("REQ_APPLICATION_HANDLE") ));
				  elemValue.addElement("lwReqSql").addText(oRS.getString("REQ_STMT_TEXT"));				
				  elemValue.addElement("lwWaitSeconds").addText(Long.toString(oRS.getLong("LOCK_WAIT_ELAPSED_TIME")));
				  
				  elemValue.addElement("lwHldAppID").addText(Long.toString(oRS.getLong("HLD_APPLICATION_HANDLE") ));
				}
				oRS.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
			  return doc;
		  }

	 

}
