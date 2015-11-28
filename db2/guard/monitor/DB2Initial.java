package db2.guard.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import db2.guard.tools.Base64Coder;
import db2.guard.tools.DatabaseBean;
import db2.guard.tools.SQLHelper;
import db2.guard.tools.TimeTools;
import db2.guard.tools.XMLOpt;

public class DB2Initial {
	static Logger loggerBlack = Logger.getLogger("blackfile");
	static DatabaseBean oDB = new DatabaseBean();

	public static void main(String[] args) {
		
		DB2Initial oDB2I = new DB2Initial();
		try {
			oDB2I.readConfig();
			oDB2I.startScan();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startScan() {
		try {
			SimpleDateFormat otdf = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
			// System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
			if (Integer.valueOf(otdf.format(new Date())) >= 20140515) {
				System.out
						.println("Limited Version, We can't open our radar to scan database");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		PropertyConfigurator.configure("log4j.properties");
		loggerBlack
				.info("DataGuard is ready, we shall scan database using radar");

		try {
			TimeTools oTT = new TimeTools();

			String strCurTimeStamp = TimeTools.getTimeStampFile();

			oDB.setCurTime(strCurTimeStamp);
			long lST = oTT.getStartTime();
			// Read XML configure document
			this.readConfig();
			Document doc = DocumentFactory.getInstance().createDocument();

			// 创建ProcessingInstruction
			Map<String, String> inMap = new HashMap<String, String>();
			inMap.put("type", "text/xsl");
			inMap.put("href", "simple.xsl");
			doc.addProcessingInstruction("xml-stylesheet", inMap);

			// Connect database
			Connection oCon = SQLHelper.getConnection(oDB);

			long lET = oTT.getEndTime();
			float fTT = (float) (lET - lST) / 1000;

			oDB.setElapsedTime(fTT);

			if (oCon == null) {
				Element root = doc.addElement("report");

				Element elemLinkInfo = root.addElement("linkInfo");
				Element elem = elemLinkInfo.addElement("dbInfo");
				elem.addElement("title").addText("本次巡检信息");
				elem.addElement("dbName").addText(oDB.getDBName());
				elem.addElement("dbIpaddr").addText(oDB.getIPAddr());

				elem.addElement("scanTime").addText(oDB.getCurTime());
				elem.addElement("connSeconds").addText("-1");

				elem.addElement("status").addText("0");
				elem.addElement("lkDesc").addText(
						"巡检发现数据库无法连接,尽快检测主机、数据库、网络设备及端口");

				//XMLOpt oXML = new XMLOpt(strCurTimeStamp);
				//oXML.dumpXML(doc);
				
				XMLOpt oXML = new XMLOpt(oDB.getWebPath(),oDB.getLogPath(),strCurTimeStamp);
				oXML.dumpXMLog(doc);
				

				SummaryReport oSumReport = new SummaryReport();
				oSumReport.DailyReportInfo(doc);

				return;
			}
			
			ResultSet oRS = SQLHelper.getResultSet(oCon,
					DB2V97ESE.strDB2Version);

			// Scan Instance Info and DB Connect Info
			DB2InstLink oDB2IL = new DB2InstLink();
			doc = oDB2IL.linkInfo(oRS, oDB, doc);

			// Scan db2set\dbm\db configuration

			DB2BPCfg oBP = new DB2BPCfg();
			if(oBP.db2checkDate(oCon, doc)<=0){
				Element root = doc.getRootElement();
                root.detach();
                root = doc.addElement("report");

				Element elemLinkInfo = root.addElement("linkInfo");
				Element elem = elemLinkInfo.addElement("dbInfo");
				elem.addElement("title").addText("本次巡检信息");
				elem.addElement("dbName").addText(oDB.getDBName());
				elem.addElement("dbIpaddr").addText(oDB.getIPAddr());

				elem.addElement("scanTime").addText(oDB.getCurTime());
				elem.addElement("connSeconds").addText("-1");

				elem.addElement("status").addText("0");
				elem.addElement("lkDesc").addText(
						"巡检软件截至到2014-03-01日截止，超期不能使用");

				//XMLOpt oXML = new XMLOpt(strCurTimeStamp);
				//oXML.dumpXML(doc);
				
				XMLOpt oXML = new XMLOpt(oDB.getWebPath(),oDB.getLogPath(),strCurTimeStamp);
				oXML.dumpXMLog(doc);
				

				SummaryReport oSumReport = new SummaryReport();
				oSumReport.DailyReportInfo(doc);
				
				return;
			}
			doc = oBP.db2BPInfo(oCon, doc);

			DB2TcbCfg oTC = new DB2TcbCfg();
			doc = oTC.db2TcbInfo(oCon, doc);

			DB2LockCfg oLW = new DB2LockCfg();
			doc = oLW.db2LockWaitInfo(oCon, doc);

			DB2SqlCfg oSql = new DB2SqlCfg();
			doc = oSql.db2SqlInfo(oCon, doc);

			DB2EnvCfg oEnv = new DB2EnvCfg();

			doc = oEnv.db2setInfo(oCon, doc);

			doc = oEnv.db2DBMInfo(oCon, doc);

			doc = oEnv.db2DBInfo(oCon, doc);

			DB2TabInfo oTAB = new DB2TabInfo();
			doc = oTAB.db2TabInfo(oCon, doc);

			DB2IdxInfo oIdx = new DB2IdxInfo();
			doc = oIdx.db2IdxInfo(oCon, doc);

			DB2ProblemMsg oMSG = new DB2ProblemMsg();
			doc = oMSG.db2ErrorInfo(oCon, doc);

			// Write XML
		//	XMLOpt oXML = new XMLOpt(strCurTimeStamp);
		//	oXML.dumpXML(doc);
			XMLOpt oXML = new XMLOpt(oDB.getWebPath(),oDB.getLogPath(),strCurTimeStamp);
			oXML.dumpXMLog(doc);

		/*	SummaryReport oSumReport = new SummaryReport();
			oSumReport.DailyReportInfo(doc);
			oSumReport.MonthReportInfo(doc);
		*/
			//doc.detach();

		} catch (Exception e) {
			e.printStackTrace();
			loggerBlack.info(e.toString());

		}
	}

	private void readConfig() throws DocumentException {
		SAXReader reader = new SAXReader();

		// Document doc = reader
		// .read("C:/mydata/WorkProjects/javaProgram/DB2_GUARD/config/config-sample.xml");

		Document doc = reader.read(".//db2//conf//config.xml");

		Element oRoot = doc.getRootElement();

		oDB.setDBName(oRoot.element("database").elementText("dbname"));
		oDB.setIPAddr(oRoot.element("database").elementText("ipaddr"));
		oDB.setUser(oRoot.element("database").elementText("user"));
		oDB.setPort(oRoot.element("database").elementText("port"));
		oDB.setWebPath(oRoot.element("database").elementText("webpath"));
		oDB.setLogPath(oRoot.element("database").elementText("logpath"));

		XMLOpt.mkdir(oRoot.element("database").elementText("webpath"));
		oDB.setPassword(oRoot.element("database").elementText("password"));

		String strMachine = "Testing"
				+ oRoot.element("database").elementText("dbname")
				+ oRoot.element("database").elementText("ipaddr")
				+ oRoot.element("database").elementText("user").toUpperCase()
				+ oRoot.element("database").elementText("port")
				+ oRoot.element("database").elementText("password")
						.toUpperCase();

		if (oRoot.element("database").elementText("sn") == null
				|| oRoot.element("database").elementText("sn").length() == 0) {

			if (oRoot.element("database").elementText("machinecode") == null
					|| oRoot.element("database").elementText("machinecode")
							.length() == 0) {
				strMachine = Base64Coder.getMD5(strMachine.getBytes());
				oRoot.element("database").addElement("machinecode")
						.addText(strMachine.toUpperCase());
				XMLOpt oXml = new XMLOpt(".//db2//conf//config");
				oXml.dumpXML(doc);
				System.out.println(strMachine);
				// oRoot.write();
			}
			System.exit(0);
		}

		strMachine = oRoot.element("database").elementText("machinecode")
				.trim()
				+ "Testing";

		String strXMLSN = oRoot.element("database").elementText("sn");
		String strTrueSN = Base64Coder.getMD5(strMachine.getBytes())
				.toUpperCase();
		System.out.println("strTrueSN=" + strTrueSN);
		if (strXMLSN.equals(strTrueSN) == false) {
			System.exit(0);
		}

		/*
		 * String strPassword =
		 * oRoot.element("database").elementText("password"); if (strPassword ==
		 * null) { strPassword = ""; } strPassword = strPassword.trim();
		 * 
		 * 
		 * if (strPassword.length() != 0) { StringBuffer strPValue1 = new
		 * StringBuffer(strPassword); String strPReverseCode =
		 * strPValue1.reverse().toString(); String strPValue =
		 * Base64Coder.decodeString(strPReverseCode);
		 * oDB.setPassword(strPValue); System.out.println("#strPassword='" +
		 * strPValue + "'"); } else { oDB.setPassword(strPassword); }
		 */

		// oDB.toPrint();
	}
}
