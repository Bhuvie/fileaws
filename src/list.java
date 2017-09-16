

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
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Servlet implementation class list
 */
@WebServlet("/list")
public class list extends HttpServlet {
	private static final long serialVersionUID = 7L;
	AmazonS3 s3client;String bucketName;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public list() {
        super();
        AWSCredentials credentials =   new BasicAWSCredentials("******************", "*************************");
        s3client = new AmazonS3Client(credentials);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
		bucketName = username.concat("020993");
		PrintWriter pw=response.getWriter();
		pw.append("<div class='row' style="+'"'+"padding-bottom:10px"+'"'+"><h4><div class='col-sm-5'>FileName</div><div class='col-sm-3'>File Size</div><div class='col-sm-4'>LastUpdatedOn</div></h4></div>");
		
		ObjectListing objectListing = s3client.listObjects(new ListObjectsRequest()
                .withBucketName(bucketName));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        	pw.append("<div class='row' style="+'"'+"padding-bottom:10px"+'"'+"><div class='col-sm-5'>"+objectSummary.getKey()+"</div><div class='col-sm-3'>"+objectSummary.getSize()+"</div><div class='col-sm-4'>"+objectSummary.getLastModified().toString()+"</div></div>");

     
        }
        pw.close();
	}

}
