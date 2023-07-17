package com.ets.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ets.repositories.FileRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class FileService {

	@Value("${upload.file.path}")
	private String uploadFilePath;
	
	@Autowired
	private FileRepository fileRepository;
	
    private final List<String> fileExtensions = Arrays.asList("png", "jpeg", "jpg", "docx", "pdf", "xlsx");


	public JsonObject addFile(MultipartFile file) {
		JsonObject result = new JsonObject();
		String filePath,fileName,extension;
		try {
			 fileName = file.getOriginalFilename();
			 filePath = uploadFilePath + fileName;

			File dest = new File(filePath);
			extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			if(!fileExtensions.contains(extension)) {
				result.addProperty("result", false);
				result.add("data",null);
				result.addProperty("exceptionCode", "this file extension is not allowed to be uploaded to the system");
				return result;
			}
	        
			file.transferTo(dest);
			
			
		} catch (IOException e) {
			e.printStackTrace();
			result.addProperty("result", false);
			result.addProperty("exceptionCode", e.getMessage());
			return result;
		}
		
		com.ets.modals.File fileRecord = new com.ets.modals.File(fileName,extension,filePath,file.getSize());
		fileRepository.save(fileRecord);
		Gson gson = new Gson();
		result.addProperty("result", true);
		result.addProperty("data",gson.toJson(fileRecord));
		result.add("exceptionCode", null);
		return result;
		
		
	}
	
	
	public JsonObject getFileList() {
		JsonObject result = new JsonObject();
		List<com.ets.modals.File> list =fileRepository.findAll();
		
		Gson gson = new Gson();

        JsonArray listArray = gson.toJsonTree(list).getAsJsonArray();
        result.addProperty("result", true);
		result.add("data",listArray);
		result.add("exceptionCode", null);
		return result;
		
        
		
	}
	
	public JsonObject getFile(Long fileId) {
		JsonObject result = new JsonObject();
		Optional<com.ets.modals.File> file =fileRepository.findById(fileId);
		Gson gson = new Gson();
		if (file.isPresent()) {
			com.ets.modals.File fileRecord =file.get();
			fileRecord.setFileByteArray(convertFileToByteArray(fileRecord.getFilePath()));
			result.addProperty("result", true);
    		result.addProperty("data",gson.toJson(fileRecord));
    		result.add("exceptionCode", null);
    		return result;
        } else {
        	result.addProperty("result", false);
    		result.add("data",null);
    		result.addProperty("exceptionCode", "File not found");
    		return result;
        }
		
	}
	
	public JsonObject deleteFile(Long fileId) {
		JsonObject result = new JsonObject();
		com.ets.modals.File file =fileRepository.findById(fileId).get();
		if(file!=null) {
			fileRepository.deleteById(fileId);
			File fileDeleted = new File(file.getFilePath());
			if(fileDeleted.exists()) {
				fileDeleted.delete();
			}
			result.addProperty("result", true);
    		result.add("data",null);
    		result.add("exceptionCode", null);
    		
    		return result;
			
		}
		else {
			result.addProperty("result", false);
    		result.add("data",null);
    		result.addProperty("exceptionCode", "File Not Found");
    		
    		return result;
		}
		
		
		
	}
	
	
	private byte[] convertFileToByteArray(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
