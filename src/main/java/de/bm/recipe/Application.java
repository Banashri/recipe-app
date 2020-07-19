package de.bm.recipe;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.JsonReader;

import de.bm.recipe.dao.Interval;
import de.bm.recipe.utils.TimeInterval;
import okio.Okio;

public class Application {

	private Map<String, Map<String, List<Interval>>> recipeMap;
	
	private void setup() throws IOException {
		
		JsonReader reader = JsonReader.of(Okio.buffer(Okio.source(Paths.get(getClass().getClassLoader().getResource("recipe.json").getFile()))));
		reader.beginArray();
		
		Map<String, Map<String, List<Interval>>> map = new HashMap<>();
		
		while (reader.hasNext()) {
			reader.beginObject();
			
			String recipe = null, delivery = null, postcode = null;
			
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("recipe"))
					recipe = reader.nextString();
				else if (name.equals("delivery")) 
					delivery = reader.nextString();
				else if (name.equals("postcode")) 
					postcode = reader.nextString();
			}
			
			if (!map.containsKey(recipe))
				map.put(recipe, new HashMap<String, List<Interval>>());
			
			if (!map.get(recipe).containsKey(postcode))
				map.get(recipe).put(postcode, new ArrayList<Interval>());
			
			map.get(recipe).get(postcode).add(TimeInterval.convert(delivery));
			
			reader.endObject();
		}
		reader.endArray();
		
		recipeMap = map;
	}
	
	private void deliveryCountAt(String postcode, String timeRange) {
		List<String> eligibleRecipes = new ArrayList<>();
		
		int count = 0;
		for (String recipe : recipeMap.keySet()) {
			Map<String, List<Interval>> deliveryMap = recipeMap.get(recipe);
			if (!deliveryMap.containsKey(postcode)) continue;
			
			count += containsDeliveryInTimeRange(recipe, deliveryMap.get(postcode),  timeRange, eligibleRecipes);
		}
		System.out.println(eligibleRecipes + " and count " + count);
	}
	
	private int busiestPostCode() {
		
		int max = Integer.MIN_VALUE;
		String postcode = null;
		
		for (String recipe : recipeMap.keySet()) {
			for (String post : recipeMap.get(recipe).keySet()) {
				if (postcode == null || max < recipeMap.get(recipe).size()) {
					postcode = post;
					max = recipeMap.get(recipe).size();
				}
			}
		}
		System.out.println("Busiest postcode is " + postcode + " and count " + max);
		return max;
	}
	
	public List<String> matchedByName(List<String> names) {
		List<String> result = new ArrayList<>();
		for (String recipe : recipeMap.keySet()) {
			for (String name : names)
				if (recipe.contains(name))
					result.add(recipe);
		}
		System.out.println("Matched names " + names + " are : " + result);
		return result;
	}
	
	
	private int containsDeliveryInTimeRange(String recipe, List<Interval> intervals, String timeRange, List<String> eligibleRecipes) {
		
		Interval time = TimeInterval.convert(timeRange);
		int count = 0;
		
		boolean isFound = false;
		for (Interval i : intervals) {
			if (isStartAfter(time, i) && isEndBefore(time, i)) {
				isFound = true;
				count++;
			}
		}
		if (isFound) eligibleRecipes.add(recipe);
		return count;
	}
	
	private boolean isStartAfter(Interval time, Interval in) {
		
		String timePeriod = time.getStart().substring(time.getStart().length() - 2);
		int timeDigit = Integer.parseInt(time.getStart().substring(0, time.getStart().length() - 2)) % 12;
		
		String inPeriod = in.getStart().substring(in.getStart().length() - 2);
		int inDigit = Integer.parseInt(in.getStart().substring(0, in.getStart().length() - 2)) % 12;
		
		if (timePeriod.equals(inPeriod)) {
			return timeDigit <= inDigit;
		} else if (timePeriod.compareTo(inPeriod) < 0) return true;
		return false;
	}

	private boolean isEndBefore(Interval time, Interval in) {
		
		String timePeriod = time.getEnd().substring(time.getEnd().length() - 2);
		int timeDigit = Integer.parseInt(time.getEnd().substring(0, time.getEnd().length() - 2)) % 12;
		
		String inPeriod = in.getEnd().substring(in.getEnd().length() - 2);
		int inDigit = Integer.parseInt(in.getEnd().substring(0, in.getEnd().length() - 2)) % 12;
		
		if (timePeriod.equals(inPeriod)) {
			return timeDigit >= inDigit;
		} else if (timePeriod.compareTo(inPeriod) > 0) return true;
		return false;
	}

	private void getUniqueRecipeCount() {
		System.out.println("Total recipe " + recipeMap.size());
	}
	
	private void getRecipeCounts() {
		for (String recipe : recipeMap.keySet())
			System.out.println("Recipe " + recipe + " exists for " + recipeMap.get(recipe).size() + " times");
	}
	
	public static void main(String[] args) throws Exception {
		Application app = new Application();
		app.setup();
		app.getUniqueRecipeCount();
		app.getRecipeCounts();
		app.deliveryCountAt("10120", "Wednesday 11AM - 3PM");
		app.busiestPostCode();
		app.matchedByName(Arrays.asList("Potato", "Veggie", "Mushroom"));
	}
}



