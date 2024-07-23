package com.ashokit.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ashokit.entity.FormData;
import com.ashokit.service.FormDataService;


@RestController
@RequestMapping("/api")
public class FormDataController {

	@Autowired
	private FormDataService formDataService;

	@CrossOrigin
	@PostMapping("/submit")
	public ResponseEntity<String> submitForm(@RequestBody FormData formData) {
		try {
			formDataService.saveFormData(formData);
			formDataService.sendExcelToEmail(formData.getEmail());
			return ResponseEntity.ok("Form data submitted and email sent.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
		}
	}

	@CrossOrigin
	@GetMapping("/getAllData")
	public List<FormData> getAllMessages() {
		return formDataService.getAllMessages();
	}
}
