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

            // ����Request���󲢽���
            Request request = new Request(input, message);
            request.parse();
            // ����Ƿ��ǹرշ�������
            if (request.getPath().equals(HttpServer.SHUTDOWN_COMMAND)) {
                client.close();
            }
            else {
            	// ���� Response ����
                Response response = new Response(output, request);
                response.sendStaticResource();

                // �ر� socket ����
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
