package com.example.androidtp3form;

import android.provider.BaseColumns;

public class TableData {

	public TableData(){
		
	}
public static abstract class TableInfo implements BaseColumns{
	public static final String USER_NAME="user_name";
	public static final String USER_LNAME="user_lname";
	public static final String USER_TEL="user_tel";
	public static final String USER_ID="user_id";
	public static final String USER_PASS="user_pass";
	public static final String DATABASE_NAME="user_info";
	public static final String TABLE_NAME="reg_info";
	
}
}
