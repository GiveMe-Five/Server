package com.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.bean.NewsBean;
import com.service.NewsService;

public class GetNews extends HttpServlet {

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
		String idStr = request.getParameter("id");
		int id = Integer.parseInt(idStr);
		NewsBean nb = ns.get(id);
		JSONObject json = new JSONObject();
		try {
			json.put("id", nb.getId());
			json.put("title", nb.getTitle());
			json.put("content", nb.getContent());
			json.put("type", nb.getType());
			json.put("url", nb.getUrl());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(json.toString());
		out.flush();
		out.close();
	}

}
