package com.ku.seoultrace.ranking;

public class MyItem {
	

	String imgUrl;
	String name;
	int drawPoint;
	int placeNumber;
	
	MyItem() {
	}

	MyItem(String imgUrl, String name, int drawPoint, int placeNumber) {

		this.imgUrl = imgUrl;
		this.name = name;
		this.drawPoint = drawPoint;
		this.placeNumber = placeNumber;
	}

	public int getPlaceNumber() {
		return placeNumber;
	}

	public void setPlaceNumber(int placeNumber) {
		this.placeNumber = placeNumber;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDrawPoint() {
		return drawPoint;
	}

	public void setDrawPoint(int drawPoint) {
		this.drawPoint = drawPoint;
	}

	@Override
	public String toString() {
		return "MyItem [imgUrl=" + imgUrl + ", name=" + name + ", drawPoint="
				+ drawPoint + ", placeNumber=" + placeNumber + "]";
	}
	
	

}