package com.engineer.rpc.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.engineer.rpc.RPCInvocation;
import com.engineer.rpc.RPCServer;

public class BIOListener extends Thread {
	private ServerSocket socket;
	private RPCServer server;

	public BIOListener(RPCServer server) {
		this.server = server;
	}

	@Override
	public void run() {

		System.out.println("启动服务器中，打开端口" + server.getPort());
		try {
			socket = new ServerSocket(server.getPort());
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		while (server.isRunning()) {
			try {
				
				Socket client = socket.accept();
				ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				RPCInvocation invo = (RPCInvocation) ois.readObject();
				server.call(invo);
				ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
				oos.writeObject(invo);
				oos.flush();
				oos.close();
				ois.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		try {
			if (socket != null && !socket.isClosed())
				socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
