package eu.nabord.classes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/** http://muzikant-android.blogspot.fr/2011/02/how-to-get-root-access-and-execute.html */

public abstract class ExecuteAsRootBase {
	private static boolean retval = false;

	public static boolean canRunRootCommands() {

		try {
			if(retval == true)
				return retval;

			Process suProcess = Runtime.getRuntime().exec("su");

			DataOutputStream os = new DataOutputStream(
					suProcess.getOutputStream());
			DataInputStream osRes = new DataInputStream(
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

				if (exitSu) {
					os.writeBytes("exit\n");
					os.flush();
				}
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
	
	public static List<String> execute(String command) {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add(command);
		return execute(commands).get(0);
	}

	public static List<List<String>> execute(ArrayList<String> commands) {

		try {
			List<List<String>> results = new ArrayList<List<String>>();
			
			//ArrayList<String> commands = getCommandsToExecute();
			if (null != commands && commands.size() > 0) {
				if(retval == false && !canRunRootCommands())
					return null;

				Process suProcess = Runtime.getRuntime().exec("su");

				DataOutputStream os = new DataOutputStream(
						suProcess.getOutputStream());
				BufferedReader osRes = new BufferedReader(
				        new InputStreamReader(suProcess.getInputStream()));


				// Execute commands that require root access
				for (String currCommand : commands) {
					Log.e("Sudo command", "Executing \""+ currCommand + "\"");
					os.writeBytes(currCommand + "\n");
					os.flush();
					
					/*byte[] buffer = new byte[4096];
					int read;
					//read method will wait forever if there is nothing in the stream
					//so we need to read it in another way than while((read=stdout.read(buffer))>0)
					while(true){
						List<String> resultCmd = new ArrayList<String>();
						
					    read = osRes.readLine(buffer);
					    resultCmd.add(new String(buffer, 0, read));
					    if(read < 4096){
					        //we have read everything
					    	results.add(resultCmd);
					        break;
					    }
					}*/
					
					List<String> resultCmd = new ArrayList<String>();
					while (osRes.ready()) {
					    final String line = osRes.readLine();
					    if (line == null) break;

					    resultCmd.add(line);
						Log.e("Sudo command", "Returns \""+ line + "\"");
					}
					results.add(resultCmd);
					
					/*List<String> resultCmd = new ArrayList<String>();
					String result;
					while ((result = osRes.readLine()) != null){
						resultCmd.add(result);
						Log.e("Sudo command", "Returns \""+ result + "\"");
					}
					results.add(resultCmd);*/
				}

				os.writeBytes("exit\n");
				os.flush();

				try {
					int suProcessRetval = suProcess.waitFor();
					if (255 != suProcessRetval) {
						// Root access granted
						retval = true;
					} else {
						// Root access denied
						retval = false;
					}
				} catch (Exception ex) {
					Log.e("ROOT", "Error executing root action", ex);
				}
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