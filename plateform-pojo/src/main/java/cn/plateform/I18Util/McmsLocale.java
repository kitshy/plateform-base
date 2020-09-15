package cn.plateform.I18Util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 说明
 *
 * @Description : 描述
 * @Author : songHuaYu
 * @Date: 2020/08/13
 */
public class McmsLocale {

    public static final Locale ENGLISH;
    public static final Locale UNITED_STATES;
    public static final Locale SIMPLIFIED_CHINESE;
    public static final Locale CHINA;
    public static final Locale TRADITIONAL_CHINESE;
    public static final Locale TAIWAN;
    public static final Locale TRADITIONAL_CHINESE_HK;
    public static final Locale HONGKONG;
    public static final Locale GERMANY;
    public static final Locale GERMAN;
    public static final Locale SPANISH;
    public static final Locale SPAIN;
    public static final Locale FRENCH;
    public static final Locale FRANCE;
    public static final Locale ITALIAN;
    public static final Locale ITALY;
    public static final Locale PORTUGUESE;
    public static final Locale PORTUGAL;
    public static final Locale BRAZIL;
    public static final Locale RUSSIAN;
    public static final Locale RUSSIA;
    public static final Locale JAPANESE;
    public static final Locale JAPAN;
    public static final Locale ARABIC;
    public static final Locale SAUDI_ARABIA;
    public static final Locale KOREAN;
    public static final Locale KOREA;
    public static final Locale TURKISH;
    public static final Locale TURKEY;
    public static final Locale THAI;
    public static final Locale THAILAND;
    public static final Locale THAIENGLISH;
    public static final Locale VIETNAMESE;
    public static final Locale VIETNAM;
    public static final Locale DUTCH;
    public static final Locale NETHERLANDS;
    public static final Locale HEBREW;
    public static final Locale ISRAEL;
    public static final Locale HEBREW_NEW;
    public static final Locale INDONESIAN;
    public static final Locale INDONESIA;
    public static final Locale POLISH;
    public static final Locale POLAND;
    public static final Locale HINDI;
    public static final Locale INDIA;
    public static final Locale UKRAINIAN;
    public static final Locale UKRAINE;
    public static final Locale MALAY;
    public static final Locale MALAYSIA;
    public static final Locale MALAYSIAENGLISH;
    public static final Locale FILIPINO;
    public static final Locale PHILIPPINES;
    public static final Locale PHILIPPINESENGLISH;
    public static final Locale SINGAPORE;
    public static final Locale PSEUDO_KEY;
    private static final Map<String, Locale> LOCALE_MAP;

    public McmsLocale() {
    }

    public static Locale fromString(String localeString) {
        return localeString == null ? null : (Locale)LOCALE_MAP.get(localeString);
    }

    public static Map<String, Locale> getLocaleMap() {
        return LOCALE_MAP;
    }

