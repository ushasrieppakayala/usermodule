package com.capg.hcms.usermanagementsystem.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import com.capg.hcms.usermanagementsystem.exceptions.ContactNumberAlreadyExistException;
import com.capg.hcms.usermanagementsystem.exceptions.EmailAlreadyExistException;
import com.capg.hcms.usermanagementsystem.exceptions.PassKeyMisMatchException;
import com.capg.hcms.usermanagementsystem.exceptions.UserEmailInvalidException;
import com.capg.hcms.usermanagementsystem.exceptions.UserNameAlreadyExistException;
import com.capg.hcms.usermanagementsystem.exceptions.UserNameInvalidException;
import com.capg.hcms.usermanagementsystem.exceptions.UserNotFoundException;
import com.capg.hcms.usermanagementsystem.exceptions.UserNumberInvalidException;
import com.capg.hcms.usermanagementsystem.exceptions.UserPasswordInvalidException;
import com.capg.hcms.usermanagementsystem.model.Appointment;
import com.capg.hcms.usermanagementsystem.model.DiagnosticCenter;
import com.capg.hcms.usermanagementsystem.model.TestManagement;
import com.capg.hcms.usermanagementsystem.model.User;
import com.capg.hcms.usermanagementsystem.repo.UserRepo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/***************************************************************************************************
-Author                   :  Ushasri Eppakayala
-Created/Modified Date    :  13-08-2020   
-Description              :  UserServiceImpl Class implements services for User Management System
***************************************************************************************************/

@Service
public class UserServiceImpl implements IUserService{
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private Random random;
	@Autowired
	private RestTemplate restTemplate;
	

	/****************************************************************************************
	-Function Name            :     registerUser
	-Input Parameters         :     User Object
	-Return Type              :     User object
	-Throws                   :     UserNameInvalidException,UserPasswordInvalidException,
	                                UserEmailInvalidException, UserNumberInvalidException,
	                                UserNameAlreadyExistException,EmailAlreadyExistException
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     13-08-2020
	-Description              :     adding user to the user_data database table 
	*****************************************************************************************/
	
	@Override
	public User registerUser(User user) throws UserNameInvalidException, 
	  UserPasswordInvalidException,UserEmailInvalidException, UserNumberInvalidException
	  ,UserNameAlreadyExistException,EmailAlreadyExistException 
	{
		
		Pattern p1=Pattern.compile("[A-Z]{1}[a-zA-Z0-9]{6,14}$");
		Matcher m1=p1.matcher(user.getUserName());
		Pattern p2=Pattern.compile("^(?=.*[0-9])"+ "(?=.*[a-z])(?=.*[A-Z])"+ "(?=.*[@#$%^&+=])"+ "(?=\\S+$).{8,20}$");
		Matcher m2=p2.matcher(user.getUserPassword());
		Pattern p3=Pattern.compile("^(.+)@(.+)$");
		Matcher m3=p3.matcher(user.getUserEmail());
		Pattern p4=Pattern.compile("\\d{10}");
		Matcher m4=p4.matcher(user.getContactNumber().toString());
		if(!(m1.find() &&  m1.group().equals(user.getUserName())))
		{
			throw new UserNameInvalidException("Username should start with capital letter ad size should be 7-14  characters");
			
		}
		else if(!( m2.find() &&  m2.group().equals(user.getUserPassword())) )
		{
   			throw new UserPasswordInvalidException("User password must contain "
   					+ "capital letter,small letters and special character "
   					+ "without starting with number and range should be between 8 and 20");
		}
		else if(!( m3.find() &&  m3.group().equals(user.getUserEmail())) )
		{
   			throw new UserEmailInvalidException("user email is not valid");
		}
		else if(!( m4.find() &&  m4.group().equals(user.getContactNumber().toString())) )
		{
			throw new UserNumberInvalidException("contact number should contain 10 digits and starting may be 7,8 or 9");
		}
		else if(userRepo.getUserByUserName(user.getUserName())!=null)
			throw new UserNameAlreadyExistException("User with Name "+user.getUserName()+" already exist");
		
		else if(userRepo.getUserByContactNumber(user.getContactNumber())!=null)
			throw new ContactNumberAlreadyExistException("User with ContactNumber "+user.getContactNumber()+" already exist");
		
		else if(userRepo.getUserByUserEmail(user.getUserEmail())!=null)
			throw new EmailAlreadyExistException("User with Email "+user.getUserEmail()+" already exist");
		 else
			 user.setUserRole("customer");
			 user.setUserId(String.valueOf(random.nextInt(100000)).substring(0, 5));
		     userRepo.save(user);
		return user;
			
	}


