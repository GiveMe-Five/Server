package com.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bean.NewsBean;
import com.service.NewsService;

public class GetNewsList extends HttpServlet {

	NewsService ns = new NewsService();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		int start = 0;
		int limit = 10;
		if(startStr!=null){
			start = Integer.parseInt(startStr);
		}
		if(limitStr!=null){
			limit = Integer.parseInt(limitStr);
		}
		List<NewsBean> list = ns.getNewsList(start, limit);
		JSONArray array = new JSONArray();
		for(int i = 0; i < list.size(); i++){
			NewsBean nb = list.get(i);
			JSONObject json = new JSONObject();
			try {
				json.put("id", nb.getId());
				json.put("title", nb.getTitle());
				json.put("type", nb.getType());
				json.put("url", nb.getUrl());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			array.put(json);
		}
		out.print(array.toString());
		out.flush();
		out.close();
	}

}
