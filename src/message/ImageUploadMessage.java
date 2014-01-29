package message;

public class ImageUploadMessage extends Message {

	private String imgName;
	
	public ImageUploadMessage(String imgName) {
		this.imgName = imgName;
	}
	
}
