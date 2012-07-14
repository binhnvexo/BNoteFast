/**
 * 
 */
package com.android.bnote;

import java.io.File;
import java.io.FileOutputStream;

import com.android.common.CommonConstant;
import com.android.handler.FileHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * @author BinhNV
 * 
 */
public class BnoteTitle extends Activity implements OnItemSelectedListener {

	private EditText fileName;
	private String fileContent;
	private Spinner spinner;
	private String fileType;
	private String[] items = { "txt", "xml" };
	private Integer requestCode;
	private String fName;
	private FileHandler handler;
	private String originalName;
	private String originalType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title_edit);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner = (Spinner) findViewById(R.id.filetype);
		spinner.setOnItemSelectedListener(this);
		spinner.setAdapter(adapter);

		handler = new FileHandler();
		fileName = (EditText) findViewById(R.id.filename);
		
		if (requestCode == null) {
			Bundle extras = getIntent().getExtras();
			requestCode = (extras != null) ? extras.getInt(CommonConstant.REQUEST_CODE) : null;
			if (requestCode != null) {
				if (requestCode.equals(CommonConstant.ADD_ID)) {
					fileContent = (extras != null) ? extras.getString(CommonConstant.FILE_CONTENT) : null;
				} else if (requestCode.equals(CommonConstant.RENAME_ID)) {
					fName = (extras != null) ? extras.getString(CommonConstant.FILE_NAME) : null;
					String[] name = fName.split("\\.");					
					originalType = name[name.length - 1];
					originalName = "";
					for (int i = 0; i < name.length - 1; i++) {
						if (i == name.length - 2) {
							originalName += name[i];
						} else {
							originalName += name[i] + ".";
						}						
					}														
					fileName.setText(originalName);

					int pos = 0;
					for (int i = 0; i < items.length; i++) {
						if (items[i].equals(originalType)) {
							pos = i;
						}
					}
					spinner.setSelection(pos);
				}
			}
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int position,
			long id) {
		fileType = items[position];
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, CommonConstant.MENU_SAVE, Menu.NONE, R.string.save);
		menu.add(Menu.NONE, CommonConstant.MENU_BACK, Menu.NONE, R.string.back);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case CommonConstant.MENU_SAVE:
			if (requestCode.equals(CommonConstant.ADD_ID)) {
				originalName = fileName.getText().toString();
				originalType = fileType;
				if (originalName != null && !originalName.equals("")
						&& originalType != null && !originalType.equals("")
						&& fileContent != null && !fileContent.equals("")) {
					try {
						File f = new File(Environment.getExternalStorageDirectory() + "/" + originalName + "." + originalType);
						FileOutputStream output = new FileOutputStream(f);
						handler.writeFile(output, fileContent);
						backToList();
					} catch (Exception e) {
						e.printStackTrace();
						backToList();
					}
				} else {
					new AlertDialog.Builder(this).setTitle("Warning")
							.setMessage("Please input both of title and body!")
							.setNegativeButton("Close",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).show();
					return false;
				}
			} else if (requestCode.equals(CommonConstant.RENAME_ID)) {
				if (fName != null && !fName.equals("")) {
					if (!fName.equals(fileName.getText().toString())) {
						File f = new File(Environment.getExternalStorageDirectory() + "/" + fName);
						f.renameTo(new File(Environment.getExternalStorageDirectory() + "/" + fileName.getText().toString() + "." + fileType));
						File fdel = new File(Environment.getExternalStorageDirectory() + "/" + fName);
						fdel.delete();
						backToList();
					}
				}
			}
			return true;
		case CommonConstant.MENU_BACK:
			Intent intent = new Intent(this, BnoteEdit.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if (requestCode.equals(CommonConstant.ADD_ID)) {				
				intent.putExtra(CommonConstant.REQUEST_CODE, CommonConstant.ADD_ID);
				intent.putExtra(CommonConstant.FILE_CONTENT, fileContent);
				startActivityForResult(intent, CommonConstant.EDIT_ID);
			} else if (requestCode.equals(CommonConstant.RENAME_ID)) {				
				intent.putExtra(CommonConstant.REQUEST_CODE, CommonConstant.EDIT_ID);
				intent.putExtra(CommonConstant.FILE_NAME, fName);				
			}			
			startActivityForResult(intent, CommonConstant.EDIT_ID);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void backToList() {
		Intent intent = new Intent(this, BnoteList.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, CommonConstant.LIST_ID);
	}

}
