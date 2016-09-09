package com.szzgkon.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zhangyongke on 16/9/9.
 */

/**
 * 获取焦点的textView
 */
public class FocusedTextView extends TextView {

    //用代码new对象时，走这个构造方法
    public FocusedTextView(Context context) {
        super(context);
    }
    //有属性时候走这个构造方法
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //有style样式的时候走这个构造方法
    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 表示有没有获取焦点
     * @return
     * 跑马灯要运行，首先要用此函数是否有焦点，是true的话，跑马灯才会有效果
     * 不管实际上textview有无焦点，都返回true，让跑马灯认为有焦点
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
