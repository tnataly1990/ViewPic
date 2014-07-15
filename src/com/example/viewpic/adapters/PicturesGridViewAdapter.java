package com.example.viewpic.adapters;

import java.util.ArrayList;

import com.example.viewpic.R;
import com.example.viewpic.beans.Picture;
import com.example.viewpic.beans.PictureHolder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * class of adapter for pictures main grid view
 * @author Natalia_Golovanchikova
 *
 */
public class PicturesGridViewAdapter extends ArrayAdapter<Picture> {

	private Context context;
	private int layoutResourceId;
	ArrayList<Picture> picturesList = new ArrayList<Picture>();
	
	public PicturesGridViewAdapter(Context context, int layoutResourceId,
			ArrayList<Picture> picturesList) {
		super(context, layoutResourceId, picturesList);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.picturesList = picturesList;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		View row = view;
		PictureHolder pictureHolder = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, viewGroup, false);
			pictureHolder = new PictureHolder();
			pictureHolder.description = (TextView) row.findViewById(R.id.pictureDesc);
			pictureHolder.title = (TextView) row.findViewById(R.id.pictureTitle);
			pictureHolder.image = (ImageView) row.findViewById(R.id.picture);
			row.setTag(pictureHolder);
		} else {
			pictureHolder = (PictureHolder) row.getTag();
		}
		Picture item = picturesList.get(position);
		pictureHolder.description.setText(item.getDescription());
		pictureHolder.title.setText(item.getTitle());
		pictureHolder.image.setImageBitmap(item.getImage());		
		return row;
	}
	
}
