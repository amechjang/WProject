package com.jang.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadDemo extends HttpServlet {
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String filename = null;
		request.setCharacterEncoding("utf-8");
		// �����ϴ��ļ���ʽ
		List<String> type = Arrays.asList(".jpg",".avi",".png",".txt",".pdf");
		try {
			// 1.��������
			DiskFileItemFactory fac = new DiskFileItemFactory();
			fac.setSizeThreshold(1024*1024); //Ĭ�ϻ���Ϊ10k���ϴ��ļ����ڻ������ڴ����Ƚ��ļ�������ʱ�ļ��У��ڽ����ϴ�
			System.out.println(this.getServletContext().getRealPath("/temp"));
			fac.setRepository(new File(this.getServletContext().getRealPath("/temp"))); //��ʱ�ļ�
			// 2.������������
			ServletFileUpload upload = new ServletFileUpload(fac);
			//2.1���õ��ļ����洢
			upload.setFileSizeMax(1024*1024);
			// 3.����request
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				if (item.isFormField()) {
					String inputname = item.getFieldName(); //�˴�Ϊ����ͨ����������
					String inputvalue = item.getString("utf-8");
					//String inputvalue1 = new String((item.getString().getBytes("iso8859-1")),"utf-8");
					System.out.println(inputname + ":" + inputvalue);
				} else {
					if(item.getName() == null ||item.getName().trim()==""){
						continue;	
					}
					if(type.contains(item.getName().substring(item.getName().lastIndexOf(".")))){
						/*if(item.getName().lastIndexOf("\\")>0){ //IE6.0
							filename = item.getName().substring(
									item.getName().lastIndexOf("\\") + 1);
							System.out.println("1:"+request.getHeader("user-agent"));
						}else{*/
							filename = item.getName();	
							System.out.println("2:"+request.getHeader("user-agent"));
						//}
					}else{
						request.setAttribute("message", "������ļ�"+item.getName().substring(item.getName().lastIndexOf("."))+"��ʽ����ȷ");
						request.getRequestDispatcher("/fileTypeErro.jsp").forward(request, response);
						return;
					}
					InputStream input = item.getInputStream();
					int len = 0;
					byte[] b = new byte[1024];
					FileOutputStream out = new FileOutputStream(this.getServletContext().getRealPath("/WEB-INF/upload")+"\\"  
							+ filename);
					System.out.println(this.getServletContext().getRealPath("/WEB-INF/upload")+"\\"+ filename);
					while ((len = input.read(b)) > 0) {
						out.write(b, 0, len);
					}
					input.close();
					out.close();
					item.delete(); //�ϴ���ɣ����رպ�ʱɾ����ʱ�ļ�
					System.out.println("upload success");
				}
			}
		}catch(FileUploadBase.FileSizeLimitExceededException e){
			request.setAttribute("message", "�ļ�����5M���޷��ϴ�");
			request.getRequestDispatcher("/fileTypeErro.jsp").forward(request, response);
			return;
		} 
		catch (FileUploadException e) {
			throw new RuntimeException(e);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
