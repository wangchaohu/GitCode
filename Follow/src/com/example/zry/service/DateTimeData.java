package com.example.zry.service;
/**
 * 存放实时数据表数据
 * @author zhangruyi 3.20
 * 
 *
 */
public class DateTimeData {
	/**
	 * 运动时间
	 */
	public String date;
	/**
	 * 总时间
	 */
	public int time;
	/**
	 * 总步数
	 */
	public int step;
	/**
	 * 总热量
	 */
	public float calorimetry;
	/**
	 * 总里数
	 */
	public float kilometer;
	
	public DateTimeData()
	{
		
	}
	/**
	 * 构造方法
	 * @param date 运动时间
	 * @param time 总时间
	 * @param totalStep 步数
	 * @param totalCalorimetry 总热量
	 * @param totalKilometer 总里数
	 */
	public DateTimeData(String date, int time, int step, float calorimetry,
			float kilometer) {
		this.date = date;
		this.time = time;
		this.step = step;
		this.calorimetry = calorimetry;
		this.kilometer = kilometer;
	}
	
}
