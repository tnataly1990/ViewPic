package com.example.viewpic.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;

/**
 * class utility for saving pictures path in file
 * 
 * @author Nataly
 * 
 */
public class FilesUtility {
	private static final String CAN_T_CREATE_APPLICATION_FOLDER = "Can't create application folder.";
	private static final String CREATED_APPLICATION_FOLDER = "Created application folder.";
	private static final String APPLICATION_FOLDER_WAS_FOUND = "Application folder was found.";
	private static final String VIEWPIC = "viewpic";
	// file to storage
	private static final String VIEWPIC_TXT = "viewpic.txt";
	//create application folder
	public static void createAppFolder() {
		File dir = new File(Environment.getExternalStoragePublicDirectory(VIEWPIC), File.separator);
		if (!dir.exists()) {
			if (dir.mkdir()) {
				LogsUtility.log(null, LogsUtility.INFO, CREATED_APPLICATION_FOLDER);
			} else {
				LogsUtility.log(null, LogsUtility.ERROR, CAN_T_CREATE_APPLICATION_FOLDER);
			}
		} else {
			LogsUtility.log(null, LogsUtility.INFO, APPLICATION_FOLDER_WAS_FOUND);
		}
	}
	// write new path to storage file
	public static void write(Context context, String path)
			throws FileNotFoundException, IOException {
		//File dir = new File(context.getFilesDir(), "/");
		File dir = new File(Environment.getExternalStoragePublicDirectory(VIEWPIC), "/");
		dir.mkdirs();
		//File file = new File(context.getFilesDir(), VIEWPIC_TXT);
		File file = new File(Environment.getExternalStoragePublicDirectory(VIEWPIC), VIEWPIC_TXT);
		BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
		try {
			output.append(path);
			output.newLine();
		} finally {
			output.flush();
			output.close();
		}
	}
	// write paths from storage file
	public static ArrayList<String> read(Context context)
			throws FileNotFoundException, IOException {
		ArrayList<String> list = new ArrayList<String>();
		//File dir = new File(context.getFilesDir(), "/");
		File dir = new File(Environment.getExternalStoragePublicDirectory(VIEWPIC), "/");
		dir.mkdirs();
		File file = new File(dir, VIEWPIC_TXT);
		Reader in = new InputStreamReader(new FileInputStream(file));
		BufferedReader bufferedReader = new BufferedReader(in);
		try {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				list.add(line);
			}
		} finally {
			in.close();
		}
		return list;
	}
}
