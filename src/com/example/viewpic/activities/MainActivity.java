package com.example.viewpic.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.viewpic.R;
import com.example.viewpic.adapters.PicturesGridViewAdapter;
import com.example.viewpic.beans.Picture;
import com.example.viewpic.utility.FilesUtility;
import com.example.viewpic.utility.LogsUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * class of main grid view activity
 * 
 * @author Natalia_Golovanchikova
 * 
 */
public class MainActivity extends Activity {

	private static final String DD_MM_YYYY_HH_MM_SS = "dd.MM.yyyy HH:mm:ss";
	private static final String LOADED_FROM_STORAGE_FILE = "Loaded from storage file: ";
	private static final String STORAGE_FILE_NOT_FOUND = "Storage file not found.";
	private static final String ADDED_TO_STORAGE = "Added to storage: ";
	private static final String ADDED_IMAGE = "Added image: ";
	private static final String TEMP = "temp.jpg";
	private static final int FOUR = 4;
	private static final String CANCEL = "Cancel";
	private static final String OPEN_GALLERY = "Open gallery";
	private static final String START_CAMERA = "Start camera";
	private static final String TEMP_JPG = TEMP;
	private static final String IMAGE = "image/*";
	private static final String SELECT_FILE2 = "Select File";
	private static final String NO_DESCRIPTION = "No description";
	private static final String POSITION = "position";
	protected static final int REQUEST_CAMERA = 0;
	protected static final int SELECT_FILE = 1;

	private static ArrayList<Picture> picturesList = new ArrayList<Picture>();
	private static PicturesGridViewAdapter picturesGridViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogsUtility.log(getApplicationContext(), LogsUtility.INFO, 
				"Start application at:" + new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS).format(Calendar.getInstance().getTime()));
		picturesList.clear();
		setGridData();
		setContentView(R.layout.activity_main);
		GridView gridView = (GridView) findViewById(R.id.picturesGridView);
		picturesGridViewAdapter = new PicturesGridViewAdapter(this,
				R.layout.picture_grid, picturesList);
		gridView.setAdapter(picturesGridViewAdapter);
		// run edit activity listener
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long value) {
				Intent i = new Intent(getBaseContext(),
						EditPictureActivity.class);
				i.putExtra(POSITION, position);
				startActivity(i);
			}
		});

		// run search pictures from sd card/camera activity
		OnClickListener searchPicturesListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				selectPicture();
			}
		};
		Button addButton = (Button) findViewById(R.id.addBtn);
		addButton.setOnClickListener(searchPicturesListener);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK) {
				if (requestCode == REQUEST_CAMERA) {
					// Should add picture on sd card and show it in list
				} else if (requestCode == SELECT_FILE) {
					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };

					Cursor cursor = getContentResolver().query(selectedImage,
							filePathColumn, null, null, null);
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();

					Toast.makeText(MainActivity.this,
							ADDED_IMAGE + picturePath, Toast.LENGTH_LONG)
							.show();
					addPicture(
							readPicture(picturePath),
							picturePath.substring(picturePath.lastIndexOf("/") + 1),
							picturePath);
					FilesUtility.write(getApplicationContext(), picturePath);
					LogsUtility.log(getApplicationContext(), LogsUtility.INFO,
							ADDED_TO_STORAGE + picturePath);
					update();
				}
			} else {
				Toast.makeText(MainActivity.this,
						R.string.Picture_was_not_selected, Toast.LENGTH_SHORT)
						.show();
			}
		} catch (OutOfMemoryError e) {
			LogsUtility.log(getApplicationContext(), LogsUtility.ERROR,
					e.getMessage());
		} catch (RuntimeException e) {
			LogsUtility.log(getApplicationContext(), LogsUtility.ERROR,
					e.getMessage());
		} catch (Exception e) {
			LogsUtility.log(getApplicationContext(), LogsUtility.ERROR,
					e.getMessage());
		}
	}

	/**
	 * updates grid view
	 */
	public static void update() {
		picturesGridViewAdapter.notifyDataSetChanged();
	}

	/**
	 * get list of pictures
	 * 
	 * @return the list of pictures
	 */
	public static ArrayList<Picture> getPicturesList() {
		return picturesList;
	}

	// add new picture
	private void addPicture(Bitmap image, String name, String path) {
		picturesList.add(new Picture(image, name, NO_DESCRIPTION));
	}

	// init start data for pictures view
	private void setGridData() {
		ArrayList<String> pathList = new ArrayList<String>();
		try {
			pathList = (ArrayList<String>) FilesUtility
					.read(getApplicationContext());
		} catch (FileNotFoundException e) {
			LogsUtility.log(getApplicationContext(), LogsUtility.ERROR,
					STORAGE_FILE_NOT_FOUND);
		} catch (IOException e) {
			LogsUtility.log(getApplicationContext(), LogsUtility.ERROR,
					e.getMessage());
		}
		for (String path : pathList) {
			LogsUtility.log(getApplicationContext(), LogsUtility.INFO,
					LOADED_FROM_STORAGE_FILE + path);
			addPicture(readPicture(path),
					path.substring(path.lastIndexOf("/") + 1), path);
		}
	}

	// start select picture dialog
	private void selectPicture() {
		final String[] items = { START_CAMERA, OPEN_GALLERY, CANCEL };

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle(R.string.addPic);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals(items[0])) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(android.os.Environment
							.getExternalStorageDirectory(), TEMP_JPG);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals(items[1])) {
					startGallery();
				} else if (items[item].equals(items[2])) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	// start gallery
	private void startGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType(IMAGE);
		startActivityForResult(Intent.createChooser(intent, SELECT_FILE2),
				SELECT_FILE);
	}

	// read picture and decoding
	private Bitmap readPicture(String picturePath) {
		Bitmap bm;
		BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
		btmapOptions.inSampleSize = FOUR;
		btmapOptions.inPurgeable = true;
		btmapOptions.inScaled = false;
		bm = BitmapFactory.decodeFile(picturePath, btmapOptions);
		return bm;
	}
}
