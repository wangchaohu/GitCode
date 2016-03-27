package com.transfer;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.db.DBManager;

public class Transfer {

	private static String date = null;
	private static Date day;
	private static SimpleDateFormat df;
	private static String datetime = getDate();
	private static int tStep = 0;
	private static float tCalorimetry = 0;
	private static float tKilometer = 0;
	private static boolean flog = true;
	
	public Transfer(){
		
	}
	// 获取当前日期
	public static String getDate() {
//		try {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		date = df.format(new Date());
			return date.toString();
//		} 
//		finally {
//			day = null;
//			df = null;
//		}
	}

	//插入数据
	public void transferData(String step, String calorimetry, String kilometer) {
	    tStep = Integer.parseInt(step);
		tCalorimetry = Float.parseFloat(calorimetry);
		tKilometer = Float.parseFloat(kilometer);
				

		String insterData = "insert into day_table(todate, totalStep, totalCalorimetry, totalKilometer) values("+
		                      datetime + "," + tStep+ "," + tCalorimetry+ "," + tKilometer+");";
		String updateData = "update day_table set totalStep="+ tStep+",totalcalorimetry=" + tCalorimetry + ",totalKilometer=" 
		                    + tKilometer + "where todate=" + datetime 	+";";
		// 获取DB对象
		DBManager sql = DBManager.createInstance();
		sql.connectDB();

		System.out.println(flog);
		if(flog == true){
		sql.executeUpdate(insterData);
		flog = false;
		System.out.println(flog);
		}
		else{
		sql.executeUpdate(updateData);
		System.out.println(flog);}
		
//		if(ret != 0)
//		{
//			sql.closeDB();
//		}
		sql.closeDB();
	}
	
	//查询数据
	public void selectData()
	{
		//SQL查询语句
		String selectdata = "select * from where todate='" + datetime + "';";
		
		//获取DB对象
		DBManager sql = DBManager.createInstance();
		sql.connectDB();
		
		sql.executeQuery(selectdata);
		
		
	}
	
}
