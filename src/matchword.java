import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by Bhuvie on 2/28/2017.
 */
@WebServlet("/matchword")
public class matchword extends HttpServlet {
    private static final long serialVersionUID = 7L;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AmazonS3 s3client;
        String bucketName;
        AWSCredentials credentials = new BasicAWSCredentials("******************", "******************");
        s3client = new AmazonS3Client(credentials);
        Cookie cookie = null;
        Cookie[] cookies = null;
        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();
        String username = "";
        for (int i = 0; i < cookies.length; i++) {
            cookie = cookies[i];
            if ((cookie.getName()).compareTo("username") == 0) {
                username = cookie.getValue();
            }
        }
        bucketName = username + "020993";
        String keyword = request.getParameter("keywrd");
        final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(4);
        ListObjectsV2Result result;
        int flag=0;
        do {
            result = s3client.listObjectsV2(req);

            for (S3ObjectSummary objectSummary :
                    result.getObjectSummaries()) {
                S3Object object = s3client.getObject(new GetObjectRequest(bucketName, objectSummary.getKey()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));

                String line1 = reader.readLine();
                String line2 = reader.readLine();
                String line3 = reader.readLine();
                String line4 = reader.readLine();
                //if (line1.contains(keyword)) {
                    PrintWriter pw = response.getWriter().append("Filename:"+objectSummary.getKey()+" Contents:"+"Line1: "+line1+"Line2: "+line2+"Line3: "+line3+"Line4: "+line4);
                    flag=1;
                    pw.close();
                //}
//              else {
//                    PrintWriter pw = response.getWriter().append("Doesn't match any word in the first line");
//                    //response.sendRedirect("/login.html");
//
//                }

            }
        } while (result.isTruncated() == true);
        if(flag==0)
        {
            PrintWriter pw = response.getWriter().append("Doesn't match any word in the first line of any file");
            pw.close();
        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
