package com.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import com.models.FileInfo;

public class Base64Image {

	public static void FileUpload() {

		MultipartFile inputFile = null;
		FileInfo fileInfo = new FileInfo();
		// String inputFile=null;
		if (!inputFile.isEmpty()) {
			try {
				String originalFilename = inputFile.getOriginalFilename();
				File destinationFile = new File("C:\\Auronia\\upload" + File.separator + originalFilename);
				inputFile.transferTo(destinationFile);
				fileInfo.setFileName(inputFile.getOriginalFilename());
				fileInfo.setPath(destinationFile.getPath());
				fileInfo.setSize(inputFile.getSize());
				fileInfo.setFileName(destinationFile.getPath());

			} catch (Exception e) {

			}
		} else {
		}

	}

	public static String encoder(String imagePath) {
		String base64Image = "";
		File file = new File("C:\\Auronia\\upload\\left-arrow.png");// imagePath
		try (FileInputStream imageInFile = new FileInputStream(file)) {
			// Reading a Image file from file system
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			base64Image = Base64.getEncoder().encodeToString(imageData);
		} catch (FileNotFoundException e) {
			// System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			// System.out.println("Exception while reading the Image " + ioe);
		}
		return base64Image;
	}

	public static void decoder(String base64Image, String pathFile) {
		try (FileOutputStream imageOutFile = new FileOutputStream(pathFile)) {
			// Converting a Base64 String into Image byte array
			byte[] imageByteArray = Base64.getDecoder().decode(base64Image);
			imageOutFile.write(imageByteArray);
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
	}
}
