package mob.mydiary.Manager.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;

import mob.mydiary.R;
import mob.mydiary.Manager.ColorTools;
import mob.mydiary.Manager.ScreenHelper;
import mob.mydiary.Manager.ThemeManager;


public class MyDiaryImageButton extends android.support.v7.widget.AppCompatImageButton {


    public MyDiaryImageButton(Context context) {
        super(context);
    }

    public MyDiaryImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDiaryImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.setBackground(ThemeManager.getInstance().getButtonBgDrawable(getContext()));
        this.setColorFilter(ColorTools.getColor(getContext(), R.color.imagebutton_hint_color));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            this.setStateListAnimator(null);
        }
        this.setMinimumWidth(ScreenHelper.dpToPixel(getContext().getResources(), 80));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
