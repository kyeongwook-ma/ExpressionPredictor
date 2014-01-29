import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageUtil {
	public static void saveByteToFile(String fileName, byte[] bytes) {

		String path = "./Image" + fileName +".jpg";
		
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(bytes);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] convertFiletoByte(String fileName, String path) {
		ByteArrayOutputStream bos = null;
	
			
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(path + fileName+ "-Label.jpg" ));

			bos = new ByteArrayOutputStream();

			while(is.available() > 0) 
				bos.write(is.read());

			is.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}

	

		return bos.toByteArray();
	}

}
