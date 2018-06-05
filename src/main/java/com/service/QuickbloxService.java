package com.service;

import java.util.List;
import java.util.Map;

import com.models.QBChatMessage;

public interface QuickbloxService {
	String loginAndGetToken();
	
	String registerNewUser(String token, String email);
	
	String loginToChat(int externalUserId);
	
	boolean recordMessages(List<QBChatMessage> lst);
	
	String logoutFromChat(int externalUserId);
	
	List<QBChatMessage> getUnreadMessages(int fromId, int toId);
	
	List<Map> getQbConfigList(String name);
	
	List<Map> getDomainName() ;
}
