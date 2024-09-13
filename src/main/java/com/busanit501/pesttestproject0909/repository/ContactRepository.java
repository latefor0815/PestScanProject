package com.busanit501.pesttestproject0909.repository;

import com.busanit501.pesttestproject0909.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}