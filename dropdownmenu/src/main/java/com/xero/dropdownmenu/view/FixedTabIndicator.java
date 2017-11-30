package com.xero.dropdownmenu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xero.dropdownmenu.R;
import com.xero.dropdownmenu.Util;

import java.util.List;

public class FixedTabIndicator extends LinearLayout {

    private Paint mDividerPaint;
    private Paint mLinePaint;

    private int mDividerPadding;
    private int mDrawableRight;

    private int mTabCount;
    private int mCurrentIndicatorPosition;
    private int mLastIndicatorPosition;


    private OnItemClickListener onItemClickListener;
    private int mTabSelecteColor;
    private int mTabDefaultColor;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position, boolean open);
    }

    public FixedTabIndicator(Context context) {
        this(context, null);
    }

    public FixedTabIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedTabIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FixedTabIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        setWillNotDraw(false);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));

        mLinePaint = new Paint();
        mLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.black));

        mDividerPadding = Util.dp(getContext(), 16);
        mDrawableRight = Util.dp(getContext(), 8);

        mTabSelecteColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        mTabDefaultColor = ContextCompat.getColor(getContext(), R.color.black);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mTabCount - 1; ++i) {// 分割线的个数比tab的个数少一个
            final View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }
            canvas.drawLine(child.getRight(), mDividerPadding, child.getRight(), getMeasuredHeight() - mDividerPadding, mDividerPaint);
        }
    }

    public void setTitles(List<String> titles) {
        if (titles == null || titles.isEmpty()) {
            return;
        }
        removeAllViews();
        mTabCount = titles.size();
        for (int i = 0; i < mTabCount; i++) {
            addView(generateView(titles.get(i), i));
        }
        postInvalidate();
    }

    private View generateView(String title, int i) {
        AppCompatTextView tv = new AppCompatTextView(getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(mTabDefaultColor);
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setMaxEms(6);
        Drawable drawable = getResources().getDrawable(R.drawable.level_filter);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        tv.setCompoundDrawablePadding(mDrawableRight);

        RelativeLayout rl = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(-2, -2);
        rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl.addView(tv, rlParams);
        rl.setId(i);

        LayoutParams params = new LayoutParams(-1, -1);
        params.weight = 1;
        params.gravity = Gravity.CENTER;
        rl.setLayoutParams(params);

        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTab(v.getId());
            }
        });

        return rl;
    }

    private void switchTab(int pos) {
        TextView tv = getChildAtCurPos(pos);

        Drawable drawable = tv.getCompoundDrawables()[2];
        int level = drawable.getLevel();

        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(tv, pos, level == 1);
        }

        if (mLastIndicatorPosition == pos) {
            tv.setTextColor(level == 0 ? mTabSelecteColor : mTabDefaultColor);
            drawable.setLevel(1 - level);

            return;
        }

        mCurrentIndicatorPosition = pos;
        resetPos(mLastIndicatorPosition);

        //highLightPos(pos);
        tv.setTextColor(mTabSelecteColor);
        tv.getCompoundDrawables()[2].setLevel(1);

        mLastIndicatorPosition = pos;
    }

    public void resetPos(int pos) {
        TextView tv = getChildAtCurPos(pos);
        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        tv.getCompoundDrawables()[2].setLevel(0);
    }

    public void resetCurrentPos() {
        resetPos(mCurrentIndicatorPosition);
    }

    public TextView getChildAtCurPos(int pos) {
        return (TextView) ((ViewGroup) getChildAt(pos)).getChildAt(0);
    }

    public void setCurrentText(String text) {
        setPositionText(mCurrentIndicatorPosition, text);
    }

    public void setPositionText(int position, String text) {
        if (position < 0 || position > mTabCount - 1) {
            throw new IllegalArgumentException("position 越界");
        }
        TextView tv = getChildAtCurPos(position);
        tv.setTextColor(mTabDefaultColor);
        tv.setText(text);
        tv.getCompoundDrawables()[2].setLevel(0);
    }

    public int getLastIndicatorPosition() {
        return mLastIndicatorPosition;
    }

}
