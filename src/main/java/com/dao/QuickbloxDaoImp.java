package com.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dto.QBSession;
import com.dto.QBUser;
import com.dto.QuickbloxUser;
import com.dto.QuickbloxUserResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.models.DomainNames;
import com.models.DomainValues;
import com.models.QBChatMessage;
import com.models.Users;
import com.util.HttpsClientFactory;
import com.util.QuickbloxHelper;

@Repository
public class QuickbloxDaoImp implements QuickbloxDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public String loginAndGetToken() {
		// TODO Auto-generated method stub
		String url = "https://api.quickblox.com/session.json";
		
		HttpClient client = null;
		
		try {
			client = HttpsClientFactory.getHttpsClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("QuickBlox-REST-API-Version", "0.1.1");
		httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
		
		String authSecret = "qaNfTY7cTvYKNhh";
		
		Map<String, Object> data = new TreeMap<String, Object>();
		data.put("application_id", "69460");
		data.put("auth_key", "jmNhGTzCgtqnSJv");
		
		int timestamp = (int)Instant.now().getEpochSecond();
		data.put("timestamp",  timestamp + "");
		
		Random randomGenerator = new Random();
		Integer randomInt = randomGenerator.nextInt(10000000);
		data.put("nonce", Integer.toString(randomInt));
		
		String signature = QuickbloxHelper.generateSignature(data, authSecret);
		
		data.put("signature", signature);
		
		data.put("user", new QBUser("auronia", "auro@123"));
		
		Gson gson = new Gson();
		String dataJson = gson.toJson(data);
		
		System.out.println("Data JSON " + dataJson);
		
		StringEntity stringEntity = new StringEntity(dataJson, "UTF-8");
        
        stringEntity.setContentType("application/json;charset=UTF-8");
		
        httpPost.setEntity(stringEntity);
       
        Map<String, QBSession> sessionObj = null;
        String token = "";
        
        InputStream is = null;
        HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
	        
			is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
	                is, "UTF-8"), 8);
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	        is.close();
	        String json = sb.toString();
	        
	        System.out.println("Response " + json);
	        
	        sessionObj = (Map<String, QBSession>)gson.fromJson(json, 
	        		new TypeToken<Map<String, QBSession>>(){}.getType());
	        
	        token = sessionObj.get("session").getToken();
	        
	        System.out.println("Token " + token);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return token;
	}

	@Override
	public String registerNewUser(String token, String email) {
		// TODO Auto-generated method stub
		String userIdCreationUrl = "https://api.quickblox.com/users.json";
		
		HttpClient client = null;
		
		try {
			client = HttpsClientFactory.getHttpsClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpPost httpPost = new HttpPost(userIdCreationUrl);
		httpPost.addHeader("QuickBlox-REST-API-Version", "0.1.0");
		httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
		httpPost.addHeader("QB-Token", token.trim());
		
		Gson gson = new Gson();
		
		Users uObj = userDao.fetchUserByEmail(email);
		
		QuickbloxUser userObj = new QuickbloxUser();
		
		userObj.setEmail(uObj.getUserEmail());
		userObj.setPassword(uObj.getPassword());
		userObj.setPhone(uObj.getPerson().getPhone());
		userObj.setFull_name(uObj.getPerson().getFirstName() + " " + uObj.getPerson().getLastName());
		
		Map<String, QuickbloxUser> userMap = new HashMap<String, QuickbloxUser>();
		userMap.put("user", userObj);
		
		String userJson = gson.toJson(userMap);
		
		StringEntity stringEntity = new StringEntity(userJson, "UTF-8");
        stringEntity.setContentType("application/json;charset=UTF-8");
        httpPost.setEntity(stringEntity);
        
        Map<String, QuickbloxUserResponse> resObj = null;
        String id = "";
        
        InputStream is = null;
        HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
	        
			is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
	                is, "UTF-8"), 8);
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	        is.close();
	        String json = sb.toString();
	        
	        System.out.println("Response " + json);
	        
	        if(!json.contains("errors")) {
	        	resObj = (Map<String, QuickbloxUserResponse>)gson.fromJson(json, 
		        		new TypeToken<Map<String, QuickbloxUserResponse>>(){}.getType());
		        
		        id = Integer.toString(resObj.get("user").getId());
		        
		        System.out.println("ID " + id);
		        
		        //Save the id in user table
		        uObj.setExternalid(id);
		        userDao.save(uObj);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return id;
	}

	@Override
	public String loginToChat(int externalUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String logoutFromChat(int externalUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QBChatMessage> getUnreadMessages(int fromId, int toId) {
		// TODO Auto-generated method stub
		
		String query = "select " + 
				"        CONCAT(persons.PER_FIRST_NAME, ' ', persons.PER_LAST_NAME) as per_name, " + 
				"        chat_history.message " + 
				"    from " + 
				"        users, " + 
				"        persons, " + 
				"        chat_history " + 
				"    where " + 
				"        chat_history.read_status = 0 " + 
				"        and persons.per_id = users.USR_PER_ID " + 
				"	and chat_history.CHAT_TO = ? " + 
				"	and chat_history.CHAT_FROM = ? " + 
				"	and chat_history.CHAT_FROM = users.USR_EXTERNAL_ID " + 
				"    order by " + 
				"        chat_history.chat_timestamp desc";
		
		Session session = sessionFactory.openSession();
		
		Query q = session.createNativeQuery(query);
		q.setParameter(1, toId);
		q.setParameter(2, fromId);
		
		List<Object[]> lst = q.list();
		
		List<QBChatMessage> msgLst = new ArrayList<QBChatMessage>();
		for(Object[] o : lst) {
			
			QBChatMessage m = new QBChatMessage();
			
			m.setChatFromName(o[0].toString());
			m.setMessage(o[1].toString());
			
			msgLst.add(m);
		}
		
		System.out.print("Chat History Size " + lst.size());
		
		return msgLst;
	}
	
	public static void main(String[] args) {
		QuickbloxDaoImp imp = new QuickbloxDaoImp();
		String token = imp.loginAndGetToken();
		String id = imp.registerNewUser(token, "shirali.shah@raystechserv.com");
	}

	@Override
	public List<Map> getQbConfigList(String name) {
		// TODO Auto-generated method stub
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
					String QbConfiguration = (domainvalues.getValuetext() + " : " + domainvalues.getValuecode());
					Integer id = domainvalues.getId();
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					// map.put("id", id);
					// map.put("specialist", specialist);
					resultList.add(QbConfiguration);
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
	public List<Map> getDomainName() {
		// TODO Auto-generated method stub
		List<DomainNames> list = new ArrayList<DomainNames>();
		List resultList = new ArrayList<>();
		Session session = sessionFactory.openSession();
		try {
			String SQL_QUERY = "FROM DomainNames";
			Query query = session.createQuery(SQL_QUERY);
			list = query.list();
			if (!list.isEmpty()) {
				for (DomainNames domainName : list) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("id", domainName.getId());
					map.put("Name", domainName.getName());
					resultList.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public boolean recordMessages(List<QBChatMessage> lst) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		for(QBChatMessage msg : lst) {
			msg.setChatTimeStamp(new Date());
			session.save(msg);
		}
		
		return true;
	}
	
	

}
