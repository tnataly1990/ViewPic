package com.example.viewpic.beans;

import android.graphics.Bitmap;

/**
 * class of Picture object
 * @author Natalia_Golovanchikova
 *
 */
public class Picture {

	Bitmap image;
	String title;
	String description;
	
	/*
	 * constructor of Picture class
	 */
	public Picture(Bitmap image, String title, String description) {
		super();
		this.image = image;
		this.title = title;
		this.description = description;
	}
	/**
	 * get image
	 * @return the image
	 */
	public Bitmap getImage() {
		return image;
	}
	/**
	 * set image
	 * @param image the image
	 */
	public void setImage(Bitmap image) {
		this.image = image;
	}
	/**
	 * get title of image
	 * @return the title of image
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * set title for image
	 * @param title the title of image
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * get description of image
	 * @return description of image
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * set description for image
	 * @param description the description of image
	 */
	public void setDescription(String description) {
		this.description = description;
	}
		
}
