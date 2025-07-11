<%--
    Document   : LandingPage
    Created on : Jun 24, 2025, 7:58:31 AM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title data-setting="app_name" data-rightJoin=" Clinic And Patient Management Dashboard">KiviCare  Clinic And Patient Management Dashboard</title>
  <meta name="description" content="KiviCare is a revolutionary Bootstrap Admin Dashboard Template and UI Components Library. The Admin Dashboard Template and UI Component features 8 modules.">
  <meta name="keywords" content="premium, admin, dashboard, template, bootstrap 5, clean ui, Kivicare, admin dashboard,responsive dashboard, optimized dashboard,">
  <meta name="author" content="Iqonic Design">
  <meta name="DC.title" content="KiviCare Clinic And Patient Management Dashboard">
  <!-- Config Options -->
  <meta name="setting_options" content='{&quot;saveLocal&quot;:&quot;sessionStorage&quot;,&quot;storeKey&quot;:&quot;kivicare&quot;,&quot;setting&quot;:{&quot;app_name&quot;:{&quot;value&quot;:&quot;KiviCare&quot;},&quot;theme_scheme_direction&quot;:{&quot;value&quot;:&quot;ltr&quot;},&quot;theme_scheme&quot;:{&quot;value&quot;:&quot;light&quot;},&quot;theme_style_appearance&quot;:{&quot;value&quot;:[&quot;theme-default&quot;]},&quot;theme_color&quot;:{&quot;value&quot;:&quot;default&quot;},&quot;theme_transition&quot;:{&quot;value&quot;:&quot;theme-with-animation&quot;},&quot;theme_font_size&quot;:{&quot;value&quot;:&quot;theme-fs-sm&quot;},&quot;page_layout&quot;:{&quot;value&quot;:&quot;container-fluid&quot;},&quot;header_navbar&quot;:{&quot;value&quot;:&quot;default&quot;},&quot;header_banner&quot;:{&quot;value&quot;:&quot;default&quot;},&quot;sidebar_color&quot;:{&quot;value&quot;:&quot;sidebar-white&quot;},&quot;card_color&quot;:{&quot;value&quot;:&quot;card-default&quot;},&quot;sidebar_type&quot;:{&quot;value&quot;:[]},&quot;sidebar_menu_style&quot;:{&quot;value&quot;:&quot;text-hover&quot;},&quot;footer&quot;:{&quot;value&quot;:&quot;default&quot;},&quot;body_font_family&quot;:{&quot;value&quot;:null},&quot;heading_font_family&quot;:{&quot;value&quot;:null}}}'>
  <!-- Google Font Api KEY-->
  <meta name="google_font_api" content="AIzaSyBG58yNdAjc20_8jAvLNSVi9E4Xhwjau_k">
  <!-- Favicon -->
  <link rel="shortcut icon" href="./assets/images/favicon.ico" />

  <!-- Library / Plugin Css Build -->
  <link rel="stylesheet" href="./assets/css/core/libs.min.css" />

  <!-- flaticon css -->
  <link rel="stylesheet" href="./assets/vendor/flaticon/css/flaticon.css" />

  <!-- font-awesome css -->
  <link rel="stylesheet" href="./assets/vendor/font-awesome/css/all.min.css" />

  <!-- Flatpickr css -->
  <link rel="stylesheet" href="./assets/vendor/flatpickr/dist/flatpickr.min.css" />

  <link rel="stylesheet" href="./assets/vendor/sheperd/dist/css/sheperd.css">

  <!-- Sweetlaert2 css -->
  <link rel="stylesheet" href="./assets/vendor/sweetalert2/dist/sweetalert2.min.css"/>

  <!-- Kivicare Design System Css -->
  <link rel="stylesheet" href="./assets/css/kivicare.min.css?v=1.4.1" />

  <!-- Custom Css -->
  <link rel="stylesheet" href="./assets/css/custom.min.css?v=1.4.1" />

  <!-- RTL Css -->
  <link rel="stylesheet" href="./assets/css/rtl.min.css?v=1.4.1"/>

  <!-- Customizer Css -->
  <link rel="stylesheet" href="./assets/css/customizer.min.css?v=1.4.1"/>

  <!--landing css-->
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="./assets/css/landing.css">

  <!-- Google Font -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@300;400;500;600;700&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;1,300;1,400;1,500&display=swap" rel="stylesheet">
