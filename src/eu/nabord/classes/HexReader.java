package eu.nabord.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;

public class HexReader {

	private String nameFile;
	private String pathFile;
	private String pathBackup;
	private static RandomAccessFile raf;
	private final int MAX_ALLOWED_VALUE = 65535;
	public final int PRE_BACKUP = 0;
	public final int POST_BACKUP = 1;

	public HexReader(String pathFile, String mode, String pathBackup) throws FileNotFoundException {
		this.pathFile = pathFile;
		this.nameFile = pathFile.substring(pathFile.lastIndexOf("/")+1);
		this.pathBackup = pathBackup;
		
		raf = new RandomAccessFile(pathFile, mode);
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
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
		if (value > MAX_ALLOWED_VALUE || value < 0)
			throw new Exception("Unexpected value for address "+ address +". Must be between 0 and "+ MAX_ALLOWED_VALUE);
		
		//this.createBackup(PRE_BACKUP);
		
		String str_value = Integer.toHexString(value);
		while(str_value.length() < 4)
			str_value = "0"+str_value;
		
		//Log.v("Conversion result", "Address " + address + " : " + str_value);
		Integer ad = Integer.parseInt(address, 16);
		raf.seek(ad);
		
		raf.writeByte(Integer.parseInt(str_value.substring(2, 4), 16) & 0x00FF);
		raf.writeByte(Integer.parseInt(str_value.substring(0, 2), 16) & 0x00FF);
		
		//this.createBackup(POST_BACKUP);
	}
	
	public void createBackup(int type) {
		ArrayList<String> commands = new ArrayList<String>();
		
		switch (type) {
		case PRE_BACKUP:
			File backupFile = new File(pathBackup + nameFile + ".pre");
			File dataFile = new File(pathFile);
			if (backupFile.exists()) {
				Date lastModified = new Date(backupFile.lastModified());
				Date lastModified2 = new Date(dataFile.lastModified());
				
				if (lastModified.compareTo(lastModified2) >= 0)
					return;
			}

			commands.add("mkdir -p "+pathBackup);
			commands.add("cp "+ pathFile + " "+ pathBackup + nameFile + ".pre");
			break;

		case POST_BACKUP:
			commands.add("mkdir -p "+pathBackup);
			commands.add("cp "+ pathFile + " "+ pathBackup + nameFile + ".post");
			commands.add("touch "+ pathBackup + nameFile + ".pre");
			break;
		}
		ExecuteAsRootBase.execute(commands);
	}
	
	public void restoreBackup() {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add("cp "+pathBackup + nameFile + ".pre "+ pathFile);
		ExecuteAsRootBase.execute(commands);
	}
	
	public void close () {
		try {
			raf.close();
		} catch (IOException e) {}
	}
}
