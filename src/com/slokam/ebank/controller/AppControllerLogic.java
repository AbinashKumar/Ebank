package com.slokam.ebank.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.slokam.ebank.exception.EBankException;
import com.slokam.ebank.pojo.PersonSearchPojo;
import com.slokam.ebank.pojo.UserPojo;
import com.slokam.ebank.service.AdminService;
import com.slokam.ebank.service.LoginService;
import com.slokam.ebank.service.PersonService;
import com.slokam.ebank.service.UserService;

public class AppControllerLogic {

	public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	 UserService userService=(UserService)context.getBean("userSrv");
	 AdminService adminService=(AdminService)context.getBean("adminSrv");
	 String nextPage = "login.jsp";
 	 Integer Id = null;
 	 String strId = request.getParameter("id");
 	 if(strId!=null && !strId.trim().equals(" "));
 	   Id = Integer.parseInt(strId);
	  UserPojo pojo = new UserPojo();
	  pojo.setId(Id);
	  //UserService userService = null;
	  //AdminService adminService = null;
	  Collection<UserPojo> collection = null;
	  try{
		  //userService = new UserService();
		  userService.deleteUser(pojo);
		  //adminService = new AdminService();
		  collection = adminService.getUsers();
		  request.setAttribute("Users",collection);
		  nextPage = "admin_management.jsp";
	  }catch(EBankException e){
		  request.setAttribute("Error", e.getMessage());
	  }
	  request.getRequestDispatcher(nextPage).forward(request, response);
	}
	
	public void displayUserDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer id = Integer.parseInt(request.getParameter("id"));
		//UserService userService = null;
		ApplicationContext  context = new ClassPathXmlApplicationContext("application-context.xml");
		UserService userService = (UserService)context.getBean("userSrv");
		UserPojo pojo = null;
		String nextPage = "login.jsp";
		try {
			//userService = new UserService();
			pojo = userService.getUser(id);
			request.setAttribute("userData", pojo);
			nextPage = "register.jsp";
		}catch(EBankException e) {
			request.setAttribute("Error", e.getMessage());
		}
	   request.getRequestDispatcher(nextPage).forward(request, response);
	}
	public void displayUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//UserService service = new UserService();
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		UserService userService = (UserService)context.getBean("userSrv");
		String nextPage = "login.jsp";
		UserPojo userPojo = (UserPojo)request.getSession().getAttribute("user");
		UserPojo pojo = null;
		if(userPojo!=null){
		try{
			Integer uid = Integer.parseInt(request.getParameter("id"));
			pojo = userService.getUserInfo(uid);
			request.setAttribute("userData", pojo);
			nextPage = "user_management.jsp";
		}catch(EBankException e){
		  request.setAttribute("Error", e.getMessage());
		}
	  }
	   request.getRequestDispatcher(nextPage).forward(request, response);
	 }
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("uname").trim();
		String password = request.getParameter("pwd").trim();

		System.out.println("UserName: "+userName);
		System.out.println("Password: "+password);

		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		LoginService loginService=(LoginService)context.getBean("loginSrv");
		//service class to call the method..
           
		//LoginService service = new LoginService();
		 AdminService adminService=(AdminService)context.getBean("adminSrv");
		 
		 UserService userService=(UserService)context.getBean("userSrv");
		 
		String nextPage = "login.jsp";
		//String result = null;
		UserPojo userPojo = null;
		try {
			 userPojo = loginService.verifyLoginUser(userName,password);
			 request.getSession().setAttribute("user", userPojo);
			if(userPojo==null) {
				//nextPage="Home.jsp";
				//request.getRequestDispatcher(nextPage).forward(request,response);
				request.setAttribute("Error", "Inavlid userName and Password");
			} else if(userPojo.getRole().equals("Admin")) {
				//request.setAttribute("Error", "Inavlid userName and Password");
				//request.getRequestDispatcher(nextPage).forward(request, response);
				Collection<UserPojo> collection = null;
				//AdminService adminService = new AdminService();
				collection = adminService.getUsers();
				request.setAttribute("users", collection);
				nextPage="admin_management.jsp";

			}else if(userPojo.getRole().equals("User")){

				//UserService userService = new UserService();

				UserPojo userData = userService.getUserInfo(userPojo.getId());
				request.setAttribute("userData", userData);
				nextPage="user_management.jsp";
				// request.getRequestDispatcher(nextPage).forward(request, response);request.getRequestDispatcher(nextPage).forward(request, response);
			}

		}catch(EBankException e) {
			request.setAttribute("Error", e.getMessage());
		}
		request.getRequestDispatcher(nextPage).forward(request, response);
	}
	public void regisDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  
		UserPojo userPojo = new UserPojo();
		userPojo.setUserName("");
		userPojo.setPassword("");
		userPojo.setEmailId("");
		userPojo.setAge(0);
		userPojo.setPhone("");
		
		request.setAttribute("userData", userPojo);
		request.getRequestDispatcher("register.jsp").forward(request, response);
	}
	public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer id = null;
		String idStr = request.getParameter("userId");
		//System.out.println(idStr);
		if(idStr!=null && !idStr.trim().equals("") && !idStr.trim().equals("null")) {
			id=Integer.parseInt(idStr);
		}
		  
		String userName = request.getParameter("uname").trim();
		String password = request.getParameter("pwd").trim();
		String Email = request.getParameter("email").trim();
		String age = request.getParameter("age").trim();
		String phone = request.getParameter("phone").trim();
		//setting to UserPojo..
		UserPojo userPojo = new UserPojo();
		userPojo.setUserName(userName);
		userPojo.setPassword(password);
		userPojo.setEmailId(Email);
		userPojo.setAge(Integer.parseInt(age));
		userPojo.setPhone(phone);
		userPojo.setRole("User");
		userPojo.setStatus("Active");
		userPojo.setBalance(4500.12);
		userPojo.setId(id); 
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		LoginService loginService = (LoginService)context.getBean("loginSrv");
		LoginService loginSrv = (LoginService)context.getBean("loginSrv");
		//call to LoginService..
		try {
			//LoginService loginService = new LoginService();
			if(id == null)
			    loginService.regisUser(userPojo);
			else {
				 //LoginService loginSrv = new LoginService();
				 loginSrv.updateUser(userPojo);
			  }
		    }catch(EBankException e){
			request.setAttribute("Error", e.getMessage());
		}
		request.setAttribute("msg","Successfully Data Inserted by User.");
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}
	public void searchPerson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  String personName = request.getParameter("personName");
		  String passportNo = request.getParameter("passportNo");
		  String phoneNo = request.getParameter("phoneNo");
		  String courseName = request.getParameter("courseName");
		  //set the value the PersonPojo.
		  PersonSearchPojo searchPojo = new PersonSearchPojo();
		  searchPojo.setCourseName(courseName);
		  searchPojo.setPassport(passportNo);
	      searchPojo.setPersonName(personName);
	      searchPojo.setPhoneNo(phoneNo);
		  //call the service class method..
	      ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	      PersonService personService = (PersonService)context.getBean("personSrv");
	      List<Object[]> list = null;
		  try {
			 // PersonService personService = new PersonService();
			  list = personService.searchPerson(searchPojo);
			  request.setAttribute("result", list);
		   }catch(EBankException e) {
			e.printStackTrace();
			request.setAttribute("Error", e.getMessage());
			//request.getRequestDispatcher("person_management.jsp").forward(request, response);
		  }
		  request.getRequestDispatcher("person_management.jsp").forward(request, response);
		}
	public void searchUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Collection<UserPojo> collection = null;
		String nextPage = "login.jsp";
	    String userName = request.getParameter("userName");
	    //AdminService service = new AdminService();
	    ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	    AdminService adminService = (AdminService)context.getBean("adminSrv");
	    try{
	    	 collection = adminService.searchUsers(userName);
	    	 request.setAttribute("users", collection);
	    	 nextPage = "admin_management.jsp";
	    }catch(EBankException e) {
	       request.setAttribute("Error", e.getMessage());	    	
	    }
	   request.getRequestDispatcher(nextPage).forward(request, response);
	}
}
  
