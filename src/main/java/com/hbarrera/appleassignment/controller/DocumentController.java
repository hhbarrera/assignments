package com.hbarrera.appleassignment.controller;

import java.io.IOException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import com.hbarrera.appleassignment.exception.ResourceNotFoundException;
import com.hbarrera.appleassignment.model.Document;
import com.hbarrera.appleassignment.repository.DocumentRepository;

@RestController
@RequestMapping("/storage")
public class DocumentController {
    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/documents/{docId}")
    public @ResponseBody byte[] getDocumentById(@PathVariable(value = "docId") String docId)
    throws ResourceNotFoundException {
        Document document = documentRepository.findById(docId)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found for docId :: " + docId));
        return document.getDocContent();
    }

    @PostMapping("/documents")
    public ResponseEntity <?> createDocument(@RequestParam("docContent") MultipartFile docContent, @RequestParam("docName") String docName) {
    	Document document = new Document();
    	document.setDocName(docName);
    	document.setDocFileName(docContent.getOriginalFilename());
    	document.setDocFileSize(docContent.getSize());
    	document.setDocFileType(docContent.getContentType());
    	try {
			document.setDocContent(docContent.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	final Document createdDocument = documentRepository.save(document);
    	return new ResponseEntity<>(createdDocument.getDocId(), HttpStatus.CREATED);
    }

    @PutMapping("/documents/{docId}")
    public ResponseEntity <?> updateDocument(@PathVariable(value = "docId") String docId,
    		@RequestParam("docContent") MultipartFile docContent, @RequestParam("docName") String docName) throws ResourceNotFoundException {
    	Document document = documentRepository.findById(docId)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found for docId :: " + docId));

    	document.setDocName(docName);
    	document.setDocFileName(docContent.getOriginalFilename());
    	document.setDocFileSize(docContent.getSize());
    	document.setDocFileType(docContent.getContentType());
    	try {
			document.setDocContent(docContent.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	document.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        //final Document updatedDocument = documentRepository.save(document);
        //return new ResponseEntity<>(updatedDocument, HttpStatus.NO_CONTENT);
        documentRepository.save(document);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/documents/{docId}")
    public ResponseEntity<?> deleteDocument(@PathVariable(value = "docId") String docId)
    throws ResourceNotFoundException {
    	return documentRepository.findById(docId)
    	        .map(document -> {
    	        	documentRepository.delete(document);
    	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    	         })
            .orElseThrow(() -> new ResourceNotFoundException("Document not found for docId :: " + docId));
    }

}
