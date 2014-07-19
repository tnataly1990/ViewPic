package com.example.viewpic.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.os.Environment;

public class LogsUtility {

	private static final String DD_MM_YYYY_HH_MM_SS = "dd.MM.yyyy HH:mm:ss";
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	private static final String VIEWPIC_LOGS = "viewpiclogs.txt";
	private static final String VIEWPIC = "viewpic";

	public static void log(Context context, String type, String message) {	
		BufferedWriter output = null;
		try {
			//File dir = new File(context.getFilesDir(), "/");			
			File dir = new File(Environment.getExternalStoragePublicDirectory(VIEWPIC), File.separator);
			dir.mkdirs();
			//File file = new File(context.getFilesDir(), VIEWPIC_LOGS);
			File file = new File(Environment.getExternalStoragePublicDirectory(VIEWPIC), VIEWPIC_LOGS);
			output = new BufferedWriter(new FileWriter(file, true));
			String log = type + new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS).format(Calendar.getInstance().getTime()) + " " + message;
			output.append(log);
			output.newLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.flush();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
