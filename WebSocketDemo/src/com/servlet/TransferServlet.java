package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;
import com.transfer.Transfer;

import javafx.scene.chart.PieChart.Data;

/**
 * Servlet implementation class TransferServlet
 */

public class TransferServlet extends HttpServlet {
	private static final long serialVersionUID = 369840050351775312L;

	public static String data = null;

//	public static String[] data = new String[4];
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 接收信息totalStep, totalCalorimetry, totalKilometer
		String step = request.getParameter("step");
		String calorimetry = request.getParameter("calorimetry");
		String kilometer = request.getParameter("kilometer");

		//
		data = step+","+calorimetry+","+kilometer;

		
		Transfer transfer = new Transfer();
		System.out.println(step+","+calorimetry+ ","+kilometer);
		//插入处理
		transfer.transferData(step, calorimetry, kilometer);
		
		 // 返回信息
//		 response.setCharacterEncoding("UTF-8");
//		 response.setContentType("text/html");
//		 PrintWriter out = response.getWriter();
//		 out.print("step：" + step);
//		 out.print("calorimetry：" + calorimetry);
//		 out.print("kilometer:" + kilometer);
//		 out.flush();
//		 out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
