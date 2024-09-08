package com.demo.Phonebook.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.Phonebook.Entity.Contact;
import com.demo.Phonebook.Entity.UserInfo;
import com.demo.Phonebook.enums.ContactTypeEnum;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
	List<Contact> findAllByUserInfo(UserInfo userInfo);
	List<Contact> findByUserInfoAndType(UserInfo userInfo , ContactTypeEnum type);
}
