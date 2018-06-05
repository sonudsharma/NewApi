package com.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.ui.ModelMap;

import com.models.Cities;
import com.models.DomainValues;
import com.models.FileInfo;
import com.models.Hospital;
import com.models.Mediatype;
import com.models.State;
import com.models.TokenHistory;
import com.models.Users;

@Repository
public class UserDaoImp implements UserDao {
	private static final Logger LOGGER = Logger.getLogger(UserDaoImp.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public long save(Users user) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.saveOrUpdate(user);
			transaction.commit();
		} catch (Exception ex) {
			transaction.rollback();
			throw ex;
		} finally {
			session.close();
		}

		return user.getId();
	}

	public void updateToken(Integer id, String token) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Users user = session.byId(Users.class).load(id);
			user.setToken(token);
			transaction.commit();

		} catch (Exception ex) {
			transaction.rollback();
			throw ex;
		} finally {
			session.close();
		}
	}

	/*
	 * @Override public void updateUser(Integer id, Users user) { Session
	 * session = sessionFactory.getCurrentSession(); Users user2 =
	 * session.byId(Users.class).load(id); user2.setToken(user.getToken()); //
	 * user2.setPassword(user.getPassword()); //
	 * user2.setUsername(user.getUsername()); //
	 * sessionFactory.getCurrentSession().saveOrUpdate(user); session.flush(); }
	 */

	@Override
	public Users get(Integer id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		Users user = null;
		try {
			transaction = session.beginTransaction();
			user = session.get(Users.class, id);
			// user = session.byId(Users.class).load(id);
			transaction.commit();
			return user;

		} catch (Exception e) {

		}
		return user;

		// return sessionFactory.getCurrentSession().get(Users.class, id);
	}

	/*
	 * @Override public List<Users> list() { Session session =
	 * sessionFactory.getCurrentSession(); CriteriaBuilder cb =
	 * session.getCriteriaBuilder(); CriteriaQuery<Users> cq =
	 * cb.createQuery(Users.class); Root<Users> root = cq.from(Users.class);
	 * cq.select(root); Query<Users> query = session.createQuery(cq); return
	 * query.getResultList(); }
	 */

	@Override
	public void delete(Integer UserId) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Users user = (Users) session.get(Users.class, UserId);
			user.setRecordStatusFlg("Inactive");
			session.update(user);
			transaction.commit();
		} catch (Exception ex) {
			transaction.rollback();
		} finally {
			session.close();
		}

	}

	@Override
	public Boolean authenticate(String username, String password) {
		Session session = sessionFactory.openSession();
		boolean userFound = false;

		String SQL_QUERY = "from Users where username = ? and  password = ?";
		Query query = session.createQuery(SQL_QUERY);
		query.setParameter(0, username);
		query.setParameter(1, password);
		List list = query.list();
		if ((list != null) && (list.size() > 0)) {
			userFound = true;
		}

		session.close();
		return userFound;

	}

	@Override
	public Users fetchUser(String username, String password, String pin) {
		Session session = sessionFactory.openSession();
		Users userFound = null;
		// Query using Hibernate Query Language
		String SQL_QUERY = "from Users as o where o.recordStatusFlg ='Active' and o.username=? and ((o.password=? or o.pin=?))";
		Query query = session.createQuery(SQL_QUERY);
		query.setParameter(0, username);
		query.setParameter(1, password);
		query.setParameter(2, pin);
		List list = query.list();

		if ((list != null) && (list.size() > 0)) {
			userFound = (Users) list.get(0);
		}
		session.close();
		return userFound;
	}

	@Override
	public Integer userRegistration(Users user) {

		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(user);
			transaction.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			transaction.rollback();
			throw ex;
		} finally {
			session.close();
		}

		return user.getId();

	}

	@Override
	public Users fetchUserByEmail(String email) {
		Session session = sessionFactory.openSession();
		Users userFound = null;
		// Query using Hibernate Query Language
		String SQL_QUERY = " from Users as o where o.userEmail=?";
		Query query = session.createQuery(SQL_QUERY);
		query.setParameter(0, email);
		List list = query.list();

		if ((list != null) && (list.size() > 0)) {
			userFound = (Users) list.get(0);
		}

		session.close();
		return userFound;
	}

	@Override
	public Object updateProfile(Users user) {
		ModelMap model = new ModelMap();
		Session session = sessionFactory.openSession();

		Transaction transaction = null;

		try {

			Users userExist = session.byId(Users.class).load(user.getId());
			if (userExist == null) {
				model.put("msg", "user does not exist");
				return model;
			}
			session.clear();
			transaction = session.beginTransaction();
			session.update(user);
			transaction.commit();

		} catch (Exception ex) {

			transaction.rollback();
			ex.printStackTrace();

		} finally {
			session.close();
		}
		model.put("Success", "1");
		model.put("msg", "Update successfully");
		return model;

	}

	@Override
	public Map<String, String> resetPassword(Map<String, Object> userMap) {
		Map<String, String> model = new HashMap<String, String>();
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {

			boolean userFound = false;
			transaction = session.beginTransaction();
			String SQL_QUERY = "update Users set password=? where username = ?";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, userMap.get("password").toString());
			query.setParameter(1, userMap.get("username").toString());
			int iResult = query.executeUpdate();
			if (iResult < 0) {
				model.put("successCode", "0");
				model.put("msg", "Invalid Email Id");
				return model;
			}
			transaction.commit();
			model.put("successCode", "1");
			model.put("msg", "Password reset successfully");

		} catch (Exception ex) {

			transaction.rollback();
			ex.printStackTrace();
			model.put("successCode", "0");
			model.put("msg", "Invalid Email Id");

		} finally {
			session.close();
		}
		return model;

	}

	@Override
	public List<Map> getDoctorList(String userType) {
		List<Users> list = new ArrayList<Users>();
		List<Map> resultList = new ArrayList<Map>();
		Session session = sessionFactory.openSession();
		String isOnline = null;
		try {

			String SQL_QUERY = "from Users as o where o.recordStatusFlg ='Active' and o.type=? ORDER BY o.isOnline Desc";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, userType);
			list = query.list();

			if (!list.isEmpty()) {
				for (Users user : list) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					if(user.getPerson().getHospital()==null) {
						map.put("HospitalName", "");
						map.put("HospitalAddress", "");
					}
					map.put("HospitalName", user.getPerson().getHospital().getName());
					map.put("HospitalAddress", user.getPerson().getHospital().getAddress());
					map.put("title", user.getPerson().getFullName());
					map.put("user_id", user.getId());
					if (userType.equalsIgnoreCase("Doctor")) {
						map.put("Speciality", user.getPerson().getSpeciality());
					}
					map.put("PhotoUrl", user.getPerson().getPhotourl());
					map.put("QuickBloxId", user.getExternalid());
					map.put("isOnline", user.getIsOnline());
					resultList.add(map);
				}
			}
			session.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return resultList;
	}

	@Override
	public Hospital getHospital(Long id) {
		Session session = sessionFactory.openSession();
		Hospital hospital = null;
		try {
			hospital = session.get(Hospital.class, id);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return hospital;
	}

	@Override
	public Users getPerson(Integer id) {

		Session session = sessionFactory.openSession();
		Users user = null;
		try {
			user = session.get(Users.class, id);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return user;

	}

	@Override
	public Object getupload(FileInfo fileInfo) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			session.clear();
			transaction = session.beginTransaction();
			session.save(fileInfo);
			transaction.commit();
		} catch (Exception ex) {
			transaction.rollback();
			throw ex;
		} finally {
			session.close();
		}

		return fileInfo;
	}

	@Override
	public List<Hospital> list() {
		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Hospital> cq = cb.createQuery(Hospital.class);
		Root<Hospital> root = cq.from(Hospital.class);
		cq.select(root);
		Query<Hospital> query = session.createQuery(cq);
		return query.getResultList();
	}

	@Override
	public List<Map> getSpecialist(String name) {
		List<DomainValues> list = new ArrayList<DomainValues>();
		List resultList = new ArrayList();
		Session session = sessionFactory.openSession();
		try {
			String SQL_QUERY = "SELECT b FROM DomainNames a, DomainValues b WHERE b.domainname = a.id AND a.name= ?";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, name);
			list = query.list();

			if (!list.isEmpty()) {
				for (DomainValues domainvalues : list) {
					String specialist = domainvalues.getValuetext();
					Integer id = domainvalues.getId();
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					// map.put("id", id);
					// map.put("specialist", specialist);
					resultList.add(specialist);
				}
			}

			session.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}

		return resultList;
	}

	@Override
	public void saveToken(Integer id, String token, Date expiration) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		TokenHistory tokenhistory = new TokenHistory();
		try {
			transaction = session.beginTransaction();
			Users user = session.byId(Users.class).load(id);
			tokenhistory.setToken(token);
			tokenhistory.setUser(user);
			tokenhistory.setCreateUserId(user.getId());
			tokenhistory.setExpireDate(expiration);
			// user.setToken(token);
			session.save(tokenhistory);
			transaction.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			transaction.rollback();
			throw ex;
		} finally {
			session.close();
		}

	}

	@Override
	public List<Map> getMedialist(String name) {
		List<Mediatype> list = new ArrayList<Mediatype>();
		List resultList = new ArrayList();
		Session session = sessionFactory.openSession();
		try {
			String SQL_QUERY = "SELECT b FROM DomainNames a, Mediatype b WHERE b.domainname = a.id AND a.name= ?";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, name);
			list = query.list();

			if (!list.isEmpty()) {
				for (Mediatype mediatype : list) {
					// Integer id = mediatype.getId();
					// String Title = mediatype.getTitle();
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("id", mediatype.getId());
					map.put("Title", mediatype.getTitle());
					map.put("Type", mediatype.getType());
					map.put("URl", mediatype.getUrl());
					map.put("Category", mediatype.getCategory());
					map.put("Description", mediatype.getDescription());
					map.put("Upload_Date", mediatype.getUploadDate());

					resultList.add(map);
				}
			}
			session.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}

		return resultList;
	}

	@Override
	public List<Map> getMedia(String category) {
		List<Mediatype> list = new ArrayList<Mediatype>();
		List resultList = new ArrayList();
		Session session = sessionFactory.openSession();
		// Mediatype media = null;
		try {
			String SQL_QUERY = "FROM Mediatype m WHERE m.category=?";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, category);
			list = query.list();

			if (!list.isEmpty()) {
				for (Mediatype mediatype : list) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("id", mediatype.getId());
					map.put("Title", mediatype.getTitle());
					map.put("Type", mediatype.getType());
					map.put("URl", mediatype.getUrl());
					map.put("Category", mediatype.getCategory());
					map.put("Description", mediatype.getDescription());
					map.put("Upload_Date", mediatype.getUploadDate());
					resultList.add(map);

				}
			}
			// media = session.get(Mediatype.class, category);
			// media = session.getClass();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return resultList;
	}
	/*
	 * @Override public Boolean encryptPassword() { List<Users> list = new
	 * ArrayList<Users>(); Session session = sessionFactory.openSession();
	 * Transaction transaction = null; try { PasswordService ps =
	 * PasswordService.getInstance(); String SQL_QUERY = " from Users"; Query
	 * query = session.createQuery(SQL_QUERY); list = query.list(); transaction
	 * = session.beginTransaction(); if (!list.isEmpty()) { for (Users user :
	 * list) { Users userUpdate = (Users) session.get(Users.class,
	 * user.getId());
	 * userUpdate.setPassword(ps.encrypt(userUpdate.getPassword(), 1));
	 * session.update(userUpdate); // transaction.commit(); }
	 * 
	 * } } catch (Exception ex) { ex.printStackTrace(); transaction.rollback();
	 * } finally { session.close(); }
	 * 
	 * return null; }
	 */

	@Override
	public Cities getCity(Integer cityId) {
		Session session = sessionFactory.openSession();
		Cities cities = null;
		try {
			String SQL_QUERY = "FROM Cities c WHERE c.id=?";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, cityId);
			cities = (Cities) query.uniqueResult();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return cities;
	}

	public List<Map> getState(String name) {
		List<State> list = new ArrayList<State>();
		List resultList = new ArrayList();
		Session session = sessionFactory.openSession();
		try {
			String SQL_QUERY = "SELECT b FROM DomainNames a, State b WHERE b.domainname = a.id AND a.name= ? ORDER BY b.name";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, name);
			list = query.list();

			if (!list.isEmpty()) {
				for (State state : list) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					// String Statename = state.getName();
					map.put("id", state.getId());
					map.put("name", state.getName());
					resultList.add(map);
				}
			}
			session.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}

		return resultList;
	}

	@Override
	public List<Map> getAllCity(String name) {
		List<Cities> list = new ArrayList<Cities>();
		List resultList = new ArrayList();
		Session session = sessionFactory.openSession();
		try {
			String SQL_QUERY = "SELECT b FROM DomainNames a, Cities b WHERE b.domainname = a.id AND a.name= ? ORDER BY b.name";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, name);
			list = query.list();

			if (!list.isEmpty()) {
				for (Cities city : list) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("id", city.getId());
					map.put("name", city.getName());
					resultList.add(map);
				}
			}
			session.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}

		return resultList;
	}

	@Override
	public List<Map> getCityByStateId(Integer id) {
		List<Cities> list = new ArrayList<Cities>();
		List resultList = new ArrayList();
		Session session = sessionFactory.openSession();
		try {
			String SQL_QUERY = "SELECT b FROM State a, Cities b WHERE b.state=a.id and a.id = ? order by b.name";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, id);
			list = query.list();

			if (!list.isEmpty()) {
				for (Cities city : list) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("id", city.getId());
					map.put("name", city.getName());
					resultList.add(map);
				}
			}
			session.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}

		return resultList;
	}

	@Override
	public List<Map> getStateByCountryId(Integer id) {
		List<State> list = new ArrayList<State>();
		List resultList = new ArrayList();
		Session session = sessionFactory.openSession();
		try {
			String SQL_QUERY = "SELECT b FROM Countries a, State b WHERE b.country=a.id and a.id = ? order by b.name";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, id);
			list = query.list();

			if (!list.isEmpty()) {
				for (State state : list) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("id", state.getId());
					map.put("name", state.getName());
					resultList.add(map);
				}
			}
			session.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}

		return resultList;
	}
	
	@Override
	public List<Map> getPerson(String name) {
		List<Map> resultList = new ArrayList<Map>();
		List<Users> list = new ArrayList<Users>();
		Session session = sessionFactory.openSession();
		try {
			name = "%" + name.trim().toLowerCase() + "%";
			String SQL_QUERY = "SELECT u FROM Users u WHERE  "
					+ "LOWER(u.person.firstName) LIKE :name OR LOWER(u.person.lastName) "
					+ "LIKE :name OR LOWER(concat(rtrim(u.person.firstName) ,' ',rtrim(u.person.lastName))) LIKE :name";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter("name", name);
			list = query.list();
			if (!list.isEmpty()) {
				for (Users users : list) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("HospitalName", users.getPerson().getHospital().getName());
					map.put("HospitalAddress", users.getPerson().getHospital().getAddress());
					map.put("title", users.getPerson().getFullName());
					map.put("user_id", users.getId());
					map.put("PhotoUrl", users.getPerson().getPhotourl());
					map.put("QuickBloxId", users.getExternalid());
					map.put("isOnline", users.getIsOnline());
					resultList.add(map);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return resultList;
	}

	@Override
	public List<Map> getDoctorListReport(String userType) {
		// TODO Auto-generated method stub
		List<Users> list = new ArrayList<Users>();
		List<Map> resultList = new ArrayList<Map>();
		Session session = sessionFactory.openSession();
		try {

			String SQL_QUERY = " from Users as o where o.recordStatusFlg ='Active' and o.type=?";
			Query query = session.createQuery(SQL_QUERY);
			query.setParameter(0, userType);
			list = query.list();
			if (!list.isEmpty()) {
				for (Users user : list) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("FirstName", user.getPerson().getFirstName());
					map.put("LastName", user.getPerson().getLastName());
					map.put("phone", user.getPerson().getPhone());
					map.put("Address", user.getPerson().getAddress().getFulladdress());
					map.put("Gender", user.getPerson().getGender());
					map.put("Dob", user.getPerson().getDob());
					map.put("Weight", user.getPerson().getWeight());
					map.put("Height", user.getPerson().getHeight());
					
					resultList.add(map);
				}
			}
			session.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return resultList;
	}
	
	@Override
	public List<Map> getDoctorListReport(String userType, String fromDate1, String toDate1) {
		List<Users> list = new ArrayList<Users>();
		List<Map> resultList = new ArrayList<Map>();
		Session session = sessionFactory.openSession();
		// Date fromDate = new Date();
		// String fromDate1 = "2018-05-25";
		SimpleDateFormat dateformat2 = new SimpleDateFormat("yyyy-MM-dd");
		// String fromDate=null;
		// String toDate = null;
		try {
			LOGGER.info(fromDate1);
			LOGGER.info(toDate1);
			Date fromDate = null;
			Date toDate = null;

			if (fromDate1 != null) {
				fromDate = dateformat2.parse(fromDate1);
			}
			if (toDate1 != null) {
				toDate = dateformat2.parse(toDate1);
			}

			StringBuilder sb = new StringBuilder("SELECT u FROM Users u WHERE u.recordStatusFlg='Active' AND u.type=?");
			if (fromDate != null && toDate != null) {
				sb.append("AND  DATE_FORMAT(u.recordTimeStamp, '%Y-%m-%d' ) >= ? "
						+ "AND DATE_FORMAT(u.recordTimeStamp, '%Y-%m-%d' ) <= ?");
			}
			if (fromDate != null && toDate == null) {
				sb.append("AND  DATE_FORMAT(u.recordTimeStamp, '%Y-%m-%d' ) >= ?");
			}
			if (fromDate == null && toDate != null) {
				sb.append("AND DATE_FORMAT(u.recordTimeStamp, '%Y-%m-%d' ) <= ?");
			}
			String SQL_QUERY = sb.toString();
			Query query = session.createQuery(SQL_QUERY);

			LOGGER.info("In getDoctorListReport " + query);

			query.setParameter(0, userType);

			if (fromDate != null && toDate != null) {
				query.setParameter(1, dateformat2.format(fromDate));
				query.setParameter(2, dateformat2.format(toDate));
			} else if (fromDate != null && toDate == null) {
				query.setParameter(1, dateformat2.format(fromDate));
			} else if (fromDate == null && toDate != null) {
				query.setParameter(1, dateformat2.format(toDate));
			}
			list = query.list();
			if (!list.isEmpty()) {

				for (Users user : list) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("id", user.getId());
					map.put("FirstName", user.getPerson().getFirstName());
					map.put("LastName", user.getPerson().getLastName());
					map.put("phone", user.getPerson().getPhone());
					if (user.getPerson().getAddress() == null) {
						map.put("Address", "");
					} else {
						map.put("Address", user.getPerson().getAddress().getFulladdress());
					}
					map.put("Gender", user.getPerson().getGender());
					map.put("Dob", user.getPerson().getDob());
					map.put("Weight", user.getPerson().getWeight());
					map.put("Height", user.getPerson().getHeight());
					map.put("Date", user.getRecordTimeStamp());

					resultList.add(map);
				}

			}
			session.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return resultList;
	}

	@Override
	public Users fetchUser(String username, String password, String pin, String type) {
		Session session = sessionFactory.openSession();
		Users userFound = null;
		// Query using Hibernate Query Language
		String SQL_QUERY = "from Users as o where o.recordStatusFlg ='Active' and o.type =? and o.username=? and ((o.password=? or o.pin=?))";
		Query query = session.createQuery(SQL_QUERY);
		query.setParameter(0, type);
		query.setParameter(1, username);
		query.setParameter(2, password);
		query.setParameter(3, pin);
		List list = query.list();

		if ((list != null) && (list.size() > 0)) {
			userFound = (Users) list.get(0);
		}
		session.close();
		return userFound;
	}
	
	
	
}


