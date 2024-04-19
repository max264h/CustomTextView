package com.example.customtextview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private CustomTextView textView;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        dialog = new Dialog(MainActivity.this);

        textView.setTextIsSelectable(true);
        String articleText = "在清晨的晨光中，我我走進了森林。森林裡的樹樹苍翠，小鳥鳥在樹上歡快地歌唱。我我感受到了大自然的美麗，心情愉悅。突然間，我我看到了一隻小小的松鼠在樹上跳躍。我我停下腳步，靜靜地觀察著它。松鼠似乎察覺到了我的存在，轉過頭來，用它的小小眼睛瞪著我我小鳥鳥。我們相視了一會兒，然後松鼠咯咯地笑了。我我也跟著笑了起來。這是一段美好的時刻小鳥鳥，我我會永遠記得。";
        textView.setText(articleText);

        // 自定義的 TextView 點擊事件
        textView.setOnSelectionCompletedListener(new CustomTextView.OnSelectionCompletedListener() {
            @Override
            public void onSelectionCompleted() {
                showCustomSelectionView(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // 當 Dialog 消失後會呼叫更新選取文字的背景顏色
                        textView.updateSelectedText();
                        Log.e("test", "onDismiss: ");
                    }
                });
            }
        });
    }

    private void showCustomSelectionView(DialogInterface.OnDismissListener onDismissListener) {
        // 獲取選取的文字的 X、Y 座標
        int startIndex = textView.getSelectionPoint().x;
        int screenY = textView.getSelectionPoint().y;

        // 綁定 Dialog的畫面
        dialog.setContentView(R.layout.custom_dialog);
        // 設定當 Dialog 消失後的監聽器
        dialog.setOnDismissListener(onDismissListener);

        // 設定 Dialog 的視窗位置
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        // 設置 Dialog 的顯示位置在頂部，預設的 Y 座標 0 在畫面正中間所以把位置拉到最上方
        layoutParams.gravity = Gravity.TOP;
        layoutParams.x = startIndex;

        // 根據需要調整Dialog的Y座標
        if (screenY < 0) {
            layoutParams.y = screenY;
        } else {
            // 這個像素是體感像素，還要再測試過
            layoutParams.y = screenY - 350; // 向上偏移350像素
        }

        // 設定 Window 的參數並顯示 Dialog
        window.setAttributes(layoutParams);
        dialog.show();

        ImageView image1 = dialog.findViewById(R.id.imageView);
        ImageView image2 = dialog.findViewById(R.id.imageView2);
        ImageView image3 = dialog.findViewById(R.id.imageView3);
        ImageView image4 = dialog.findViewById(R.id.imageView4);
        ImageView image5 = dialog.findViewById(R.id.imageView5);

        image1.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "這是第一張圖片", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        image2.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "這是第二張圖片", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        image3.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "這是第三張圖片", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        image4.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "這是第四張圖片", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        image5.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "這是第五張圖片", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    public void touchMe(View view) {
        Log.e("test", "touchMe: ");
    }
}
