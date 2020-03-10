package cn.plateform.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;

/**
 * 参数校验
 */
@Component
public class ParamUtil {

    //多参判断
    public static boolean isNullOrEmpty(String... str){
        boolean t = false;
        if (str!=null){
            for (int i=0;i<str.length;i++){
                String s = str[i];
                s = s==null? s:s.trim();
                if (s==null||"".equals(s)||"undefined".equals(s)||"null".equals(s)){
                    t=true;
                    break;
                }
            }
        }else {
            t=true;
        }
        return t;
    }

    public static boolean isNullOrEmpty(Object obj){
        if (!(obj instanceof Object[])){
            if (obj instanceof Date){
                if (obj.equals(new Date(0L))){               //0L表示标准格林时间，此处是如果时间如果为标准格林时间表示为null
                    return true;
                }
            }else if (obj==null||"".equals(obj)){
                return true;
            }
        }else {
            Object[] o = (Object[])(Object[]) obj;         //?
            for (int i=0;i<o.length;i++){
                Object object = o[i];
                if (object instanceof Date){
                    if (object.equals(new Date(0L))){
                        return true;
                    }
                }else if (object==null||"".equals(object)){
                    return true;
                }
            }
        }
        return false;
    }

    public static JSONObject getJSONExceptionBody(HttpServletRequest request){
        try {
            String jsonString = request.getParameter("data");
            JSONObject parm =new JSONObject();
            if (!isNullOrEmpty((Object) jsonString)){
                if (!jsonString.startsWith("{")){
                    jsonString = urlDeCode(jsonString);
                }
                if (jsonString.trim().startsWith("{")){
                    parm=JSON.parseObject(jsonString);
                    if (parm == null) {
                        parm=new JSONObject();
                    }
                }
            }
            if (parm.isEmpty()){
                Enumeration var = request.getParameterNames();
                while (var.hasMoreElements()){
                    String name = (String) var.nextElement();
                    String value = request.getParameter(name);
                    parm.put(name,value);
                }
            }
            return parm;
        }catch (Exception va){
            va.printStackTrace();
            return null;
        }
    }

    //加密，
    public static String urlEncode(String str){
        String url = "";
        try {
            url = URLEncoder.encode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
    //解密
    public static String urlDeCode(String str){
        String url="";
        try {
            url = URLDecoder.decode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

}