</head>
<body>
<!-- Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-light bg-light shadow-sm">
  <a class="navbar-brand">
    <a href="LandingPage.jsp">
      <div class="logo-main">
        <img class="logo-normal img-fluid" src="./assets/images/logo.png" height="30" alt="logo">
      </div>
    </a>
  </a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
          aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" href="About.jsp">About</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="BookAppointment.jsp">Book Appointment</a>
      </li>
      <li class="nav-item active">
        <a class="nav-link" href="Help.jsp">Help</a>
      </li>
      <li class="nav-item active">
        <a class="nav-link" href="AdminDashboard.jsp">Admin dashboard</a>
      </li>
      <li class="nav-item active">
        <a class="nav-link" href="SysAdminDashboard.html">System Admin dashboard</a>
      </li>
    </ul>
  </div>
</nav>

<!-- Top Banner -->
<div class="top-banner d-flex text-white">
  <div class="banner-text d-flex align-items-center pl-4" style="width: 66.66%;">
    <strong>KiviCare – The key to better health</strong>
  </div>
  <div class="banner-image" style="width: 33.33%;">
    <img src="./assets/images/banner-doctor1.JPEG" alt="App Banner" class="banner-img">
  </div>
</div>

<div class="container landing-container">
  <div class="hero">
    <h1>Welcome to KiviCare</h1>
    <p>Your one-stop solution for all your medical needs.</p>
    <div class="mt-2">
      <a href="login.jsp" class="btn btn-primary btn-custom">Login</a>
      <a href="register.jsp" class="btn btn-outline-primary btn-custom ml-2">Register</a>
    </div>
  </div>
</div>

<!-- Specialist Section -->
<div class="container my-2">
  <h3 class="mb-4 font-weight-bold">Our consulting specialists</h3>
  <div class="row text-center">
    <!-- Card 1 -->
    <div class="col-md-3 mb-4">
      <div class="card specialist-card">
        <div class="card-body">
          <div class="specialist-icon mb-3">
            <i class="fas fa-virus fa-2x text-primary"></i>
          </div>
          <h5 class="card-title">Covid-19 Test</h5>
          <p class="card-text">Fast and accurate Covid-19 test, ensuring up to 97% accuracy.</p>
        </div>
      </div>
    </div>
    <!-- Card 2 (Highlighted) -->
    <div class="col-md-3 mb-4">
      <div class="card specialist-card">
        <div class="card-body">
          <div class="specialist-icon mb-3">
            <i class="fas fa-lungs fa-2x text-primary"></i>
          </div>
          <h5 class="card-title">Heart Lungs</h5>
          <p class="card-text">We pride ourselves as the leading experts in heart lungs health research.</p>
        </div>
      </div>
    </div>
    <!-- Card 3 -->
    <div class="col-md-3 mb-4">
      <div class="card specialist-card">
        <div class="card-body">
          <div class="specialist-icon mb-3">
            <i class="fas fa-capsules fa-2x text-primary"></i>
          </div>
          <h5 class="card-title">Suppliment</h5>
          <p class="card-text">Quality imported supplement enhances your strength and longevity.</p>
        </div>
      </div>
    </div>
    <!-- Card 4 -->
    <div class="col-md-3 mb-4">
      <div class="card specialist-card">
        <div class="card-body">
          <div class="specialist-icon mb-3">
            <i class="fas fa-brain fa-2x text-primary"></i>
          </div>
          <h5 class="card-title">Mental Health</h5>
          <p class="card-text">With KiviCare, we make sure your mental problems are heard.</p>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
