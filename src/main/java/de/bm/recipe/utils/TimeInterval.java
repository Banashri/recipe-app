package de.bm.recipe.utils;

import java.util.Arrays;
import java.util.List;

import de.bm.recipe.dao.Interval;

public class TimeInterval {
	
	public static Interval convert(String interval) {
		String[] parts = interval.split("[\\s]");
		
		String startStr = parts[1];
		String endStr = parts[3];
		
		String startPeriod = startStr.substring(startStr.length() - 2, startStr.length());
		String endPeriod = endStr.substring(endStr.length() - 2, endStr.length());
		
		String startHour = startStr.substring(0, startStr.length() - 2);
		String endHour = endStr.substring(0, endStr.length() - 2);
		
		int diff = 0;
		int start = Integer.valueOf(startHour) % 12;
		int end = Integer.valueOf(endHour);
		
		if (startPeriod.equals(endPeriod)) {
			if (start < end) diff = end - start;
			else
				diff = 24 + end - start;
		} else 
			diff = 12 + end - start;
		return new Interval(startStr, endStr);
	}

	public static void main(String[] args) {

		List<String> intervals = Arrays.asList("Wednesday 11AM - 2PM", "Thursday 10PM - 1PM", "Thursday 1AM - 12AM");
		
		for (String interval : intervals) {
			String[] parts = interval.split("[\\s]");
			
			String startStr = parts[1];
			String endStr = parts[3];
			
			String startPeriod = startStr.substring(startStr.length() - 2, startStr.length());
			String endPeriod = endStr.substring(endStr.length() - 2, endStr.length());
			
			String startHour = startStr.substring(0, startStr.length() - 2);
			String endHour = endStr.substring(0, endStr.length() - 2);
			
			int diff = 0;
			int start = Integer.valueOf(startHour) % 12;
			int end = Integer.valueOf(endHour);
			
			if (startPeriod.equals(endPeriod)) {
				if (start < end) diff = end - start;
				else
					diff = 24 + end - start;
			} else 
				diff = 12 + end - start;
			
			System.out.println("Interval " + interval + " converted to -> " + startStr + ", " + diff);
		}
	}

}
