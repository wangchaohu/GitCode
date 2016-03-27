package com.wch.connect;



import com.example.changepage1.R;
import com.wch.connect.WebService;
import com.wch.connect.WebServicePost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	// 登录按钮
	private Button logbtn;
	// 调试文本，注册文本
	private TextView infotv, regtv;
	// 显示用户名和密码
	EditText username, password;
	// 创建等待框
	private ProgressDialog dialog;
	// 返回的数据
	private String info;
	// 返回主线程更新数据
	private static Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// 获取控件
		username = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.pass);
		logbtn = (Button) findViewById(R.id.login);
		regtv = (TextView) findViewById(R.id.register);

		// 设置按钮监听器
		logbtn.setOnClickListener(this);
		regtv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			// 检测网络，无法检测wifi
//			if (!checkNetwork()) {
//				Toast toast = Toast.makeText(Login.this,网络未连接", Toast.LENGTH_SHORT);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();
//				break;
//			}
			// 提示框
			dialog = new ProgressDialog(this);
			dialog.setTitle("提示");
			dialog.setMessage("正在登录，请稍等...");
			dialog.setCancelable(false);
			dialog.show();
			// 创建子线程，分别进行get和Post传输数据
			new Thread(new MyThread()).start();
			break;
		case R.id.register:
			Intent regItn = new Intent(Login.this, Register.class);
			// overridePendingTransition(anim_enter);
			startActivity(regItn);
			break;
		};
	}

	// 子线程接收数据，主线程修改数据
	public class MyThread implements Runnable {
		@Override
		public void run() {
			info = WebService.executeHttpGet(username.getText().toString(), password.getText().toString());
			// info =
			// WebServicePost.executeHttpPost(username.getText().toString(),
			// password.getText().toString());
			handler.post(new Runnable() {
				@Override
				public void run() {
					//将返回的信息显示在界面上
					infotv.setText(info);
					dialog.dismiss();
					Toast toast = Toast.makeText(Login.this, info, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			});
		}
	}

	// 检测网络
	private boolean checkNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

}
