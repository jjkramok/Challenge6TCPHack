package ns.tcphack;

import java.util.Arrays;

class MyTcpHandler extends TcpHandler {
	
	public static final byte[] SRC = new byte[16]; //TODO add address
	public static final byte[] DST = new byte[16]; //TODO add address
	public static final byte[] HOP_LMT = new byte[]{0x1e}; //hop limit = 30
	
	public static void main(String[] args) {
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
            byte[] dtin = this.receiveData(1000);
            System.out.println("incoming dataArray: " + Arrays.toString(dtin));

		}   
	}
	
	/**
	 * Adding src and dst addresses to a packet aswell as a hop limit
	 * @param packet the packet in which the addresses will be written (length of 40 octets)
	 * @return byte[] with src and dst addresses copied into
	 */
	public byte[] addDefault(byte[] packet) {
		System.arraycopy(SRC, 0, packet, 8, SRC.length);
		System.arraycopy(DST, 0, packet, 24, DST.length);
		System.arraycopy(HOP_LMT, 0, packet, 7, HOP_LMT.length);
		return packet;
	}
	
}
