package com.hbarrera.appleassignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.web.client.HttpClientErrorException;

import com.hbarrera.appleassignment.model.Document;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppleAssignmentApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentControllerIntegrationTest {
     @Autowired
     private TestRestTemplate restTemplate;

     @LocalServerPort
     private int port;

     private String getRootUrl() {
         return "http://localhost:" + port;
     }

     @Test
     public void contextLoads() {

     }

    @Test
    public void testGetDocumentById() {
        String docId = "ABCDEFGHIJ1234567890";
   	    Document document = restTemplate.getForObject(getRootUrl() + "/documents/" + docId, Document.class);
        assertNotNull(document);
    }

    @Test
    public void testCreateDocument() {
    	Document document = new Document();
    	try {
	    	document.setDocName("The Apple Assignment");
			document.setDocContent(read(new File("Apple Assignment.docx")));
	    	document.setDocFileName("Apple Assignment.docx");
	    	document.setDocFileSize(1234567890);
	    	document.setDocFileType("Specification");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ResponseEntity<Document> postResponse = restTemplate.postForEntity(getRootUrl() + "/documents", document, Document.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdateDocument() {
        String docId = "ABCDEFGHIJ1234567890";
        Document document = restTemplate.getForObject(getRootUrl() + "/documents/" + docId, Document.class);
    	document.setDocName("The Apple Assignment is now 2");
    	document.setDocFileName("Apple Assignment2.docx");
    	try {
			document.setDocContent(read(new File("Apple Assignment2.docx")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        restTemplate.put(getRootUrl() + "/documents/" + docId, document);
        Document updatedDocument = restTemplate.getForObject(getRootUrl() + "/documents/" + docId, Document.class);
        assertNotNull(updatedDocument);
    }

    @Test
    public void testDeleteDocument() {
         String docId = "ABCDEFGHIJ1234567890";
         Document document = restTemplate.getForObject(getRootUrl() + "/documents/" + docId, Document.class);
         assertNotNull(document);
         restTemplate.delete(getRootUrl() + "/documents/" + docId);
         try {
        	 document = restTemplate.getForObject(getRootUrl() + "/documents/" + docId, Document.class);
         } catch (final HttpClientErrorException e) {
              assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
         }
    }
    
    public byte[] read(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        }finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }    
}