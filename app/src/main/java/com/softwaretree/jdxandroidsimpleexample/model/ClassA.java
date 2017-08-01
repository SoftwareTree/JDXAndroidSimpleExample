package com.softwaretree.jdxandroidsimpleexample.model;

import java.util.Date;

public class ClassA {
    private int aId;
    private String aString;
    private Date aDate;  
    private boolean aBoolean; 
    private float aFloat;
       
    /**
     * Default no-arg constructor needed for JDX
     */
    public ClassA() {
    }

    public ClassA(int aId, String aString, Date aDate, boolean aBoolean, float aFloat) {
        this.aId = aId;
        this.aString = aString;
        this.aDate = aDate;
        this.aBoolean = aBoolean;
        this.aFloat = aFloat;
    }

	public int getAId() {
		return aId;
	}

	public void setAId(int aId) {
		this.aId = aId;
	}

	public String getAString() {
		return aString;
	}

	public void setAString(String aString) {
		this.aString = aString;
	}

	public Date getADate() {
		return aDate;
	}

	public void setADate(Date aDate) {
		this.aDate = aDate;
	}

	public boolean getABoolean() {
		return aBoolean;
	}

	public void setABoolean(boolean aBoolean) {
		this.aBoolean = aBoolean;
	}

	public float getAFloat() {
		return aFloat;
	}

	public void setAFloat(float aFloat) {
		this.aFloat = aFloat;
	}
    
}
