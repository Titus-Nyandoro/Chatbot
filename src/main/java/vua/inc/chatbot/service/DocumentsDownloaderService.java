package vua.inc.chatbot.service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import vua.inc.chatbot.utils.Constants;;

@Service
public class DocumentsDownloaderService {
  
    public void load(String url) {
        try {
            URL pdfUrl = new URL(url);
            URLConnection connection = pdfUrl.openConnection();
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);
        
            String destinationPath = "downloaded.pdf";
            Resource resource = new ClassPathResource(destinationPath);
            // resource.getFile().getAbsolutePath()
            File destinationFile = new File("/home/macharia/projects/vua/ai2/Chatbot/src/main/resources/downloaded.pdf");
            FileOutputStream output = new FileOutputStream(destinationFile, false);
            byte[] byteArray = new byte[1024]; 
            int readLength;
            // URL url = new URL(pdfUrl);
            InputStream inputStream = connection.getInputStream();
            while ((readLength = inputStream.read(byteArray)) > 0) {
                output.write(byteArray, 0, readLength);
            }
            // inputStream.close();
            output.flush();                                
            output.close();
            inputStream.close();
            
           
            
        } catch (IOException e) {
            // Handle download errors gracefully
            throw new RuntimeException("Error downloading document", e);
        } catch (IllegalArgumentException e) {
            // Handle non-PDF resources
            throw new RuntimeException("Non-PDF resource provided: " + e.getMessage());
        }
     }
     public void getDocumentsFromLink() {
        // List<Document> docs = new ArrayList<>();
        Constants.DOWNLOAD_LIST.stream()
                .forEach(url -> {
                    try {
                        load(url);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to load document from " + url, e);
                    }
                });
        
        
    }
}
   

    

