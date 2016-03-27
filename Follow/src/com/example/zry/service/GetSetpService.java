package com.example.zry.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.example.data.db.PedometerDB;
import com.example.sj.FragmentPedometer;
import com.example.zry.pedometer.StepDetector;
import com.wch.connect.WebService;
import com.wch.connect.WebServicePost;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GetSetpService extends Service {
	private SensorManager sensorManager;
	private StepDetector stepDetector;
	// 用于更新步数的接口
	private OnprogressListener progress;
	static int total_step = 0;
	static int currentStep=0;
	static long currentTime=0;
	static long newTime=0;
	static int totalTime=1;
	static int time=1;
	private static Thread myThread;
//	Context context;
	PedometerDB database;
	LocalBinder local = new LocalBinder();
	/**
	 * 注册接口的方法，供外部调用
	 * 
	 * @param progress
	 */
	public void setOnprogressListener(OnprogressListener progress) {
		this.progress = progress;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return local;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d("GetStepService", "步数检测已启动");
				database=PedometerDB.getInstance(FragmentPedometer.activity);
				DateTimeData data=database.queryNow(new DateTimeData());
				total_step=data.step;
				totalTime=data.time;
				startStepDetector();
				Timer timer = new Timer(true);
				TimerTask task = new TimerTask() {
					public void run() {
						currentTime=System.currentTimeMillis() ;
						if(currentTime-newTime<5000&&currentTime-newTime>0){
							totalTime++;
							time++;
							Log.d("GetStepService", "----------运动中--------------");
						}else if(currentTime-newTime>5000){
							if (progress != null&&time>=5) {
								progress.onprogress(total_step,totalTime-=5,0);
								DateTimeData currentData=new DateTimeData(getCurrentTime(),time,currentStep,total_step/2.0f,calculateCalories());
								database.insertNow(currentData);
//								DateTimeData data=database.queryNow(new DateTimeData());
								currentStep=0;
								time=1;
								//开启线程，向服务器传数据
								myThread = new MyThread();
								myThread.start();
							}
						}
					}
				};
				timer.schedule(task, 0, 1000);
			}

		}).start();
		
	}
	
	@Override
	public  void onDestroy(){
		Log.d("GetStepService", "----------运动中--------------"+total_step);
//		DateTimeData data=database.queryNow(new DateTimeData());
//		total_step=data.step;
//		DateTimeData currentData=new DateTimeData(getCurrentTime(),data.time,data.step,data.step/2.0f,calculateCalories());//getCurrentTime(),time,total_step,total_step/2.0f,calculateCalories());
//		database.insertNow(currentData);
		super.onDestroy();
	}
	/**
	 * 获取传感器数据
	 */
	private void startStepDetector() {
		Log.d("GetStepService", "获取传感器实例");
		stepDetector = new StepDetector(this);
		// 获取传感器管理器的实例
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		// 获得传感器的类型，这里获得的类型是加速度传感器
		// 此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_UI);
		stepDetector.setOnSensorChangeListener(new StepDetector.OnSensorChangeListener() {
			@Override
			public void onChange() {
				newTime=System.currentTimeMillis() ;
				total_step++;
				currentStep++;
				Log.d("GetStepService", "service" + total_step);
				// 当数据变化时通知activity
				if (progress != null) {
					progress.onprogress(total_step,totalTime,getVelocity());
					newTime=System.currentTimeMillis() ;
				}

			}
		});

	}
	/**
	 * 计算当前运动生成的热量
	 * 
	 * @return
	 */
	public float calculateCalories() {
		return 70 * total_step * 80 / 10000.0f;
	}
	public double getVelocity() {
		double velocity = Double.parseDouble(String.format("%.2f", currentStep/(time*2.0f)));
		return velocity;
	}
	public String getCurrentTime()
	{
		Date now=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(now);
		Log.d("GetStepService", time+"时间");
		return time;
	}
	
	public class LocalBinder extends Binder {

		private int step = 0;
		public int getStep() {
			step = total_step;
			return step;
		}
		public GetSetpService getservice() {
			return GetSetpService.this;
		}
	}
	
	public class MyThread extends Thread
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			WebService.executeHttpGet(String.valueOf(total_step), String.valueOf(calculateCalories()), String.valueOf(total_step/2.0f));
		}
	}
}
