package cn.plateform.I18Util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 说明
 *
 * @Description : 描述
 * @Author : songHuaYu
 * @Date: 2020/08/13
 */
public enum  McmsLanguage {

    EN("english", "英语", McmsLocale.UNITED_STATES, "en"),
    ZH("Simplified Chinese", "简体中文", McmsLocale.SIMPLIFIED_CHINESE, "zh"),
    ZT("Traditional Chinese", "繁体中文", McmsLocale.TRADITIONAL_CHINESE, "zt"),
    ZTH("Traditional Chinese(HK)", "繁体中文(香港)", McmsLocale.TRADITIONAL_CHINESE_HK, "zth"),
    ES("spanish", "西班牙语", McmsLocale.SPAIN, "es"),
    PT("portuguese", "葡萄牙语", McmsLocale.PORTUGAL, "pt"),
    FR("french", "法语", McmsLocale.FRANCE, "fr"),
    DE("german", "德语", McmsLocale.GERMANY, "de"),
    IT("italian", "意大利语", McmsLocale.ITALY, "it"),
    RU("russian", "俄语", McmsLocale.RUSSIA, "ru"),
    JA("japanese", "日语", McmsLocale.JAPAN, "ja"),
    KO("korean", "韩语", McmsLocale.KOREA, "ko"),
    AR("arabic", "阿拉伯语", McmsLocale.SAUDI_ARABIA, "ar"),
    TR("turkish", "土耳其语", McmsLocale.TURKEY, "tr"),
    TH("thai", "泰语", McmsLocale.THAILAND, "th"),
    VI("vietnamese", "越南语", McmsLocale.VIETNAM, "vi"),
    NL("dutch", "荷兰语", McmsLocale.NETHERLANDS, "nl"),
    HE("hebrew", "希伯来语", McmsLocale.ISRAEL, "he"),
    ID("indonesian", "印尼语", McmsLocale.INDONESIA, "id"),
    PL("polish", "波兰语", McmsLocale.POLAND, "pl"),
    HI("hindi", "印地语", McmsLocale.INDIA, "hi"),
    UK("ukrainian", "乌克兰语", McmsLocale.UKRAINE, "uk"),
    MS("malay", "马来语", McmsLocale.MALAYSIA, "ms"),
    TL("filipino", "菲律宾语", McmsLocale.PHILIPPINES, "tl"),
    PSEUDO_KEY("pseudo_key", "伪语种", McmsLocale.PSEUDO_KEY, "pseudo_key"),
    SG("singapore english ", "新加坡英语", McmsLocale.SINGAPORE, "sg"),
    ENTH("thaiEnglish ", "泰国英语", McmsLocale.THAIENGLISH, "enTH"),
    ENPH("philippinesEnglish ", "菲律宾英语", McmsLocale.PHILIPPINESENGLISH, "enPH"),
    ENMY("malaysiaEnglish ", "马来西亚英语", McmsLocale.MALAYSIAENGLISH, "enMY");

    private static final Map<Locale, McmsLanguage> LOCALE_LANGUAGE_MAP;
    private static List<String> LANGUAGE_LIST = new ArrayList(values().length);
    String value;
    String name;
    Locale locale;
    String lang;

    private McmsLanguage(String value) {
        this.value = value;
    }

    private McmsLanguage(String value, String name) {
        this.value = value;
        this.name = name;
    }

    private McmsLanguage(String value, String name, Locale locale, String lang) {
        this.value = value;
        this.name = name;
        this.locale = locale;
        this.lang = lang;
    }

    public static List<String> getValues() {
        return LANGUAGE_LIST;
    }

    public static McmsLanguage fromValue(String language) {
        if (StringUtils.isEmpty(language)) {
            return null;
        } else {
            McmsLanguage[] arr$ = values();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                McmsLanguage i18nresourcesLanguage = arr$[i$];
                if (StringUtils.equalsIgnoreCase(i18nresourcesLanguage.name(), language) || StringUtils.equalsIgnoreCase(i18nresourcesLanguage.getValue(), language) || StringUtils.equalsIgnoreCase(i18nresourcesLanguage.getLocale().toString(), language)) {
                    return i18nresourcesLanguage;
                }
            }

            if (StringUtils.endsWithIgnoreCase(language, "iw")) {
                return HE;
            } else if (StringUtils.endsWithIgnoreCase(language, "in")) {
                return ID;
            } else {
                Locale locale = McmsLocale.fromString(language);
                if (locale == null && language.length() > 2) {
                    locale = McmsLocale.fromString(language.substring(0, 2));
                }

                return fromLocale(locale);
            }
        }
    }

    public static McmsLanguage fromLocale(Locale locale) {
        if (locale == null) {
            return null;
        } else {
            McmsLanguage language = (McmsLanguage)LOCALE_LANGUAGE_MAP.get(locale);
            return language == null ? (McmsLanguage)LOCALE_LANGUAGE_MAP.get(new Locale(locale.getLanguage())) : language;
        }
    }

    public String getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getLang() {
        return this.lang;
    }

    static {
        McmsLanguage[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            McmsLanguage i18nresourcesLanguage = arr$[i$];
            LANGUAGE_LIST.add(i18nresourcesLanguage.getValue());
        }

        LOCALE_LANGUAGE_MAP = new HashMap();
        LOCALE_LANGUAGE_MAP.put(McmsLocale.ENGLISH, EN);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.UNITED_STATES, EN);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.SIMPLIFIED_CHINESE, ZH);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.CHINA, ZH);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.TRADITIONAL_CHINESE, ZT);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.TAIWAN, ZT);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.TRADITIONAL_CHINESE_HK, ZTH);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.HONGKONG, ZTH);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.GERMAN, DE);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.GERMANY, DE);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.SPANISH, ES);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.SPAIN, ES);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.FRENCH, FR);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.FRANCE, FR);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.ITALIAN, IT);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.ITALY, IT);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.PORTUGUESE, PT);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.PORTUGAL, PT);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.BRAZIL, PT);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.RUSSIAN, RU);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.RUSSIA, RU);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.JAPANESE, JA);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.JAPAN, JA);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.ARABIC, AR);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.SAUDI_ARABIA, AR);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.KOREAN, KO);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.KOREA, KO);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.TURKISH, TR);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.TURKEY, TR);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.THAI, TH);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.THAILAND, TH);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.VIETNAMESE, VI);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.VIETNAM, VI);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.DUTCH, NL);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.NETHERLANDS, NL);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.HEBREW, HE);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.ISRAEL, HE);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.INDONESIAN, ID);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.INDONESIA, ID);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.POLISH, PL);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.POLAND, PL);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.HINDI, HI);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.INDIA, HI);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.UKRAINE, UK);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.UKRAINIAN, UK);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.MALAY, MS);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.MALAYSIA, MS);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.FILIPINO, TL);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.PHILIPPINES, TL);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.PSEUDO_KEY, PSEUDO_KEY);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.SINGAPORE, SG);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.THAIENGLISH, ENTH);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.PHILIPPINESENGLISH, ENPH);
        LOCALE_LANGUAGE_MAP.put(McmsLocale.MALAYSIAENGLISH, ENMY);
    }


}
