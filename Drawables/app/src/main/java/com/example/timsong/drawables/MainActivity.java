package com.example.timsong.drawables;

import android.animation.ObjectAnimator;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void transitionDrawable(View view) {
        // Transition the clicked view's background drawable:)
        Drawable backgroundDrawable = view.getBackground();
        if (backgroundDrawable instanceof TransitionDrawable) {
            ((TransitionDrawable) backgroundDrawable).startTransition(1000);
        }
    }

    public void progressForward(View view) {
        // Retrieve the reference for my progress view:)
        View progressView = findViewById(R.id.layerDrawableView);
        LayerDrawable background = (LayerDrawable) progressView.getBackground();
        ClipDrawable clipLayer = (ClipDrawable) background.findDrawableByLayerId(R.id.clipDrawableLayer);
        if (clipLayer.getLevel() < 10000) {
            clipLayer.setLevel(clipLayer.getLevel() + 1000);
        } else {
            Toast.makeText(this, "Mission completed already:)", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.multi_layers_menu, menu);
        return true;
    }

    public void onGroupItemClick(MenuItem item) {
        Toast.makeText(this, item.getTitle() + " is clicked.", Toast.LENGTH_SHORT).show();
        if (item.isCheckable()) {
            item.setChecked(!item.isChecked());
            // Change the MenuItem's checked state manually, which is necessary. Note it's only persist for per-session basis, which means it will not be stored after activity destroyed.
        }
    }

    public void showCustomizedToast(View view) {
        View toastViewRoot = getLayoutInflater().inflate(R.layout.custom_toast_layout, null);
        TextView toastTextView = (TextView) toastViewRoot.findViewById(R.id.toast_text_view);
        toastTextView.setText("You see? I am customized:)");

        Toast customizedToast = new Toast(this); // Do not use this constructor unless you need to customize the view.
        customizedToast.setView(toastViewRoot);
        customizedToast.setDuration(Toast.LENGTH_LONG);
        customizedToast.setGravity(Gravity.END | Gravity.BOTTOM, 0, 150);
        customizedToast.show();
    }

    public void rotate360(View view) {
        ObjectAnimator.ofFloat(view, "rotation", 0f, 360f).setDuration(5000).start();
    }
}
