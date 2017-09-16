


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.services.s3.model.*;

import org.apache.tomcat.util.http.fileupload.IOUtils;

/**
 * Servlet implementation class upload
 */
@WebServlet("/upload")
@MultipartConfig(location="/", fileSizeThreshold=1024*1024, 
maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class upload extends HttpServlet {
	AmazonS3 s3client;String bucketName;
	private static final long serialVersionUID = 3L;

    public upload() {
        super();
        AWSCredentials credentials =   new BasicAWSCredentials("********", "*********");
        s3client = new AmazonS3Client(credentials);
        
        //s3client.createBucket(bucketName);

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie cookie = null;
		  Cookie[] cookies = null;
	      // Get an array of Cookies associated with this domain
	      cookies = request.getCookies();
	      String username="";
	      for (int i = 0; i < cookies.length; i++){
	            cookie = cookies[i];
	      if((cookie.getName( )).compareTo("username") == 0 )
	      {
	    	  username=cookie.getValue();
	      }}
		bucketName = username+"020993";
		Part filePart = request.getPart("file");
		String fileName = getFileName(filePart);
	    InputStream fileContent = filePart.getInputStream();
		final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(4);
		ListObjectsV2Result result;
		int flag=0;
		int noofkeys=0;
		do {
			result = s3client.listObjectsV2(req);

			for (S3ObjectSummary objectSummary :
					result.getObjectSummaries()) {
				S3Object object = s3client.getObject(new GetObjectRequest(bucketName, objectSummary.getKey()));
				noofkeys++;
			}
		}while (result.isTruncated() == true);
	    String prefix = fileName;
        String suffix = "";
	    if (fileName.contains("."))
        {
            prefix = fileName.substring(0, fileName.lastIndexOf('.'));
            suffix = fileName.substring(fileName.lastIndexOf('.'));
        }
	    File tempFile=File.createTempFile(prefix, suffix);

	    tempFile.deleteOnExit();
	    FileOutputStream out = new FileOutputStream(tempFile);
	    IOUtils.copy(fileContent, out);

		if(noofkeys<4&&suffix.equals(".txt")) {
			s3client.putObject(new PutObjectRequest(bucketName, fileName,
					tempFile));
			PrintWriter pw = response.getWriter().append("Text File Uploaded Successfully");
			pw.close();
		}
		else
		{
			PrintWriter pw = response.getWriter().append("More than 4 files not allowed");
			pw.close();
		}




		out.close();fileContent.close();

	}
	

	private String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String s= cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
				String[] sa =s.split("\\\\");
				return sa[sa.length-1];
			}
		}
		return null;
	}
}
