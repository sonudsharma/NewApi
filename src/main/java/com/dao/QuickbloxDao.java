package com.dao;

import java.util.List;
import java.util.Map;

import com.models.QBChatMessage;

public interface QuickbloxDao {
	String loginAndGetToken();
	
	String registerNewUser(String token, String email);
	
	String loginToChat(int externalUserId);
	
	String logoutFromChat(int externalUserId);
	
	List<QBChatMessage> getUnreadMessages(int fromId, int toId);
	
	List<Map> getQbConfigList(String name);
	
	List<Map> getDomainName() ;
	
	boolean recordMessages(List<QBChatMessage> lst);
}
