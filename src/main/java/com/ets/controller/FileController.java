package com.ets.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ets.services.FileService;
import com.google.gson.JsonObject;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileService fileService;

	@PostMapping("/uploadFile")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			JsonObject result =fileService.addFile(file);

			return ResponseEntity.status(HttpStatus.OK).body(result.toString());

		} else {
			return ResponseEntity.status(HttpStatus.OK).body("");
		}
	}

	@GetMapping("/getFileList")
	public ResponseEntity<String> getFileList() {
		return ResponseEntity.status(HttpStatus.OK).body(fileService.getFileList().toString());
	}
	
	@GetMapping("/getFile")
	public ResponseEntity<String> getFile(Long id) {	
		return ResponseEntity.status(HttpStatus.OK).body(fileService.getFile(id).toString());
	}
	
	@PostMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(Long id) {		
		return ResponseEntity.status(HttpStatus.OK).body(fileService.deleteFile(id).toString());
	}

}
