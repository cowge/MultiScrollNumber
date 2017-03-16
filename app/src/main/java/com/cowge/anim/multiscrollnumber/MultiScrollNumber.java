package com.cowge.anim.multiscrollnumber;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 多位数数字 分开滚动效果控件
 * 支持数字从大到小和从小到大
 */
public class MultiScrollNumber extends LinearLayout {
    private Context mContext;
    private List<Integer> mTargetNumbers = new ArrayList<>();
    private List<Integer> mPrimaryNumbers = new ArrayList<>();
    private List<ScrollNumber> mScrollNumbers = new ArrayList<>();
    private int mTextSize = 21;

    private int[] mTextColors = new int[]{Color.parseColor("#ee4f4f")};
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private String mFontFileName;
    private Typeface typeface;
    private int maxDifference = 0;
    private boolean isSp2Px = true;
    private int postDeplyScrollRunnableTime;

    public void setPostDeplyScrollRunnableTime(int postDeplyScrollRunnableTime) {
        this.postDeplyScrollRunnableTime = postDeplyScrollRunnableTime;
    }

    public MultiScrollNumber(Context context) {
        this(context, null);
    }

    public MultiScrollNumber(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiScrollNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MultiScrollNumber);
        int primaryNumber = typedArray.getInteger(R.styleable.MultiScrollNumber_primary_number, 0);
        int targetNumber = typedArray.getInteger(R.styleable.MultiScrollNumber_target_number, 0);
        int numberSize = typedArray.getInteger(R.styleable.MultiScrollNumber_number_size, 21);
        int defaultColor = typedArray.getColor(R.styleable.MultiScrollNumber_number_color, Color.parseColor("#ee4f4f"));
        mTextColors = new int[]{defaultColor};

        setNumber(primaryNumber, targetNumber);
        setTextSize(numberSize);

        typedArray.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    public void setNumber(int val) {
        resetView();

        int number = val;
        if (val == 0) {
            mTargetNumbers.add(0);
        } else {
            while (number > 0) {
                int i = number % 10;
                mTargetNumbers.add(i);
                number /= 10;
            }
        }
        maxDifference = 0;
        for (int i = mTargetNumbers.size() - 1; i >= 0; i--) {
            ScrollNumber scrollNumber = new ScrollNumber(mContext);
            LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            scrollNumber.setPadding(0, dip2px(mContext, 2), 0, dip2px(mContext, 2));
            scrollNumber.setLayoutParams(layoutParams);
            scrollNumber.setTextColor(mTextColors[i % mTextColors.length]);
            scrollNumber.setTextSize(mTextSize, isSp2Px);
            scrollNumber.setInterpolator(mInterpolator);
            scrollNumber.setPostDeplyScrollRunnableTime(postDeplyScrollRunnableTime);
            if (!TextUtils.isEmpty(mFontFileName))
                scrollNumber.setTextFont(mFontFileName);
            if (typeface != null) {
                scrollNumber.setTypeface(typeface);
            }
            scrollNumber.setNumber(0, mTargetNumbers.get(i), i * 10);
            if (mTargetNumbers.get(i) > maxDifference) {
                maxDifference = mTargetNumbers.get(i);
            }
            mScrollNumbers.add(scrollNumber);
            addView(scrollNumber);
        }
    }

    private void resetView() {
        mPrimaryNumbers.clear();
        mTargetNumbers.clear();
        mScrollNumbers.clear();
        removeAllViews();
    }


