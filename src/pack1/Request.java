package pack1;

import java.io.InputStream;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Request {

    private InputStream input;
    private String path;
    private HashMap<String, String> message;

    public Request(InputStream input, HashMap<String, String> mess) {
    	message = mess;
        this.input = input;
    }

    //��InputStream�ж�ȡrequest��Ϣ������request�л�ȡuriֵ
    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        request.append("\n");
System.out.print(request.toString());
        path = parseUri(request.toString());
        dealRequest();
    }

    private void dealRequest() {
    	String Mess = "";
    	Mess += path;
    	int index = Mess.indexOf(' ');
    	path = Mess.substring(index+1, Mess.length());
    	
    	
    }

    private String parseUri(String requestString) {
        int index1, index2;
        String methon = "";
        index1 = requestString.indexOf(' ');
        if(index1 != -1)
        	methon = requestString.substring(0, index1);
        index2 = requestString.indexOf(' ', index1 + 1);

        if(index1<index2) {
        	if(methon.equals("POST"))
            {//����û�������
        		int index3 = requestString.indexOf("username=")+9;
        		int index4 = requestString.indexOf("&");
        		int index5 = requestString.indexOf("password=")+9;
        		String username = requestString.substring(index3, index4);
        		String password = requestString.substring(index5, requestString.length()-1);
System.out.println(username+", "+password);

        		if(!username.equals("")&&!password.equals("")) {
        			//�ȶ��û�����
        			if(message.containsKey(username)) {
        				String up = message.get(username);
        				if(up.equals(password))
        					System.out.println("sucess");
        				if(message.get(username).equals(password)){
                    		return "/welcome.html";
                		}
        			}
        			else {
        				message.put(username, password);
        				File userFile = new File(HttpServer.WEB_ROOT, "user.txt");
        				try {
							FileOutputStream fos = new FileOutputStream(userFile, true);
							String writein = username+" "+password+"\n";
							fos.write(writein.getBytes());//�洢post��Ϣ������
							//�Զ������û���Ϣ
						} catch (FileNotFoundException e) {}
        				catch (IOException e) {}
                		return "/move.html";
        			}
        		}
        		return "/move.html";
            	//�ض���
            }
            else if(methon.equals("GET")) {//�����û��������Դ
            	String temp = requestString.substring(index1 + 1, index2);
            	return temp;
            }
        }
        
        return null;
    }

    public String getPath() {
        return path;
    }

}
