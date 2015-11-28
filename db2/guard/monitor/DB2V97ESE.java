package db2.guard.monitor;

public class DB2V97ESE {
	public static String strTimeStamp="SELECT CURRENT TIMESTAMP FROM SYSIBM.SYSDUMMY1 ";
	public static String strDB2Version="SELECT INST_NAME,SERVICE_LEVEL FROM SYSIBMADM.ENV_INST_INFO";
//	public static String strOSVersion="select member,varchar(HOST_NAME,12) as HOST_NAME, varchar(OS_NAME,8) as OS_NAME, 	varchar(OS_VERSION,8) as OS_VERSION, varchar(OS_RELEASE,18) 	as OS_RELEASE from table(SYSPROC.ENV_GET_SYSTEM_RESOURCES()) order by MEMBER";
//	public static String strMemDyn="select member, varchar(HOST_NAME,12) as HOST_NAME, MEMORY_TOTAL, MEMORY_FREE,    MEMORY_SWAP_TOTAL, MEMORY_SWAP_FREE, VIRTUAL_MEM_TOTAL, VIRTUAL_MEM_FREE    from table(SYSPROC.ENV_GET_SYSTEM_RESOURCES()) order by member";
//	public static String strCPUDyn= "select MEMBER, varchar(HOST_NAME,12) as HOST_NAME,  CPU_ONLINE,cast( CPU_LOAD_SHORT as dec(3,1)) as cpu_short, cast(CPU_LOAD_MEDIUM as dec(3,1)) as cpu_medium, cast(CPU_LOAD_LONG as dec(3,1)) as cpu_long from table(SYSPROC.ENV_GET_SYSTEM_RESOURCES()) order by MEMBER ";
//    public static String strNetDyn= "SELECT MEMBER,varchar(adapter_name, 20) as name,        packets_received,        packets_sent,       total_bytes_received,       total_bytes_sent FROM TABLE(ENV_GET_NETWORK_RESOURCES()) order by member";
	
	//Scan db2set configuration
   public static String strDB2Set=" SELECT REG_VAR_NAME,REG_VAR_VALUE from SYSIBMADM.REG_VARIABLES where level='I'";	
   
   //Scan DBM CFG
 //  public static String strDB2_DBM_CFG="SELECT NAME,VALUE, VALUE_FLAGS FROM SYSIBMADM.DBMCFG where value <> ''";
  //-- db2 v10.1 
    public static String strDB2_DBM_CFG="SELECT NAME,VALUE, VALUE_FLAGS FROM SYSIBMADM.DBMCFG where value is not null";
   
   
   //Scan DB CFG
  // public static String strDB2_DB_CFG=" SELECT NAME, VALUE,DEFERRED_VALUE_FLAGS as VALUE_FLAGS FROM SYSIBMADM.DBCFG  where value <> ''";
 //-- db2 v10.1 
   public static String strDB2_DB_CFG=" SELECT NAME, VALUE,DEFERRED_VALUE_FLAGS as VALUE_FLAGS FROM SYSIBMADM.DBCFG  where value is not null";
   
