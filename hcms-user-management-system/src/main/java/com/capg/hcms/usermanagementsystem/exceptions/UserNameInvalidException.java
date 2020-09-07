package com.capg.hcms.usermanagementsystem.exceptions;

/***********************************************************************************************
-Author                   :  Ushasri Eppakayala
-Created/Modified Date    :  14-08-2020    
-Description              :  UserNameInvalidException Class for handling Runtime Exceptions
*************************************************************************************************/
public class UserNameInvalidException extends RuntimeException {
	
	public UserNameInvalidException(String message)
	{
		super(message);
	}
}
