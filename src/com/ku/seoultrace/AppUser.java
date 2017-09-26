package com.ku.seoultrace;


public class AppUser {
	private String name;
	private String phone;
	private String email;
	
	public void setName(String name){
		this.name= name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return this.phone;
	}
	
	public void setEmail(String email){
		this.email=email;
	}
	public String getEmail(){
		return this.email;
	}
}
