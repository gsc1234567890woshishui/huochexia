package com.zjwocai.qundui.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {

	/**
	 * 读取输入流,转成字符串返回
	 * 
	 * @param in
	 *            输入流
	 * @return 读取后的字符串
	 * @throws IOException
	 */
	public static String readFromStream(InputStream in) throws IOException {
		if (in != null) {
			ByteArrayOutputStream out = null;
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			String result = out.toString();
			in.close();
			out.close();

			return result;
		}

		return "";
	}
}
