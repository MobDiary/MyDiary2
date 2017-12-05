package mob.mydiary.Manager;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.media.RatingCompat;

import mob.mydiary.R;
import java.io.File;

public class ThemeManager {
    public final static int CUSTOM = 0;

    public final static String CUSTOM_TOPIC_BG_FILENAME = "custom_topic_bg";

    public int currentTheme = CUSTOM;

    private static ThemeManager instance = null;

    private ThemeManager() {
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            synchronized (ThemeManager.class) {
                if (instance == null) {
                    instance = new ThemeManager();
                }
            }
        }
        return instance;
    }

    public void saveTheme(Context context, int themeId) {
        SPFManager.setTheme(context, themeId);
    }

    public void setCurrentTheme(int themeBySPF) {
        this.currentTheme = themeBySPF;
    }

    public int getCurrentTheme() {

        return currentTheme;
    }

    public Drawable getTopicItemSelectDrawable(Context context) {
        return createTopicItemSelectBg(context);
    }

    private Drawable createTopicItemSelectBg(Context context) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                new ColorDrawable(getThemeMainColor(context)));
        stateListDrawable.addState(new int[]{},
                new ColorDrawable(Color.WHITE));
        return stateListDrawable;
    }

    /**
     * Any theme using the same topic bg , if it exist.
     *
     * @param context
     * @return
     */


    public Drawable getEntriesBgDrawable(Context context) {
        Drawable bgDrawable;
        FileManager diaryFM = new FileManager(context, FileManager.DIARY_ROOT_DIR);
        File entriesBg = new File(
                diaryFM.getDirAbsolutePath()
                        + "/" + CUSTOM_TOPIC_BG_FILENAME);
        if (entriesBg.exists()) {
            bgDrawable = Drawable.createFromPath(entriesBg.getAbsolutePath());
        } else {
            bgDrawable = ViewTools.getDrawable(context, R.drawable.background);
        }
        return bgDrawable;
    }

    private Drawable getEntriesBgDefaultDrawable(Context context) {
        Drawable defaultBgDrawable;
        defaultBgDrawable = new ColorDrawable(SPFManager.getMainColor(context));
        return defaultBgDrawable;
    }


    public Drawable getButtonBgDrawable(Context context) {
        return createButtonCustomBg(context);
    }

    public Drawable createDiaryViewerInfoBg(Context context) {
        int dp10 = ScreenHelper.dpToPixel(context.getResources(), 10);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(getThemeMainColor(context));
        shape.setCornerRadii(new float[]{dp10, dp10, dp10, dp10, 0, 0, 0, 0});
        return shape;
    }

    public Drawable createDiaryViewerEditBarBg(Context context) {
        int dp10 = ScreenHelper.dpToPixel(context.getResources(), 10);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(getThemeMainColor(context));
        shape.setCornerRadii(new float[]{0, 0, 0, 0, dp10, dp10, dp10, dp10});
        return shape;
    }

    /**
     * Create the custom button programmatically
     *
     * @param context
     * @return
     */
    private Drawable createButtonCustomBg(Context context) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, createCustomPressedDrawable(context));
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled},
                ViewTools.getDrawable(context, R.drawable.button_bg_disable));
        stateListDrawable.addState(new int[]{},
                ViewTools.getDrawable(context, R.drawable.button_bg_n));
        return stateListDrawable;
    }

    /**
     * The Custom button press drawable
     *
     * @param context
     * @return
     */
    private Drawable createCustomPressedDrawable(Context context) {
        int padding = ScreenHelper.dpToPixel(context.getResources(), 5);
        int mainColorCode = ThemeManager.getInstance().getThemeMainColor(context);
        int boardColor = ColorTools.getColor(context, R.color.button_boarder_color);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.getPadding(new Rect(padding, padding, padding, padding));
        gradientDrawable.setCornerRadius(ScreenHelper.dpToPixel(context.getResources(), 3));
        gradientDrawable.setStroke(ScreenHelper.dpToPixel(context.getResources(), 1), boardColor);
        gradientDrawable.setColor(mainColorCode);
        return gradientDrawable;
    }

    /**
     * This color also is secondary color.
     *
     * @param context
     * @return
     */
    public int getThemeDarkColor(Context context) {
        int darkColor;
        darkColor = SPFManager.getSecondaryColor(context);

        return darkColor;
    }

    public int getThemeMainColor(Context context) {
        int mainColor;

        mainColor = SPFManager.getMainColor(context);

        return mainColor;
    }
}