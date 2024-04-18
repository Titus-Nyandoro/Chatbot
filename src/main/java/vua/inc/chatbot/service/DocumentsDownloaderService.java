package vua.inc.chatbot.service;


import java.io.FileOutputStream;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.beans.factory.annotation.Value;

import vua.inc.chatbot.utils.Constants;;

@Service
public class DocumentsDownloaderService {
  
    public void load(String url) {
        try {
            URL pdfUrl = new URL(url);
            URLConnection urlConnection = pdfUrl.openConnection();
            if (!urlConnection.getContentType().equalsIgnoreCase("application/pdf")) {
                throw new IllegalArgumentException("Resource provided not a pdf");
            }

            // FileOutputStream outputStream = new FileOutputStream("src/main/resources/downloaded.pdf");
            // byte[] byteArray = new byte[1024]; 
            // int readLength;
            // URL url = new URL(pdfUrl);
            // InputStream inputStream = pdfUrl.openStream();
            Path tempDir = Paths.get("src/main/resources/");
            InputStream inputStream = pdfUrl.openStream();
            Path tempFile = Files.createTempFile(tempDir,"downloadedFile", ".pdf");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            // while ((readLength = inputStream.read(byteArray)) > 0) {
            //     outputStream.write(byteArray, 0, readLength);
            // }
            inputStream.close();
            // /outputStream.flush();
            
           
            
           
            // outputStream.close();
            // inputStream.close();
            
           
            
        } catch (Exception e) {
            throw new RuntimeException("error downloading the resource",e);
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
   
    // private String loadText(String url) throws IOException {
    //     URL textUrl = new URL(url);
    //     try (InputStream in = textUrl.openStream()) {
    //         byte[] fileBytes = in.readAllBytes();
    //         return new String(fileBytes, "UTF-8");
    //     }
    // }
    // public String loadHtml(String urlString) throws IOException {
    //     URL url = new URL(urlString);
    //     try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
    //         StringBuilder sb = new StringBuilder();
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             sb.append(line).append(System.lineSeparator());
    //         }
    //         System.out.println("called");
    //         return sb.toString();

    //     }
    // }

    // private String loadDocx(String url) throws IOException {
    //     URL docxUrl = new URL(url);
    //     try (InputStream in = docxUrl.openStream()) {
    //         XWPFDocument document = new XWPFDocument(in);
    //         StringBuilder text = new StringBuilder();
    //         document.getBodyElements().forEach(element -> {
    //             if (element instanceof XWPFParagraph) {
    //                 XWPFParagraph paragraph = (XWPFParagraph) element;
    //                 paragraph.getRuns().forEach(run -> text.append(run.getText(0)));
    //             }
    //         });
    //         document.close();
    //         return text.toString();
    //     }
    // }
    

