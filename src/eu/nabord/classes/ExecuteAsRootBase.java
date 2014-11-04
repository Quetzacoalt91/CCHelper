package eu.nabord.classes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

/** http://muzikant-android.blogspot.fr/2011/02/how-to-get-root-access-and-execute.html */

public abstract class ExecuteAsRootBase {
	private static boolean retval = false;
	private static Process suProcess = null;
	private static DataOutputStream os = null;
	private static DataInputStream osRes = null;
	
	public static void close() {
		if (suProcess != null) {
			try {
				os.writeBytes("exit\n");
				os.flush();
				os.close();
				osRes.close();
				int exit_code = suProcess.waitFor();
				if(exit_code != 0)
					Log.w("su", "su Process exited with code "+ exit_code);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			suProcess = null;
			os = null;
			osRes = null;
		}
		retval = false;
	}

	public static boolean canRunRootCommands() {

		try {
			//if(retval == true && suProcess != null)
				//return retval;

			suProcess = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh"});

			os = new DataOutputStream(
					suProcess.getOutputStream());
			osRes = new DataInputStream(
					suProcess.getInputStream());

			if (null != os && null != osRes) {
				// Getting the id of the current user to check if this is root
				os.writeBytes("id\n");
				os.flush();

				String currUid = osRes.readLine();
				boolean exitSu = false;
				if (null == currUid) {
					retval = false;
					exitSu = false;
					Log.d("ROOT", "Can't get root access or denied by user");
				} else if (true == currUid.contains("uid=0")) {
					retval = true;
					exitSu = true;
					Log.d("ROOT", "Root access granted");
				} else {
					retval = false;
					exitSu = true;
					Log.d("ROOT", "Root access rejected: " + currUid);
				}

				/*if (exitSu) {
					os.writeBytes("exit\n");
					os.flush();
				}*/
				//close();
			}
		} catch (Exception e) {
			// Can't get root !
			// Probably broken pipe exception on trying to write to output
			// stream (os) after su failed, meaning that the device is not
			// rooted

			retval = false;
			Log.d("ROOT", "Root access rejected [" + e.getClass().getName()
					+ "] : " + e.getMessage());
		}

		return retval;
	}
	
	public static void execute(String command) {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add(command);
		execute(commands, false);
	}
	
	public static void execute(ArrayList<String> commands) {
		execute(commands, false);
	}
	
	public static List<String> execute(String command, boolean show_result) {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add(command);
		return execute(commands, show_result).get(0);
	}

	public static List<List<String>> execute(ArrayList<String> commands, boolean show_result) {

		try {
			List<List<String>> results = new ArrayList<List<String>>();
			
			//ArrayList<String> commands = getCommandsToExecute();
			if (null != commands && commands.size() > 0) {
				if(retval == false && !canRunRootCommands())
					throw new SecurityException();

				/*Process suProcess = Runtime.getRuntime().exec("su");

				DataOutputStream os = new DataOutputStream(
						suProcess.getOutputStream());
				DataInputStream osRes = new DataInputStream(
				        suProcess.getInputStream());*/


				// Execute commands that require root access
				for (String currCommand : commands) {
					Log.d("Sudo command", "Executing \""+ currCommand + "\"");
					os.writeBytes(currCommand + "\n");
					//os.flush();

					if(show_result)
					{
						byte[] buffer = new byte[4096];
						int read;
						String fatStr = "";
						//read method will wait forever if there is nothing in the stream
						//so we need to read it in another way than while((read=stdout.read(buffer))>0)
						while(true){
						    read = osRes.read(buffer);
						    fatStr = fatStr + (new String(buffer, 0, read));
						    if(read < 4096){
						        //we have read everything
						        break;
						    }
						}
						results.add(Arrays.asList(fatStr.split("\\r?\\n")));
					}
				}

				/*os.writeBytes("exit\n");
				os.flush();*/

				/*os.close();
				osRes.close();*/
				close();
			}
			return results;
			
		} catch (IOException ex) {
			Log.w("ROOT", "Can't get root access", ex);
		} catch (SecurityException ex) {
			Log.w("ROOT", "Can't get root access", ex);
		} catch (Exception ex) {
			Log.w("ROOT", "Error executing internal operation", ex);
		}
		
		return null;
	}

	public abstract ArrayList<String> getCommandsToExecute();
}