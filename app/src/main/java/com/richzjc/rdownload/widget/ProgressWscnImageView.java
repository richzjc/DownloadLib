package com.richzjc.rdownload.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.richzjc.rdownload.R;
import static android.graphics.Canvas.ALL_SAVE_FLAG;

public class ProgressWscnImageView extends AppCompatImageView {

    private float mProgress;
    private int mHeight;
    private int mWidth;
    private float mRadius;
    private int mMaskColor;

    private Paint mPaint;
    private RectF mProgressOval;

    public ProgressWscnImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ProgressWscnImageView(Context context) {
        super(context);
        init(context, null);
    }

    public ProgressWscnImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressImageView);
        try {
            this.mProgress = a.getInteger(R.styleable.ProgressImageView_pi_progress, 0);
            //this.mMaskColor = a.getColor(R.styleable.ProgressImageView_pi_mask_color, Color.argb(180, 0, 0, 0));
            this.mMaskColor = Color.parseColor("#cc000000");

            this.mPaint = new Paint();
            mPaint.setColor(mMaskColor);
            mPaint.setAntiAlias(true);
        } finally {
            a.recycle();
        }
    }

    private void initParams() {
        if (mWidth == 0)
            mWidth = getWidth();

        if (mHeight == 0)
            mHeight = getHeight();

        if (mWidth != 0 && mHeight != 0) {
            if (mRadius == 0)
                mRadius = (float) (0.5f * Math.sqrt(mWidth * mWidth + mHeight * mHeight));

            if (mProgressOval == null)
                mProgressOval = new RectF(
                        mWidth / 2f - mRadius,
                        mHeight / 2f - mRadius,
                        mWidth / 2 + mRadius,
                        mHeight / 2f + mRadius);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, ALL_SAVE_FLAG);

        initParams();
        drawProgress(canvas);
        canvas.restoreToCount(sc);
    }

    private void drawProgress(Canvas canvas) {
        canvas.drawArc(mProgressOval, -90 + mProgress * 3.6f, 360 - mProgress * 3.6f, true, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
        mPaint.setXfermode(null);
    }

    /**
     * @param progress the progress ,range [0,100]
     */
    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }
}
