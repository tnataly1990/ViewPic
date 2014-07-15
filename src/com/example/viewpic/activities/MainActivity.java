package com.example.viewpic.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.example.viewpic.R;
import com.example.viewpic.adapters.PicturesGridViewAdapter;
import com.example.viewpic.beans.Picture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
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

	private static final String CANCEL = "Cancel";
	private static final String OPEN_GALLERY = "Open gallery";
	private static final String START_CAMERA = "Start camera";
	private static final String TEMP_JPG = "temp.jpg";
	private static final String IMAGE = "image/*";
	private static final String SELECT_FILE2 = "Select File";
	private static final String MY_LIFE_IS_A_LIE = "My life is a lie";
	private static final String CAKE = "Cake";
	private static final String SWEEEEEETY_COOOFFFEEE = "Sweeeeeety cooofffeee";
	private static final String COFFEE = "Coffee";
	private static final String NO_DESCRIPTION = "No description";
	private static final String NEW_PICTURE = "New picture";
	private static final String POSITION = "position";
	protected static final int REQUEST_CAMERA = 0;
	protected static final int SELECT_FILE = 1;

	private static ArrayList<Picture> picturesList = new ArrayList<Picture>();
	private static PicturesGridViewAdapter picturesGridViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		picturesList.clear();
		setContentView(R.layout.activity_main);
		setGridData();
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
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				Bundle extras = data.getExtras();
		        Bitmap imageBitmap = (Bitmap) extras.get("data");
				addPicture(imageBitmap);
				update();
			} else if (requestCode == SELECT_FILE) {
				try {
					Uri selectedImageUri = data.getData();
					String tempPath = getPath(selectedImageUri,
							MainActivity.this);
					Bitmap bm;
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
					bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
					addPicture(bm);
					update();
				} catch (Exception e) {
					Toast.makeText(MainActivity.this,
							"onActivityResult SELECT_FILE " + e.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		} else {
			Toast.makeText(MainActivity.this,
					R.string.Picture_was_not_selected, Toast.LENGTH_SHORT)
					.show();
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
	private void addPicture(Bitmap image) {
		picturesList.add(new Picture(image, NEW_PICTURE, NO_DESCRIPTION));
	}

	// init start data for pictures view
	private void setGridData() {
		Bitmap a = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.a);
		Bitmap b = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.b);

		picturesList.add(new Picture(a, COFFEE, SWEEEEEETY_COOOFFFEEE));
		picturesList.add(new Picture(b, CAKE, MY_LIFE_IS_A_LIE));
	}

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
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType(IMAGE);
					startActivityForResult(
							Intent.createChooser(intent, SELECT_FILE2),
							SELECT_FILE);
				} else if (items[item].equals(items[2])) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}