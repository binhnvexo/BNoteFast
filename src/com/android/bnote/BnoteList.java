package com.android.bnote;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.common.CommonConstant;
import com.android.wrapper.TitleWrapper;

public class BnoteList extends ListActivity {	
	
	private File[] files;
	private List<String> fileNames;	
	private ArrayAdapter<String> adapter;
	private TextView noNote;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);        
        
        //get array of file in root of sdcard
        files = Environment.getExternalStorageDirectory().listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				return (filename.endsWith(".txt") || filename.endsWith(".xml"));
			}
			
		});
                      
        noNote = (TextView) findViewById(R.id.no_note);
        fileNames = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, fileNames);        
        adapter.notifyDataSetChanged();
        
        if (files.length > 0) {
        	for (File file : files) {
        		fileNames.add(file.getName());
    		}        	
        	setListAdapter(adapter);           	
		}         
        
        if (fileNames.size() > 0) {
			noNote.setText("");
		} else {
			noNote.setText(R.string.no_note);
		}
        registerForContextMenu(getListView());
        
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		String fileName = new String();
		fileName = fileNames.get(position);
		
		Intent intent = new Intent(this, BnoteEdit.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(CommonConstant.REQUEST_CODE, CommonConstant.EDIT_ID);
		intent.putExtra(CommonConstant.FILE_NAME, fileName);
		startActivityForResult(intent, CommonConstant.EDIT_ID);
		
	}
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {		
		menu.add(Menu.NONE, CommonConstant.MENU_EDIT, Menu.NONE, R.string.menu_edit);
		menu.add(Menu.NONE, CommonConstant.MENU_DELETE, Menu.NONE, R.string.delete);
		menu.add(Menu.NONE, CommonConstant.MENU_TITLE, Menu.NONE, R.string.menu_title);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		String fileName = fileNames.get(info.position);
		
		switch (item.getItemId()) {
			case CommonConstant.MENU_EDIT:
				
				Intent intent = new Intent(this, BnoteEdit.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(CommonConstant.REQUEST_CODE, CommonConstant.EDIT_ID);
				intent.putExtra(CommonConstant.FILE_NAME, fileName);
				startActivityForResult(intent, CommonConstant.EDIT_ID);
				return true;
				
			case CommonConstant.MENU_DELETE:
					
				deleteNote(info.position);					
				return true;
				
			case CommonConstant.MENU_TITLE:
				
				LayoutInflater inflater = LayoutInflater.from(this);
				View titleView = inflater.inflate(R.layout.title_edit, null);
				final TitleWrapper wrapper = new TitleWrapper(titleView, this);
				final Context nestContext = this;
				
				final String name = fileName;
				wrapper.setNameAndType(fileName);
				
				new AlertDialog.Builder(this).setTitle(R.string.rename_title).setView(titleView).setPositiveButton(R.string.ok_btn,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										String originalName = wrapper.getFileNameStr();
										String originalType = wrapper.getFileTypeStr();
										if (originalName != null && !originalName.equals("")) {
											if (!(originalName + "." + originalType).equals(name)) {
												File f = new File(Environment.getExternalStorageDirectory() + "/" + name);
												f.renameTo(new File(Environment.getExternalStorageDirectory() + "/" + originalName + "." + originalType));
												File fdel = new File(Environment.getExternalStorageDirectory() + "/" + name);
												fdel.delete();										
												int count = fileNames.size();
												for (int i = 0; i < count; i++) {
													if (fileNames.get(i).equals(name)) {
														fileNames.remove(i);
														i--;
														count--;
													}
												}
												fileNames.add(originalName + "." + originalType);
												adapter = new ArrayAdapter<String>(BnoteList.this, android.R.layout.simple_expandable_list_item_1, fileNames);
												getListView().setAdapter(adapter);
												if (fileNames.size() > 0) {
													noNote.setText("");
												} else {
													noNote.setText(R.string.no_note);
												}
											}
										} else {
											Toast.makeText(nestContext, "Please input name of file", Toast.LENGTH_SHORT).show();
										}
									}
								}).show();
				return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, CommonConstant.MENU_ADD, Menu.NONE, R.string.menu_add).setIcon(R.drawable.add_icon);
		menu.add(Menu.NONE, CommonConstant.MENU_CLOSE, Menu.NONE, R.string.menu_close).setIcon(R.drawable.close_icon);
		return super.onCreateOptionsMenu(menu);		
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case CommonConstant.MENU_ADD:
				
				Intent intent = new Intent(this, BnoteEdit.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(CommonConstant.REQUEST_CODE, CommonConstant.ADD_ID);
				startActivityForResult(intent, CommonConstant.ADD_ID);
				return true;
				
			case CommonConstant.MENU_CLOSE:
				
				finish();				
				return true;
				
		}
		return super.onMenuItemSelected(featureId, item);
	}	
	
	private void deleteNote(final int id) {
		new AlertDialog.Builder(this).setTitle("Delete").setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String fileName2 = new String();
				fileName2 = fileNames.get(id);
				File f = new File(Environment.getExternalStorageDirectory() + "/" + fileName2);
				f.delete();				
				int count = fileNames.size();
				for (int i = 0; i < count; i++) {
					if (fileNames.get(i).equals(fileName2)) {
						fileNames.remove(i);
						i--;
						count--;
					}
				}
				adapter = new ArrayAdapter<String>(BnoteList.this, android.R.layout.simple_expandable_list_item_1, fileNames);
				getListView().setAdapter(adapter);
				if (fileNames.size() > 0) {
					noNote.setText("");
				} else {
					noNote.setText(R.string.no_note);
				}
			}
		}).setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).show();
	}
	
}