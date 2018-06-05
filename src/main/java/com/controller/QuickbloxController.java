package com.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dto.DoctorsMsgCountBarChart;
import com.dto.QBResponse;
import com.google.gson.Gson;
import com.models.QBChatMessage;
import com.service.QuickbloxService;

@RestController
public class QuickbloxController {
	
	@Autowired
	private QuickbloxService quickbloxService;

	@RequestMapping(method=RequestMethod.POST, value = "/loginToChat/{id}")
	public String loginToChat(@PathVariable("id") Integer externalId) {
		quickbloxService.loginToChat(externalId);
		
		Gson gson = new Gson();
		
		QBResponse res = new QBResponse();
		res.setSuccess(1);
		res.setMessage("Quickblox Login Success");
		
		return gson.toJson(res);
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/logoutFromChat/{id}")
	public String logoutFromChat(@PathVariable("id") Integer externalId) {
		quickbloxService.logoutFromChat(externalId);
		
		Gson gson = new Gson();
		
		QBResponse res = new QBResponse();
		res.setSuccess(1);
		res.setMessage("Quickblox Logout Success");
		
		return gson.toJson(res);
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/recordMessages")
	public String recordMessages(@RequestBody List<QBChatMessage> lst) {
		quickbloxService.recordMessages(lst);
		
		Gson gson = new Gson();
		
		QBResponse res = new QBResponse();
		res.setSuccess(1);
		res.setMessage("Chat History inserted successfully");
		
		return gson.toJson(res);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/retrieveChatHistory/{from}/{to}")
	public String retrieveChatHistory(@PathVariable("from") Integer fromId, @PathVariable("to") Integer toId) {
		List<QBChatMessage> msgLst = quickbloxService.getUnreadMessages(fromId, toId);
		
		Gson gson = new Gson();
		return gson.toJson(msgLst);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/GetDoctorsMsgCountChartData")
	public String GetDoctorsMsgCountChartData() {
		//Map<String, Integer> chartData = quickbloxService.GetDoctorsMsgCountChartData();
		
		List<DoctorsMsgCountBarChart> chartData = new ArrayList<DoctorsMsgCountBarChart>();
		
		chartData.add(new DoctorsMsgCountBarChart("Hitanshi Shah", 120, 10));
		chartData.add(new DoctorsMsgCountBarChart("Bhavana Sharma", 89, 450));
		chartData.add(new DoctorsMsgCountBarChart("Vinita Bhagia", 45, 67));
		chartData.add(new DoctorsMsgCountBarChart("Anshul Warman", 12, 90));
		chartData.add(new DoctorsMsgCountBarChart("Ramesh Goyal", 40, 131));
		chartData.add(new DoctorsMsgCountBarChart("Jayesh Prajapati", 78, 23));
		chartData.add(new DoctorsMsgCountBarChart("Sharad Jain", 412, 450));
		chartData.add(new DoctorsMsgCountBarChart("Subir Ghosh", 321, 230));
		chartData.add(new DoctorsMsgCountBarChart("Shivani Patel", 340, 403));
		chartData.add(new DoctorsMsgCountBarChart("Ram Pandit", 403, 131));
		
		
		Gson gson = new Gson();
		return gson.toJson(chartData);
	}
}
