package com.capg.hcms.usermanagementsystem.model;

import java.util.List;

/***********************************************************************************************
-Author                   :  Ushasri Eppakayala
-Created/Modified Date    :  11-08-2020   
-Description              :  AppointmentList Bean class
*************************************************************************************************/
public class AppointmentList {
	
	private List<Appointment> appointmentsList;

	public AppointmentList() {
		super();
	}

	public AppointmentList(List<Appointment> appointmentsList) {
		super();
		this.appointmentsList = appointmentsList;
	}

	public List<Appointment> getAppointmentList() {
		return appointmentsList;
	}

	public void setAppointmentList(List<Appointment> appointmentsList) {
		this.appointmentsList = appointmentsList;
	}

	@Override
	public String toString() {
		return "AppointmentList [appointmentsList=" + appointmentsList + "]";
	}
	
	


}
