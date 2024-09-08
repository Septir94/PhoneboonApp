package com.demo.Phonebook.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.demo.Phonebook.DTO.ContactDTO;
import com.demo.Phonebook.DTO.PhonebookDTO;
import com.demo.Phonebook.DTO.ResponseBody;
import com.demo.Phonebook.Entity.Contact;
import com.demo.Phonebook.Entity.UserInfo;
import com.demo.Phonebook.ExceptionHandler.BadRequestException;
import com.demo.Phonebook.ExceptionHandler.NotFoundException;
import com.demo.Phonebook.Repository.ContactRepository;
import com.demo.Phonebook.Repository.UserInfoPageRepository;
import com.demo.Phonebook.Repository.UserInfoRepository;
import com.demo.Phonebook.enums.ContactTypeEnum;

@Service
public class PhonebookService {

	@Autowired
	UserInfoRepository userInfoRepo;
	@Autowired
	ContactRepository contactRepo;
	@Autowired
	UserInfoPageRepository pageRepo;

	public ResponseBody<PhonebookDTO> createPhonebook(PhonebookDTO dto) throws ParseException, BadRequestException {
		if (dto.getFirstName().isEmpty() && dto.getFirstName().isBlank()) {
			throw new BadRequestException("Please input first name");
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setCompany(dto.getCompany());
		userInfo.setDeleted(false);
		userInfo.setFirstName(dto.getFirstName());
		userInfo.setMiddleName(dto.getMiddleName());
		userInfo.setLastName(dto.getLastName());
		userInfo = userInfoRepo.save(userInfo);

		for (ContactDTO c : dto.getContacts()) {
			Contact contact = new Contact();
			contact.setType(ContactTypeEnum.valueOf(c.getType()));
			contact.setValue(c.getValue());
			contact.setUserInfo(userInfo);
			contactRepo.save(contact);
		}

		ResponseBody<PhonebookDTO> response = new ResponseBody<PhonebookDTO>("Insert Success", dto);
		return response;
	}

	public ResponseBody<PhonebookDTO> findPhonebookById(int id) {
		Optional<UserInfo> optPhonebook = userInfoRepo.findById(id);
		if (optPhonebook.isEmpty()) {
			throw new NotFoundException("Phone book is not found");
		} else {

			UserInfo userInfo = optPhonebook.get();
			if (userInfo.isDeleted()) {
				throw new NotFoundException("Phone book is not found");
			}

			ModelMapper mapper = new ModelMapper();
			PhonebookDTO dto = mapper.map(userInfo, PhonebookDTO.class);
			dto.setContacts(mapContactToDto(userInfo.getContacts()));

			ResponseBody<PhonebookDTO> response = new ResponseBody<PhonebookDTO>("Get Phone book data success", dto);
			return response;
		}

	}

	public List<PhonebookDTO> getAllPhonebook() throws ParseException {
		List<PhonebookDTO> list = new ArrayList<PhonebookDTO>();
		userInfoRepo.findAllByOrderByFirstName().forEach(a -> {
			if (!a.isDeleted()) {
				ModelMapper mapper = new ModelMapper();

				PhonebookDTO dto = mapper.map(a, PhonebookDTO.class);
				dto.setContacts(mapContactToDto(a.getContacts()));
				list.add(dto);
			}
		});
		return list;
	}

//	
////	Pagination
	public List<PhonebookDTO> getPagePhonebook(Integer page, Integer size, String search) {
		List<PhonebookDTO> list = new ArrayList<PhonebookDTO>();
		PageRequest pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());

		Page<UserInfo> userPage = pageRepo.findAllPhonebookPage(search, pageable);

		for (UserInfo u : userPage) {
			if (!u.isDeleted()) {
				ModelMapper mapper = new ModelMapper();
				PhonebookDTO dto = mapper.map(u, PhonebookDTO.class);
				dto.setContacts(mapContactToDto(u.getContacts()));
				list.add(dto);
			}
		}
		return list;
	}

//	
//	
	public ResponseBody<PhonebookDTO> deletePhonebook(Integer id) {
		Optional<UserInfo> userInfo = userInfoRepo.findById(id);
		if (userInfo.isEmpty() || (userInfo.isPresent() && userInfo.get().isDeleted()))
			throw new NotFoundException("Phone book Tidak Terdaftar");
		userInfo.get().setDeleted(true);
		userInfoRepo.save(userInfo.get());

		userInfo.get().getContacts().forEach(a -> {
			a.setDeleted(true);
			contactRepo.save(a);
		});

		ResponseBody<PhonebookDTO> response = new ResponseBody<PhonebookDTO>("Deleted Sucess", new PhonebookDTO());
		return response;

	}

//	 
	public ResponseBody<PhonebookDTO> updatePhonebook(Integer id, PhonebookDTO dto)
			throws ParseException, BadRequestException {
		Optional<UserInfo> optUserInfo = userInfoRepo.findById(id);

		if (optUserInfo.isEmpty() || (optUserInfo.isPresent() && optUserInfo.get().isDeleted())) {
			throw new NotFoundException("Data is not found");
		}

		UserInfo userInfo = optUserInfo.get();
		userInfo.setCompany(validateIsNotEmpty(dto.getCompany()) ? dto.getCompany() : userInfo.getCompany());
		userInfo.setFirstName(validateIsNotEmpty(dto.getFirstName()) ? dto.getFirstName() : userInfo.getFirstName());
		userInfo.setMiddleName(dto.getMiddleName());
		userInfo.setLastName(dto.getLastName());
		userInfoRepo.save(userInfo);

		dto.getContacts().forEach(a -> {
			if (a.getId() != null) {
				Optional<Contact> contact = contactRepo.findById(a.getId());
				if (contact.isPresent()) {
					contact.get().setType(validateIsNotEmpty(a.getType()) ? ContactTypeEnum.valueOf(a.getType())
							: contact.get().getType());
					contact.get().setValue(validateIsNotEmpty(a.getValue()) ? a.getValue() : contact.get().getValue());
					contactRepo.save(contact.get());
				} else if (validateIsNotEmpty(a.getType())) {
					List<Contact> contacts = contactRepo.findByUserInfoAndType(userInfo,ContactTypeEnum.valueOf(a.getType()));
					if (contacts.size() > 0) {
						Contact thisContact = contacts.get(0);
						thisContact.setType(validateIsNotEmpty(a.getType()) ? ContactTypeEnum.valueOf(a.getType())
								: thisContact.getType());
						thisContact.setValue(validateIsNotEmpty(a.getValue()) ? a.getValue() : thisContact.getValue());
						contactRepo.save(thisContact);
					} else {
						Contact newContact = new Contact();
						newContact.setType(ContactTypeEnum.valueOf(a.getType()));
						newContact.setValue(a.getValue());
						newContact.setUserInfo(userInfo);
						contactRepo.save(newContact);
					}
				}

			}
		});

		ResponseBody<PhonebookDTO> response = new ResponseBody<PhonebookDTO>("Update Success", dto);
		return response;
	}
//	

	private List<ContactDTO> mapContactToDto(List<Contact> list) {
		ModelMapper mapper = new ModelMapper();
		List<ContactDTO> result = new ArrayList<ContactDTO>();
		list.forEach(a -> {
			ContactDTO dto = mapper.map(a, ContactDTO.class);
			result.add(dto);
		});
		return result;
	}

	private boolean validateIsNotEmpty(String str) {
		return !str.isBlank() && !str.isEmpty() && str != null;
	}
}
