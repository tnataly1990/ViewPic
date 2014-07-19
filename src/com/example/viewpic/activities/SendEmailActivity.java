package com.example.viewpic.activities;

import com.example.viewpic.R;
import com.example.viewpic.beans.Picture;
import com.example.viewpic.utility.LogsUtility;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * class of send emails activity
 * 
 * @author Natalia_Golovanchikova
 * 
 */
public class SendEmailActivity extends Activity {

	private static final String TITLE = "title";
	private static final String MESS_TYPE = "image/png";
	private static final String POSITION = "position";
	private TextView toEmailText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_email);
		toEmailText = (EditText) findViewById(R.id.toEmailText);
		final EditText subjectEmailText = (EditText) findViewById(R.id.subjectEmailText);
		int number = getIntent().getIntExtra(POSITION, 0);

		final Picture picture = MainActivity.getPicturesList().get(number);
		subjectEmailText.setText(picture.getTitle() + " : "
				+ picture.getDescription());

		// run search pictures from sd card/camera activity
		OnClickListener sendEmailListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isValidEmail(toEmailText.getText().toString())) {
					final Intent emailIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					emailIntent.setType(MESS_TYPE);
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
							new String[] { toEmailText.getText().toString() });
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							subjectEmailText.getText().toString());
					Bitmap bitmap = picture.getImage();
					String path = Images.Media.insertImage(
							getContentResolver(), bitmap, TITLE, null);
					Uri screenshotUri = Uri.parse(path);
					emailIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
					try {
						startActivity(Intent.createChooser(emailIntent,
								R.string.Send_mail+""));
						finish();
					} catch (Exception e) {
						LogsUtility.log(getApplicationContext(), LogsUtility.ERROR, e.getMessage());
						Toast.makeText(SendEmailActivity.this, R.string.Send_mail_problems,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SendEmailActivity.this,
							R.string.Email_is_not_valid, Toast.LENGTH_SHORT).show();
				}
			}
		};
		Button sendButton = (Button) findViewById(R.id.sendEmailBtn);
		sendButton.setOnClickListener(sendEmailListener);

		// select contact for sending email
		OnClickListener selectContactlListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent pickIntent = new Intent(Intent.ACTION_PICK,
						android.provider.ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(pickIntent, 0);
			}
		};
		toEmailText.setOnClickListener(selectContactlListener);
	}

	// email validation
	private final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Uri contactData = data.getData();
			String id = contactData.getLastPathSegment();
			String email = null;
			Cursor cursor = getContentResolver().query(Email.CONTENT_URI, null,
					Email.CONTACT_ID + "=?", new String[] { id }, null);
			int emailIdx = cursor.getColumnIndex(Email.DATA);
			if (cursor.moveToFirst()) {
				email = cursor.getString(emailIdx);
				Toast.makeText(SendEmailActivity.this,
						R.string.Set_first_email_by_default, Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(SendEmailActivity.this,
						R.string.Ops_person_has_not_email, Toast.LENGTH_SHORT)
						.show();
			}
			toEmailText.setText(email);
		} else {
			Toast.makeText(SendEmailActivity.this, R.string.Please_enter_email,
					Toast.LENGTH_SHORT).show();
		}
	}
}
