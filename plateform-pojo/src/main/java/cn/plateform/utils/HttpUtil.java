package cn.plateform.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


/**
 * 这个Https协议工具类，采用HttpsURLConnection实现。
 * 提供get和post两种请求静态方法
 * 
 * @author marker
 * @date 2014年8月30日
 * @version 1.0
 */
public class HttpUtil {

	private static int defaultTimeOut=5000;
	
	private static TrustManager myX509TrustManager = new X509TrustManager() {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	};

	
	public static String sendHttpsPOST(String url, String data) {
		String result = null;

		try {
			// 设置SSLContext
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { myX509TrustManager },
					null);

			// 打开连接
			// 要发送的POST请求url?Key=Value&amp;Key2=Value2&amp;Key3=Value3的形式
			URL requestUrl = new URL(url);
			HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl
					.openConnection();
			httpsConn.setConnectTimeout(defaultTimeOut);
			// 设置套接工厂
			httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());

			// 加入数据
			httpsConn.setRequestMethod("POST");
			httpsConn.setDoOutput(true);
			OutputStream out = httpsConn.getOutputStream() ;
			 
			if (data != null) {
                out.write(data.getBytes("UTF-8"));
            }
			out.flush();
			out.close();

			// 获取输入流
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpsConn.getInputStream()));
			int code = httpsConn.getResponseCode();
			if (HttpsURLConnection.HTTP_OK == code) {
				String temp = in.readLine();
				/* 连接成一个字符串 */
				while (temp != null) {
					if (result != null) {
                        result += temp;
                    } else {
                        result = temp;
                    }
					temp = in.readLine();
				}
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public static String sendHttpsGET(String url) {
		String result = null;

		try {
			// 设置SSLContext
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { myX509TrustManager },
					null);

			// 打开连接
			// 要发送的POST请求url?Key=Value&amp;Key2=Value2&amp;Key3=Value3的形式
			URL requestUrl = new URL(url);
			HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl
					.openConnection();
			httpsConn.setConnectTimeout(defaultTimeOut);
			// 设置套接工厂
			httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());

			// 加入数据
			httpsConn.setRequestMethod("GET");
//			httpsConn.setDoOutput(true);
			  

			// 获取输入流
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpsConn.getInputStream()));
			int code = httpsConn.getResponseCode();
			if (HttpsURLConnection.HTTP_OK == code) {
				String temp = in.readLine();
				/* 连接成一个字符串 */
				while (temp != null) {
					if (result != null) {
                        result += temp;
                    } else {
                        result = temp;
                    }
					temp = in.readLine();
				}
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	public static String getBody(HttpServletRequest request, String charsetname) throws IOException {
	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    try {  
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {  
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream,charsetname));
	            char[] charBuffer = new char[128];  
	            int bytesRead = -1;  
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {  
	                stringBuilder.append(charBuffer, 0, bytesRead);  
	            }  
	        } else {  
	            stringBuilder.append("");  
	        }  
	    } catch (IOException ex) {
	        throw ex;  
	    } finally {  
	        if (bufferedReader != null) {  
	            try {  
	                bufferedReader.close();  
	            } catch (IOException ex) {
	                throw ex;  
	            }  
	        }  
	    }  
	  
	    body = stringBuilder.toString();  
	    return body;  
	}


	public static JSONObject getBodyJSON(HttpServletRequest request, String charsetname)
	{
		String body;
		try {
			body = getBody(request,charsetname);
			//body=new String(body.getBytes("ISO8859-1"),"UTF-8");
			if (!Util.isNullOrEmpty(body)){
				if(body.startsWith("data="))
				{
					String str=body.substring(5,body.length());
					body= URLDecoder.decode(str,"UTF-8");
				}
				JSONObject json=JSONObject.parseObject(body);
				return json;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		return null;
	}

	public static JSONArray getBodyJSONToJsonArray(HttpServletRequest request, String charsetname)
	{
		String body;
		try {
			body = getBody(request,charsetname);
			//body=new String(body.getBytes("ISO8859-1"),"UTF-8");
			JSONArray json=JSONArray.parseArray(body);
			return json;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public static byte[] downLoadFile(String fileUrl) {
		HttpClient client = new HttpClient();
		GetMethod get = null;
		FileOutputStream output = null;
		byte[] rv=null;
		try {
			get = new GetMethod(fileUrl);

			int i = client.executeMethod(get);

			if (200 == i) {
				rv=get.getResponseBody();
			} else {
				System.out.println("DownLoad file occurs exception, the error code is :" + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
		}
		return rv;
	}

}