   //Scan Bufferpool
   public static String strDB2_BP_HITRATIO ="SELECT c.BUFFERPOOLID, SUBSTR(A.BP_NAME,1,20) AS BP_NAME,    A.TOTAL_HIT_RATIO_PERCENT, A.DATA_HIT_RATIO_PERCENT,  A.DATA_LOGICAL_READS,A.DATA_PHYSICAL_READS , A.INDEX_HIT_RATIO_PERCENT , A.INDEX_LOGICAL_READS,A.INDEX_PHYSICAL_READS ,(B.BP_CUR_BUFFSZ * c.PAGESIZE )/1048576 as BP_SIZE_MB, b.BP_TBSP_USE_COUNT FROM SYSIBMADM.BP_HITRATIO A, SYSIBMADM.SNAPBP_PART B, syscat.bufferpools C  WHERE a.bp_name = b.bp_name and b.bp_name = c.bpname";                                                                                                                                                                                                                                                                                                                       
   //public static String strDB2_BP_HITRATIO ="select a.*, (B.BP_CUR_BUFFSZ * c.PAGESIZE )/1048576 as BP_SIZE_MB FROM SYSIBMADM.MON_BP_UTILIZATION a, SYSIBMADM.SNAPBP_PART B, syscat.bufferpools C  WHERE a.bp_name = b.bp_name and b.bp_name = c.bpname";
   
   
   //Scan Lock wait
   public static String strDB2_LockWait ="select TABSCHEMA||'.'||TABNAME as TAB,LOCK_MODE,APPLICATION_ID as IPAddr,REQ_APPLICATION_NAME,REQ_APPLICATION_HANDLE,REQ_STMT_TEXT,LOCK_WAIT_ELAPSED_TIME,HLD_APPLICATION_HANDLE from sysibmadm.MON_LOCKWAITS A, sysibmadm.MON_CONNECTION_SUMMARY B WHERE A.REQ_APPLICATION_HANDLE = B.APPLICATION_HANDLE order by LOCK_WAIT_ELAPSED_TIME desc,REQ_APPLICATION_HANDLE asc,HLD_APPLICATION_HANDLE asc fetch first 50 rows only";
   public static String strDB2_DeadLock="select A.TABSCHEMA||'.'||A.TABNAME as TAB,a.LOCK_MODE,APPLICATION_ID as IPAddr,a.REQ_APPLICATION_NAME,A.REQ_STMT_TEXT,A.REQ_APPLICATION_HANDLE,a.LOCK_WAIT_ELAPSED_TIME,A.HLD_APPLICATION_HANDLE from sysibmadm.MON_LOCKWAITS A inner join sysibmadm.MON_LOCKWAITS C on A.REQ_APPLICATION_HANDLE =C.HLD_APPLICATION_HANDLE and A.HLD_APPLICATION_HANDLE= C.REQ_APPLICATION_HANDLE join sysibmadm.MON_CONNECTION_SUMMARY B on A.REQ_APPLICATION_HANDLE = B.APPLICATION_HANDLE  order by a.LOCK_WAIT_ELAPSED_TIME, HLD_APPLICATION_HANDLE asc fetch first 50 rows only";
   
   //Scan Top Sql
   public static String strDB2_TopTimeSql="select * from SYSIBMADM.TOP_DYNAMIC_SQL   ORDER BY AVERAGE_EXECUTION_TIME_S DESC,NUM_EXECUTIONS DESC FETCH FIRST 50 ROWS ONLY";
   public static String strDB2_TopExesNumSql="select NUM_EXECUTIONS, AVERAGE_EXECUTION_TIME_S, STMT_SORTS,   SORTS_PER_EXECUTION, STMT_TEXT  FROM SYSIBMADM.TOP_DYNAMIC_SQL   ORDER BY NUM_EXECUTIONS DESC,AVERAGE_EXECUTION_TIME_S DESC FETCH FIRST 50 ROWS ONLY";
   public static String strDB2_TopSortSql="select NUM_EXECUTIONS, AVERAGE_EXECUTION_TIME_S, STMT_SORTS,   SORTS_PER_EXECUTION, STMT_TEXT  FROM SYSIBMADM.TOP_DYNAMIC_SQL   ORDER BY STMT_SORTS DESC, NUM_EXECUTIONS DESC FETCH FIRST 50 ROWS ONLY";
   
   //Scan TableSpace 
   public static String strDB2_Tcb="select A.*,b.TBSP_PAGE_SIZE ,b.TBSP_TYPE,b.TBSP_CONTENT_TYPE,C.BPNAME from SYSIBMADM.SNAPTBSP_PART A, SYSIBMADM.SNAPTBSP B, syscat.bufferpools C WHERE A.TBSP_ID = B.TBSP_ID AND B.TBSP_CUR_POOL_ID = C.BUFFERPOOLID";
   
   public static String strDB2_Containers="select * from SYSIBMADM.SNAPCONTAINER order by TBSP_ID asc, CONTAINER_ID asc";
   
   //Scan error Logs
   public static String strDB2_Problems = "SELECT timestamp, instancename,dbname,SUBSTR(MSG,1,400) AS MSG  FROM SYSIBMADM.PDLOGMSGS_LAST24HOURS WHERE msgseverity = 'C'  or msgseverity = 'E'   ORDER BY TIMESTAMP DESC";
   
