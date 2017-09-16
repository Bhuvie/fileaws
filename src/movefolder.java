import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Bhuvie on 3/1/2017.
 */
@WebServlet("/movefolder")
public class movefolder extends HttpServlet {
    private static final long serialVersionUID = 10L;
    AmazonS3 s3client;String bucketName;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AWSCredentials credentials =   new BasicAWSCredentials("*********************", "***********************");
        s3client = new AmazonS3Client(credentials);
        Cookie cookie = null;
        Cookie[] cookies = null;
        cookies = request.getCookies();
        String username="";
        for (int i = 0; i < cookies.length; i++){
            cookie = cookies[i];
            if((cookie.getName( )).compareTo("username") == 0 )
            {
                username=cookie.getValue();
            }}
        bucketName = request.getParameter("movfolduser")+"020993";
        String fileName=request.getParameter("movefoldfile");
        String bucketName1=request.getParameter("movefolduser")+"020993";
        System.out.println("Downloading an object");
        GetObjectRequest gor=new GetObjectRequest(bucketName, fileName);
        String prefix = fileName;
        String suffix = "";
        if (fileName.contains("."))
        {
            prefix = fileName.substring(0, fileName.lastIndexOf('.'));
            suffix = fileName.substring(fileName.lastIndexOf('.'));
        }
        File tempFile = File.createTempFile(prefix, suffix);
        if(username.equals("admin")) {
            ObjectMetadata object = s3client.getObject(gor, tempFile);
            s3client.putObject(new PutObjectRequest(bucketName1, fileName,
                    tempFile));
            s3client.deleteObject(bucketName, fileName);
            PrintWriter pw = response.getWriter().append("File moved");
            pw.close();
        }
        else
        {
            PrintWriter pw = response.getWriter().append("Only Admin can move the file");
            pw.close();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
