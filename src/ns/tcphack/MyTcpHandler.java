package ns.tcphack;

import java.util.Arrays;

class MyTcpHandler extends TcpHandler {
	
	public static final String SRC = "2001:610:1908:f000:84ce:5142:acd3:a2dd";
	public static final String DST = "2001:67c:2564:a170:a00:27ff:fe11:cecb";
	public static final byte EMPTY = 0b00000000;
	public static final byte HOP_LMT = 30; //hop limit = 30
	
	public static void main(String[] args) {
		//http://[2001:67c:2564:a170:a00:27ff:fe11:cecb]:7711/?nr=1234567
		
		new MyTcpHandler();
	}

	public MyTcpHandler() {
		super();
		
		boolean done = false;
		while (!done) {
			// TODO: Implement your client for the server by combining:
			//        - Send packets, use this.sendData(byte[]).
			//           The data passed to sendData should contain raw
			//           IP/TCP/(optionally HTTP) headers and data.
			//        - Receive packets, you can retreive byte arrays using
			//           byte[] this.receiveData(long timeout).
			//           The timeout passed to this function will indicate the maximum amount of
			//           milliseconds for receiveData to complete. When the timeout expires before a
			//           packet is received, an empty array will be returned.
			//
			//           The data you'll receive and send will and should contain all packet 
			//           data from the network layer and up.
			
            byte[] dtout = new byte[40]; //An outgoing packet, with size 40
            
            this.addDefault(dtout); //outgoing now has addresses
            
            
            this.sendData(dtout);
            done = true;
            //byte[] dtin = this.receiveData(1000);
            //System.out.println("incoming dataArray: " + Arrays.toString(dtin));

		}   
	}
	
	/**
	 * Adding src and dst addresses to a packet aswell as a hop limit
	 * @param packet the packet in which the addresses will be written (length of 40 octets)
	 * @return byte[] with src and dst addresses copied into
	 */
	public byte[] addDefault(byte[] packet) {
		packet[0] = (byte) 0x60; //version 6, and 4 0's for traffic class
		packet[1] = EMPTY; 
		packet[2] = EMPTY;
		packet[3] = EMPTY; //empty bytes for traffic class and flow label
		
		packet[7] = HOP_LMT;
		byte[] src = convertIP(SRC);
		byte[] dst = convertIP(DST);
		System.arraycopy(src, 0, packet, 8, src.length);
		System.arraycopy(dst, 0, packet, 24, dst.length);
		return packet;
	}
	
	//2001:67c:2564:a170:a00:27ff:fe11:cecb
	public byte[] convertIP(String ip) {
		byte[] res = new byte[16];
		String[] parts = ip.split(":");
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].length() < 4) {
				parts[i] = "0" + parts[i]; //Retrieving omitted single leading zero
			} //[0] [1] [2] [3] ... [15]
			res[i*2] = parts[i].substring(0, 2).getBytes()[0];
			res[i*2+1] = parts[i].substring(2).getBytes()[0];
		}
		return res;
	}
	
}
