package cn.plateform.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Base64Utils;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.*;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Util {
    public final static int DateType_START = 1;
    public final static int DateType_CURRENT = 2;
    public final static int DateType_END = 3;
    /**
     * 当前IOC
     */
    //public static ApplicationContext applicationContext;
    /**
     * 用于校验时间字符串的有效性
     */
    public static int[] DAYS = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};



    /**
     * 生产GUID
     *
     * @return UUID{...}格式
     */
    public static String NewGuid() {
        String uid = UUID.randomUUID().toString().toUpperCase();
        uid = uid.replaceAll("-", "");
        return uid;
    }

    public static String NewSortGuid() {
        String uid = UniqId.getInstance().getUniqID();
        uid = uid.replaceAll("-", "");
        return uid;
    }

    /**
     * 获取JOSONObject对象的PropertyName属性值
     *
     * @param obj          JSONObject 对象
     * @param propertyName 属性
     * @return
     */
    public static Object getJSONValue(JSONObject obj, String propertyName) {
        Object rv = null;
        if (obj == null) {
            return null;
        }
        if (!obj.containsKey(propertyName)) {
            return null;
        }
        try {
            rv = obj.get(propertyName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rv;
    }

    public static Method getField(Object obj, String fieldname) {
        if (fieldname.length() > 0) {
            String methodname = "get" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1, fieldname.length());
            Method m;
            try {
                m = obj.getClass().getMethod(methodname);
                return m;
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
        return null;
    }

    public static Class<?> getFieldType(Object obj, String fieldname) {
        if (fieldname.length() > 0) {
            String methodname = "get" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1, fieldname.length());
            Method m;
            try {
                m = obj.getClass().getMethod(methodname);
                return m.getReturnType();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
        return null;
    }

    public static Object getFieldValue(Object obj, String fieldname) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.containsKey(fieldname)) {
                return map.get(fieldname);
            } else {
                return null;
            }
        } else if (obj instanceof JSONObject) {
            JSONObject json = (JSONObject) obj;
            if (json.containsKey(fieldname)) {
                return json.get(fieldname);
            } else {
                return null;
            }
        }
//        else if (obj instanceof net.sf.json.) {
//            net.sf.json.JSONObject json = (net.sf.json.JSONObject) obj;
//            if (json.containsKey(fieldname)) {
//                return json.get(fieldname);
//            } else {
//                return null;
//            }
//        }
        else {

            if (fieldname.length() > 0) {
                String methodname = "get" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1, fieldname.length());
                Method m;
                try {
                    m = obj.getClass().getMethod(methodname);
                    return m.invoke(obj);
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }

            }
            return null;
        }
    }

    public static void setFieldValue(Object obj, String fieldname, Object value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (value != null) {
            setFieldValue(obj, fieldname, value, value.getClass());
        } else {
            setFieldValue(obj, fieldname, value, null);
        }
    }

    public static Method getMethod(Class cls, String methodName) {
        if (cls == null) {
            return null;
        }
        for (Method m : cls.getMethods()) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        return null;
    }

    /**
     * 当属性类型为接口时，传入的实际对象class不等于接口，会导致 NoSuchMethodException 异常
     *
     * @param obj
     * @param fieldname
     * @param value
     * @param valueClass
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @author ly
     */
    public static void setFieldValue(Object obj, String fieldname, Object value, Class<?> valueClass) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (obj == null) {
            return;
        }

        if (obj instanceof Map) {
            Map map = (Map) obj;
            map.put(fieldname, value);
        } else {

            if (fieldname.length() > 0) {

                String methodname = "set" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1, fieldname.length());
                try {
                    Method m = null;
                    m = getMethod(obj.getClass(), methodname);// obj.getClass().getMethod(methodname);

                    if (m == null) {
                        log.info("No Method:" + methodname);
                    } else {
                        m.invoke(obj, value);
                    }
                } catch (Exception e) {
                    log.error("setFieldValue error: fieldname:" + fieldname);
                    //e.printStackTrace();
                }
            }
        }

    }

    public static void setFieldValueEx(Object obj, String fieldName, Object value) {

        try {
            int dotIndex = fieldName.indexOf(".");
            if (dotIndex > 0) {
                Object subObj = getFieldValue(obj, fieldName.substring(0, dotIndex));
                setFieldValueEx(subObj, fieldName.substring(dotIndex + 1), value);
            } else {
                setFieldValue(obj, fieldName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Object getFieldValueEx(Object obj, String fieldName) {
        int dotIndex = fieldName.indexOf(".");
        try {
            if (dotIndex > 0) {
                Object subObj = getFieldValue(obj, fieldName.substring(0, dotIndex));
                return getFieldValueEx(subObj, fieldName.substring(dotIndex + 1));
            } else {
                return getFieldValue(obj, fieldName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isNumeric(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        }
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]*([Ee]{1}[0-9]+)?");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 正则表达式匹配
     *
     * @param patternStr 匹配规则
     * @param str        需要匹配的字符串
     * @return
     */
    public static boolean regexMatched(String patternStr, String str) {
        if (isNullOrEmpty(str, patternStr)) {
            return false;
        }
        boolean result =false;
        try {
            result = str.matches(".*?" + patternStr + ".*?");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 正则表达式获取匹配的字符串 （匹配内容用"()"包含)
     *
     * @param str
     * @param patternStr
     * @return
     */
    public static List<String> regexMatchList(String str, String patternStr) {
        List<String> result = new ArrayList<String>();
        try {
            Pattern p = Pattern.compile(patternStr);
            Matcher m = p.matcher(str);

            while (m.find()) {
                result.add(m.group(1));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 正则表达式获取匹配的字符串 （匹配内容用"()"包含)
     *
     * @param str
     * @param patternStr
     * @return
     */
    public static String regexMatchOne(String str, String patternStr) {
        try {
            Pattern p = Pattern.compile(patternStr);
            Matcher m = p.matcher(str);
            if (m.find()) {
                return m.group(1);
            } else {
                return "";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 查找并替换匹配的字符串
     *
     * @param str
     * @param patternStr
     * @param replaceValue
     * @return
     */
    public static String regexMatchReplace(String str, String patternStr, String replaceValue) {
        StringBuffer stringBuffer = new StringBuffer(str);
        try {
            Pattern p = Pattern.compile(patternStr);
            Matcher m = p.matcher(stringBuffer);
            while (m.find()) {
                m.replaceAll(replaceValue);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }



    public static String FilterString(String htmlScourceCode) {
        String regexPatten = "\\r\\n\\s*|\\r\\s*|\\n\\s*|\\t\\s*";
        //String regexPatten="\\S*";
        String strOutput = "";
        Pattern pattern = Pattern.compile(regexPatten, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(htmlScourceCode);
        strOutput = matcher.replaceAll("");

        // Regex regex = new Regex(regexPatten,RegexOptions.IgnoreCase);
        // strOutput=regex.Replace(htmlScourceCode,"");
        return strOutput;
    }


    public static String getByTag(String str, String tag) {
        str = FilterString(str);
        String pattern = "&lt;" + tag + "&gt;(.*?)&lt;/" + tag + "&gt;";
        String result = regexMatchOne(str, pattern);
        if (isNullOrEmpty(result)) {
            pattern = "<" + tag + ">(.*?)</" + tag + ">";
            result = regexMatchOne(str, pattern);
        }
        return result;
    }

    public static List<String> getByTagList(String str, String tag) {
        str = FilterString(str);
        String pattern = "&lt;" + tag + "&gt;(.*?)&lt;/" + tag + "&gt;";
        List<String> result = regexMatchList(str, pattern);
        if (result.size() == 0) {
            pattern = "<" + tag + ">(.*?)</" + tag + ">";
            result = regexMatchList(str, pattern);
        }
        return result;
    }

    public static String getByTag(String type, String str, String tag) {
        str = FilterString(str);
        String pattern = "&lt;" + type + "&gt;.*?&lt;" + tag + "&gt;(.*?)&lt;/" + tag + "&gt;";
        String result = regexMatchOne(str, pattern);
        if (isNullOrEmpty(result)) {
            pattern = "<" + type + ">.*?<" + tag + ">(.*?)</" + tag + ">";
            result = regexMatchOne(str, pattern);
        }
        return result;
    }


    public static List<String> getByTagList(String type, String str, String tag) {
        str = FilterString(str);
        String pattern = "&lt;" + type + "&gt;.*?&lt;" + tag + "&gt;(.*?)&lt;/" + tag + "&gt;";
        List<String> result = regexMatchList(str, pattern);
        if (result.size() == 0) {
            pattern = "<" + type + ">.*?<" + tag + ">(.*?)</" + tag + ">";
            result = regexMatchList(str, pattern);
        }
        return result;
    }

    public static boolean isEoN(Object testStr) {
        return (testStr == null || "".equals(testStr.toString().trim()) || "null".equals(testStr.toString().trim()));
    }

    public static boolean isNullOrEmpty(String... str) {
        boolean t = false;
        if (str != null) {
            for (int i = 0; i < str.length; i++) {
                String n = str[i];
                n = (n == null ? n : n.trim());
                if (n == null || "".equals(n) || "undefined".equals(n) || "null".equals(n)) {
                    t = true;
                    break;
                }
            }
        } else {
            t = true;
        }
        return t;
    }

    public static boolean isNullOrEmpty(Object obj) {
        if (obj instanceof Object[]) {
            Object[] o = (Object[]) obj;
            for (int i = 0; i < o.length; i++) {
                Object object = o[i];
                if (object instanceof Date) {
                    if (object.equals(new Date(0))) {
                        return true;
                    }
                } else if ((object == null) || (("").equals(object))) {
                    return true;
                }
            }
        } else {
            if (obj instanceof Date) {
                if (obj.equals(new Date(0))) {
                    return true;
                }
            } else if ((obj == null) || (("").equals(obj))) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNullOrZero(List<?> obj) {
        return (obj == null || obj.size() == 0);
    }

    /**
     * @param date
     * @return日期格式
     */
    private static String analyseDateFormat(String date) {
        String result = null;
        String input = date.trim();
        if (Pattern.matches("\\d{2,4}-\\d{1,2}-\\d{1,2}\\s{1}\\d{1,2}:\\d{1,2}:\\d{1,2}", input)) {
            result = "yyyy-MM-dd HH:mm:ss";
        }else if (Pattern.matches("\\d{2,4}-\\d{1,2}-\\d{1,2}\\s{1}\\d{1,2}:\\d{1,2}.*", input)) {
            result = "yyyy-MM-dd HH:mm";
        }else if (Pattern.matches("\\d{2,4}-\\d{1,2}-\\d{1,2}\\s{1}\\d{1,2}.*", input)) {
            result = "yyyy-MM-dd HH";
        }else if (Pattern.matches("\\d{2,4}-\\d{1,2}-\\d{1,2}.*", input)) {
            result = "yyyy-MM-dd";
        } else if (Pattern.matches("\\d{1,2}-\\d{1,2}-\\d{2,4}.*", input)) {
            result = "MM-dd-yyyy";
        } else if (Pattern.matches("\\d{2,4}/\\d{1,2}/\\d{1,2}.*", input)) {
            result = "yyyy/MM/dd";
        } else if (Pattern.matches("\\d{1,2}/\\d{1,2}/\\d{2,4}.*", input)) {
            result = "MM/dd/yyyy";
        } else if (Pattern.matches("\\d{6,8}.*", input)) {
            result = "yyyyMMdd";
        }
        if (result==null&&date.trim().length() > 12) {
            result = result + " HH:mm:ss";
        }
        return result;
    }

    public static Date parseDate(String date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDate(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * @param date
     * @return yyyy-MM-dd格式
     */
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
        /*if (date == null)
            return null;
		int mYear = date.getYear() + 1900;
		int mMonth = date.getMonth();
		int mDay = date.getDate();
		StringBuilder sb = new StringBuilder();
		sb.append(mYear).append("-").append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append((mDay < 10) ? "0" + mDay : mDay);
		return sb.toString();*/
    }

    public static String formatDate(Date date, int DateType) {
        SimpleDateFormat sdf;
        switch (DateType) {
            case DateType_START:
                sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                break;
            case DateType_END:
                sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                break;
            case DateType_CURRENT:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            default:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
        }
        return sdf.format(date);
    }

    public static Date parseDateN(Object date) {
        if (date != null) {
            return parseDate(date.toString());
        } else {
            return parseDate("1900-01-01");
        }
    }

    public static double parseDoubleN(Object data) {
        if (data != null) {
            try {
                return Double.parseDouble(data.toString());
            } catch (Exception ex) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static float parseFloatN(Object data) {
        if (data != null) {
            try {
                return Float.parseFloat(data.toString());
            } catch (Exception ex) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static int parseIntN(Object data) {
        return parseIntN(data, 0);

    }

    public static int parseIntN(Object data, int def) {
        if (data != null) {
            try {
                return Integer.parseInt(data.toString());
            } catch (Exception ex) {
                return def;
            }
        } else {
            return def;
        }
    }

    public static String parseStringN(Object data) {
        if (data == null || "null".equals(data.toString().toLowerCase())) {
            return "";
        } else {
            return data.toString();
        }
    }


    public static Date parseDate(String date) {
        Date time = new Date();
        try {
            String formatStr = analyseDateFormat(date);
            if (formatStr == null) {
                DateFormat sd = SimpleDateFormat.getDateTimeInstance();
                time = sd.parse(date);//  .parse(date);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
                time = sdf.parse(date);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 将json中存在的属性填充到Object对象对应的属性中去,如果Json对象中不存在的将忽略
     *
     * @param obj  目标对象
     * @param json
     * @return
     */
    public static Object ApplyObject(Object obj, JSONObject json) {
        if (json == null) {
            return obj;
        }
        if (obj == null) {
            return obj;
        }
        // 遍历jsonObject数据，添加到Map对象
        for (Map.Entry<String, Object> map : json.entrySet()) {
            String key = map.getKey();
            Object value;
            try {
                value = map.getValue();
                if (obj instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) obj;
                    jsonObj.put(key, value);
                } else if (obj instanceof Map) {
                    Map mapObj = (Map) obj;
                    mapObj.put(key, value);
                } else {
                    if (!(value instanceof JSONObject) && !(value instanceof JSONArray)) {
                        Method method = getField(obj, key);
                        if (method != null) {
                            if (value != null) {
                                value = Caster(method.getReturnType(), value.toString());
                            }

                        }
                        setFieldValue(obj, key, value);
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return obj;
    }

    /**
     * 字符串到其他类型转换
     *
     * @param type
     * @param value
     * @return
     */
    public static Object Caster(Class<?> type, String value) {

        Object returnValue = null;
        if (isEoN(value)) {
            return null;
        }

        if ("java.util.Date".equals(type.getName())) {
            if (isNumeric(value)) {
                returnValue = new Date(Long.parseLong(value));
            } else {
                returnValue = parseDate(value);
            }
        } else if (type.getName().endsWith("Integer")) {
            if (isNumeric(value)) {
                returnValue = Integer.valueOf(value);
            }
        } else if (type.getName().endsWith("int")) {
            if (isNumeric(value)) {
                returnValue = Integer.valueOf(value);
            } else {
                returnValue = 0;
            }
        } else if (type.getName().endsWith("Double")) {
            if (isNumeric(value)) {
                returnValue = Double.valueOf(value);
            }
        } else if (type.getName().endsWith("double")) {
            if (isNumeric(value)) {
                returnValue = Double.valueOf(value);
            } else {
                returnValue = 0.0;
            }
        } else if (type.getName().endsWith("Byte")) {
            if (isNumeric(value)) {
                returnValue = Byte.valueOf(value);
            }
        } else if (type.getName().endsWith("byte")) {
            if (isNumeric(value)) {
                returnValue = Byte.valueOf(value);
            } else {
                returnValue = 0;
            }
        } else if (type.getName().endsWith("Boolean")) {
            returnValue = Boolean.parseBoolean(value);
        } else if (type.getName().endsWith("bool")) {
            returnValue = Boolean.parseBoolean(value);
        } else if (type.getName().endsWith("java.math.BigDecimal")) {
            returnValue = new BigDecimal(value);
        } else {
            returnValue = value.toString();
        }


        return returnValue;
    }

    public static Map<String, Object> toMap(JSONObject json) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        // 将json字符串转换成jsonObject
        // SONObject jsonObject = JSONObject.fromObject(object);
        // 遍历jsonObject数据，添加到Map对象
        for (Map.Entry<String, Object> map : json.entrySet()) {
            String key = map.getKey();
            Object value;
            try {
                value = map.getValue();
                data.put(key, value);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return data;
    }

    public static String toString(Document doc) throws TransformerFactoryConfigurationError, TransformerException {
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(source, result);
        return (writer.getBuffer().toString());
    }

    public static Long getTimeStamp() {
        return System.nanoTime() / 1000000000L;
        //return (new Date()).getTime()/1000;
        //return System.currentTimeMillis() / 1000;
    }

    public static Long getMillisTimeStamp() {
        return System.nanoTime() / 1000000L;
        //return (new Date()).getTime()/1000;
        //return System.currentTimeMillis() / 1000;
    }

    public static boolean isValidDate(String date) {
        try {
            if (isNullOrEmpty(date)) {
                return false;
            }
            date = date.trim();
            int len = date.length();
            if (len == 10) {
                int year = Integer.parseInt(date.substring(0, 4));
                if (year <= 0) {
                    return false;
                }
                int month = Integer.parseInt(date.substring(5, 7));
                if (month <= 0 || month > 12) {
                    return false;
                }
                int day = Integer.parseInt(date.substring(8, 10));
                if (day <= 0 || day > DAYS[month]) {
                    return false;
                }
                if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {
                    return false;
                }
            } else if (len == 19) {
                int year = Integer.parseInt(date.substring(0, 4));
                if (year <= 0) {
                    return false;
                }
                int month = Integer.parseInt(date.substring(5, 7));
                if (month <= 0 || month > 12) {
                    return false;
                }
                int day = Integer.parseInt(date.substring(8, 10));
                if (day <= 0 || day > DAYS[month]) {
                    return false;
                }
                if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {
                    return false;
                }
                int hour = Integer.parseInt(date.substring(11, 13));
                if (hour < 0 || hour > 23) {
                    return false;
                }
                int minute = Integer.parseInt(date.substring(14, 16));
                if (minute < 0 || minute > 59) {
                    return false;
                }
                int second = Integer.parseInt(date.substring(17, 19));
                if (second < 0 || second > 59) {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static final boolean isGregorianLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    /**
     * 随机码
     */
    public static String createInvitingCode(boolean b, int len) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        if (!b) {
            base = "0123456789";
        }
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static boolean isNullOrZero(short prizeNo) {
        // TODO Auto-generated method stub
        return false;
    }

//	public static boolean send(String mobile,ShardedJedis obj,int seconds,String msgContent,String str) throws Exception {
//
//		// 组建请求
//		String straddr = addr + "?uid=" + userNo + "&pwd=" + pwd + "&mobile="
//				+ mobile + "&content="
//				+ URLEncoder.encode(msgContent,"gbk");
//		// 发送请求
//		URL url = new URL(straddr);
//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestMethod("POST");
//		BufferedReader in = new BufferedReader(new InputStreamReader(url
//				.openStream()));
//
//		// 返回结果
//		boolean back = false;
//		String inputline = in.readLine();
//		if(!Util.isNullOrEmpty(inputline)&&inputline.indexOf("100")>=0){
//			if(obj!=null){
//				obj.set(mobile, str);
//				if(seconds>0){
//					obj.expire(mobile, seconds);
//				}else{/**默认100秒失效*/
//					obj.expire(mobile, 100);
//				}
//			}
//			back = true;
//		}
//		System.out.println("Response:" + URLDecoder.decode(inputline,"gbk") );
//		return back;
//	}

    public static String createFileName(String fileName, String directoryName, String a) {
        /**获取当前年月*/
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String name = format.format(time);

        /**获取当前的毫秒级时间和4位随机数作为作为文件名称*/
        String number = createInvitingCode(true, 4);
        String n = number;
        if (!isNullOrEmpty(directoryName)) {
            return directoryName + "/" + name + "/" + n + fileName;
        } else {
            return name + "/" + n + fileName;
        }
    }

    /**
     * http---post请求
     */
    public static String sendPostRequestByForm(String path, String params) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");// 提交模式
        // conn.setConnectTimeout(10000);//连接超时 单位毫秒
        // conn.setReadTimeout(2000);//读取超时 单位毫秒
        conn.setDoOutput(true);// 是否输入参数
        conn.setDoInput(true);
        byte[] bypes = params.toString().getBytes();
        conn.getOutputStream().write(bypes);// 输入参数
        InputStream inStream = conn.getInputStream();
        return readInputStream(inStream).toString();
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }

    /**
     * 获取实体对象的所有属性名称数组
     */
    public static String[] getPropertyNameByModel(Object model) {
        Field[] field = model.getClass().getDeclaredFields();        //获取实体类的所有属性，返回Field数组
        String[] backs = null;
        if (field != null && field.length > 0) {
            backs = new String[field.length];
            for (int i = 0; i < field.length; i++) {
                String propertyName = field[i].getName();
                backs[i] = propertyName;
            }
        }

        return backs;
    }

    /**
     * Set集合转List
     */
    public static List<?> setChangeToList(Set<?> set) {
        List<Object> li = null;
        if (set != null) {
            li = new ArrayList<Object>();
            Iterator<?> in = set.iterator();
            while (in.hasNext()) {
                Object obj = in.next();
                li.add(obj);
            }
        }

        return li;
    }

    public static int changeBack(String t) {
        int i = isNullOrEmpty(t) ? 0 : Integer.parseInt(t);
        return i;
    }

    public static String checkObject(String str) {
        String backStr = isNullOrEmpty(str) ? "" : str.trim();
        return backStr;
    }

    /**
     * 利用java反射，以map赋值一个对象
     */
    public static Object createBeans(Object obj, Map<?, ?> map) throws Exception {
        Field[] field = obj.getClass().getDeclaredFields();
        if (field != null && field.length > 0) {
            for (int i = 0; i < field.length; i++) {
                String keyName = field[i].getName();
                String filedName = field[i].getName();
                filedName = filedName.substring(0, 1).toUpperCase() + filedName.substring(1); // 将属性的首字符大写，方便构造get，set方法
                Object value = map.get(keyName);
                if (value != null) {
                    Method method = obj.getClass().getMethod("set" + filedName, field[i].getType());
                    method.invoke(obj, map.get(keyName));
                }
            }
        }
        return obj;
    }

    /**
     * 利用BeanCopier类对对象进行copy
     */
    public static Object copyBeansByBeanCopierAtoB(Object a, Object b) {
        BeanCopier copy = BeanCopier.create(a.getClass(), b.getClass(), false);
        copy.copy(a, b, null);
        return b;
    }

    /**
     * 对象序列化---注意需要序列化的类需要 implements Serializable
     */
    public static byte[] createSerialize(Object obj) {
        byte[] backByte = null;
        try {
            if (obj != null) {
                ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
                ObjectOutputStream objoutputstream = new ObjectOutputStream(outputstream);
                objoutputstream.writeObject(obj);
                backByte = outputstream.toByteArray();
                objoutputstream.flush();
                objoutputstream.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return backByte;
    }

    /**
     * 对象反序列化---注意需要反序列化的类需要 implements Serializable
     */
    public static Object antiSerialization(byte[] backByte) {
        Object obj = null;
        try {
            if (backByte != null) {
                ObjectInputStream inputstream = new ObjectInputStream(new ByteArrayInputStream(backByte));
                obj = inputstream.readObject();
                return obj;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 公共加法运算---四舍五入
     */
    public static BigDecimal decimalSur(int i, BigDecimal... decimal) {
        BigDecimal m = BigDecimal.ZERO;
        if (decimal != null && decimal.length > 0) {
            for (BigDecimal d : decimal) {
                if (d != null) {
                    m = m.add(d);
                }
            }
        }
        return m.setScale(i, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 数字格式化
     *
     * @param data
     * @param format 12.34  "0.0","#.#","000.000","###.###"  分别为 12.3 12.3 012.340 12.34
     * @return
     */
    public static Double decimalRound(Double data, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return Double.parseDouble(df.format(data));
    }

    /**
     * 正整数校验
     *
     * @param m:需要校验的数字
     */
    public static boolean checkInteger(String... m) {
        boolean t = true;
        String expression = "^[1-9]\\d*$";
        if (m != null) {
            for (int i = 0; i < m.length; i++) {
                if (m[i] != null) {
                    t = Pattern.matches(expression, m[i]);
                    if (!t) {
                        break;
                    }
                } else {
                    t = false;
                    break;
                }
            }
        } else {
            t = false;
        }
        return t;
    }


    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        List<List<T>> listArray = new ArrayList<List<T>>();

        ArrayList<T> al = new ArrayList<T>();
        for (T x : list) {
            al.add(x);
            if (pageSize == al.size()) {
                listArray.add(al);
                al = new ArrayList<T>();
            }
        }

        if (0 != al.size()) {
            listArray.add(al);
        }

        return listArray;
    }

    /**
     * 获取今日指定时间点的long值
     */
    public static long getNowTimeStamp(String hms) {
        long lo = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String newTime = format.format(new Date()) + " " + hms;
            lo = formatSec.parse(newTime).getTime();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return lo;
    }

    /**
     * 获取当前时间的sql时间戳
     */
    public static Timestamp getNowSqlTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    public static Timestamp getSqlTimestamp(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = format.parse(time);
            return new Timestamp(d.getTime());
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 对大额数字进行描述性缩写
     */
    public static String descNum(String number) {
        if (!isNullOrEmpty(number)) {
            double num = Double.parseDouble(number);
            DecimalFormat df = new DecimalFormat("#.00");
            if (num >= 100000000) {
                return df.format(num / 100000000) + "亿";
            } else if (num >= 10000) {
                return df.format(num / 10000) + "万";
            } else {
                return number;
            }
        } else {
            return "";
        }
    }

    public static String readWord(StringBuffer Str, String split) {
        String rv = "";
        int index = Str.indexOf(split);
        if (index > -1) {
            rv = Str.substring(0, index);
            Str.delete(0, index + split.length());
        } else {
            rv = Str.toString();
            Str.delete(0, Str.length());
        }
        return rv;
    }

    public static String readWord(String Str, String b_flag, String e_flag) {
        String rv = "";
        int b_index = Str.indexOf(b_flag);
        int e_index = Str.indexOf(e_flag);
        if (b_index > -1 && e_index > b_index) {
            rv = Str.substring(b_index + b_flag.length(), e_index);

        } else {
            rv = "";

        }
        return rv;
    }

    /**
     * 获取当前非空数据
     */
    public static String getStr(Object t) {
        String str = "";
        try {
            str = t == null ? "" : t.toString();
        } catch (Exception e) {
            // TODO: handle exception
            str = "";
        }
        return str;
    }

    /**
     * 按照字节数读取字符串
     * @param text
     * @param maxlength
     * @return
     */
    public static String readWordLenB(StringBuffer text,int maxlength)
    {
        if(isNullOrEmpty(text)) {
            return "";
        } else
        {
            StringBuffer rv=new StringBuffer(text.toString());
            int len=0;
            try {
                len=text.toString().getBytes("GBK").length;
                if(len<maxlength) {
                    text.delete(0,text.length());
                    return rv.toString();
                }
                int count=0;
                StringBuffer stringBuffer=new StringBuffer();
                for(int i=0;i<text.length();i++)
                {
                    String tmp=text.substring(i,i+1);
                    count+=tmp.getBytes("GBK").length;
                    if(count>maxlength) {
                        text.delete(0,i);
                        break;
                    }
                    stringBuffer.append(tmp);
                }

                return stringBuffer.toString();

            } catch (UnsupportedEncodingException e) {

                return rv.toString();
            }

        }
    }

//    JavaCodeFactory反序列化工具
//    public static CodecFactory getCodecFactory() {
//        return new JavaCodeFactory();
//    }

    @SuppressWarnings("deprecation")
    public static String intervalTime(Date time) {
        Date nowTime = new Date();
        long seconds = nowTime.getTime() - time.getTime();
        if (time != null && seconds > 0) {
            int intervalYear = nowTime.getYear() - time.getYear();
            int intervalMonth = nowTime.getMonth() - time.getMonth();

            Calendar aCalendar = Calendar.getInstance();
            aCalendar.setTime(time);
            int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
            aCalendar.setTime(nowTime);
            int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
            int intervalDay = day2 - day1;

            int intervalHours = nowTime.getHours() - time.getHours();
            int intervalMinis = nowTime.getMinutes() - time.getMinutes();
            if (intervalYear > 0) {
                return intervalYear + "年前";
            } else if (intervalMonth > 0) {
                return intervalMonth + "月前";
            } else if (intervalDay > 0) {
                return intervalDay + "天前";
            } else if (intervalHours > 0) {
                return intervalHours + "小时前";
            } else if (intervalMinis > 0) {
                return intervalMinis + "分钟前";
            } else if ((seconds / 1000) > 1) {
                return seconds / 1000 + "秒钟前";
            } else {
                return "刚刚";
            }
        }
        return null;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * 如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean) throws IntrospectionException,
            IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo
                .getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!"class".equals(propertyName)) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    public static Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static String urlEncode(String str) {
        String url = "";
        try {
            url = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String urlDecode(String str) {
        String url = "";
        if (!isNullOrEmpty(str)) {
            try {
                url = URLDecoder.decode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return url;
    }


    public static String getImgFileType(String head) {
        String type = "";
        String jpeg = "jpeg";
        String jpg = "jpg";
        String png = "png";
        if (head.indexOf(jpeg) >= 0 || head.indexOf(jpg) >= 0) {
            return ".jpg";
        } else if (head.indexOf(png) >= 0) {
            return ".png";
        }
        return type;
    }

    /**
     * 比较两个年月日的大小
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int date1Year = calendar.get(Calendar.YEAR);
        calendar.setTime(date2);
        int date2Year = calendar.get(Calendar.YEAR);
        if (date1Year > date2Year) {
            return 1;
        } else if (date1Year == date2Year) {
            calendar.setTime(date1);
            int day1 = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.setTime(date2);
            int day2 = calendar.get(Calendar.DAY_OF_YEAR);
            if (day1 == day2) {
                return 0;
            } else {
                return day1 > day2 ? 1 : -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * 根据类名 className 产生实体类
     *
     * @param className
     * @return
     */
    public static Object getClass(String className) {
        try {
            Object obj = Class.forName(className).newInstance();
            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 比较两个时间的时分秒的大小
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareTime(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int hour1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int hour2 = calendar2.get(Calendar.HOUR_OF_DAY);
        if (hour1 > hour2) {
            return 1;
        } else if (hour1 == hour2) {
            int minute1 = calendar1.get(Calendar.MINUTE);
            int minute2 = calendar2.get(Calendar.MINUTE);
            if (minute1 > minute2) {
                return 1;
            } else if (minute1 == minute2) {
                int second1 = calendar1.get(Calendar.SECOND);
                int second2 = calendar2.get(Calendar.SECOND);
                if (second1 == second2) {
                    return 0;
                } else {
                    return second1 > second2 ? 1 : -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * 获取某天星期
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            return 7;
        } else {
            return day - 1;
        }
    }

    /**
     * 获取当前星期
     *
     * @return
     */
    public static int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            return 7;
        } else {
            return day - 1;
        }
    }

    public static String formatDateFromTimestamp(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static int diffOfDateMinute(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int hour1 = calendar.get(Calendar.HOUR_OF_DAY);
        int minute1 = calendar.get(Calendar.MINUTE);

        calendar.setTime(date2);
        int hour2 = calendar.get(Calendar.HOUR_OF_DAY);
        int minute2 = calendar.get(Calendar.MINUTE);

        int min1 = hour1 * 60 + minute1;
        int min2 = hour2 * 60 + minute2;
        return min1 > min2 ? min1 - min2 : min2 - min1;
    }

    /**
     * 对象Clone
     *
     * @param obj
     * @return
     */
    public static Object cloneObject(Object obj) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(obj);
        Object result = JSON.toJavaObject(jsonObject, obj.getClass());
        return result;
    }

    /**
     * 读取properties配置文件的属性值
     *
     * @param propertiesName：配置文件名称
     * @param key：属性变量名
     * @return
     */
    public static String getPropertiesValues(String propertiesName, String key) {
        String backStr = "";
        try {
            ResourceBundle bundlers = ResourceBundle.getBundle(propertiesName); //读取配置文件
            backStr = bundlers.getString(key);//获取属性值
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backStr;
    }


    public static StringBuffer getStackTraceInfo(StackTraceElement[] stackTraceElements) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < stackTraceElements.length; i++) {
            StackTraceElement element = stackTraceElements[i];
            stringBuffer.append(element.getClassName()).append(".")
                    .append(element.getMethodName()).append("[")
                    .append(element.getLineNumber()).append("]")
                    .append("\r\n");
        }
        return stringBuffer;
    }



    public static String convertWeixinText(String html) {
        if (!isNullOrEmpty(html)) {
            String content = html;
            content = content.replaceAll("\r", "");
            content = content.replaceAll("\n", "");
            content = content.replaceAll("<br/>", "\n");
            content = content.replaceAll("<BR/>", "\n");
            content = content.replaceAll("<br>", "\n");
            content = content.replaceAll("<BR>", "\n");
            content = content.replaceAll("&nbsp;", " ");
            return content;
        } else {
            return html;
        }
    }

    public static String getResourceUrl(String resourceId) {
        return "/System/imgResourceUrl.do?imgResourceId=" + resourceId;
    }


    /**
     * 将emoji表情替换成*
     *
     * @param source
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {
        if (!isNullOrEmpty(source)) {
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
        } else {
            return source;
        }
    }

    /**
     * 如果Obj为Null 返回默认值defaultValue
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static Object isNull(Object obj, Object defaultValue) {
        if (obj == null || "".equals(obj)) {
            return defaultValue;
        } else {
            return obj;
        }
    }

    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    /**
     * 判断是否是双休日
     *
     * @param dateTime:时间格式字符串
     * @return Boolean:true(是双休日)，false（不是双休日），null(校验异常)
     */
    public static Boolean checkHoliday(String dateTime) {
        Boolean bool = null;
        try {
            if (!isNullOrEmpty(dateTime)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date time = format.parse(dateTime);
                int day = time.getDay();
                bool = day == 0 || day == 6;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    public static boolean isSameWeek(String date1, String date2) {
        // 0.先把Date类型的对象转换Calendar类型的对象
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(parseDate(date1, "yyyyMMdd"));
        cal2.setTime(parseDate(date2, "yyyyMMdd"));

        // 1.比较当前日期在年份中的周数是否相同
        return cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * java去除字符串中的空格、回车、换行符、制表符
     *
     * @param s
     * @return
     */
    public static String filterStr(String s) {
        if (s != null) {
            return s.replaceAll("\\s*|\t|\r|\n", "");
        }
        return "";
    }

    public static JSONObject toDoData(BigDecimal amount) {
        JSONObject json = new JSONObject();
        Map<String, Integer> backMap = new HashMap<String, Integer>();
        try {
            if (amount.compareTo(new BigDecimal(8)) > 0) {
                String keyThr = "nn3";
                String keySec = "nn2";
                String keyOne = "nn1";
                int amountInteger = amount.intValue();
                json = toDivision(json, amountInteger, 200, "3");
                int nn = json.getInteger(keyThr);
                if (nn > 0) {
                    json.remove(keyThr);
                    json = toDivision(json, nn, 80, "2");
                    nn = json.getInteger(keySec);
                    if (nn > 0) {
                        json.remove(keySec);
                        json = toDivision(json, nn, 8, "1");
                        json.remove(keyOne);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject toDivision(JSONObject json, int a, int b, String type) throws Exception {
        int nn = a % b;
        int count = a / b;
        json.put("nn" + type, nn);
        json.put("count" + type, count);
        return json;
    }
    public static String[] getThisWeekStartEnd() {
        String[] date = new String[2];

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
        date[0] = df.format(cal.getTime());
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        date[1] = df.format(cal.getTime());
        return date;
    }

    public static String[] getThisWeekStartEnd(String dateStr) {
        String[] date = new String[2];

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setTime(parseDate(dateStr, "yyyyMMdd"));
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        date[0] = format.format(cal.getTime());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        date[1] = format.format(cal.getTime());
        return date;
    }

    public static String getQrCodeUrl(String ticket) {
        return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + urlEncode(ticket);
    }

    public static Date addDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    public static Date getDayDate(Date date) {
        String todayStr = formatDate(date, "yyyyMMdd");
        return parseDate(todayStr + "000000", "yyyyMMddHHmmss");
    }

    /**
     * 获取访问者IP
     * <p>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-Real-IP");
        //log.info("X-Real-IP:"+ip);
        if (!ObjectUtils.isEmpty(ip.replace(" ","")) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        //log.info("X-Forwarded-For:"+ip);
        ip = request.getHeader("X-Forwarded-For");
        if (!ObjectUtils.isEmpty(ip.replace(" ","")) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            //log.info("getRemoteAddr:"+request.getRemoteAddr());
            return request.getRemoteAddr();
        }
    }


    /**
     * 版本号比较
     * 前者>后者 返回1;
     * 前者=后者 返回0;
     * 前者<后者 返回-1;
     * 版本格式不一致返回 -2;
     *
     * @param newVersion
     * @param oldVersion
     * @return 1, 0,-1,-2
     */
    public static int versionCompare(String newVersion, String oldVersion) {
        int result = 0;
        if (isNullOrEmpty(newVersion, oldVersion)) {
            return -2;
        }
        try {
            String[] oldArray = oldVersion.split("\\.");
            String[] newArray = newVersion.split("\\.");
            if (oldArray.length != newArray.length) {
                result = -2;
            } else {
                for (int i = 0; i < oldArray.length; i++) {
                    if (Integer.parseInt(newArray[i]) != Integer.parseInt(oldArray[i])) {
                        if (Integer.parseInt(newArray[i]) > Integer.parseInt(oldArray[i])) {
                            result = 1;
                        } else {
                            result = -1;
                        }
                        break;
                    }
                }

            }
        } catch (Exception e) {
            result = -2;
        }
        return result;
    }

    public static String computePercent(double a, double b, int precision) {
        double percent = a / b;
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(precision);
        //最后格式化并输出
        return nt.format(percent);
    }

    /**
     * 对list简单分页
     *
     * @param list：分页的list信息
     * @param page：当前页数（首页page=1）
     * @param pageNum：一页显示的条数
     * @param <T>
     * @return
     */
    public static <T> List<T> doPageList(List<T> list, int page, int pageNum) {
        List<T> backList = new ArrayList<T>();
        if (!isNullOrZero(list)) {
            int size = list.size();
            if (page == 1) {//首页
                int p = size > pageNum ? pageNum : size;
                backList = list.subList(0, p);
            } else {
                int p = (page - 1) * pageNum;
                int n = p + pageNum;
                p = p > size ? size : p;
                n = n > size ? size : n;
                backList = list.subList(p, n);
            }
        }

        return backList;
    }

    /**
     * 校验正整数
     *
     * @param str
     * @return
     */
    public static boolean isInt(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 校验正数，小数精确到小数点后3位
     *
     * @param str
     * @return
     */
    public static boolean isPositiveNumeric(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return false;
        }
        if (isInt(str)) {
            return true;
        }
        Pattern pattern = Pattern.compile("^[0-9]{1,}\\.[0-9]{1,3}$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 获取异常的完整信息
     *
     * @param ex
     * @return
     */
    public static String getExceptionAllInformation(Exception ex) {
        String ret = "";
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            ex.printStackTrace(pout);
            ret = new String(out.toByteArray());
            pout.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * base64转inputStream
     *
     * @param base64string
     * @return
     */
    public static InputStream BaseToInputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {

            byte[] bytes1 = Base64Utils.decodeFromString(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return stream;
    }

    /**
     * 分页 如果页数或者每页条数为空，那么默认取20条数据
     *
     * @param page         页数
     * @param everyPageNum 每页条数
     * @return
     */
    public static String publicPagInation(String page, String everyPageNum) {
        String limit = "";
        int startNum = 0;
        int endNum = 0;
        if (!isNullOrEmpty(page) && !isNullOrEmpty(everyPageNum)) {
            int pageInt = Integer.parseInt(page);
            endNum = Integer.parseInt(everyPageNum);
            if (pageInt > 1) {
                startNum = (pageInt - 1) * endNum;
            }
            limit = " limit " + startNum + "," + endNum;
        } else {
            limit = " limit " + 0 + "," + 20;
        }
        return limit;
    }

    /**
     * 集合拆分为指定大小的小集合
     *
     * @param resList 集合
     * @param count   指定大小
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> split(List<T> resList, int count) {

        if (resList == null || count < 1) {
            return null;
        }
        List<List<T>> ret = new ArrayList<List<T>>();
        int size = resList.size();
        if (size <= count) { //数据量不足count指定的大小
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            //前面pre个集合，每个大小都是count个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<T>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;

    }

    /**
     * 数组拆分为指定大小的小集合
     *
     * @param resList
     * @param count
     * @param <T>
     * @return
     */
    public static <T> List<JSONArray> jsonSplit(JSONArray resList, int count) {

        if (resList == null || count < 1) {
            return null;
        }
        List<JSONArray> ret = new ArrayList<JSONArray>();
        int size = resList.size();
        if (size <= count) { //数据量不足count指定的大小
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            //前面pre个集合，每个大小都是count个元素
            for (int i = 0; i < pre; i++) {
                JSONArray itemList = new JSONArray();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if (last > 0) {
                JSONArray itemList = new JSONArray();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;

    }

    public static String getRealPath() {
        try {
            String classPath = null;
            try {
                // toURI() 20% to 空格
                classPath = Util.class.getClassLoader().getResource("/").toURI().getPath();
            } catch (URISyntaxException e) {

                e.printStackTrace();
            }

            String rootPath = "";
            // windows下
            if ("\\".equals(File.separator)) {
                rootPath = classPath.substring(1, classPath.indexOf("/WEB-INF/classes"));
                rootPath = rootPath.replace("/", "\\");
            }
            // linux下
            if ("/".equals(File.separator)) {
                rootPath = classPath.substring(0, classPath.indexOf("/WEB-INF/classes"));
                rootPath = rootPath.replace("\\", "/");
            }
            return rootPath + File.separator;
        } catch (Exception e) {
            return null;
        }
    }

    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 从request获取json
     *
     * @param request
     * @return
     */
    public static JSONObject getJSON(HttpServletRequest request) {
         return getJSON(request,false);
    }

    public static JSONObject getJSONExceptBody(HttpServletRequest request) {
        try {
            String jsonString = request.getParameter("data");
            //jsonString = Util.urlDecode(jsonString);
            JSONObject param = new JSONObject();
            if (!isNullOrEmpty(jsonString)) {
                if (!jsonString.startsWith("{")) {
                    jsonString = urlDecode(jsonString);
                }
                if (jsonString.trim().startsWith("{")) {
                    param = JSON.parseObject(jsonString);
                    if (param == null) {
                        param = new JSONObject();
                    }
                }
            }
            if (param.isEmpty()) {
                Enumeration<String> var = request.getParameterNames();
                while (var.hasMoreElements()) {
                    String name = var.nextElement();//调用nextElement方法获得元素
                    String value = request.getParameter(name);
                    param.put(name, value);
                }
            }
//            param.putAll(request.getParameterMap());
            return param;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从request获取json
     *
     * @param request
     * @return
     */
    public static JSONObject getJSON(HttpServletRequest request, Boolean urlDecode) {
        try {
            String jsonString = request.getParameter("data");
            JSONObject param = new JSONObject();
            if (!isNullOrEmpty(jsonString)) {
                if (!jsonString.startsWith("{")) {
                    jsonString = urlDecode(jsonString);
                }

                if(jsonString.trim().startsWith("{")) {
                    param = JSON.parseObject(jsonString);
                    if(param==null) {
                        param=new JSONObject();
                    }
                }
            }
            JSONObject jsonBody = HttpUtil.getBodyJSON(request, "UTF-8");
            if (jsonBody != null) {
                request.getSession().setAttribute("SESSION_BODY",jsonBody);
                ApplyObject(param, jsonBody);
            }
            else
            {
                jsonBody= (JSONObject) request.getSession().getAttribute("SESSION_BODY");
                if(jsonBody!=null)
                {
                    ApplyObject(param, jsonBody);
                }
            }
            if (param.isEmpty()) {
                Enumeration<String> var = request.getParameterNames();
                while (var.hasMoreElements()) {
                    String name = var.nextElement();//调用nextElement方法获得元素
                    String value =request.getParameter(name);
                    param.put(name, value);
                }
            }
//            param.putAll(request.getParameterMap());
            return param;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从request的json中获取参数
     *
     * @param request
     * @param parameter
     * @return
     */
    public static String getJSONParameter(HttpServletRequest request, String parameter) {
        String val = "";
        try {
            val = request.getParameter(parameter);
            if (isNullOrEmpty(val)) {
                JSONObject json = getJSON(request);
                return json == null ? "" : json.getString(parameter);
            } else {
                return val;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 从request的json中获取参数
     *
     * @param request
     * @return
     */
    public static String getGroupId(HttpServletRequest request) {
        try {
            String groupNo = request.getHeader("groupId");
            return "001";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    /***
     * 日期月份减一个月
     *
     * @param datetime 2016-11
     * @return 2016-10
     */
    public static String dateFormat(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.MONTH, -1);
        date = cl.getTime();
        return sdf.format(date);
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(date);
    }

    public static String dateFormat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /****
     * 传入具体日期 ，返回具体日期减一个月。
     *
     * @param date 2016-12-30
     * @return 2016-11-30
     * @throws ParseException
     */
    public static Date subMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);

        rightNow.add(Calendar.MONTH, -1);
        Date dt1 = rightNow.getTime();

        return dt1;
    }

    /****
     * 传入具体日期 ，返回具体日期加年。
     *
     * @param date 2016-11-30
     * @return 2016-12-30
     * @throws ParseException
     */
    public static Date addYear(Date date, int years) throws ParseException {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);

        rightNow.add(Calendar.YEAR, years);
        Date dt1 = rightNow.getTime();

        return dt1;
    }

    /****
     * 传入具体日期 ，返回具体日期加一个月。
     *
     * @param date 2016-11-30
     * @return 2016-12-30
     * @throws ParseException
     */
    public static Date addMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);

        rightNow.add(Calendar.MONTH, 1);
        Date dt1 = rightNow.getTime();

        return dt1;
    }

    /****
     * 传入具体日期 ，返回具体日期加天数。
     *
     * @param date 2016-11-30
     * @param days 10
     * @return 2016-12-9
     * @throws ParseException
     */
    public static Date addDays(String date, Integer days) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(date);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);

            rightNow.add(Calendar.DATE, days);
            Date dt1 = rightNow.getTime();

            return dt1;
        } catch (ParseException e) {
            return null;
        }
    }

    /****
     * 获取月末最后一天
     *
     * @param sDate 2016-11-24
     * @return 30
     */
    public static String getMonthMaxDay(String sDate) {
        SimpleDateFormat sdf_full = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf_full.parse(sDate + "-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        int last = cal.getActualMaximum(Calendar.DATE);
        return String.valueOf(last);
    }

    /**
     * 某一个月第一天和最后一天
     *
     * @param date
     * @return
     */
    public static Map<String, String> getFirstday_Lastday_Month(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date theDate = calendar.getTime();

        //第一天
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first);
        day_first = str.toString();

        //最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last);
        day_last = endStr.toString();

        Map<String, String> map = new HashMap<String, String>();
        map.put("first", day_first);
        map.put("last", day_last);
        return map;
    }

    /**
     * 判断是否是月末
     *
     * @param date 2016-11-30
     * @return true
     */
    public static boolean isMonthEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE) == cal
                .getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Date JudgeIntervalTime(String dateInterval) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if ("0".equals(dateInterval)) {
            format.format(cal.getTime());
        } else if ("1".equals(dateInterval)) {
            cal.add(Calendar.DATE, -1);
            format.format(cal.getTime());
        } else if ("7".equals(dateInterval)) {
            cal.add(Calendar.DATE, -7);
            format.format(cal.getTime());
        } else if ("30".equals(dateInterval)) {
            cal.add(Calendar.DATE, -30);
            format.format(cal.getTime());
        } else {
            cal.add(Calendar.DATE, -10000);
        }
        return cal.getTime();
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.MONTH)) + 1;
    }

    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static String getMaxDayThisMonth() {
        Calendar calendar = Calendar.getInstance();
        // 设置时间,当前时间不用设置
        // calendar.setTime(new Date());
        // 设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

        // 打印
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(calendar.getTime());
    }


    public static boolean isSuccess(JSONObject json) {
        if (json == null) {
            return false;
        }
        Integer errcode = json.getInteger("errcode");
        return errcode == null || errcode == 0;
    }

    // 随机生成16位字符串
    public static String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取两个日期之间的日期
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 日期集合
     */
    public static List<String> getBetweenDates(String startDate, String endDate) {

        Date start = parseDate(startDate, "yyyyMMdd");
        Date end = parseDate(endDate, "yyyyMMdd");
        List<String> result = new ArrayList<String>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (!tempStart.after(tempEnd)) {
            result.add(formatDate(tempStart.getTime(), "yyyyMMdd"));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    /**
     * 根据身份证获取年龄
     * @param idCard
     * @return
     */
    public static int getAgeByIdCard(String idCard) {
        int iAge = 0;
        Calendar cal = Calendar.getInstance();
        String year = idCard.substring(6, 10);
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.valueOf(year);
        return iAge;
    }

    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new Comparator<String>(){

                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });

        sortMap.putAll(map);

        return sortMap;
    }


    /**
     * 使用 Map按key进行排序
     * @param json
     * @return
     */
    public static Map<String, String> sortMapByKey(JSONObject json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(
                new Comparator<String>(){

                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });

        for(String key:json.keySet())
        {
            sortMap.put(key,json.getString(key));
        }
        return sortMap;
    }


    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 查询匹配的数量
     * @param source
     * @param regexStr
     * @return
     */
    public static int finder(String source, String regexStr) {
        String regex = "[a-zA-Z]+";
        if (regexStr != null && !"".equals(regexStr)) {
            regex = regexStr;
        }
        int n = 0;
        try {
            Pattern expression = Pattern.compile(regex);
            Matcher matcher = expression.matcher(source);


            while (matcher.find()) {
                n++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * 查询匹配的不重复数量
     * @param source
     * @param regexStr
     * @return
     */
    public static int findUniqueCount(String source, String regexStr) {
        String regex = "[a-zA-Z]+";
        if (regexStr != null && !"".equals(regexStr)) {
            regex = regexStr;
        }
        Map<String,String> map =new HashMap<>();
        try {
            Pattern expression = Pattern.compile(regex);
            Matcher matcher = expression.matcher(source);

            while (matcher.find()) {
                String str = matcher.group(1);
                map.put(str, str);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return map.size();
    }

    /**
     * 延时执行方法
     * @param runnable
     * @param seconds
     */
    public static void delayRun( final Runnable runnable,long seconds)
    {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(runnable!=null) {
                    runnable.run();
                }
                this.cancel();
                timer.cancel();
            }
        }, seconds*1000);
    }

    public static void main(String[] args)
    {
        Runnable r=new Runnable() {
            @Override
            public void run() {
                System.out.println("delay runed!");
            }
        };
        System.out.println("begin to run");
        delayRun(r,5);
    }

    public static String getGBK(String str) {
        String utf8 = null;
        String gbk = str;
        try {
            utf8 = new String(str.getBytes("UTF-8"));
            String unicode = new String(utf8.getBytes(), "UTF-8");
            gbk = new String(unicode.getBytes("GBK"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return gbk;
    }


    public static String getCutString(String s,int num){
        int k=0;
        String temp="";
        for (int i = 0; i <s.length(); i++)
        {
            byte[] b=(s.charAt(i)+"").getBytes();
            k=k+b.length;
            if(k>num)
            {
                break;
            }
            temp=temp+s.charAt(i);
        }
        return temp;
    }

/**
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在
     * 包含汉字的字符串时存在隐患，现调整如下：
     * @param src 要截取的字符串
     * @param start_idx 开始坐标（包括该坐标)
     * @param end_idx   截止坐标（包括该坐标）
     * @return
     */
    public static String substring(String src, int start_idx, int end_idx){
        byte[] b = src.getBytes();
        String tgt = "";
        for(int i=start_idx; i<=end_idx; i++){
            tgt +=(char)b[i];
        }
        return tgt;
    }

}
