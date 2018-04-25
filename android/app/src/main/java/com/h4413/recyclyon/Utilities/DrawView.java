package com.h4413.recyclyon.Utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class DrawView extends View {
    Paint mPaint = new Paint();

    Rect mRect;

    public DrawView(Context context, Rect rect) {
        super(context);
        mRect = rect;
    }

    @Override
    public void onDraw(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        canvas.drawRect(mRect, mPaint);
    }

}
