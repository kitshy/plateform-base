package cn.plateform.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * 敏感词汇过滤
 */
@Component
public class SensitiveWordUtil {

    /**
     * 敏感词匹配规则
     */
    public static final int MinMatchTYpe = 1;      //最小匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国]人
    public static final int MaxMatchType = 2;      //最大匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国人]

    /**
     * todo暂时用不   =--- 获取文件内部的敏感词汇到set集合
     */
    public Set<String> getFileToSet(String url){
        Set<String> set = new HashSet<>();
        try {
            ClassPathResource resource = new ClassPathResource("static/贪腐词库.txt");
            File file =  resource.getFile();
            //此方法在linux的jar不行
            //File file = ResourceUtils.getFile("classpath:static/贪腐词库.txt");
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(reader);
            String str;
            while ((str=bf.readLine())!=null){
                set.add(str);
            }
            System.out.println(file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }


    /**
     * 敏感词集合
     */
    public static HashMap sensitiveWordMap;

    /**
     * 类锁---初始化敏感词库
     * @param sensitiveWord
     */
    public static synchronized void init(Set<String> sensitiveWord){
        initSensitiveWordMap(sensitiveWord);
    }

    /**
     * 初始化敏感词库，构建dfa算法
     * @param sensitiveWordSet
     */
    private static void initSensitiveWordMap(Set<String> sensitiveWordSet){
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        String key;
        Map nowMap;
        Map<String,String> newWorMap;
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while (iterator.hasNext()){
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                //转换成char型
                char keyChar = key.charAt(i);
                //库中获取关键字
                Object wordMap = nowMap.get(keyChar);
                //如果存在该key，直接赋值，用于下一个循环获取
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<>();
                    //不是最后一个
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    //最后一个
                    nowMap.put("isEnd", "1");
                }
            }
        }
        
    }

    /**
     * 判断文字是否包含敏感字符
     * @param text 文字
     * @param matchType   匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return
     */
    public static boolean contains(String text,int matchType){
        boolean flag = false;
        for (int i=0;i<text.length();i++){
            int matchFlag= checkSensitiveWord(text,i,matchType);
            if (matchFlag>0){
                flag=true;
            }
        }
        return flag;
    }

    /**
     * 判断文字是否包含敏感字符 --最大匹配规则
     * @param text
     * @return
     */
    public static boolean contanis(String text){
        return contains(text,MaxMatchType);
    }

    /**
     * 获取文字中的敏感词汇
     * @param text     文字
     * @param matchType    匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return
     */
    public static Set<String> getSensitiveWord(String text,int matchType){
        Set<String> sensitiveWordList = new HashSet<>();
        for (int i=0;i<text.length();i++){
            //判断是否包含敏感字符
            int length  = checkSensitiveWord(text,i,matchType);
            if (length>0){   //存在,加入list中
                sensitiveWordList.add(text.substring(i,i+length));
                i=i+length-1; //减1的原因，是因为for会自增
            }
        }
        return sensitiveWordList;
    }

    /**
     *  获取文字中的敏感词汇 ---最大匹配规则
     * @param text
     * @return
     */
    public static Set<String> getSensitiveWord(String text){
        return getSensitiveWord(text,MaxMatchType);
    }

    /**
     * 替换敏感词汇(替换的为字符char)
     * @param text     文本
     * @param replaceChar 替换的字符，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符：*， 替换结果：我爱***
     * @param matchType   敏感词匹配规则
     * @return
     */
    public static String replaceSensitiveWord(String text,char replaceChar,int matchType){
        String resultText = text;
        //获取所有的敏感词
        Set<String> set = getSensitiveWord(text,matchType);
        Iterator<String > iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()){
            word=iterator.next();
            replaceString=getReplaceChars(replaceChar,word.length());
            resultText = resultText.replaceAll(word,replaceString);
        }
        return resultText;
    }

    /**
     * 替换敏感词汇(替换的为字符char)  ---最大匹配规则
     * @param text
     * @param replaceChar
     * @return
     */
    public static String replaceSensitiveWord(String text,char replaceChar){
        return replaceSensitiveWord(text,replaceChar,MaxMatchType);
    }

    /**
     * 替换敏感词汇(替换的为字符串string)
     * @param text     文本
     * @param replaceText 替换的字符串，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符：*， 替换结果：我爱***
     * @param matchType   敏感词匹配规则
     * @return
     */
    public static String replaceSensitiveWord(String text,String replaceText,int matchType){
        String resultText = text;
        //获取所有的敏感词
        Set<String > set = getSensitiveWord(text,matchType);
        Iterator<String > iterator = set.iterator();
        String word;
        while (iterator.hasNext()){
            word=iterator.next();
            resultText = resultText.replaceAll(word,replaceText);
        }
        return resultText;
    }

    /**
     * 替换敏感词汇(替换的为字符串String)  ---最大匹配规则
     * @param text
     * @param replaceText
     * @return
     */
    public static String replaceSensitiveWord (String text,String replaceText){
        return replaceSensitiveWord(text, replaceText, MaxMatchType);
    }

    /**
     * 获取替换的字符串
     * @param replaceChar
     * @param length
     * @return
     */
    private static String getReplaceChars(char replaceChar,int length){
        String resultReplace = String.valueOf(replaceChar);
        for (int i=1;i<length;i++){
            resultReplace+=replaceChar;
        }
        return resultReplace;
    }

    /**
     * 检查文中是否包含敏感词，如果存中返回敏感词长度，不存在返回0
     * @param text
     * @param beginIndex
     * @param matchType
     * @return
     */
    private static int checkSensitiveWord(String text,int beginIndex,int matchType){
        //敏感词结束标识位：用于敏感词只有1位的情况
       boolean flag = false;
        //匹配标识数默认为0
       int matchFlag = 0;
       char word;
       Map nowMap =sensitiveWordMap;
       for (int i=beginIndex;i<text.length();i++){
           word = text.charAt(i);
           //获取指定key
           nowMap=(Map) nowMap.get(word);
           if (nowMap!=null){         //存在，则判断是否为最后一个
               //找到相应key，匹配标识+1
               matchFlag++;
               //如果为最后一个匹配规则,结束循环，返回匹配标识数
               if ("1".equals(nowMap.get("isEnd"))){
                   flag=true;
                   //最小规则，直接返回,最大规则还需继续查找
                   if (MinMatchTYpe==matchFlag){
                       break;
                   }
               }
           }else {
               break;
           }
       }
       if (matchFlag<2||!flag){
           matchFlag=0;
       }
       return matchFlag;
    }
}
