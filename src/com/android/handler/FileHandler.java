/**
 * 
 */
package com.android.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author BinhNV
 *
 */
public class FileHandler {

	//method for write file to SD card
	public void writeFile(OutputStream out, String content) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(out);
		writer.write(content);			
		writer.close();
	}
	
	//method for read file from SD card
	public String readFile(InputStream in) throws IOException {
		if (in != null) {
			InputStreamReader tmp = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(tmp);
			StringBuffer buffer = new StringBuffer();
			String str;
			while ((str = reader.readLine()) != null) {
				buffer.append(str + "\n");
			}
			in.close();
			return buffer.toString();
		}
		return null;		
	}
	
}