    public void setNumber(int from, int to) {
        boolean isReciprocal = false;
        if (to < from) {
            isReciprocal = true;
        }

        resetView();
        if (!isReciprocal) {
            // operate to
            int number = to, count = 0;
            if (to == 0) {
                mTargetNumbers.add(0);
                count++;
            } else {
                while (number > 0) {
                    int i = number % 10;
                    mTargetNumbers.add(i);
                    number /= 10;
                    count++;
                }
            }
            // operate from
            number = from;
            while (count > 0) {
                int i = number % 10;
                mPrimaryNumbers.add(i);
                number /= 10;
                count--;
            }
        } else {
            // operate to
            int number = from, count = 0;
            if (from == 0) {
                mTargetNumbers.add(0);
                count++;
            } else {
                while (number > 0) {
                    int i = number % 10;
                    mPrimaryNumbers.add(i);
                    number /= 10;
                    count++;
                }
            }
            // operate from
            number = to;
            while (count > 0) {
                int i = number % 10;
                mTargetNumbers.add(i);
                number /= 10;
                count--;
            }
        }
        boolean isGreaterZero = false;
        maxDifference = 0;
        for (int i = mTargetNumbers.size() - 1; i >= 0; i--) {
            ScrollNumber scrollNumber = new ScrollNumber(mContext);
            LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            scrollNumber.setPadding(0, dip2px(mContext, 2), 0, dip2px(mContext, 2));
            scrollNumber.setLayoutParams(layoutParams);
            scrollNumber.setTextColor(mTextColors[i % mTextColors.length]);
            scrollNumber.setTextSize(mTextSize, isSp2Px);
            if (!TextUtils.isEmpty(mFontFileName))
                scrollNumber.setTextFont(mFontFileName);
            if (typeface != null) {
                scrollNumber.setTypeface(typeface);
            }
            scrollNumber.setReciprocal(isReciprocal);
            if (isReciprocal && !isGreaterZero && mTargetNumbers.get(i) == 0 && i != 0) {
                scrollNumber.setZeroNoDraw(true);
            } else if (isReciprocal && mTargetNumbers.get(i) > 0) {
                isGreaterZero = true;
            }
            scrollNumber.setNumber(mPrimaryNumbers.get(i), mTargetNumbers.get(i), i * 10);
            scrollNumber.setPostDeplyScrollRunnableTime(postDeplyScrollRunnableTime);
            if (mTargetNumbers.get(i) - mPrimaryNumbers.get(i) > maxDifference) {
                maxDifference = mTargetNumbers.get(i) - mPrimaryNumbers.get(i);
            }
            mScrollNumbers.add(scrollNumber);
            addView(scrollNumber);
        }

    }

    //获取动画大概结束的时间
    public int getAnimStopDuration() {
        switch (maxDifference) {
            case 9:
                return 3097;
            case 8:
                return 2763;
            case 7:
                return 2396;
            case 6:
                return 2072;
            case 5:
                return 1737;
            case 4:
                return 1396;
            case 3:
                return 1071;
            case 2:
                return 829;
            case 1:
                return 629;
        }
        return 0;
    }

    public void setTextColors(int[] textColors) {
        if (textColors == null || textColors.length == 0)
            throw new IllegalArgumentException("color array couldn't be empty!");
        mTextColors = textColors;
        for (int i = mScrollNumbers.size() - 1; i >= 0; i--) {
            ScrollNumber scrollNumber = mScrollNumbers.get(i);
            scrollNumber.setTextColor(mTextColors[i % mTextColors.length]);
        }
    }

    public void setTextSize(int textSize, boolean isSp2Px) {
        if (textSize <= 0) throw new IllegalArgumentException("text size must > 0!");
        this.isSp2Px = isSp2Px;
        mTextSize = textSize;
        for (ScrollNumber s : mScrollNumbers) {
            s.setTextSize(textSize, isSp2Px);
        }
    }

    public void setTextSize(int textSize) {
        setTextSize(textSize, true);
    }

    public void setInterpolator(Interpolator interpolator) {
        if (interpolator == null)
            throw new IllegalArgumentException("interpolator couldn't be null");
        mInterpolator = interpolator;
        for (ScrollNumber s : mScrollNumbers) {
            s.setInterpolator(interpolator);
        }
    }

    /**
     * 必须放到assets目录下，建议用 setTypeface，避免内存的多余创建
     *
     * @param fileName
     */
    @Deprecated
    public void setTextFont(String fileName) {
        if (TextUtils.isEmpty(fileName)) throw new IllegalArgumentException("file name is null");
        mFontFileName = fileName;
        for (ScrollNumber s : mScrollNumbers) {
            s.setTextFont(fileName);
        }
    }

    /**
     * 设置字体样式
     *
     * @param typeface
     */
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        for (ScrollNumber s : mScrollNumbers) {
            s.setTypeface(typeface);
        }
    }
    int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
