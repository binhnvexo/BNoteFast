/**
 * 
 */
package com.android.bnote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.android.common.CommonConstant;
import com.android.handler.FileHandler;
import com.android.wrapper.TitleWrapper;

/**
 * @author BinhNV
 * 
 */
public class BnoteEdit extends Activity {

	private Integer requestCode;
	private String title;
	private TextView body;
	private FileHandler handler;
	private String fileName;
	private String fileContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_edit);

		// get request code
		if (requestCode == null) {
			Bundle extras = getIntent().getExtras();
			requestCode = (extras != null) ? extras.getInt(CommonConstant.REQUEST_CODE) : null;
			if (requestCode.equals(CommonConstant.EDIT_ID)) {
				fileName = (extras != null) ? extras.getString(CommonConstant.FILE_NAME) : null;
			} else if (requestCode.equals(CommonConstant.ADD_ID)) {
				fileContent = (extras != null) ? extras.getString(CommonConstant.FILE_CONTENT) : null;
			}
		}

		body = (TextView) findViewById(R.id.body);
		handler = new FileHandler();

		if (fileContent != null && !fileContent.equals("")) {
			body.setText(fileContent);
		}

		if (requestCode.equals(CommonConstant.EDIT_ID)) {
			try {
				File f = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
				FileInputStream input = new FileInputStream(f);
				body.setText(handler.readFile(input));
				title = fileName;
			} catch (IOException e) {
				Toast.makeText(this, "Exception : " + e.toString(),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, CommonConstant.MENU_SAVE, Menu.NONE, R.string.save).setIcon(R.drawable.save_icon);
		menu.add(Menu.NONE, CommonConstant.MENU_DELETE_ALL_TEXT, Menu.NONE, R.string.delete_all_text).setIcon(R.drawable.delete_text_icon);
		if (!requestCode.equals(Integer.valueOf(CommonConstant.ADD_ID))) {			
			menu.add(Menu.NONE, CommonConstant.MENU_TITLE, Menu.NONE, R.string.menu_title).setIcon(R.drawable.rename_icon);
		}
		menu.add(Menu.NONE, CommonConstant.MENU_BACK, Menu.NONE, R.string.back).setIcon(R.drawable.back_icon);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case CommonConstant.MENU_SAVE:
			if (requestCode.equals(CommonConstant.ADD_ID)) {
				final String mBody = body.getText().toString();
				if (mBody != null && !mBody.equals("")) {
					try {
						LayoutInflater inflater = LayoutInflater.from(this);
						View titleView = inflater.inflate(R.layout.title_edit, null);
						final TitleWrapper wrapper = new TitleWrapper(titleView, this);
						final Context nestContext = this;
						new AlertDialog.Builder(this).setTitle(R.string.add_name).setView(titleView).setPositiveButton(R.string.ok_btn,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {

												String originalName = wrapper.getFileNameStr();
												String originalType = wrapper.getFileTypeStr();
												String content = mBody;
												if (originalName != null && !originalName.equals("") && originalType != null && !originalType.equals("")
														&& content != null && !content.equals("")) {
													try {
														File f = new File(Environment.getExternalStorageDirectory() + "/" + originalName + "." + originalType);
														FileOutputStream output = new FileOutputStream(f);
														handler.writeFile(output,content);
														backToList();
													} catch (Exception e) {
														e.printStackTrace();
														backToList();
													}
												} else {
													Toast.makeText(nestContext,"Please input name of file",Toast.LENGTH_SHORT).show();
												}
											}
										}).setNegativeButton(R.string.cancel_btn,new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {

											}
										}).show();
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						backToList();
					}
				} else {
					new AlertDialog.Builder(this).setTitle("Warning").setMessage("Please input content!").setNegativeButton("Close",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

										}
									}).show();
					return false;
				}
			} else if (requestCode.equals(CommonConstant.EDIT_ID)) {
				String mTitle = title;
				String mBody = body.getText().toString();
				if (mTitle != null && !mTitle.equals("") && mBody != null && !mBody.equals("")) {
					try {
						File f = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
						FileOutputStream output = new FileOutputStream(f);
						handler.writeFile(output, mBody);
						backToList();
					} catch (Exception e) {
						e.printStackTrace();
						backToList();
					}
				} else {
					new AlertDialog.Builder(this).setTitle("Warning").setMessage("Please input content!").setNegativeButton("Close",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).show();
					return false;
				}
			}
			return true;
		case CommonConstant.MENU_DELETE_ALL_TEXT:
			body.setText("");
			return true;
		case CommonConstant.MENU_DELETE:
			File f = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
			f.delete();
			backToList();
			return true;
		case CommonConstant.MENU_BACK:
			backToList();
			return true;
		case CommonConstant.MENU_TITLE:			
			LayoutInflater inflater = LayoutInflater.from(this);
			View titleView = inflater.inflate(R.layout.title_edit, null);
			final TitleWrapper wrapper = new TitleWrapper(titleView, this);
			final Context nestContext = this;
			wrapper.setNameAndType(title);
			new AlertDialog.Builder(this).setTitle(R.string.rename_title)
					.setView(titleView).setPositiveButton(R.string.ok_btn,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									String originalName = wrapper.getFileNameStr();
									String originalType = wrapper.getFileTypeStr();
									if (originalName != null && !originalName.equals("")) {
										if (!(originalName + "." + originalType).equals(fileName)) {
											File f = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
											f.renameTo(new File(Environment.getExternalStorageDirectory() + "/" + originalName + "." + originalType));
											File fdel = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
											fdel.delete();
											backToList();
										}
									} else {
										Toast.makeText(nestContext, "Please input name of file", Toast.LENGTH_SHORT).show();
									}
								}
							}).show();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void backToList() {
		Intent intent = new Intent(this, BnoteList.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, CommonConstant.LIST_ID);
	}

}
