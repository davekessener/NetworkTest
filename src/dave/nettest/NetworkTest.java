package dave.nettest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class NetworkTest
{
	public static void main(String[] args) throws IOException
	{
		if(args.length > 0 && args[0].equals("server"))
		{
			runServer(args);
		}
		else if(args.length > 0 && args[0].equals("client"))
		{
			runClient(args);
		}
		else
		{
			System.out.println("usage:\n\tserver port\n\tclient server-ip port");
		}
	}

	private static void runServer(String[] args) throws IOException
	{
		final int port = Integer.parseInt(args[1]);
		
		try (final ServerSocket ss = new ServerSocket(port))
		{
			System.out.println("Listening on port " + port);
			
			while(true)
			{
				try (final Socket client = ss.accept())
				{
					System.out.println("Accepted connection from " + client.getInetAddress().getHostAddress());
					
					final DataInputStream in = new DataInputStream(client.getInputStream());
					final DataOutputStream out = new DataOutputStream(client.getOutputStream());
					
					String recv = in.readUTF();
					
					System.out.println("Received '" + recv + "'");
					System.out.println(REQUEST.equals(recv) ? "SUCCESS" : "FAILURE");
					
					out.writeUTF(RESPONSE);
				}
			}
		}
	}
	
	private static void runClient(String[] args) throws IOException
	{
		final String ip = args[1];
		final int port = Integer.parseInt(args[2]);
		
		System.out.println("Connecting to " + ip + ":" + port);
		
		try (final Socket server = new Socket())
		{
			final SocketAddress addr = new InetSocketAddress(ip, port);
			
			server.connect(addr);
			
			final DataInputStream in = new DataInputStream(server.getInputStream());
			final DataOutputStream out = new DataOutputStream(server.getOutputStream());
			
			out.writeUTF(REQUEST);
			
			String recv = in.readUTF();
			
			System.out.println("Received '" + recv + "'");
			System.out.println(RESPONSE.equals(recv) ? "SUCCESS" : "FAILURE");
		}
	}
	
	private static final String REQUEST = "client message";
	private static final String RESPONSE = "server message";
}
