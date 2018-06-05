package com.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.QuickbloxDao;
import com.models.QBChatMessage;

@Service
@Transactional
public class QuickbloxServiceImp implements QuickbloxService {
	
	@Autowired
	private QuickbloxDao quickbloxDao;

	@Override
	public String loginAndGetToken() {
		// TODO Auto-generated method stub
		return quickbloxDao.loginAndGetToken();
	}

	@Override
	public String registerNewUser(String token, String email) {
		// TODO Auto-generated method stub
		return quickbloxDao.registerNewUser(token, email);
	}

	@Override
	public String logoutFromChat(int externalUserId) {
		// TODO Auto-generated method stub
		return quickbloxDao.logoutFromChat(externalUserId);
	}

	@Override
	public List<QBChatMessage> getUnreadMessages(int fromId, int toId) {
		// TODO Auto-generated method stub
		return quickbloxDao.getUnreadMessages(fromId, toId);
	}

	@Override
	public List<Map> getQbConfigList(String name) {
		// TODO Auto-generated method stub
		return quickbloxDao.getQbConfigList(name);
	}

	@Override
	public List<Map> getDomainName() {
		// TODO Auto-generated method stub
		return quickbloxDao.getDomainName();
	}

	@Override
	public String loginToChat(int externalUserId) {
		// TODO Auto-generated method stub
		return quickbloxDao.loginToChat(externalUserId);
	}

	@Override
	public boolean recordMessages(List<QBChatMessage> lst) {
		// TODO Auto-generated method stub
		return quickbloxDao.recordMessages(lst);
	}
	
	
	

}
