

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Servlet implementation class delete
 */
@WebServlet("/delete")
public class delete extends HttpServlet {
	private static final long serialVersionUID = 5L;
	AmazonS3 s3client;String bucketName;
    public delete() {
        super();
        AWSCredentials credentials =   new BasicAWSCredentials("************************", "**********************");
        s3client = new AmazonS3Client(credentials);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName1=request.getParameter("defile");
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
		if(username=="admin") {
			String bucketName1=request.getParameter("bname1")+"020993";
			s3client.deleteObject(bucketName1, fileName1);
			PrintWriter pw = response.getWriter().append("<script language=\"javascript\">" + "alert(" + "\"File Deleted Successfully\"" + ");" + "window.location.replace(\"\\index.html\");" + "</script>");
			pw.close();
		}
		else
		{
			PrintWriter pw = response.getWriter().append("<script language=\"javascript\">" + "alert(" + "\"Only admin can Delete\"" + ");" + "window.location.replace(\"\\index.html\");" + "</script>");
			pw.close();
		}
	}

}
