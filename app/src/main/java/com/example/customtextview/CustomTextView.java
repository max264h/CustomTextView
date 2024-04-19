package com.example.customtextview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    // 自定義 TextView
    private int startOffset = -1;
    private int endOffset = -1;
    // 跟選取字串的背景顏色相關
    private BackgroundColorSpan backgroundColorSpan;

    // 對外的監聽器
    private OnSelectionCompletedListener onSelectionCompletedListener;

    public void setOnSelectionCompletedListener(OnSelectionCompletedListener listener) {
        this.onSelectionCompletedListener = listener;
    }
    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 設定選取的字串的背景顏色
        backgroundColorSpan = new BackgroundColorSpan(Color.YELLOW);
    }

    public interface OnSelectionCompletedListener {
        void onSelectionCompleted();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 獲取動作類型
        int action = event.getAction();
        Layout layout = getLayout();

        switch (action) {
            // 當按下 TextView 做得事情
            case MotionEvent.ACTION_DOWN:
                // 獲取按下位置的 X 和 Y 座標
                int startX = (int) event.getX();
                int startY = (int) event.getY();
                // 獲取按下位置所在該行的字元偏移量
                startOffset = layout.getOffsetForHorizontal(layout.getLineForVertical(startY), startX);
                endOffset = startOffset;
                break;

            // 當按下 TextView 並滑動時做得事情
            case MotionEvent.ACTION_MOVE:
                // 獲取按下位置的 X 和 Y 座標
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                // 獲取按下位置所在該行的字元偏移量
                endOffset = layout.getOffsetForHorizontal(layout.getLineForVertical(endY), endX);
                // 更新選取字串的背景顏色
                updateSelectedText();
                break;

            // 沒在按 TextView 做得事情
            case MotionEvent.ACTION_UP:
                // 更新選取字串的背景顏色
                updateSelectedText();
                // 當點擊事件背觸發且有選取文字的時候才會呼叫 onSelectionCompleted() 顯示 Dialog
                if (onSelectionCompletedListener != null && !TextUtils.isEmpty(getSelectedText())) {
                    onSelectionCompletedListener.onSelectionCompleted();
                }
                startOffset = -1;
                endOffset = -1;
                break;
        }
        // 返回 true 結束觸摸事件
        return true;
    }

    public void updateSelectedText() {
        // 更新選取的文字的背景顏色
        // 先獲取文字，用 Spannable 來調整指定字串的背景顏色
        Spannable spannable = new SpannableString(getText());

        if (startOffset != -1 && endOffset != -1) {
            // 如果 startOffset 和 endOffset 都不為 -1,設置背景顏色
            int selectionStart = Math.min(startOffset, endOffset);
            int selectionEnd = Math.max(startOffset, endOffset);
            spannable.setSpan(backgroundColorSpan, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Selection.setSelection(spannable, selectionEnd);
        } else {
            // 如果 startOffset 或 endOffset 為 -1,移除背景顏色
            spannable.removeSpan(backgroundColorSpan);
            Selection.removeSelection(spannable);
        }

        setText(spannable);
    }
    public Point getSelectionPoint() {
        // 獲取 選取文字的 X 和 Y 座標
        if (startOffset != -1 && endOffset != -1) {
            int selectionStart = Math.min(startOffset, endOffset);

            Layout layout = getLayout();
            int line = layout.getLineForOffset(selectionStart);
            int relativeY = layout.getLineTop(line);

            int[] location = new int[2];
            getLocationOnScreen(location);

            int screenX = (int) layout.getPrimaryHorizontal(selectionStart) + location[1];
            int screenY = relativeY + location[1];

            return new Point(screenX, screenY);
        }
        return null;
    }

    private String getSelectedText() {
        // 獲取選取的文字
        if (startOffset != -1 && endOffset != -1) {
            int selectionStart = Math.min(startOffset, endOffset);
            int selectionEnd = Math.max(startOffset, endOffset);
            return getText().subSequence(selectionStart, selectionEnd).toString();
        }
        return "";
    }
}
