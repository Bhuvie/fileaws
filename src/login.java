

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 102831973239L;
	AmazonS3 s3client;String bucketName="users0051";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();AWSCredentials credentials =   new BasicAWSCredentials("*******************", "**********************");
        s3client = new AmazonS3Client(credentials);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user=request.getParameter("usrname");
		String passwd=request.getParameter("passwd");
		String userstr=user+"_"+passwd;
		Cookie cookie=new Cookie("username",user);
		S3Object object = s3client.getObject(new GetObjectRequest(bucketName, "users.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));
		int flag=1;
		 while (flag==1) {
	            String line = reader.readLine();
	            if(line.contains(userstr))
	            {
	            	response.addCookie(cookie);
//	            	ServletContext sc = getServletContext();
//	            	sc.getRequestDispatcher("/index.html").forward(request, response);
					PrintWriter pw=response.getWriter().append("<script language=\"javascript\">"+"alert(" + "\"Login Successful\"" + ");"+"window.location.replace(\"\\index.html\");"+"</script>");
	            	flag=0;
	            }
	            else
	            {
	            	PrintWriter pw=response.getWriter().append("<script language=\"javascript\">"+"alert(" + "\"Login failed\"" + ");"+"window.location.replace(\"\\login.html\");"+"</script>");
	            	pw.close();
					//response.sendRedirect("/login.html");
					flag=0;
	            }
		 }
	}

}
