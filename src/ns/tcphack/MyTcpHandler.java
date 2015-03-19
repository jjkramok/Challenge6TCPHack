package ns.tcphack;

import java.util.Arrays;

class MyTcpHandler extends TcpHandler {
	
	public static final String SRC = "2001:610:1908:f000:2ab2:bdff:fe5a:7ad5";
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
			
            byte[] dtOut = new byte[60]; //An outgoing packet, with size 40
            
            //IPv6
            dtOut = this.addDefault(dtOut); //outgoing now has addresses
            dtOut[4] = EMPTY; //payload length (continued)
            dtOut[5] = (byte) 0x14; //payload length
            dtOut[6] = (byte) 0xfd; //nextHeader (253)
            //dtOut[6] = (byte) 0X06; //nextHeader (6)
            
            //TCP
            byte[] tcp = new byte[20];
            tcp = tcpAddDefault(tcp);
            
            //Combining IP and TCP
            System.arraycopy(tcp, 0, dtOut, 40, tcp.length); //pasting the tcp payload after the ip-header
            
            //Sending
            this.sendData(dtOut);
            System.out.println("Packet sent");
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
		//Bytes 4 till 6 are to be filled by caller
		packet[7] = HOP_LMT;
		byte[] src = convertIP(SRC);
		byte[] dst = convertIP(DST);
		System.arraycopy(src, 0, packet, 8, src.length);
		System.arraycopy(dst, 0, packet, 24, dst.length);
		return packet;
	}
	
	public byte[] tcpAddDefault(byte[] header) {
		header[0] = EMPTY; //src port (255)
		header[1] = (byte) 0xff; //src port 
		header[2] = (byte) 0x1e; //dst port (7711)
		header[3] = (byte) 0x1f; //dst port
		
		//header[2] = (byte) 0x1e; //dst test port (7701)
		//header[3] = (byte) 0x15; //dst test port
		
		for (int i = 4; i < 8; i++) { //SEQ
			header[i] = EMPTY;
		}
		for (int i = 8; i < 12; i++) { //ACK
			header[i] = EMPTY;
		}
		header[12] = (byte) 0xf0; //data offset, reserved and NS
		header[13] = (byte) 0x02; //SYN flag set
		header[14] = (byte) 0xff; //window
		header[16] = (byte) 0x00; //window
		
		
		return header;
	}
	
	
	//2001:67c:2564:a170:a00:27ff:fe11:cecb
	public byte[] convertIP(String ip) {
		byte[] res = new byte[16];
		String[] parts = ip.split(":");
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].length() < 4) {
				parts[i] = "0" + parts[i]; //Retrieving omitted single leading zero
			}
			res[i*2] = parts[i].substring(0, 2).getBytes()[0];
			res[i*2+1] = parts[i].substring(2).getBytes()[0];
		}
		System.out.println(Arrays.toString(res));
		return res;
	}
	
}