	/****************************************************************************************
	-Function Name            :     deleteUser
	-Input Parameters         :     userId Object
	-Return Type              :     boolean value
	-Throws                   :     UserNotFoundException
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     13-08-2020
	-Description              :     deleting user from the user_data database table 
	*****************************************************************************************/
	
	@Override
	public boolean deleteUser(String userId) 
	{
		User user = userRepo.getOne(userId);
		if(user==null)
		{
			throw new UserNotFoundException("User Doesnot exist");
		}
		userRepo.deleteById(userId);
		return true;
	}


	/****************************************************************************************
	-Function Name            :     updateUser
	-Input Parameters         :     User Object
	-Return Type              :     User object
	-Throws                   :     UserNotFoundException
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     13-08-2020
	-Description              :     updating user to the user_data database table 
	*****************************************************************************************/
	
	@Override
	public User updateUser(User user) {
		User existingUser=userRepo.getOne(user.getUserId());
		if(existingUser==null)
		{
			throw new UserNotFoundException("User Doesnot exist");
		}
		existingUser.setUserName(user.getUserName());
		existingUser.setUserPassword(user.getUserPassword());
		existingUser.setUserEmail(user.getUserEmail());
		existingUser.setContactNumber(user.getContactNumber());
		return userRepo.save(existingUser);
	}


	/****************************************************************************************
	-Function Name            :     registerUser
	-Input Parameters         :     UserId Object
	-Return Type              :     User object
	-Throws                   :     UserNotFoundException
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     13-08-2020
	-Description              :     getting user from the user_data database table 
	*****************************************************************************************/
	
	@Override
	public User getUserById(String userId)
	{
		if(!userRepo.existsById(userId))
			throw new UserNotFoundException("User with id "+userId+" Not Found");
		return userRepo.getOne(userId);
	
	}


	/****************************************************************************************
	-Function Name            :     getAllUsers
	-Input Parameters         :     no parameters
	-Return Type              :     User list object
	-Throws                   :     UserNotFoundException
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     13-08-2020
	-Description              :     getting all the users from the user_data database table 
	*****************************************************************************************/
	
	@Override
	public List<User> getAllUsers() 
	{
		if(userRepo.findAll().isEmpty())
		{
			throw new UserNotFoundException("Users unavailable");
		}
		else {
			List<User> userList=userRepo.findAll();
			return userList;
		}
		
	}


	/****************************************************************************************
	-Function Name            :     deleteAllUsers
	-Input Parameters         :     no parameters
	-Return Type              :     boolean value
	-Throws                   :     UserNotFoundException
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     13-08-2020
	-Description              :     deletig all the users from the user_data database table 
	*****************************************************************************************/
	
	@Override
	public boolean deleteAllUsers() 
	{
		if(userRepo.findAll().isEmpty())
		{
			throw new UserNotFoundException("Users unaivailable");
		}
		 userRepo.deleteAll();
		 return true;
	}


	/****************************************************************************************
	-Function Name            :     addCenter
	-Input Parameters         :     DiagnosticCenter Object
	-Return Type              :     DiagnosticCenter object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     17-08-2020
	-Description              :     adding center 
	*****************************************************************************************/
	

