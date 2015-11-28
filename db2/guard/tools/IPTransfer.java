package db2.guard.tools;

public class IPTransfer {
	//DB2.Addr tranfer to IP Addr
	//IPAddr=192.168.11.144.46852.1303181447 ->192.168.11.144
	public static String DB2Addr2IP(String db2Addr){
		
		int position1 = db2Addr.indexOf(".");
		if(db2Addr.substring(0, 1).equals("*")){
			return "127.0.0.1";
		}
		int position2 = db2Addr.indexOf(".", position1 + 1);
		int position3 = db2Addr.indexOf(".", position2 + 1);
		int position4 = db2Addr.indexOf(".", position3 + 1);
		
		
		return db2Addr.substring(0,position4);
	}
	
	//DB2.Addr.Port tranfer to IPAddrPort
		//IPAddr=192.168.11.144.46852.1303181447 ->192.168.11.144:46852
		public static String DB2Addr2IPPort(String db2Addr){
			//db2Addr="192.168.11.144.46852.1303181447";
			int position1 = db2Addr.indexOf(".");
			if(db2Addr.substring(0, 1).equals("*")){
				return "127.0.0.1:00000";
			}
			int position2 = db2Addr.indexOf(".", position1 + 1);
			int position3 = db2Addr.indexOf(".", position2 + 1);
			int position4 = db2Addr.indexOf(".", position3 + 1);
			int position5 = db2Addr.indexOf(".", position4 + 1);
			
			
			
			return db2Addr.substring(0,position4) + ":" +db2Addr.substring(position4 +1,position5) ;
		}
		
			
	// ip long to String
		public String iplong2Str(long ipaddress) {
			StringBuffer sb = new StringBuffer("");
			sb.append(String.valueOf((ipaddress >>> 24)));
			sb.append(".");
			sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
			sb.append(".");
			sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
			sb.append(".");
			System.out.println("*" +(ipaddress & 0x000000FF));
			sb.append(String.valueOf((ipaddress & 0x000000FF)));
			return sb.toString();
		}

		// string ip to long

		public long ipStr2Long(String ipaddress) {
			long[] ip = new long[4];
			int position1 = ipaddress.indexOf(".");
			int position2 = ipaddress.indexOf(".", position1 + 1);
			int position3 = ipaddress.indexOf(".", position2 + 1);
			ip[0] = Long.parseLong(ipaddress.substring(0, position1));
			ip[1] = Long.parseLong(ipaddress.substring(position1 + 1, position2));
			ip[2] = Long.parseLong(ipaddress.substring(position2 + 1, position3));
			ip[3] = Long.parseLong(ipaddress.substring(position3 + 1));
			return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
		}

}
