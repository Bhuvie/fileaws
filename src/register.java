

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;
import org.apache.commons.io.IOUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

/**
 * Servlet implementation class register
 */
@WebServlet("/register")
public class register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	AmazonS3 s3client;
    public register() {
        super();AWSCredentials credentials =   new BasicAWSCredentials("************", "*******************");
        s3client = new AmazonS3Client(credentials);
		//s3client.setRegion(Region.getRegion(Regions.US_WEST_2));
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=request.getParameter("un");
		String password=request.getParameter("pwd");
		String userstr=username+"_"+password;
		S3Object object = s3client.getObject(new GetObjectRequest("users0051", "users.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));
		StringBuilder builder=new StringBuilder();
		s3client.createBucket(username+"020993");
		String aux="";
		while ((aux = reader.readLine()) != null) {
		    builder.append(aux);
		}
		 builder.append(" "+userstr);
		 
     	InputStream in = IOUtils.toInputStream(builder.toString(), "UTF-8");
     	ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(builder.length());
     	s3client.putObject(new PutObjectRequest("users0051", "users.txt", in, metadata));
     	
     	ServletContext sc = getServletContext();
     	sc.getRequestDispatcher("/login.html").forward(request, response);

	}

}