	@Override
	public DiagnosticCenter addCenter(DiagnosticCenter center) {

		ResponseEntity<List<TestManagement>> testManage=restTemplate.exchange("http://hcms-diagnostic-test-management-system/test/add-default", HttpMethod.GET,null,new ParameterizedTypeReference<List<TestManagement>>() {
		});
		
		List<TestManagement> listTest=testManage.getBody();
		 System.out.println(listTest);
		List<String> lists=new ArrayList();
		lists.add(listTest.get(0).getTestId());
		lists.add(listTest.get(1).getTestId());
		center.setTests(lists);
		DiagnosticCenter centerPosted=restTemplate.postForObject("http://hcms-diagnostic-center-management-system/center/addcenter", center, DiagnosticCenter.class);	
		return centerPosted;
		

	}


	/****************************************************************************************
	-Function Name            :     getAllCenters
	-Input Parameters         :     no parameters
	-Return Type              :     centerList object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     17-08-2020
	-Description              :     getting all the centers 
	*****************************************************************************************/
	
	@Override
	public List<DiagnosticCenter> getAllCenters() {
	
		ResponseEntity<List<DiagnosticCenter>> centerEntity=restTemplate.exchange("http://hcms-diagnostic-center-management-system/center/getallcenters", HttpMethod.GET,null,new ParameterizedTypeReference<List<DiagnosticCenter>>() {
		});
		List<DiagnosticCenter> centerList=centerEntity.getBody();
		return centerList;
	}

	/****************************************************************************************
	-Function Name            :     deleteAllCenters
	-Input Parameters         :     no parameters
	-Return Type              :     boolean value
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     17-08-2020
	-Description              :     deleting all the centers 
	*****************************************************************************************/
	
	@Override
	public boolean deleteAllCenters() {
		
		restTemplate.delete("http://hcms-diagnostic-center-management-system/center/removeAll");
		return true;
	}

	/****************************************************************************************
	-Function Name            :     getCenterById
	-Input Parameters         :     centerId object
	-Return Type              :     DiagnosticCenter object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     17-08-2020
	-Description              :     getting center by centerId 
	*****************************************************************************************/
	
	@Override
	public DiagnosticCenter getCenterById(String centerId) {
		
		DiagnosticCenter center=restTemplate.getForObject("http://hcms-diagnostic-center-management-system/center/getcenter/center-Id/"+centerId,DiagnosticCenter.class);
		return center;
	}

	/****************************************************************************************
	-Function Name            :     deleteCenterById
	-Input Parameters         :     centerId object
	-Return Type              :     boolean value
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     17-08-2020
	-Description              :     deleting center by centerId
	*****************************************************************************************/
	
	@Override
	public boolean deleteCenterById(String centerId) {
		
		restTemplate.delete("http://hcms-diagnostic-center-management-system/center/removecenter/centerId/"+centerId);
		return true;
	}

	/****************************************************************************************
	-Function Name            :     getAllTests
	-Input Parameters         :     no parameters
	-Return Type              :     testList object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     18-08-2020
	-Description              :     getting all the tests 
	*****************************************************************************************/
	
	@Override
	public List<TestManagement> getAllTests() {
		
		ResponseEntity<List<TestManagement>> testEntity=restTemplate.exchange("http://hcms-diagnostic-test-management-system/test/getAll", HttpMethod.GET,null,new ParameterizedTypeReference<List<TestManagement>>() {
		});
		List<TestManagement> testList=testEntity.getBody();
		return testList;
	}
	
	/****************************************************************************************
	-Function Name            :     addTest
	-Input Parameters         :     centerId and Test object
	-Return Type              :     Test object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     18-08-2020
	-Description              :     adding test in the center based on centerId 
	*****************************************************************************************/
	
	@Override
	public TestManagement addTest(String centerId,TestManagement newTest)  
	{
	
		DiagnosticCenter center=restTemplate.getForObject("http://hcms-diagnostic-center-management-system/center/getcenter/center-Id/"+centerId,DiagnosticCenter.class);
		if(center.getTests()==null)
		{
			List<String> testList=new ArrayList<>();
			testList.add(newTest.getTestId());	
		    center.setTests(testList);
		}
		else
		{
			center.getTests().add(newTest.getTestId());
		}
		
		TestManagement  addedTest=restTemplate.postForObject("http://hcms-diagnostic-test-management-system/test/addTest",newTest,TestManagement.class);

		restTemplate.put(("http://hcms-diagnostic-center-management-system/center/addtestid/"+centerId+"/testId/"+newTest.getTestId()), DiagnosticCenter.class);
		
		return  addedTest;
	}

