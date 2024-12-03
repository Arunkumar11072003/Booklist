package com.example.servlet;

import com.example.dao.BookDAO;
import com.example.model.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.*;
import java.util.List;

public class BookServlet extends HttpServlet {
    private BookDAO bookDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        bookDAO = new BookDAO(); // Initialize the BookDAO
    }

    // GET /books
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            Book book = bookDAO.getBookById(id);
            request.setAttribute("book", book);
            request.getRequestDispatcher("/bookDetails.jsp").forward(request, response);
        } else {
            List<Book> books = bookDAO.getAllBooks();
            request.setAttribute("books", books);
            request.getRequestDispatcher("/bookList.jsp").forward(request, response);
        }
    }

    // POST /books
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        int year = Integer.parseInt(request.getParameter("year"));
        Book book = new Book(title, author, year);

        if (bookDAO.addBook(book)) {
            response.sendRedirect("books");
        } else {
            response.getWriter().write("Error adding book.");
        }
    }

    // PUT /books
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        int year = Integer.parseInt(request.getParameter("year"));

        Book book = new Book(title, author, year);
        if (bookDAO.updateBook(id, book)) {
            response.sendRedirect("books");
        } else {
            response.getWriter().write("Error updating book.");
        }
    }

    // DELETE /books
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (bookDAO.deleteBook(id)) {
            response.sendRedirect("books");
        } else {
            response.getWriter().write("Error deleting book.");
        }
    }
}

