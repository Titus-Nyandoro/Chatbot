package vua.inc.chatbot.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

@Service
public class DocumentsDownloaderService {
    public String loadDocument(String url) throws IOException {
        String fileExtension = url.substring(url.lastIndexOf(".") + 1);

        switch (fileExtension) {
            case "pdf":
                return loadPdf(url);
            case "txt":
                return loadText(url);
            case "docx":
                return loadDocx(url);
            case "html":
                return loadHtml(url);
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileExtension);
        }
    }
    private String loadPdf (String url) throws IOException{
        URL pdfUrl = new URL(url);
           try (InputStream in = pdfUrl.openStream()) {
            PDDocument document = PDDocument.load(in);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();
            return text;
        }
    }
    private String loadText(String url) throws IOException {
        URL textUrl = new URL(url);
        try (InputStream in = textUrl.openStream()) {
            byte[] fileBytes = in.readAllBytes();
            return new String(fileBytes, "UTF-8");
        }
    }
     public static String loadHtml(String urlString) throws IOException {
        URL url = new URL(urlString);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            return sb.toString();
        }
    }

    private String loadDocx(String url) throws IOException {
        URL docxUrl = new URL(url);
        try (InputStream in = docxUrl.openStream()) {
            XWPFDocument document = new XWPFDocument(in);
            StringBuilder text = new StringBuilder();
            document.getBodyElements().forEach(element -> {
                if (element instanceof XWPFParagraph) {
                    XWPFParagraph paragraph = (XWPFParagraph) element;
                    paragraph.getRuns().forEach(run -> text.append(run.getText(0)));
                }
            });
            document.close();
            return text.toString();
        }
    }
    
}
