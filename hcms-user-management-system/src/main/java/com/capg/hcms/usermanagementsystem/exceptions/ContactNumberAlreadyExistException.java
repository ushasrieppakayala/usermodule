package com.capg.hcms.usermanagementsystem.exceptions;

/***********************************************************************************************
-Author                   :  Ushasri Eppakayala
-Created/Modified Date    :  14-08-2020   
-Description              :  ContactNumberAlreadyExistException Class for handling Runtime Exceptions
*************************************************************************************************/
public class ContactNumberAlreadyExistException extends RuntimeException {

	public ContactNumberAlreadyExistException(String message)
	{
		super(message);
	}
}
