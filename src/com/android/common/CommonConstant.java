/**
 * 
 */
package com.android.common;

import android.view.Menu;

/**
 * @author BinhNV
 *
 */
public class CommonConstant {

	//Request code
	public static final Integer ADD_ID = 1;
	public static final Integer EDIT_ID = 2;
	public static final Integer DELETE_ID = 3;
	public static final Integer LIST_ID = 4;
	public static final Integer TITLE_ID = 5;
	public static final Integer RENAME_ID = 6;
	
	//Menu code
	public static final int MENU_ADD = Menu.FIRST + 1;
	public static final int MENU_CLOSE = Menu.FIRST + 2;
	public static final int MENU_SAVE = Menu.FIRST + 3;
	public static final int MENU_DELETE = Menu.FIRST + 4;
	public static final int MENU_DELETE_ALL_TEXT = Menu.FIRST + 5;
	public static final int MENU_BACK = Menu.FIRST + 6;
	public static final int MENU_TITLE = Menu.FIRST + 7;
	public static final int MENU_EDIT = Menu.FIRST + 8;
	
	//name of parameter
	public static final String REQUEST_CODE = "requestCode";
	public static final String FILE_NAME = "fileName";
	public static final String FILE_CONTENT = "fileContent";
	
}
