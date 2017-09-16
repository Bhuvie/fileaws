import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created by Bhuvie on 2/28/2017.
 */
@WebServlet("/createfolder")
public class createfolder extends HttpServlet {
    private static final long serialVersionUID = 4L;
    AmazonS3 s3client;String bucketName;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AWSCredentials credentials =   new BasicAWSCredentials("**************", "***********************");
        s3client = new AmazonS3Client(credentials);
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
        String folderName=request.getParameter("cfoldertext");
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,folderName+"/",emptyContent,metadata);
        s3client.putObject(putObjectRequest);
        PrintWriter pw=response.getWriter().append("Folder Created Successfully");
        pw.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
