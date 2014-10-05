package eu.nabord.classes;

import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Log;

public class HexReader {

	private String nameFile;
	private static RandomAccessFile raf;

	public HexReader(String nameFile, String mode) throws Exception {
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

		Log.v("Result", "("+ad+") =" + number);
		return number;
	}
}
