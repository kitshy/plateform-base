package cn.plateform.I18Util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.*;

/**
 * 说明
 *
 * @Description : 描述
 * @Author : songHuaYu
 * @Date: 2020/08/13
 */
@Slf4j
public class I18Utils {

    public static void main(String[] args) {
        Locale locale = McmsLanguage.EN.locale;
    }

    public static String toLocale(String text,Kv kv){

        StrSubstitutor strSubstitutor = new StrSubstitutor(new StrLookup<String>() {

            @Override
            public String lookup(String key) {
//                文案优先使用美杜莎的值
//                String value = I18n.getMcmsValueForCurrentUser(key);
//                if (!StringUtils.isEmpty(value)){
//                    return value;
//                }
                //否则使用硬编码的kv值替换，缺省返回空
                log.warn("Missing i18nKey: {}, using hard code kv value", key);
                return Optional.ofNullable(kv)
                        .map(Kv::getMap)
                        .map(map->map.get(key))
                        .orElseGet(()->Optional.ofNullable(kv).map(Kv::getDefaultI18Utils).orElse(null));
            }
        }, "${", "}", '$');
        return strSubstitutor.replace(text);
    }


    /**
     * 文案本地化，仅使用美杜莎
     *
     * @param text 文案，使用占位符 '${key}' 替换本地化的部分
     * @return 本地化文案
     */
    public static String toLocale(String text) {
        return toLocale(text, null);
    }

    public static Kv kv(String key, String value) {
        return new Kv(key, value);
    }

    /**
     * 缺省值
     * @param text
     * @return
     */
    public static Kv def(String text) {
        Kv kv = new Kv();
        kv.defaultI18n(text);
        return kv;
    }

    public static class Kv{

        @Getter
        private final Map<String,String> map = new LinkedHashMap<>();

        @Getter
        private String defaultI18Utils;

        private Kv(String key,String value){
            this();
            map.put(key,value);
        }

        private Kv() {
        }

        public Kv kv(String key, String value) {
            map.put(key, value);
            return this;
        }

        public Kv defaultI18n(String text) {
            this.defaultI18Utils = text;
            return this;
        }

    }


}
