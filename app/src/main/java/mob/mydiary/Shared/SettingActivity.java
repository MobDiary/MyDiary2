package mob.mydiary.Shared;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import mob.mydiary.R;
import mob.mydiary.Shared.ColorTools;
import mob.mydiary.Shared.FileManager;
import mob.mydiary.Shared.SPFManager;
import mob.mydiary.Shared.ScreenHelper;
import mob.mydiary.Shared.ThemeManager;
import mob.mydiary.Shared.PhoneHelper;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    /**
     * Theme
     */
    private ThemeManager themeManager;
    //For spinner first run
    private boolean isThemeFirstRun = true;
    private boolean isLanguageFirstRun = true;
    //Because the default profile bg is color ,
    //so we should keep main color for replace when main color was changed.
    private int tempMainColorCode;
    /**
     * Profile
     */
    private String profileBgFileName = "";
    private boolean isAddNewProfileBg = false;
    /**
     * File
     */
    private FileManager tempFileManager;
    private final static int SELECT_PROFILE_BG = 0;

    /**
     * UI
     */
    private Spinner SP_setting_theme, SP_setting_language;
    private ImageView IV_setting_profile_bg, IV_setting_theme_main_color, IV_setting_theme_dark_color;
    private Button But_setting_theme_default_bg, But_setting_theme_default, But_setting_theme_apply;
    private Button But_setting_fix_photo_17_dir;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        //For set status bar
        PhoneHelper.setStatusBar(this, true);


        themeManager = ThemeManager.getInstance();
        //Create fileManager for get temp folder
        tempFileManager = new FileManager(this, FileManager.TEMP_DIR);
        tempFileManager.clearDir();

        But_setting_theme_apply.setOnClickListener(this);


        initTheme(themeManager.getCurrentTheme());
        initLanguage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PROFILE_BG) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {
                    //Compute the bg size
                    int bgWidth = ScreenHelper.getScreenWidth(this);
                    int bgHeight = getResources().getDimensionPixelOffset(R.dimen.top_bar_height);
                    UCrop.Options options = new UCrop.Options();
                    options.setToolbarColor(ThemeManager.getInstance().getThemeMainColor(this));
                    options.setStatusBarColor(ThemeManager.getInstance().getThemeDarkColor(this));
                    UCrop.of(data.getData(), Uri.fromFile(new File(tempFileManager.getDir() + "/" + FileManager.createRandomFileName())))
                            .withMaxResultSize(bgWidth, bgHeight)
                            .withOptions(options)
                            .withAspectRatio(bgWidth, bgHeight)
                            .start(this);
                } else {
                    Toast.makeText(this, getString(R.string.toast_photo_intent_error), Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri resultUri = UCrop.getOutput(data);
                    IV_setting_profile_bg.setImageBitmap(BitmapFactory.decodeFile(resultUri.getPath()));
                    profileBgFileName = FileManager.getFileNameByUri(this, resultUri);
                    isAddNewProfileBg = true;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Revert current theme
        themeManager.setCurrentTheme(SPFManager.getTheme(this));
    }


    private void initLanguage() {
        if (SPFManager.getLocalLanguageCode(this) != -1) {
            SP_setting_language.setSelection(SPFManager.getLocalLanguageCode(this));
        }
    }

    private void initTheme(int themeId) {
        if (themeId == ThemeManager.CUSTOM) {
            IV_setting_profile_bg.setOnClickListener(this);
            IV_setting_theme_main_color.setOnClickListener(this);
            IV_setting_theme_dark_color.setOnClickListener(this);

            But_setting_theme_default_bg.setOnClickListener(this);
            But_setting_theme_default_bg.setEnabled(true);
            But_setting_theme_default.setOnClickListener(this);
            But_setting_theme_default.setEnabled(true);

            IV_setting_profile_bg.setImageBitmap(null);
        } else {
            IV_setting_profile_bg.setOnClickListener(null);
            IV_setting_theme_main_color.setOnClickListener(null);
            IV_setting_theme_dark_color.setOnClickListener(null);

            But_setting_theme_default_bg.setOnClickListener(null);
            But_setting_theme_default_bg.setEnabled(false);
            But_setting_theme_default.setOnClickListener(null);
            But_setting_theme_default.setEnabled(false);
        }
        //Save the temp Main Color Code
        tempMainColorCode = themeManager.getThemeMainColor(this);
        setThemeColor();
    }

    private void setThemeColor() {
        IV_setting_theme_main_color.setImageDrawable(
                new ColorDrawable(themeManager.getThemeMainColor(this)));
        IV_setting_theme_dark_color.setImageDrawable(
                new ColorDrawable(themeManager.getThemeDarkColor(this)));
    }

    @Override
    public void onClick(View v) {
        IV_setting_profile_bg.setImageDrawable(new ColorDrawable(tempMainColorCode));
        profileBgFileName = "";
        isAddNewProfileBg = true;
        IV_setting_theme_main_color.setImageDrawable(new ColorDrawable(ColorTools.getColor(this,
                R.color.themeColor_custom_default)));
        IV_setting_theme_dark_color.setImageDrawable(new ColorDrawable(ColorTools.getColor(this,
                R.color.theme_dark_color_custom_default)));
        //Also revert the tempMainColor & profile bg
        tempMainColorCode = ColorTools.getColor(this, R.color.themeColor_custom_default);
        if (IV_setting_profile_bg.getDrawable() instanceof ColorDrawable) {
            IV_setting_profile_bg.setImageDrawable(new ColorDrawable(tempMainColorCode));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!isThemeFirstRun) {
            //Temp set currentTheme .
            //If it doesn't apply , revert it on onDestroy .
            themeManager.setCurrentTheme(position);
            initTheme(position);
        } else {
            //First time do nothing
            isThemeFirstRun = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }
}
