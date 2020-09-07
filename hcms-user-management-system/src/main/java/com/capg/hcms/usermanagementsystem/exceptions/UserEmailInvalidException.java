package com.capg.hcms.usermanagementsystem.exceptions;

/***********************************************************************************************
-Author                   :  Ushasri Eppakayala
-Created/Modified Date    :  14-08-2020    
-Description              :  UserEmailInvalidException Class for handling Runtime Exceptions
*************************************************************************************************/
public class UserEmailInvalidException extends RuntimeException {
 
	public UserEmailInvalidException(String message)
	{
		super(message);
	}
}
