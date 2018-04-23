package pack1;

import java.io.OutputStream;
import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;


public class Response {

    private static final int BUFFER_SIZE = 1024;
    private Request request;
    private OutputStream output;
    

    public Response(OutputStream op, Request req) {
        output = op;
        request = req;
    }

    private void writeFiles(File file, String Message) {
    	FileInputStream fis = null;
    	byte[] bytes = new byte[BUFFER_SIZE];
    	try {
    		System.out.println("filename:"+file.getName());
        	String message = Message;
        	message += "Content-Type: "+(new MimetypesFileTypeMap().getContentType(file))+"\r\n";
        	message += "Content-Length: "+file.length()+"\r\n";
            message += "\r\n";
            output.write(message.getBytes());
            fis = new FileInputStream(file);
            int ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch != -1) {
                output.write(bytes, 0, ch);
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }
            
    	}catch(IOException ioe) {
    		ioe.printStackTrace();
    	} finally {
            if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
    }
    
    public void sendStaticResource() throws IOException {
        try {
            //将web文件写入到OutputStream字节流中
        	String path = request.getPath();
System.out.println("requestPath:"+path);
        	File file = new File(HttpServer.WEB_ROOT, path);
        	
            if (file.exists())
            	writeFiles(file, "HTTP/1.1 200 ok\r\n");//访问成功的信息
            else {//未找到指定文件，302建议重定向的信息
            	file = new File(HttpServer.WEB_ROOT, "/NotFound.html");
                writeFiles(file, "HTTP/1.1 302 Moved Temporarily\r\nLocation: /post.html\r\n");
            }
            output.flush();
            
        } catch (Exception e) {
            // thrown if cannot instantiate a File object
            System.out.println(e.toString());
        }
    }
}
