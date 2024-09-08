package com.demo.Phonebook.Controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.Phonebook.DTO.PhonebookDTO;
import com.demo.Phonebook.DTO.ResponseBody;
import com.demo.Phonebook.ExceptionHandler.BadRequestException;
import com.demo.Phonebook.Service.PhonebookService;

@RestController
@RequestMapping("api/v1/phonebook")
@CrossOrigin(origins = "*")
public class PhonebookController {

	@Autowired
	PhonebookService service;

	@GetMapping("")
	public ResponseEntity<List<PhonebookDTO>> getAllPhonebook() throws ParseException{
		return ResponseEntity.ok().body(service.getAllPhonebook());
	}
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseBody<PhonebookDTO>> getPhonebookById(@PathVariable Integer id) {
		return ResponseEntity.ok().body(service.findPhonebookById(id));
	}
	@GetMapping("/page")
	public ResponseEntity<List<PhonebookDTO>> getPagePhonebook(@RequestParam Integer page,@RequestParam Integer size,@RequestParam String search){
		return ResponseEntity.ok().body(service.getPagePhonebook(page, size, search));
	}

	@PostMapping("")
	public ResponseEntity<ResponseBody<PhonebookDTO>> createPhonebook(@RequestBody PhonebookDTO dto)
			throws ParseException, BadRequestException {
		return ResponseEntity.ok().body(service.createPhonebook(dto));
	}

	@PatchMapping("/id/{id}")
	public ResponseEntity<ResponseBody<PhonebookDTO>> deletePhonebook(@PathVariable Integer id){
		return ResponseEntity.ok().body(service.deletePhonebook(id));
	}
	@PutMapping("/id/{id}")
	public ResponseEntity<ResponseBody<PhonebookDTO>> updatePhonebook(@PathVariable Integer id,@RequestBody PhonebookDTO dto) throws ParseException, BadRequestException{
		return ResponseEntity.ok().body(service.updatePhonebook(id, dto));
	}
}
