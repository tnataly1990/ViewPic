package com.example.viewpic.activities;

import com.example.viewpic.R;
import com.example.viewpic.beans.Picture;
import com.example.viewpic.utility.LogsUtility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
/**
 * class of edit picture activity
 * @author Natalia_Golovanchikova
 *
 */
public class EditPictureActivity extends Activity {

	private static final String POSITION = "position";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_picture);
		final int number = getIntent().getIntExtra(POSITION, 0);
		
		// set picture for edit
		final Picture selectitem = MainActivity.getPicturesList().get(number);
		final EditText nameField = (EditText) findViewById(R.id.editName);
		final EditText descField = (EditText) findViewById(R.id.editDesc);
		ImageView image = (ImageView) findViewById(R.id.editPicture);
		nameField.setText(selectitem.getTitle());
		descField.setText(selectitem.getDescription());
		image.setImageBitmap(selectitem.getImage());
		
		// run save listener
		OnClickListener saveButtonListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!nameField.getText().toString().equals(selectitem.getTitle())) {
					selectitem.setTitle(nameField.getText().toString());
				}
				if (!descField.getText().toString().equals(selectitem.getDescription())) {
					selectitem.setDescription(descField.getText().toString());
				}
				finish();
				MainActivity.update();
			}
		};
		Button saveButton = (Button) findViewById(R.id.saveBtn);
		saveButton.setOnClickListener(saveButtonListener);
		
		// run sending email activity
		OnClickListener sendEmailButtonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),
						SendEmailActivity.class);
				i.putExtra(POSITION, number);
				startActivity(i);
				LogsUtility.log(getApplicationContext(), LogsUtility.INFO, "Start SendEmailActivity was successful.");
			}
		};
		Button sendEmailButton = (Button) findViewById(R.id.sendBtn);
		sendEmailButton.setOnClickListener(sendEmailButtonListener);
	}
}
