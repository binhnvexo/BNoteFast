/**
 * 
 */
package com.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author BinhNV
 * 
 *	class extends EditText for modify skin of Text Note
 *
 */
public class LineEditText extends EditText {

	private Rect rect;
	private Paint paint;
	
	/**
	 * @param context
	 */
	public LineEditText(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public LineEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		rect = new Rect();
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(0x800000FF);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public LineEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int count = getLineCount();
		Rect r = rect;
		Paint p = paint;
		for (int i = 0; i < count; i++) {
			int baseline = getLineBounds(i, r);
			canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, p);
		}
		super.onDraw(canvas);
	}

}
