package com.gepardec.controller;

import java.io.IOException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@PersistenceUnit
	private EntityManagerFactory emf;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String id = request.getParameter("id");
			Query query = emf.createEntityManager()
					.createQuery("select c.photo " + "from Cheetah c " + "where c.id = :id");
			query.setParameter("id", Long.parseLong(id));
			
			byte[] photo = (byte[]) query.getSingleResult();
			response.reset();
			response.getOutputStream().write(photo);
			
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
	}
}
