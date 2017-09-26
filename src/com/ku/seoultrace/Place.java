package com.ku.seoultrace;

import com.parse.ParseGeoPoint;

public class Place {
	
	private int placeNumber;
	private String placeName;
	private String phoneNum;
	private String address;
	private String holiday;
	private String openTime;
	private String detailInfo;
	private String transportation;
	private String imgUrl;
	private ParseGeoPoint parseGeoPoint;
	private String category;

	public Place() {
	}
	public Place(ParseGeoPoint parseGeoPoint,String placeName,int placeNumber) {
		this.parseGeoPoint = parseGeoPoint;
		this.placeName = placeName;
		this.placeNumber = placeNumber;
	}

	public Place(int placeNumber, String placeName, String phoneNum,
			String address, String holiday, String openTime, String detailInfo,
			String transportation, String imgUrl, ParseGeoPoint parseGeoPoint,String category) {
		super();
		this.placeNumber = placeNumber;
		this.placeName = placeName;
		this.phoneNum = phoneNum;
		this.address = address;
		this.holiday = holiday;
		this.openTime = openTime;
		this.detailInfo = detailInfo;
		this.transportation = transportation;
		this.imgUrl = imgUrl;
		this.parseGeoPoint = parseGeoPoint;
		this.category = category;
		
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public ParseGeoPoint getParseGeoPoint() {
		return parseGeoPoint;
	}

	public void setParseGeoPoint(ParseGeoPoint parseGeoPoint) {
		this.parseGeoPoint = parseGeoPoint;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setPlaceNumber(int num)
	{
		this.placeNumber = num;
	}
	
	public int getPlaceNumber(){
		return this.placeNumber;
	}
	
	/**
	 * @return the holiday
	 */
	public String getHoliday() {
		return holiday;
	}

	/**
	 * @param holiday the holiday to set
	 */
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	/**
	 * @return the openTime
	 */
	public String getOpenTime() {
		return openTime;
	}

	/**
	 * @param openTime the openTime to set
	 */
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	/**
	 * @return the detailInfo
	 */
	public String getDetailInfo() {
		return detailInfo;
	}

	/**
	 * @param detailInfo the detailInfo to set
	 */
	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	/**
	 * @return the transportation
	 */
	public String getTransportation() {
		return transportation;
	}

	/**
	 * @param transportation the transportation to set
	 */
	public void setTransportation(String transportation) {
		this.transportation = transportation;
	}

	/**
	 * @return the imgUrl
	 */
	public String getImgUrl() {
		return imgUrl;
	}

	
	/**
	 * @param imgUrl the imgUrl to set
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	@Override
	public String toString() {
		return "Place [placeNumber=" + placeNumber + ", placeName=" + placeName
				+ ", phoneNum=" + phoneNum + ", address=" + address + "]";
	}
}
