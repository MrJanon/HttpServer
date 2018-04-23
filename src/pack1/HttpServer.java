package pack1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

	public static final String WEB_ROOT = "E:\\ProgramFiles\\HTML";
	public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
	public static final int THREAD_NUMBER = 200;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, String> message = new HashMap<String, String>();
		try {
			File userFile = new File(WEB_ROOT, "user.txt");
			Scanner reader = new Scanner(new FileInputStream(userFile));
			while(reader.hasNext()) {
				String next = reader.nextLine();
				if(!next.equals(""))
				{
					String line[] = next.split(" ");
					message.put(line[0], line[1]);
System.out.println("username:"+line[0]+", password:"+line[1]);
				}
			}
			reader.close();
			
			ServerSocket server = new ServerSocket(80);
			ExecutorService pool = Executors.newFixedThreadPool(THREAD_NUMBER);
			//线程池
			while(true)
			{
				Socket client = server.accept();
				client.setSoTimeout(10000);//设置连接超时
				Thread task = new Thread(new SettingsCtr(client, message));
				pool.submit(task);
				System.out.println("start");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
