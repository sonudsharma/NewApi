package com.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.UserDao;
import com.models.Cities;
import com.models.FileInfo;
import com.models.Hospital;
import com.models.Users;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

	@Autowired
	private UserDao userDao;

	@Transactional
	@Override
	public long save(Users user) {
		return userDao.save(user);
	}

	@Override
	public Users get(Integer id) {
		return userDao.get(id);
	}

	@Override
	public Hospital getHospital(Long id) {
		return userDao.getHospital(id);
	}

	/*
	 * @Override public List<Users> list() { return userDao.list(); }
	 * 
	 * @Transactional
	 * 
	 * @Override public void updateUser(Integer id, Users user) {
	 * userDao.updateUser(id, user); }
	 */

	public void updateToken(Integer id, String token) {
		userDao.updateToken(id, token);
	}

	@Transactional
	@Override
	public void delete(Integer UserId) {
		userDao.delete(UserId);
	}

	@Override
	public Boolean authenticate(String username, String password) {
		return userDao.authenticate(username, password);
	}

	@Override
	public Users fetchUser(String username, String password, String pin) {
		return userDao.fetchUser(username, password, pin);
	}

	@Override
	public Integer userRegistration(Users users) {
		return userDao.userRegistration(users);
	}

	@Override
	public Users fetchUserByEmail(String email) {
		return userDao.fetchUserByEmail(email);
	}

	@Override
	public Object updateProfile(Users user) {
		return userDao.updateProfile(user);
	}

	/*
	 * @Override public Map<String, Object> resetPassword(Map<String, String>
	 * userMap) { return userDao.resetPassword(userMap); }
	 */

	@Override
	public Map<String, String> resetPassword(Map<String, Object> userMap) {
		return userDao.resetPassword(userMap);
	}

	@Override
	public List<Map> getDoctorList(String string) {
		return userDao.getDoctorList(string);
	}

	@Override
	public Users getPerson(Integer id) {
		return userDao.getPerson(id);
	}

	@Override
	public Object getupload(FileInfo fileInfo) {

		return userDao.getupload(fileInfo);
	}

	@Override
	public List<Hospital> list() {
		return userDao.list();
	}

	@Override
	public List<Map> getSpecialist(String name) {
		return userDao.getSpecialist(name);
	}

	public void saveToken(Integer id, String token, Date expiration) {
		userDao.saveToken(id, token, expiration);
	}

	@Override
	public List<Map> getMedialist(String name) {
		return userDao.getMedialist(name);

	}

	@Override
	public List<Map> getMedia(String category) {
		return userDao.getMedia(category);
	}

	@Override
	public Cities getCity(Integer cityId) {
		return userDao.getCity(cityId);

	}

	@Override
	public List<Map> getStatelist(String name) {
		return userDao.getState(name);
	}

	@Override
	public List<Map> getAllCitylist(String name) {

		return userDao.getAllCity(name);
	}

	@Override
	public List<Map> getCityByStateId(Integer id) {

		return userDao.getCityByStateId(id);
	}

	@Override
	public List<Map> getStateByCountryId(Integer id) {
		return userDao.getStateByCountryId(id);
	}

	@Override
	public List<Map> getPerson(String name) {
		return userDao.getPerson(name);
	}
	/*
	 * @Override public Boolean encryptPassword() { return
	 * userDao.encryptPassword(); }
	 */

	@Override
	public List<Map> getDoctorListReport(String string) {
		return userDao.getDoctorListReport(string);
	}

	@Override
	public List<Map> getDoctorListReport(String userType, String fromDate, String toDate) {
		return userDao.getDoctorListReport(userType,fromDate,toDate);
	}

	@Override
	public Users fetchUser(String username, String password, String pin, String type) {
		// TODO Auto-generated method stub
		return userDao.fetchUser(username, password, pin, type);
	}

	

	

}