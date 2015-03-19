package ns.tcphack;

import java.net.*;
import java.util.*;

class MyTcpHandler extends TcpHandler {
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
			
			List<Byte> packetlist = new ArrayList<Byte>();
            byte version = 96; 		//ip version 6 (0110) with a zero part of the traffic class (0000)
            byte empty = 0;			//represents a (00000000) byte
            byte payload1 = 0;		//payload length part 1
            byte payload2 = 20;		//payload length part 2
//            byte nextHeader = 6;
            byte nextHeader = (byte)253; 	//corresponding with webserver (-3 == 253)
            byte hoplimit = 30;		//hop limit
            byte[] ownip = null;
            byte[] serverip = null;
			try {
				InetAddress srcip = Inet6Address.getByName("2001:67c:2564:a154:2ab2:bdff:fe0b:3cf9");
				ownip = srcip.getAddress();
				
				InetAddress destip = Inet6Address.getByName("2001:67c:2564:a170:a00:27ff:fe11:cecb");
				serverip = destip.getAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			//TCP
			byte srcport1 = (byte)143; 	//src port = 36710 (8f 66)
			byte srcport2 = 102;
			byte destport1 = 30;		
//			byte destport2 = 21;		//dest port = 7701 (TCP 6)
			byte destport2 = 31;		//dest port = 7711 (1e 1f) (TCP 253)
			byte seq1 = 0;				//seq
			byte seq2 = 0;
			byte seq3 = 0;
			byte seq4 = 1;
			byte ack1 = 0;				//ack
			byte ack2 = 0;
			byte ack3 = 0;
			byte ack4 = 0;
			byte datoffresflag = 80;	//header length (0101 == 5 == 20 byte) | reservation and 1 flag (0000)
			byte flags = 2;				// syn flag set
			byte windowsize1 = 112;
			byte windowsize2 = (byte)128;
			byte checksum1 = 0;
			byte checksum2 = 0;
			
			//making packet
			packetlist.add(version);
			packetlist.add(empty);
			packetlist.add(empty);
			packetlist.add(empty);
			packetlist.add(payload1);
			packetlist.add(payload2);
			packetlist.add(nextHeader);
			packetlist.add(hoplimit);
			for (byte part : ownip) {
				packetlist.add(part);
			}
			for (byte part : serverip) {
				packetlist.add(part);
			}
			packetlist.add(srcport1);
			packetlist.add(srcport2);
			packetlist.add(destport1);
			packetlist.add(destport2);
			packetlist.add(seq1);
			packetlist.add(seq2);
			packetlist.add(seq3);
			packetlist.add(seq4);
			packetlist.add(ack1);
			packetlist.add(ack2);
			packetlist.add(ack3);
			packetlist.add(ack4);
			packetlist.add(datoffresflag);
			packetlist.add(flags);
			packetlist.add(windowsize1);
			packetlist.add(windowsize2);
			packetlist.add(checksum1);
			packetlist.add(checksum2);
			
			byte[] packet = new byte[packetlist.size()];
			int i = 0;
			while (i < packet.length) {
				packet[i] = packetlist.get(i);
				i++;
			}
			this.sendData(packet);
			done = true;
		}   
	}
}
