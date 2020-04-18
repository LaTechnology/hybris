/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

/**
 * @author mkostic
 * 
 */
public class UploadedMedia {

	private String name;
	private byte[] bytes;
	
	public UploadedMedia(String name, byte[] bytes) {
		this.name = name;
		this.bytes = bytes;
	}

	protected String getName() {
		return name;
	}

	protected byte[] getBytes() {
		return bytes;
	}

}
