package com.threeti.teamlibrary.net;

import com.threeti.teamlibrary.utils.MyLogUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class BaseHttpRequest {
    public static final String GET = "get";
    public static final String POST = "post";
    public static final String HTTP_STATE = "httpstate";
    public static final String HTTP_DATA = "httpdata";
    public static final int HTTP_TIMEOUT = 0x11;
    public static final int HTTP_OTHER = 0x12;

    private static final int TIMEOUT = 30;
    private int timeOut = 0;

    public void setTime(int time) {
        timeOut = time;
    }

    public Map<String, Object> makeHTTPRequest(String method, String webAddress, String resuestdata)
            throws ConnectTimeoutException, Exception {
        return makeHTTPRequest(method, webAddress, null, resuestdata, null, null, null, null);
    }

    public Map<String, Object> makeHTTPRequest(String method, String webAddress, Map<String, String> data)
            throws ConnectTimeoutException, Exception {
        return makeHTTPRequest(method, webAddress, data, null, null, null, null, null);
    }

    public Map<String, Object> makeHTTPRequest(String method, String webAddress, Map<String, String> data,
                                               Map<String, String> header) throws ConnectTimeoutException, Exception {
        return makeHTTPRequest(method, webAddress, data, null, header, null, null, null);
    }

    public Map<String, Object> makeHTTPRequest(String method, String webAddress, Map<String, String> data,
                                               String requestdata, Map<String, String> header, Map<String, String> filesData, String authName,
                                               String authPswd) throws ConnectTimeoutException, Exception {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        HttpEntity httpEntity = null;
        HttpClient httpClient = getNewHttpClient(timeOut);
        try {
            HttpRequestBase httpRequest = null;
            if (method.equals(GET))
                httpRequest = createHttpGet(webAddress, data, header, authName, authPswd);
            else
                httpRequest = createHttpPost(webAddress, data, requestdata, header, filesData, authName, authPswd);
            HttpResponse httpResponse = executeHttpRequest(httpClient, httpRequest);
            httpEntity = httpResponse.getEntity();
            String response = "";
            response = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            dataMap.put(HTTP_DATA, response);
            dataMap.put(HTTP_STATE, httpResponse.getStatusLine().getStatusCode());
        } catch (ConnectTimeoutException e) {
            dataMap.put(HTTP_DATA, "");
            dataMap.put(HTTP_STATE, HTTP_TIMEOUT);
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            dataMap.put(HTTP_DATA, "");
            dataMap.put(HTTP_STATE, HTTP_OTHER);
            e.printStackTrace();
            throw e;
        } finally {
            if (null != httpEntity) {
                httpEntity.consumeContent();
            }
            httpClient.getConnectionManager().shutdown();
        }
        MyLogUtil.i("dataMap", dataMap.toString() + "");
        return dataMap;
    }

    private HttpClient getNewHttpClient(int time) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(params, time == 0 ? TIMEOUT * 1000 : time * 1000);
            HttpConnectionParams.setSoTimeout(params, time == 0 ? TIMEOUT * 1000 : time * 1000);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
            HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
            HttpClient client = new DefaultHttpClient(ccm, params);
            return client;
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private HttpResponse executeHttpRequest(HttpClient httpClient, HttpRequestBase httpRequest) throws IOException {
        try {
            return httpClient.execute(httpRequest);
        } catch (IOException e) {
            httpRequest.abort();
            throw e;
        }
    }

    private HttpGet createHttpGet(String url, Map<String, String> data, Map<String, String> customizedHeader,
                                  String authName, String authPswd) throws IllegalArgumentException {
        url += getGetParams(data);
        MyLogUtil.i("httpget_url", url + "");
        HttpGet httpRequest = null;
        try {
            httpRequest = new HttpGet(url);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        addHeader(httpRequest, customizedHeader);
        addBasicAuth(httpRequest, authName, authPswd);
        return httpRequest;
    }

    private String getGetParams(Map<String, String> data) {
        StringBuilder urlExt = new StringBuilder();
        if (data != null) {
            urlExt.append('&');
            for (Map.Entry<String, String> item : data.entrySet()) {
                if (item.getValue() != null) {
                    @SuppressWarnings("deprecation")
                    String key = URLEncoder.encode(item.getKey());
                    @SuppressWarnings("deprecation")
                    String value = URLEncoder.encode(item.getValue());
                    urlExt.append(key).append("=").append(value).append("&");
                }
            }
            urlExt.deleteCharAt(urlExt.length() - 1);
        }
        return urlExt.toString();
    }

    private HttpPost createHttpPost(String url, Map<String, String> data, String requestdata,
                                    Map<String, String> customizedHeader, Map<String, String> filesData, String authName, String authPswd)
            throws Exception {

        MyLogUtil.i("httppost_url", url + "");
        if (data != null)
            MyLogUtil.i("httppost_data", data + "");
        if (filesData != null)
            MyLogUtil.i("httppost_file", filesData + "");

        HttpPost httpRequest = new HttpPost(url);
        addHeader(httpRequest, customizedHeader);
        addBasicAuth(httpRequest, authName, authPswd);
        try {
            if (filesData != null) {
                MultipartEntity mpEntity = new MultipartEntity();
                StringBody stringBody = null;
                FormBodyPart fbp;
                if (data != null) {
                    for (String key : data.keySet()) {
                        if (data.get(key) != null) {
                            stringBody = new StringBody(data.get(key), Charset.forName("UTF-8"));
                            fbp = new FormBodyPart(key, stringBody);
                            mpEntity.addPart(fbp);
                        }
                    }
                }

                for (String key : filesData.keySet()) {
                    String filePath = filesData.get(key);
                    if (filePath != null) {
                        File sendFile = new File(filePath);
                        if (sendFile.exists()) {
                            FileBody fileBody;
                            fileBody = new FileBody(sendFile, "application/octet-stream");
                            fbp = new FormBodyPart(key, fileBody);
                            mpEntity.addPart(fbp);
                        }
                    }
                }
                httpRequest.setEntity((HttpEntity) mpEntity);
            } else {
                if (data != null && requestdata == null) {
                    ArrayList<NameValuePair> params = getPostParams(data);
                    httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                } else {
                    httpRequest.setEntity(new StringEntity(requestdata, HTTP.UTF_8));
                }
            }

        } catch (Exception e) {
            throw e;
        }
        return httpRequest;
    }

    public String put(String httpurl, String filepath) throws Exception {
        String result = "";
        File file = new File(filepath);
        URL url = new URL(httpurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "");
        OutputStream out = new DataOutputStream(conn.getOutputStream());

        StringBuilder sb = new StringBuilder();

//			sb.append("--").append(BOUNDARY).append("\r\n");
//			sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"123456789.txt\"");
//			sb.append("Content-Type: text/plain");
//			sb.append("\r\n");

        byte[] data = sb.toString().getBytes();
        out.write(data);
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        //byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
        //out.write(end_data);
        out.flush();
        out.close();
        // 定义BufferedReader输入流来读取URL的响应
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (FileNotFoundException e) {
            result = "";
        }
        return result;
    }

    private ArrayList<NameValuePair> getPostParams(Map<String, String> data) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        if (data != null) {
            for (Map.Entry<String, String> item : data.entrySet()) {
                if (item.getValue() != null) {
                    params.add(new BasicNameValuePair(item.getKey(), item.getValue()));
                }
            }
        }
        return params;
    }

    private void addHeader(HttpRequestBase request, Map<String, String> customizedHeader) {
        if (customizedHeader != null) {
            for (Map.Entry<String, String> item : customizedHeader.entrySet()) {
                Header header = new BasicHeader(item.getKey(), item.getValue());
                request.addHeader(header);
            }
        }
    }

    private void addBasicAuth(HttpRequestBase request, String authName, String authPswd) {
        if (authName != null && authPswd != null) {
            UsernamePasswordCredentials credentials = null;
            credentials = new UsernamePasswordCredentials(authName, authPswd);
            BasicScheme scheme = new BasicScheme();
            Header authorizationHeader = null;
            try {
                authorizationHeader = scheme.authenticate(credentials, request);
                request.addHeader(authorizationHeader);
            } catch (AuthenticationException e) {
                e.printStackTrace();
            }
        }
    }

    private class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
                UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

}
