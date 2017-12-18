package com.xero.dropdownmenu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.xero.dropdownmenu.view.FixedTabIndicator;

import java.util.List;

public class DropDownMenu extends RelativeLayout {

    private FixedTabIndicator fixedTabIndicator;
    private FrameLayout frameLayoutContainer;

    private View currentView;

    private Animation dismissAnimation;
    private Animation occurAnimation;
    private Animation alphaDismissAnimation;
    private Animation alphaOccurAnimation;

    private MenuAdapter mMenuAdapter;

    public DropDownMenu(Context context) {
        this(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setContentView(findViewById(R.id.list));
    }

    public void setContentView(View contentView) {
        removeAllViews();

        fixedTabIndicator = new FixedTabIndicator(getContext());
        fixedTabIndicator.setId(R.id.fixedTabIndicator);
        addView(fixedTabIndicator, -1, Util.dp(getContext(), 50));

        LayoutParams params = new LayoutParams(-1, -1);
        params.addRule(BELOW, R.id.fixedTabIndicator);

        addView(contentView, params);

        frameLayoutContainer = new FrameLayout(getContext());
        frameLayoutContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black_p50));
        addView(frameLayoutContainer, params);

        frameLayoutContainer.setVisibility(GONE);

        initListener();
        initAnimation();
    }

    public void setMenuAdapter(MenuAdapter adapter, List<String> titles) {
        verifyContainer();

        mMenuAdapter = adapter;
        verifyMenuAdapter();

        fixedTabIndicator.setTitles(titles);

        setPositionView();
    }

    /**
     * 可以提供两种方式:
     * 1.缓存所有view,
     * 2.只保存当前view
     * <p/>
     * 此处选择第二种
     */
    public void setPositionView() {
        int count = mMenuAdapter.getMenuCount();
        for (int position = 0; position < count; ++position) {
            setPositionView(position, findViewAtPosition(position));
        }
    }

    public View findViewAtPosition(int position) {
        verifyContainer();

        View view = frameLayoutContainer.getChildAt(position);
        if (view == null) {
            view = mMenuAdapter.getView(position, frameLayoutContainer);
        }

        return view;
    }

    private void setPositionView(int position, View view) {
        verifyContainer();
        if (view == null || position > mMenuAdapter.getMenuCount() || position < 0) {
            throw new IllegalStateException("the view at " + position + " cannot be null");
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2);
        frameLayoutContainer.addView(view, position, params);
        view.setVisibility(GONE);
    }


    public boolean isShowing() {
        verifyContainer();
        return frameLayoutContainer.isShown();
    }

    public boolean isClosed() {
        return !isShowing();
    }

    public void close() {
        if (isClosed()) {
            return;
        }

        frameLayoutContainer.startAnimation(alphaDismissAnimation);
        fixedTabIndicator.resetCurrentPos();

        if (currentView != null) {
            currentView.startAnimation(dismissAnimation);
        }
    }


    public void setPositionIndicatorText(int position, String text) {
        verifyContainer();
        fixedTabIndicator.setPositionText(position, text);
    }

    public void setCurrentIndicatorText(String text) {
        verifyContainer();
        fixedTabIndicator.setCurrentText(text);
    }

    //=======================之上对外暴漏方法=======================================
    private void initListener() {
        frameLayoutContainer.setOnClickListener(v -> {
            if (isShowing()) {
                close();
            }
        });
        fixedTabIndicator.setOnItemClickListener(this::onItemClick);
    }

    public void onItemClick(View v, int position, boolean open) {
        if (open) {
            close();
        } else {
            currentView = frameLayoutContainer.getChildAt(position);

            if (currentView == null) {
                return;
            }
            frameLayoutContainer.getChildAt(fixedTabIndicator.getLastIndicatorPosition()).setVisibility(View.GONE);
            frameLayoutContainer.getChildAt(position).setVisibility(View.VISIBLE);
            if (isClosed()) {
                frameLayoutContainer.setVisibility(VISIBLE);
                frameLayoutContainer.startAnimation(alphaOccurAnimation);
                currentView.startAnimation(occurAnimation);
            }
        }
    }


    private void initAnimation() {
        occurAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.top_in);

        SimpleAnimationListener listener = new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                frameLayoutContainer.setVisibility(GONE);
            }
        };

        dismissAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.top_out);
        dismissAnimation.setAnimationListener(listener);


        alphaDismissAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_to_zero);
        alphaDismissAnimation.setDuration(300);
        alphaDismissAnimation.setAnimationListener(listener);

        alphaOccurAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_to_one);
        alphaOccurAnimation.setDuration(300);
    }

    private void verifyMenuAdapter() {
        if (mMenuAdapter == null) {
            throw new IllegalStateException("the menuAdapter is null");
        }
    }

    private void verifyContainer() {
        if (frameLayoutContainer == null) {
            throw new IllegalStateException("you must initiation setContentView() before");
        }
    }
}
