package com.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.models.Cities;
import com.models.FileInfo;
import com.models.Hospital;
import com.models.Users;

public interface UserService {

	long save(Users user);

	Users get(Integer id);

	Boolean authenticate(String username, String password);

	List<Hospital> list();

	// void updateUser(Integer id, Users user);

	void updateToken(Integer id, String token);

	void delete(Integer UserId);

	Users fetchUser(String username, String password, String pin);

	Integer userRegistration(Users users);

	Users fetchUserByEmail(String email);

	Object updateProfile(Users user);

	// Map<String, Object> resetPassword(Map<String, String> userMap);
	Map<String, String> resetPassword(Map<String, Object> userMap);

	List<Map> getDoctorList(String string);

	Hospital getHospital(Long id);

	Users getPerson(Integer id);

	Object getupload(FileInfo fileInfo);

	List<Map> getSpecialist(String name);

	void saveToken(Integer id, String token, Date expiration);

	List<Map> getMedialist(String name);

	List<Map> getMedia(String category);

	Cities getCity(Integer cityId);

	List<Map> getStatelist(String name);

	List<Map> getAllCitylist(String name);

	List<Map> getCityByStateId(Integer id);

	List<Map> getStateByCountryId(Integer id);
	
	public List<Map> getPerson(String name);

	List<Map> getDoctorListReport(String string);

	List<Map> getDoctorListReport(String userType, String fromDate, String toDate);

	

	// Boolean encryptPassword();

	Users fetchUser(String username, String password, String pin, String type);
}
