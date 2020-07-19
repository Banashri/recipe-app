package de.bm.recipe.dao;

public class Interval {
	String start;
	String end;
	int diff;
	
	
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Interval(String start, String end) {
		this.start = start;
		this.end = end;
	}
	
	public Interval(String start, int diff) {
		this.start = start;
		this.diff = diff;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}
	
	public String toString() {
		return "Start " + start + " end " + end + " diff " + diff;
	}
}