   //Scan Tables Info
   public static String strDB2_HotTableInfo ="select A.TABSCHEMA||'.'||A.TABNAME as TAB,DATA_OBJECT_PAGES,INDEX_OBJECT_PAGES,ROWS_READ,ROWS_WRITTEN,CARD,STATS_TIME,LASTUSED,CREATE_TIME,ALTER_TIME,TBSPACE,INDEX_TBSPACE,TAB_TYPE,COMPRESSION FROM SYSIBMADM.SNAPTAB A,syscat.tables B where OWNERTYPE='U' AND TYPE='T' and A.TABSCHEMA=B.TABSCHEMA AND A.TABNAME = B.TABNAME order by (2.5* ROWS_WRITTEN + ROWS_READ) desc, STATS_TIME asc fetch first 50 rows only with ur";
   public static String strDB2_LostIdxInfo="select TABSCHEMA||'.'||TABNAME as TAB,TAB_TYPE, ROWS_READ,table_scans FROM TABLE(MON_GET_table('','',-2)) AS T WHERE TAB_TYPE = 'USER_TABLE' or TAB_TYPE = 'TEMP_TABLE' order by table_scans desc, ROWS_READ desc fetch first 50 rows only with ur";
   public static String strTableOrphanInfo="select TABSCHEMA||'.'||TABNAME as TAB,CARD,COLCOUNT,STATS_TIME,LASTUSED,TBSPACE,INDEX_TBSPACE,CREATE_TIME,ALTER_TIME,COMPRESSION from syscat.tables WHERE OWNERTYPE='U' AND TYPE='T'  order by LASTUSED asc,CARD desc, COLCOUNT desc fetch first 50 rows only with ur ";
   public static String strTableRunStatsInfo="select TABSCHEMA||'.'||TABNAME as TAB,CARD,COLCOUNT,STATS_TIME,LASTUSED,TBSPACE,INDEX_TBSPACE,CREATE_TIME,ALTER_TIME,COMPRESSION from syscat.tables WHERE OWNERTYPE='U' AND TYPE='T' and LASTUSED > '0001-01-01' order by STATS_TIME asc,LASTUSED desc,CARD desc  fetch first 50 rows only with ur ";
   public static String strTableMaxRowsInfo="select TABSCHEMA||'.'||TABNAME as TAB,CARD,COLCOUNT,STATS_TIME,LASTUSED,TBSPACE,INDEX_TBSPACE,CREATE_TIME,ALTER_TIME,COMPRESSION from syscat.tables WHERE OWNERTYPE='U' AND TYPE='T' order by CARD desc ,COLCOUNT desc,LASTUSED asc fetch first 50 rows only with ur";
   public static String strTableMinRowsInfo="select TABSCHEMA||'.'||TABNAME as TAB,CARD,COLCOUNT,STATS_TIME,LASTUSED,TBSPACE,INDEX_TBSPACE,CREATE_TIME,ALTER_TIME,COMPRESSION from syscat.tables WHERE OWNERTYPE='U' AND TYPE='T' order by CARD asc ,COLCOUNT asc,LASTUSED asc fetch first 50 rows only with ur";
   
   //Scan Indexes Info
   public static String strDB2_IdxOrphanInfo="select TABSCHEMA||'.'||TABNAME as TAB, INDNAME,COLNAMES,COLCOUNT,INDEXTYPE,STATS_TIME,LASTUSED,fullkeycard,INDCARD,COMPRESSION,CREATE_TIME FROM SYSCAT.INDEXES WHERE OWNERTYPE='U' ORDER BY  LASTUSED ASC fetch first 50 rows only with ur";
   public static String strDB2_IdxStatsInfo="select TABSCHEMA||'.'||TABNAME as TAB, INDNAME,COLNAMES,COLCOUNT,INDEXTYPE,STATS_TIME,LASTUSED,fullkeycard,INDCARD,COMPRESSION,CREATE_TIME FROM SYSCAT.INDEXES WHERE OWNERTYPE='U' AND LASTUSED > date('2001-01-01') ORDER BY  STATS_TIME ASC,LASTUSED desc FETCH FIRST 50 ROWS ONLY WITH UR";
   
   		
}
