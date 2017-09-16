import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

/**
 * Created by Bhuvie on 3/1/2017.
 */
@WebServlet("/categoryupload")
@MultipartConfig(location="/", fileSizeThreshold=1024*1024,
        maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class categoryupload extends HttpServlet {

    private static final long serialVersionUID = 8L;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AmazonS3 s3client;
        String bucketName;
        AWSCredentials credentials = new BasicAWSCredentials("*************", "****************");
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
        String categorysel = request.getParameter("ft1");
        int flag=1;
        Part filePart = request.getPart("file2");
        String fileName = getFileName(filePart);
        InputStream fileContent = filePart.getInputStream();
        String prefix = fileName;
        String suffix = "";
        if (fileName.contains("."))
        {
            prefix = fileName.substring(0, fileName.lastIndexOf('.'));
            suffix = fileName.substring(fileName.lastIndexOf('.'));
        }
        if(categorysel.equals("text"))
        {

            if(!suffix.equals(".txt"))
            {
                PrintWriter pw=response.getWriter().append("Only text files allowed");
                flag=0;
                pw.close();
            }
            if(suffix.equals(".txt"))
            {
                File tempFile=File.createTempFile(prefix, suffix);
                tempFile.deleteOnExit();
                FileOutputStream out = new FileOutputStream(tempFile);
                IOUtils.copy(fileContent, out);
                s3client.putObject(new PutObjectRequest(bucketName, "Text/"+fileName,
                        tempFile));
                out.close();fileContent.close();
                PrintWriter pw=response.getWriter().append("File Uploaded Successfully");
                flag=0;
                pw.close();
            }

        }
        if(categorysel.equals("other"))
        {
            if(suffix.equals(".txt"))
            {
                PrintWriter pw=response.getWriter().append("Text files not allowed");
                flag=0;
                pw.close();
            }
            if(!suffix.equals(".txt"))
            {
                File tempFile=File.createTempFile(prefix, suffix);
                tempFile.deleteOnExit();
                FileOutputStream out = new FileOutputStream(tempFile);
                IOUtils.copy(fileContent, out);
                s3client.putObject(new PutObjectRequest(bucketName,"Others/"+fileName,
                        tempFile));
                out.close();fileContent.close();
                PrintWriter pw=response.getWriter().append("File Uploaded Successfully");
                flag=0;
                pw.close();
            }


        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