	/****************************************************************************************
	-Function Name            :     deleteTestById
	-Input Parameters         :     centerId and TestId object
	-Return Type              :     boolean value
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     18-08-2020
	-Description              :     deleting test based on testId and centerId
	*****************************************************************************************/
	
	@Override
	public boolean deleteTestById(String centerId, String testId) {
		
		DiagnosticCenter center= restTemplate.getForObject("http://hcms-diagnostic-center-management-system/center/getcenter/center-Id/"+centerId,DiagnosticCenter.class);
		restTemplate.delete("http://hcms-diagnostic-test-management-system/test/deleteTest/id/"+testId);
		restTemplate.put(("http://hcms-diagnostic-center-management-system/center/remove-testid/"+centerId+"/test-id/"+testId), null);
		return true;
	}

	/****************************************************************************************
	-Function Name            :     getTestById
	-Input Parameters         :     testId object
	-Return Type              :     test object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     18-08-2020
	-Description              :     getting test based on testId 
	*****************************************************************************************/
	
	@Override
	public TestManagement getTestById(String testId) 
	{
		
		TestManagement existingTest=restTemplate.getForObject("http://hcms-diagnostic-test-management-system/test/getTest/id/"+testId, TestManagement.class);
		return existingTest;
	}

	/****************************************************************************************
	-Function Name            :     deleteAllTests
	-Input Parameters         :     no parameters
	-Return Type              :     boolean value
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     18-08-2020
	-Description              :     deleting all the centers 
	*****************************************************************************************/
	
	@Override
	public boolean deleteAllTests() 
	{
		
		restTemplate.delete("http://hcms-diagnostic-test-management-system/test/deleteAll");
		ResponseEntity<List<DiagnosticCenter>> centerEntity=restTemplate.exchange("http://hcms-diagnostic-center-management-system/center/removealltests", HttpMethod.GET,null,new ParameterizedTypeReference<List<DiagnosticCenter>>() {});
		List<DiagnosticCenter> centerList=centerEntity.getBody();
		return true;
	}

	/****************************************************************************************
	-Function Name            :     makeAppointment
	-Input Parameters         :     centerId and appointment objects
	-Return Type              :     appointment object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     19-08-2020
	-Description              :     making an appointment in center based on centerId 
	*****************************************************************************************/
	
	@Override
	public Appointment makeAppointment(String centerId,Appointment appointment) 
	{
		if(userRepo.getOne(appointment.getUserId())==null)
		{
			throw new UserNotFoundException("user not available");
		}
		Appointment newappointment = restTemplate.postForObject("http://hcms-appointment-management-system/appointmentuser/makeappointment",appointment, Appointment.class);
		restTemplate.put(("http://hcms-diagnostic-center-management-system/center/addappointmentid/"+centerId+"/appointmentid/"+ newappointment.getAppointmentId()), DiagnosticCenter.class);
		return newappointment;
		
	}

	/****************************************************************************************
	-Function Name            :     getAllAppointments
	-Input Parameters         :     no parameters
	-Return Type              :     AppointmentList object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     19-08-2020
	-Description              :     getting all the appointments 
	*****************************************************************************************/
	
	@Override
	public List<Appointment> getAllAppointments() 
	{
		ResponseEntity<List<Appointment>> appointmentEntity=restTemplate.exchange("http://hcms-appointment-management-system/appointmentuser/getallappointments", HttpMethod.GET,null,new ParameterizedTypeReference<List<Appointment>>() {});
		List<Appointment> appointmentList=appointmentEntity.getBody();
		return appointmentList;
	}

