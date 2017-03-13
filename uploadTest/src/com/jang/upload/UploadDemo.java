package com.jang.upload;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadDemo extends HttpServlet {
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String filename;
		request.setCharacterEncoding("utf-8");
		try {
			// 1.创建工厂
			DiskFileItemFactory fac = new DiskFileItemFactory();
			// 2.创建解析器；
			ServletFileUpload upload = new ServletFileUpload(fac);
			// 3.解析request
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				if (item.isFormField()) {
					String inputname = item.getFieldName(); //此处为表单普通输入项名称
					String inputvalue = item.getString("utf-8");
					//String inputvalue = new String((item.getString().getBytes("iso8859-1")),"utf-8");
					System.out.println(inputname + ":" + inputvalue);
				} else {
					if(item.getName().lastIndexOf("\\")>0){ //IE6.0
						filename = item.getName().substring(
								item.getName().lastIndexOf("\\") + 1);
						System.out.println("1:"+request.getHeader("user-agent"));
					}else{
						filename = item.getName();	
						System.out.println("2:"+request.getHeader("user-agent"));
					}
					
					InputStream input = item.getInputStream();
					int len = 0;
					byte[] b = new byte[1024];
					FileOutputStream out = new FileOutputStream("c:\\upload\\"
							+ filename);
					while ((len = input.read(b)) > 0) {
						out.write(b, 0, len);
					}
					input.close();
					out.close();

				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
