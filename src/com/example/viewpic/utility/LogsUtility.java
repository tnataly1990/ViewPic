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
	private static final String CAN_T_CREATE_APPLICATION_FOLDER = "Can't create application folder.";
	private static final String CREATED_APPLICATION_FOLDER = "Created application folder.";
	private static final String APPLICATION_FOLDER_WAS_FOUND = "Application folder was found.";
	private static final String VIEWPIC = "viewpic";

	/*
	 * create application folder 
	 */
	public static void createAppFolder() {
		File dir = new File(
				Environment.getExternalStoragePublicDirectory(VIEWPIC),
				File.separator);
		if (!dir.exists()) {
			if (dir.mkdir()) {
				LogsUtility.log(null, LogsUtility.INFO,
						CREATED_APPLICATION_FOLDER);
			} else {
				LogsUtility.log(null, LogsUtility.ERROR,
						CAN_T_CREATE_APPLICATION_FOLDER);
			}
		} else {
			LogsUtility.log(null, LogsUtility.INFO,
					APPLICATION_FOLDER_WAS_FOUND);
		}
	}

	/**
	 * write logs
	 * 
	 * @param context
	 *            the application context
	 * @param type
	 *            the type of action
	 * @param message
	 *            the log message
	 */
	public static void log(Context context, String type, String message) {
		BufferedWriter output = null;
		try {
			File dir = new File(
					Environment.getExternalStoragePublicDirectory(VIEWPIC),
					File.separator);
			dir.mkdirs();
			File file = new File(dir, VIEWPIC_LOGS);
			output = new BufferedWriter(new FileWriter(file, true));
			String log = type
					+ new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS).format(Calendar
							.getInstance().getTime()) + " " + message;
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
