package com.example.sj;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.changepage1.R;
import com.example.data.db.PedometerDB;
import com.example.jun.CircleBar;
import com.example.zry.service.DateTimeData;
import com.example.zry.service.GetSetpService;
import com.example.zry.service.OnprogressListener;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 益动
 * 
 * @author: zhangruyi
 * 
 */
public class FragmentPedometer extends Fragment {
	private View view;
	private TextView distance;
	private TextView calories;
	private TextView velocity;
	private TextView timeText;
	private CircleBar circleBar;
	public int total_step = 0;
	private int step_length = 80;
	private int weight = 70;
	private GetSetpService.LocalBinder binder;
	private GetSetpService getservice;
	public static Activity activity=null;
	PedometerDB database;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pedometer, container, false);
		distance = (TextView) view.findViewById(R.id.distance);
		calories = (TextView) view.findViewById(R.id.calories);
		velocity = (TextView) view.findViewById(R.id.velocity);
		timeText = (TextView) view.findViewById(R.id.time);
		activity=getActivity();
		init();
		creatService();
		return view;
	}

	// 通过handler里修改UI
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			int step = bundle.getInt("step");
			total_step=step;
			Long time = bundle.getLong("time");
			double velocitys = bundle.getDouble("velocity");
			Log.d("FragementPedometer", "热量---------------------" + velocitys);
			Log.d("FragementPedometer", "步数---------------------" + step);
			distance.setText(step / 2.0 + "");
			velocity.setText(velocitys + "");
			calories.setText(calculateCalories() + "");
			timeText.setText(time + "s");
			circleBar.setText(step);
			circleBar.setProgress(step, 1);
			circleBar.startCustomAnimation();
		}
	};
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (GetSetpService.LocalBinder) service;
			getservice = binder.getservice();
			// 注册回调方法
			getservice.setOnprogressListener(new OnprogressListener() {
				@Override
				public void onprogress(int step, long time, double velocity) {
					Message message = new Message();
					Bundle bundle = new Bundle();
					bundle.putInt("step", step);
					bundle.putLong("time", time);
					bundle.putDouble("velocity", velocity);
					message.setData(bundle);
					handler.sendMessage(message);
					Log.d("FragementPedometer", "******************************" + step);
				}
			});
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};
	
	/**
	 * 初始化
	 */
	private void init() {
		database=PedometerDB.getInstance(getActivity());
		DateTimeData data=database.queryNow(new DateTimeData());
		total_step=data.step;
		int time=data.time;
		Log.d("FragementPedometer", "************数据库步数************" + total_step);
		circleBar = (CircleBar) view.findViewById(R.id.circle);
		distance.setText(total_step / 2 + "");
		calories.setText(calculateCalories() + "");
		velocity.setText("0");
		timeText.setText(time+"s");
		circleBar.setText(total_step);
		circleBar.setProgress(total_step, 1);
		circleBar.startCustomAnimation();
	}

	/**
	 * 创建获取步数服务
	 */
	private void creatService() {
		// 启动获取步数服务
		Intent intent = new Intent(getActivity(), GetSetpService.class);
		getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 计算当前运动生成的热量
	 * 
	 * @return
	 */
	public float calculateCalories() {
		return weight * total_step * step_length / 10000.0f;
	}
//	private DateTimeData getData(){
//		DateTimeData data=new DateTimeData(getCurrentTime(),Integer.parseInt(timeText.getText()+""),
//				total_step,Float.parseFloat(calories.getText()+""),Float.parseFloat(distance.getText()+""));
//		return data;
//	}
//	public String getCurrentTime()
//	{
//		Date now=new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String time = sdf.format(now);
//		Log.d("GetStepService", time+"时间");
//		return time;
//	}
	@Override
	public void onDestroy() {
//		DateTimeData data=getData();
//		database.insertNow(data);
		Log.d("FragementPedometer", "************当前步数************" + total_step);
		getActivity().unbindService(connection);
		super.onDestroy();
	}

}
