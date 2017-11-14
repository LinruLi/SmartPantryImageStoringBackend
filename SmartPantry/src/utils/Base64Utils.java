package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Utils {

	/**
	 * Future function that are able to encode image by Base64
	 * @param imgFilePath
	 * @return
	 */
	public static String GetImageStr(String imgFilePath) {
		byte[] data = null;
		// Read image byte array
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Encode byte array
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// return encoded byte array
	}

	/**
	 * Decode Base64 then store images in server folder
	 * @param imgStr
	 * @param imgFilePath
	 * @return
	 */
	public static boolean GenerateImage(String imgStr, String imgFilePath) {
		if (imgStr == null) // if there is a null image data
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		String base64Image = imgStr.split(",")[1];
		try {
			// decode base64
			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// adjust on wrong data
					bytes[i] += 256;
				}
			}
			// generate jepg photo and then stored under a folder
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}