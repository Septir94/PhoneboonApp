package com.demo.Phonebook.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


import com.demo.Phonebook.Entity.UserInfo;

@Repository
public interface UserInfoPageRepository extends PagingAndSortingRepository<UserInfo, Integer>{
	@Query(value = "Select u from UserInfo u where u.firstName like %?1% or u.middleName like %?1% or u.lastName like %?1% or u.company like %?1%")
	Page<UserInfo> findAllPhonebookPage(String search,Pageable page);
	
}
