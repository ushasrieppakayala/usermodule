package com.capg.hcms.usermanagementsystem.exceptions;

/***********************************************************************************************
-Author                   :  Ushasri Eppakayala
-Created/Modified Date    :  14-08-2020    
-Description              :  UserPasswordInvalidException Class for handling Runtime Exceptions
*************************************************************************************************/
public class UserPasswordInvalidException extends RuntimeException {
	
    public UserPasswordInvalidException(String message)
    {
    	super(message);
    }
}
