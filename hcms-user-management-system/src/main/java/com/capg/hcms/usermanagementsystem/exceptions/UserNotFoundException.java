package com.capg.hcms.usermanagementsystem.exceptions;

/***********************************************************************************************
-Author                   :  Ushasri Eppakayala
-Created/Modified Date    :  14-08-2020    
-Description              :  UserNotFoundException Class for handling Runtime Exceptions
*************************************************************************************************/
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String message)
	{
		super(message);
	}
	public UserNotFoundException()
	{
		super();
	}
}
