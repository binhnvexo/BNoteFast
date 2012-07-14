/**
 * 
 */
package com.android.wrapper;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.android.bnote.R;

/**
 * @author BinhNV
 *
 */
public class TitleWrapper implements OnItemSelectedListener {

	private TextView fileName;	
	private Spinner fileType;
	private View base;
	private String[] items = { "txt", "xml" };
	private String fileNameStr;
	private String fileTypeStr;
	
	public TitleWrapper(View base, Context context) {
		this.base = base;
		fileName = (TextView) base.findViewById(R.id.filename);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fileType = (Spinner) base.findViewById(R.id.filetype);
		fileType.setOnItemSelectedListener(this);
		fileType.setAdapter(adapter);
	}

	public TextView getFileName() {
		if (fileName == null) {
			fileName = (TextView) base.findViewById(R.id.filename);
		}
		return fileName;
	}

	public void setFileName(TextView fileName) {
		this.fileName = fileName;
	}

	public Spinner getFileType() {
		if (fileType == null) {
			fileType = (Spinner) base.findViewById(R.id.filetype);
		}
		return fileType;
	}

	public void setFileType(Spinner fileType) {
		this.fileType = fileType;
	}	

	@Override
	public void onItemSelected(AdapterView<?> adapter, View v, int position,
			long id) {
		this.fileTypeStr = items[position];
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}	

	public void setFileNameStr(String fileNameStr) {
		this.fileNameStr = fileNameStr;
	}

	public void setFileTypeStr(String fileTypeStr) {
		this.fileTypeStr = fileTypeStr;
	}

	public String getFileNameStr() {
		if (fileNameStr == null) {
			fileNameStr = fileName.getText().toString(); 
		}
		return fileNameStr;
	}

	public String getFileTypeStr() {
		if (fileTypeStr == null) {
			fileTypeStr = ((String) fileType.getSelectedItem()).toString();
		}
		return fileTypeStr;
	}
	
	public void setNameAndType(String name) {
		if (name != null && !name.equals("")) {
			String[] nameArr = name.split("\\.");
			String originalType = nameArr[nameArr.length - 1];
			String originalName = "";
			for (int i = 0; i < nameArr.length - 1; i++) {
				if (i == nameArr.length - 2) {
					originalName += nameArr[i];
				} else {
					originalName += nameArr[i] + ".";
				}						
			}
			this.fileName.setText(originalName);
			int pos = 0;
			for (int i = 0; i < items.length; i++) {
				if (items[i].equals(originalType)) {
					pos = i;
				}
			}
			this.fileType.setSelection(pos);
		}
	}

}
