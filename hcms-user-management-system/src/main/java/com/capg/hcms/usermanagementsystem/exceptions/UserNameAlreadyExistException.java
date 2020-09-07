package com.capg.hcms.usermanagementsystem.exceptions;

/***********************************************************************************************
-Author                   :  Ushasri Eppakayala
-Created/Modified Date    :  14-08-2020    
-Description              :  UserNameAlreadyExistException Class for handling Runtime Exceptions
*************************************************************************************************/
public class UserNameAlreadyExistException extends RuntimeException {

	public  UserNameAlreadyExistException(String message)
	{
		super(message);
	}
}
