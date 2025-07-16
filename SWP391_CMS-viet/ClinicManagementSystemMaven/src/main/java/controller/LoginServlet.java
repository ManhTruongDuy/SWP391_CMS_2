package controller;

import dao.AccountPharmacistDAO;
import dao.AccountStaffDAO;
import dao.AccountPatientDAO;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.AccountPharmacist;
import model.AccountStaff;
import model.AccountPatient;

@WebServlet("/loginservlet")
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển hướng tới trang Login.jsp
        request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            String err = "Email and Password cannot be empty";
            request.setAttribute("err", err);
            request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
            return;
        }
        //check pharmacist
        AccountPharmacistDAO dao = new AccountPharmacistDAO();
        AccountPharmacist pharmacist = dao.getAccountByEmailAndPassword(email, password);

        if (pharmacist != null) {
            if(!pharmacist.getStatus().equalsIgnoreCase("Enable")){
            request.setAttribute("err", "Your account is locked.");
            request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
            return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("account", pharmacist);
            response.sendRedirect("home");
            return;
        }

        //check staff
        AccountStaffDAO staffDao = new AccountStaffDAO();
        AccountStaff staff = staffDao.getAccountByEmailAndPassword(email, password);

        if (staff != null) {
            if (!staff.getStatus().equalsIgnoreCase("Enable")) {
                request.setAttribute("err", "Your account is locked.");
                request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("account", staff);
            response.sendRedirect("home");
            return;
        }

        //check patient
        AccountPatientDAO patientDao = new AccountPatientDAO();
        AccountPatient patient = patientDao.getAccountByEmailAndPassword(email, password);

        if (patient != null) {
            if (!patient.getStatus().equalsIgnoreCase("Enable")) {
                request.setAttribute("err", "Your account is locked.");
                request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("account", patient);
            response.sendRedirect("home");
            return;
        }

        String err = "Email or Password is incorrect";
        request.setAttribute("err", err);
        request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
    }
}