    static {
        ENGLISH = Locale.ENGLISH;
        UNITED_STATES = Locale.US;
        SIMPLIFIED_CHINESE = Locale.SIMPLIFIED_CHINESE;
        CHINA = Locale.SIMPLIFIED_CHINESE;
        TRADITIONAL_CHINESE = Locale.TRADITIONAL_CHINESE;
        TAIWAN = Locale.TRADITIONAL_CHINESE;
        TRADITIONAL_CHINESE_HK = new Locale("zh", "HK");
        HONGKONG = new Locale("zh", "HK");
        GERMANY = Locale.GERMANY;
        GERMAN = Locale.GERMAN;
        SPANISH = new Locale("es", "");
        SPAIN = new Locale("es", "ES");
        FRENCH = Locale.FRENCH;
        FRANCE = Locale.FRANCE;
        ITALIAN = Locale.ITALIAN;
        ITALY = Locale.ITALY;
        PORTUGUESE = new Locale("pt", "");
        PORTUGAL = new Locale("pt", "PT");
        BRAZIL = new Locale("pt", "BR");
        RUSSIAN = new Locale("ru", "");
        RUSSIA = new Locale("ru", "RU");
        JAPANESE = Locale.JAPANESE;
        JAPAN = Locale.JAPAN;
        ARABIC = new Locale("ar", "");
        SAUDI_ARABIA = new Locale("ar", "SA");
        KOREAN = Locale.KOREAN;
        KOREA = Locale.KOREA;
        TURKISH = new Locale("tr", "");
        TURKEY = new Locale("tr", "TR");
        THAI = new Locale("th", "");
        THAILAND = new Locale("th", "TH");
        THAIENGLISH = new Locale("en", "TH");
        VIETNAMESE = new Locale("vi", "");
        VIETNAM = new Locale("vi", "VN");
        DUTCH = new Locale("nl", "");
        NETHERLANDS = new Locale("nl", "NL");
        HEBREW = new Locale("iw", "");
        ISRAEL = new Locale("iw", "IL");
        HEBREW_NEW = new Locale("iw", "HE");
        INDONESIAN = new Locale("in", "");
        INDONESIA = new Locale("in", "ID");
        POLISH = new Locale("pl", "");
        POLAND = new Locale("pl", "PL");
        HINDI = new Locale("hi", "");
        INDIA = new Locale("hi", "IN");
        UKRAINIAN = new Locale("uk", "");
        UKRAINE = new Locale("uk", "UA");
        MALAY = new Locale("ms", "");
        MALAYSIA = new Locale("ms", "MY");
        MALAYSIAENGLISH = new Locale("en", "MY");
        FILIPINO = new Locale("tl", "");
        PHILIPPINES = new Locale("tl", "PH");
        PHILIPPINESENGLISH = new Locale("en", "PH");
        SINGAPORE = new Locale("en", "SG");
        PSEUDO_KEY = new Locale("pseudo_key", "");
        LOCALE_MAP = new HashMap();
        LOCALE_MAP.put("en", ENGLISH);
        LOCALE_MAP.put("EN", ENGLISH);
        LOCALE_MAP.put("en_US", UNITED_STATES);
        LOCALE_MAP.put("zh_CN", SIMPLIFIED_CHINESE);
        LOCALE_MAP.put("zh_SG", SIMPLIFIED_CHINESE);
        LOCALE_MAP.put("zh", SIMPLIFIED_CHINESE);
        LOCALE_MAP.put("ZH", SIMPLIFIED_CHINESE);
        LOCALE_MAP.put("zt", TRADITIONAL_CHINESE);
        LOCALE_MAP.put("ZT", TRADITIONAL_CHINESE);
        LOCALE_MAP.put("zh_TW", TRADITIONAL_CHINESE);
        LOCALE_MAP.put("zth", TRADITIONAL_CHINESE_HK);
        LOCALE_MAP.put("ZTH", TRADITIONAL_CHINESE_HK);
        LOCALE_MAP.put("zh_HK", TRADITIONAL_CHINESE_HK);
        LOCALE_MAP.put("zh_MO", TRADITIONAL_CHINESE_HK);
        LOCALE_MAP.put("zh-hans", SIMPLIFIED_CHINESE);
        LOCALE_MAP.put("zh-hant", TRADITIONAL_CHINESE);
        LOCALE_MAP.put("zh-Hans", SIMPLIFIED_CHINESE);
        LOCALE_MAP.put("zh-Hant", TRADITIONAL_CHINESE);
        LOCALE_MAP.put("de", GERMAN);
        LOCALE_MAP.put("DE", GERMAN);
        LOCALE_MAP.put("de_DE", GERMANY);
        LOCALE_MAP.put("es", SPANISH);
        LOCALE_MAP.put("ES", SPANISH);
        LOCALE_MAP.put("es_ES", SPAIN);
        LOCALE_MAP.put("fr", FRENCH);
        LOCALE_MAP.put("FR", FRENCH);
        LOCALE_MAP.put("fr_FR", FRANCE);
        LOCALE_MAP.put("it", ITALIAN);
        LOCALE_MAP.put("IT", ITALIAN);
        LOCALE_MAP.put("it_IT", ITALY);
        LOCALE_MAP.put("pt", PORTUGUESE);
        LOCALE_MAP.put("PT", PORTUGUESE);
        LOCALE_MAP.put("pt_PT", PORTUGAL);
        LOCALE_MAP.put("pt_BR", BRAZIL);
        LOCALE_MAP.put("ru", RUSSIAN);
        LOCALE_MAP.put("RU", RUSSIAN);
        LOCALE_MAP.put("ru_RU", RUSSIA);
        LOCALE_MAP.put("ja", JAPANESE);
        LOCALE_MAP.put("JA", JAPANESE);
        LOCALE_MAP.put("ja_JP", JAPAN);
        LOCALE_MAP.put("ar", ARABIC);
        LOCALE_MAP.put("AR", ARABIC);
        LOCALE_MAP.put("ar_SA", SAUDI_ARABIA);
        LOCALE_MAP.put("ko", KOREAN);
        LOCALE_MAP.put("KO", KOREAN);
        LOCALE_MAP.put("ko_KR", KOREA);
        LOCALE_MAP.put("tr", TURKISH);
        LOCALE_MAP.put("TR", TURKISH);
        LOCALE_MAP.put("tr_TR", TURKEY);
        LOCALE_MAP.put("th", THAI);
        LOCALE_MAP.put("TH", THAI);
        LOCALE_MAP.put("th_TH", THAILAND);
        LOCALE_MAP.put("vi", VIETNAMESE);
        LOCALE_MAP.put("VI", VIETNAMESE);
        LOCALE_MAP.put("vi_VN", VIETNAM);
        LOCALE_MAP.put("nl", DUTCH);
        LOCALE_MAP.put("NL", DUTCH);
        LOCALE_MAP.put("nl_NL", NETHERLANDS);
        LOCALE_MAP.put("he", HEBREW);
        LOCALE_MAP.put("HE", HEBREW);
        LOCALE_MAP.put("he_IL", ISRAEL);
        LOCALE_MAP.put("iw", HEBREW);
        LOCALE_MAP.put("IW", HEBREW);
        LOCALE_MAP.put("iw_IL", ISRAEL);
        LOCALE_MAP.put("iw_HE", HEBREW);
        LOCALE_MAP.put("he_HE", HEBREW);
        LOCALE_MAP.put("in", INDONESIAN);
        LOCALE_MAP.put("IN", INDONESIAN);
        LOCALE_MAP.put("in_ID", INDONESIA);
        LOCALE_MAP.put("id", INDONESIAN);
        LOCALE_MAP.put("ID", INDONESIAN);
        LOCALE_MAP.put("id_ID", INDONESIA);
        LOCALE_MAP.put("pl", POLISH);
        LOCALE_MAP.put("PL", POLISH);
        LOCALE_MAP.put("pl_PL", POLAND);
        LOCALE_MAP.put("hi", HINDI);
        LOCALE_MAP.put("HI", HINDI);
        LOCALE_MAP.put("hi_IN", INDIA);
        LOCALE_MAP.put("uk", UKRAINIAN);
        LOCALE_MAP.put("UK", UKRAINIAN);
        LOCALE_MAP.put("uk_UA", UKRAINE);
        LOCALE_MAP.put("ms", MALAY);
        LOCALE_MAP.put("MS", MALAY);
        LOCALE_MAP.put("ms_MY", MALAYSIA);
        LOCALE_MAP.put("tl", FILIPINO);
        LOCALE_MAP.put("TL", FILIPINO);
        LOCALE_MAP.put("tl_PH", PHILIPPINES);
        LOCALE_MAP.put("pseudo_key", PSEUDO_KEY);
        LOCALE_MAP.put("en_SG", SINGAPORE);
        LOCALE_MAP.put("sg", SINGAPORE);
        LOCALE_MAP.put("SG", SINGAPORE);
        LOCALE_MAP.put("en_TH", THAIENGLISH);
        LOCALE_MAP.put("en_PH", PHILIPPINESENGLISH);
        LOCALE_MAP.put("en_MY", MALAYSIAENGLISH);
    }

}
