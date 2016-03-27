package com.example.sj;

import java.text.SimpleDateFormat;




import java.util.Calendar;
import java.util.Date;

import com.example.data.db.Step;
import com.example.changepage1.R;
import com.example.wo.Exercise;
import com.example.wo.Goals;
import com.example.wo.Historcal;
import com.example.wo.Setting;
import com.wch.connect.Login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

 
/**
 * @author shijun
 * 这是wo的碎片
 *
 */
public class FragmentWo extends Fragment implements OnClickListener{
	
    private View view;
    private LinearLayout exerciseLayout;
    private LinearLayout goalsLayout;
    private LinearLayout HistoryLayout;
    private LinearLayout settingLayout;
    private Button login_wo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.wo, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
		 
		
	}

	private void init() {
		exerciseLayout=(LinearLayout) view.findViewById(R.id.exercise);
		goalsLayout=(LinearLayout) view.findViewById(R.id.goals);
		HistoryLayout=(LinearLayout) view.findViewById(R.id.History);
		settingLayout=(LinearLayout) view.findViewById(R.id.setting);
		login_wo = (Button) view.findViewById(R.id.login_wo);
		
		exerciseLayout.setOnClickListener(this);
		goalsLayout.setOnClickListener(this);
		HistoryLayout.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
		login_wo.setOnClickListener(this);
	}

	 

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		    case R.id.History:
		    	 Intent intent = new Intent(getActivity(), Historcal.class);
		    	 startActivity(intent);
		    	 break;
		    case R.id.exercise:
		    	 Intent intent1 = new Intent(getActivity(), Exercise.class);
		    	 startActivity(intent1);
		    	 break;
		    case R.id.goals:
		    	 Intent intent2 = new Intent(getActivity(),Goals.class);
		    	 startActivity(intent2);
		    	 break;
		    case R.id.setting:
		    	 Intent intent3 = new Intent(getActivity(), Setting.class);
		    	 startActivity(intent3);
		    	 break;
		    case R.id.login_wo:
		    	Intent intent4 = new Intent(getActivity(), Login.class);
		    	startActivity(intent4);
		    	break;
		}
		
		
	}
    
	 
 
	 

}
