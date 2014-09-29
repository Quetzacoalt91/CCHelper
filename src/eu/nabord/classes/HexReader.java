package eu.nabord.classes;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class HexReader {

	private String nameFile;
	private static RandomAccessFile raf;

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
		this.raf = raf;
	}
}
