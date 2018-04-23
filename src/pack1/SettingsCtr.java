package pack1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;


public class SettingsCtr implements Runnable {

	private Socket client;

	private HashMap<String, String> message;
	public SettingsCtr(Socket c, HashMap<String, String> mess) {
		// TODO Auto-generated constructor stub
		client = c;
		message = mess;
	}

	public void run() {
await();
	}

	public void await() {

		InputStream input = null;
        OutputStream output = null;
        try {
            
            input = client.getInputStream();
            output = client.getOutputStream();

            // 创建Request对象并解析
            Request request = new Request(input, message);
            request.parse();
            // 检查是否是关闭服务命令
            if (request.getPath().equals(HttpServer.SHUTDOWN_COMMAND)) {
                client.close();
            }
            else {
            	// 创建 Response 对象
                Response response = new Response(output, request);
                response.sendStaticResource();

                // 关闭 socket 对象
                client.close();
            }
            
        } catch (SocketTimeoutException e) {
        	try {
				client.close();
			} catch (IOException e1) {}
        } catch (NullPointerException e) {
        	try {
				client.close();
			} catch (IOException e1) {}
        } catch (IOException e) {
            e.printStackTrace();
            
        }
	}
}
