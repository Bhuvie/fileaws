

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.amazonaws.auth.AWSCredentials;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;


/**
 * Servlet implementation class download
 */
@WebServlet("/download")
public class download extends HttpServlet {
	private static final long serialVersionUID = 6L;
	AmazonS3 s3client;String bucketName;

    public download() {
        super();
        AWSCredentials credentials =   new BasicAWSCredentials("******************", "***********************");
        s3client = new AmazonS3Client(credentials);
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
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
		String fileName=request.getParameter("dfilename");
		System.out.println("Downloading an object");
		GetObjectRequest gor=new GetObjectRequest(bucketName, fileName);
		String prefix = fileName;
        String suffix = "";
	    if (fileName.contains("."))
        {
            prefix = fileName.substring(0, fileName.lastIndexOf('.'));
            suffix = fileName.substring(fileName.lastIndexOf('.'));
        }
		File localFile = File.createTempFile(prefix, suffix);
		ObjectMetadata object = s3client.getObject(gor, localFile);
		response.setContentType("application/octet-stream");
	    response.setHeader("Content-Disposition", "filename=\""+fileName+"\"");
	    response.setContentLength((int) localFile.length());
	    FileInputStream fis = new FileInputStream(localFile);
//	    String decode="";
//	    try
//		{
//			AES aesobj=new AES();
//			decode=aesobj.decrypt(fis1.toString());
//			PrintWriter pw=response.getWriter().append(decode);
//		}
//		catch(Exception e)
//		{
//
//		}
//		FileInputStream fis=new FileInputStream(localFile);
	    IOUtils.copy(fis, response.getOutputStream());
	    localFile.deleteOnExit();
	    fis.close();

	    //PrintWriter pw=response.getWriter();
	    
       // S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
       // System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
      //  object.getObjectContent();
	}

}
