package eu.nabord.classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Log;

public class HexReader {

	private String nameFile;
	private static RandomAccessFile raf;
	private final int MAX_ALLOWED_VALUE = 65535;

	public HexReader(String nameFile, String mode) throws FileNotFoundException {
		this.nameFile = nameFile;
		
		raf = new RandomAccessFile(nameFile, mode);
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public RandomAccessFile getRaf() {
		return raf;
	}

	public void setRaf(RandomAccessFile raf) {
		HexReader.raf = raf;
	}
	
	public Integer getValueInFile (String address) throws IOException {
		return this.getValueInFile(address, 2);
	}
	public Integer getValueInFile (String address, int length) throws IOException {
		Integer ad = Integer.parseInt(address, 16);
				 
		raf.seek(ad);
		
		Integer number = 0;
		for( int i = 0 ; i < length ; ++i)
			number = number + (Integer.parseInt(String.valueOf(raf.readUnsignedByte() & 0x00FF)) * (int)Math.pow(256, i));

		//Log.v("Result", "("+ad+") =" + number);
		return number;
	}
	
	public void setValueInFile (String address, Integer value) throws IOException, Exception {
		Integer ad = Integer.parseInt(address, 16);
		raf.seek(ad);
		
		if (value > MAX_ALLOWED_VALUE || value < 0)
			throw new Exception("Unexpected value for address "+ address +". Must be between 0 and "+ MAX_ALLOWED_VALUE);
		
		String str_value = Integer.toHexString(value);
		while(str_value.length() < 4)
			str_value = "0"+str_value;
		
		//Log.v("Conversion result", "Address " + address + " : " + str_value);
		raf.writeByte(Integer.parseInt(str_value.substring(2, 4), 16) & 0x00FF);
		raf.writeByte(Integer.parseInt(str_value.substring(0, 2), 16) & 0x00FF);
	}
	
	public void close () {
		try {
			raf.close();
		} catch (IOException e) {}
	}
}
