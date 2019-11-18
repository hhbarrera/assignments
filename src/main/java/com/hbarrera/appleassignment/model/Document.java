package com.hbarrera.appleassignment.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Basic;
import javax.persistence.Lob;
import lombok.Data;

@Entity
@Table(name = "documents")
@Data
public class Document {

	@Id
	private String docId = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
	
	@Column(name="name", nullable = false, length = 256)
	private String docName;
	
	@Column(name="fileName", nullable = false, length = 256)
	private String docFileName;
	
	@Column(name="fileSize")
	private long docFileSize;
	
	@Column(name="fileType")
	private String docFileType;
	
	@Column(name="content",nullable = true)
    @Basic(optional = true, fetch = FetchType.EAGER)
    @Lob()
    private byte[] docContent;
	
	@Column(name="createdDate")
	private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

	@Column(name="updatedDate")
	private Timestamp updatedDate;

}
