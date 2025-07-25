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
            request.setAttribute("err", "Email and Password cannot be empty");
            request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();

        // Check Pharmacist
        AccountPharmacistDAO pharmacistDao = new AccountPharmacistDAO();
        AccountPharmacist pharmacist = pharmacistDao.getAccountByEmailAndPassword(email, password);
        if (pharmacist != null) {
            if (!"Enable".equalsIgnoreCase(pharmacist.getStatus())) {
                request.setAttribute("err", "Your account is locked.");
                request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
                return;
            }

            session.setAttribute("account", pharmacist);
            session.setAttribute("userType", "pharmacist");
            session.setAttribute("role", "Pharmacist");
            response.sendRedirect(request.getContextPath() + "/view/pharmacist/dashboard-pharmacist.html"); // Gợi ý: nên có trang riêng
            System.out.println("Redirecting to: " + request.getContextPath() + "/view/pharmacist/dashboard-pharmacist.html");
            return;
        }

        // Check Staff
        AccountStaffDAO staffDao = new AccountStaffDAO();
        AccountStaff staff = staffDao.getAccountByEmailAndPassword(email, password);
        if (staff != null) {
            if (!"Enable".equalsIgnoreCase(staff.getStatus())) {

                request.setAttribute("err", "Your account is locked.");
                request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
                return;
            }


            session.setAttribute("account", staff);
            session.setAttribute("userType", "staff");
            session.setAttribute("role", staff.getRole());

            switch (staff.getRole()) {
                case "Doctor":
                    response.sendRedirect(request.getContextPath() + "/view/doctor/home.jsp");
                    break;
                case "AdminSys":
                    response.sendRedirect(request.getContextPath() + "/view/SysAdminDashboard.html");
                    break;
                case "AdminBusiness":
                    response.sendRedirect(request.getContextPath() + "/view/AdminDashboard.html");
                    break;
                case"Warehouse Manager":
                    response.sendRedirect(request.getContextPath() + "/view/warehouse/WarehouseHome.html");
                    break;
                default:
                    request.setAttribute("err", "Invalid staff role.");
                    request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
                    break;
            }
            return;
        }

        // Check Patient
        AccountPatientDAO patientDao = new AccountPatientDAO();
        AccountPatient patient = patientDao.getAccountByEmailAndPassword(email, password);
        if (patient != null) {
            if (!"Enable".equalsIgnoreCase(patient.getStatus())) {

                request.setAttribute("err", "Your account is locked.");
                request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
                return;
            }


            session.setAttribute("account", patient);
            session.setAttribute("userType", "patient");
            session.setAttribute("role", "Patient");

            response.sendRedirect("home");
            return;
        }

        // If all fail
        request.setAttribute("err", "Email or Password is incorrect");
        request.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(request, response);
    }

}