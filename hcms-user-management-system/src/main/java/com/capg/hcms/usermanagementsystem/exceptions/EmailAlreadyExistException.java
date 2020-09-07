package com.capg.hcms.usermanagementsystem.exceptions;

/***********************************************************************************************
-Author                   :  Ushasri Eppakayala
-Created/Modified Date    :  14-08-2020    
-Description              :  EmailAlreadyExistException Class for handling Runtime Exceptions
*************************************************************************************************/
public class EmailAlreadyExistException extends RuntimeException {

	public EmailAlreadyExistException(String message)
	{
		super(message);
	}
}
