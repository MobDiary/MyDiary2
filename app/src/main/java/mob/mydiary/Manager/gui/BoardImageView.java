package mob.mydiary.Manager.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
public class BoardImageView extends android.support.v7.widget.AppCompatImageView {

    private Rect rect;
    private Paint paint ;

    public BoardImageView(Context context) {
        super(context);
        init(context, null, 0);

    }

    public BoardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BoardImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, 0);
    }


    private void init(Context context, AttributeSet attrs, int defStyle) {
        rect = new Rect();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(rect);

        rect.bottom--;
        rect.right--;
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawRect(rect, paint);
    }
}