	/****************************************************************************************
	-Function Name            :     approveAppointment
	-Input Parameters         :     appointmentId and status objects
	-Return Type              :     appointment object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     19-08-2020
	-Description              :     approving appointment based on appointment Id 
	*****************************************************************************************/
	
	@Override
	public Appointment approveAppointment(BigInteger appointmentId, boolean status)
	{
		
		List<Appointment> appointmentList=getAllAppointments();
		restTemplate.put("http://hcms-appointment-management-system/appointmentadmin/approveAppointment/" + appointmentId + "/status/" + status, null);
		Appointment approvee= restTemplate.getForObject("http://hcms-appointment-management-system/appointmentadmin/getAppointment/" + appointmentId,
				Appointment.class);
		return approvee;
	
		
	}

	/****************************************************************************************
	-Function Name            :     getAllTestsInACenter
	-Input Parameters         :     centerId object
	-Return Type              :     tsetList object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     19-08-2020
	-Description              :     getting all the tests in a particular center 
	*****************************************************************************************/
	
	@Override
	public List<TestManagement> getAllTestsInACenter(String centerId)
	{
		System.out.println(centerId);
		DiagnosticCenter center = getCenterById(centerId);

		List<String> tests = center.getTests();
		List<TestManagement> testList = new ArrayList<TestManagement>();

		for (String testId : tests) {
			testList.add(getTestById(testId));
		}
		
		return testList;
		
	}

	/****************************************************************************************
	-Function Name            :     getAllAppointmentsByCenterId
	-Input Parameters         :     centerId object
	-Return Type              :     appointmentList object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     19-08-2020
	-Description              :     getting all the appointments in center by centerId
	*****************************************************************************************/
	
	@Override
	public List<Appointment> getAllAppointmentsByCenterId(String centerId)
	{
	
			DiagnosticCenter center = getCenterById(centerId);
			List<BigInteger> appointments = center.getAppointments();
			List<Appointment> appointmentList = new ArrayList<Appointment>();
			
			for (BigInteger appointmentId : appointments) {
				appointmentList.add(getAppointment(appointmentId));
			}
			
			return appointmentList;
		
	}
	
	/****************************************************************************************
	-Function Name            :     getAppointment
	-Input Parameters         :     appointmentId object
	-Return Type              :     appointment object
	-Author                   :     Ushasri Eppakayala
	-Created/Modified Date    :     19-08-2020
	-Description              :     getting an appointment 
	*****************************************************************************************/
	
	@Override
	public Appointment getAppointment(BigInteger appointmentId) {

		return restTemplate.getForObject("http://hcms-appointment-management-system/appointmentadmin/getAppointment/" + appointmentId,
				Appointment.class);

	}

	/****************************************************************************************
	-Function Name            :     registerAdmin
	-Input Parameters         :     user object
	-Return Type              :     user object
	-Author                   :     Ushasri Eppakayala
	-Throws                   :     PassKeyMisMatchException
	-Created/Modified Date    :     23-08-2020
	-Description              :     registering admin 
	*****************************************************************************************/
	
	@Override
	public User registerAdmin(User user) throws PassKeyMisMatchException {
		
		user.setUserRole("admin");
		user.setUserId(String.valueOf(random.nextInt(1000)));
		System.out.println(user);
		if(user.getPassKey().equals("1223344"))
		{
			userRepo.save(user);
		}
		else
		{
			throw new PassKeyMisMatchException("INVALID PASSKEY");
		}
		return user;
	}

	/****************************************************************************************
	-Function Name            :     login
	-Input Parameters         :     userId and password object
	-Return Type              :     user object
	-Author                   :     Ushasri Eppakayala
	-Throws                   :     UserNotFoundException
	-Created/Modified Date    :     23-08-2020
	-Description              :     a user is loggd in
	*****************************************************************************************/
	
	@Override
	public User login(String userId, String password) throws UserNotFoundException {
		if(userRepo.existsById(userId))
		{
			User user=userRepo.getOne(userId);
			return user;
		}
		else
		{
			throw new UserNotFoundException("UserNotFound");
		}
	
	}

	
	
}
