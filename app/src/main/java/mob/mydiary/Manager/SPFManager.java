package mob.mydiary.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import mob.mydiary.R;


public class SPFManager {

    /**
     * config
     */
    private static final String SPF_CONFIG = "CONFIG";
    //Local language
    private static final String CONFIG_LOCAL_LANGUAGE = "CONFIG_LOCAL_LANGUAGE";
    /**
     * profile
     */
    private static final String SPF_PROFILE = "PROFILE";
    private static final String PROFILE_YOUR_NAME_IS = "YOUR_NAME_IS";
    private static final String PROFILE_MAIN_PAGE_BANNER_BG = "PROFILE_MAIN_PAGE_BANNER_BG";

    /**
     * Theme
     */
    //Support old version: CONFIG - CONFIG_THEME
    private static final String CONFIG_THEME = "CONFIG_THEME";
    //Theme SFP setting
    private static final String SPF_THEME = "THEME";
    private static final String THEME_MAIN_COLOR = "THEME_MAIN_COLOR";
    private static final String THEME_SEC_COLOR = "THEME_SEC_COLOR";

    /**
     * System
     */
    private static final String SPF_SYSTEM = "SYSTEM";
    //@deprecated
    private static final String FIRST_RUN = "FIRST_RUN";
    private static final String SYSTEM_VERSIONCODE = "VERSIONCODE";
    public static final int DEFAULT_VERSIONCODE = -1;
    private static final String DESCRIPTION_CLOSE = "DESCRIPTION_CLOSE";
    private static final String ENCRYPTED_PASSWORD = "ENCRYPTED_PASSWORD";

    /**
     * OOBE:
     * Add in  Version 33 , Not use now.
     */
    private static final String SPF_OOBE = "OOBE";

    /**
     * Diary auto save
     */
    private static final String SPF_DIARY = "DIARY";
    //The json file like the backup file
    private static final String DIARY_AUTO_SAVE = "DIARY_AUTO_SAVE_";

    /**
     * Config method
     */

    public static int getLocalLanguageCode(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SPF_CONFIG, 0);
        //default is 0 , follow the system
        return settings.getInt(CONFIG_LOCAL_LANGUAGE, 0);
    }

    public static void setLocalLanguageCode(Context context, int languageCode) {
        SharedPreferences settings = context.getSharedPreferences(SPF_CONFIG, 0);
        SharedPreferences.Editor PE = settings.edit();
        PE.putInt(CONFIG_LOCAL_LANGUAGE, languageCode);
        PE.commit();
    }

    /**
     * Profile method
     */

    public static String getYourName(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SPF_PROFILE, 0);
        //default is space
        return settings.getString(PROFILE_YOUR_NAME_IS, "");
    }

    public static void setYourName(Context context, String yourNameIs) {
        SharedPreferences settings = context.getSharedPreferences(SPF_PROFILE, 0);
        SharedPreferences.Editor PE = settings.edit();
        PE.putString(PROFILE_YOUR_NAME_IS, yourNameIs);
        PE.commit();
    }

    public static boolean hasCustomProfileBannerBg(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SPF_PROFILE, 0);
        //default is space
        return settings.getBoolean(PROFILE_MAIN_PAGE_BANNER_BG, false);
    }

    public static void setCustomProfileBannerBg(Context context, boolean customProfileBg) {
        SharedPreferences settings = context.getSharedPreferences(SPF_PROFILE, 0);
        SharedPreferences.Editor PE = settings.edit();
        PE.putBoolean(PROFILE_MAIN_PAGE_BANNER_BG, customProfileBg);
        PE.commit();
    }

    /**
     * Theme method
     */

    public static int getTheme(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SPF_CONFIG, 0);
        //default is close
        return settings.getInt(CONFIG_THEME, 0);
    }

    public static void setTheme(Context context, int theme) {
        SharedPreferences settings = context.getSharedPreferences(SPF_CONFIG, 0);
        SharedPreferences.Editor PE = settings.edit();
        PE.putInt(CONFIG_THEME, theme);
        PE.commit();
    }

    public static int getMainColor(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SPF_THEME, 0);
        //default is space
        return settings.getInt(THEME_MAIN_COLOR,
                context.getResources().getColor(R.color.themeColor_custom_default));
    }
    public static int getSecondaryColor(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SPF_THEME, 0);
        //default is space
        return settings.getInt(THEME_SEC_COLOR,
                context.getResources().getColor(R.color.themeColor_custom_default));
    }


    /**
     * System method
     */

    /**
     * @param context
     * @param firstRun
     * @deprecated it after version 33
     * now use ShowcaseView - singleShot to run OOBE onve.
     */
    public static void setFirstRun(Context context, boolean firstRun) {
        SharedPreferences settings = context.getSharedPreferences(SPF_SYSTEM, 0);
        SharedPreferences.Editor PE = settings.edit();
        PE.putBoolean(FIRST_RUN, firstRun);
        PE.commit();
    }

    /**
     * @param context
     * @return
     * @deprecated it after version 33
     * now use ShowcaseView - singleShot to run OOBE onve.
     */
    public static boolean getFirstRun(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SPF_SYSTEM, 0);
        return settings.getBoolean(FIRST_RUN, true);
    }



}
