package org.microapp.Diary.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum ActivityType{
	//standard values
	Running(0), 
	Swimming(1),
	Jogging(2), 
	
	//not so standard values
	test(-1), 
	unknown(-2);
	
	private int id;
	
	private ActivityType(int id) {
		this.id = id;
	}
	
	public int getID() {
		return this.id;
	}
	
	public static ActivityType getByID(int id) {
		for(ActivityType at : ActivityType.values()) {
			if(at.getID() == id) {
				return at;
			}
		}
		
		return ActivityType.unknown;
	}
	
	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer,String>();
		
		for(ActivityType at : ActivityType.values()) {
			map.put(at.getID(), at.toString());
		}
		
		return map;
	}
}
