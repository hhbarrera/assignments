package com.hbarrera.appleassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hbarrera.appleassignment.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String>{

}
