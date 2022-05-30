//package com.example.mymanager;
//
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//
//public class FileClient {
//
////    public static void main(String args[]) {
////    }
//    public static void fileServer(String rootPath, File file) {
//
//        try {
//            HttpClient httpclient = HttpClientBuilder.create().build();
//
//            // xml파일을 수신할 서버주소
//            HttpPost httppost = new HttpPost("https://ryo-cnnapi.run.goorm.io/html/imgfile");
//            StringBuffer fileData = new StringBuffer(1000);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    new FileInputStream(rootPath +	file), "utf-8"));
//            System.out.println("저장경로 = " + rootPath + file);
//            char[] buf = new char[1024];
//            int numRead = 0;
//            while ((numRead = reader.read(buf)) != -1) {
//                fileData.append(buf, 0, numRead);
//            }
//            reader.close();
//            String xml_string_to_send = fileData.toString();
//            StringEntity string_entity = new StringEntity(xml_string_to_send,"UTF-8");
//            string_entity.setContentType("application/x-www-form-urlencoded");
//            httppost.setEntity(string_entity);
//            HttpResponse httpResponse = httpclient.execute(httppost);
//            System.out.println("Response Code : "+ httpResponse.getStatusLine().getStatusCode()+ "    Response StatusLine : "
//                    + httpResponse.getStatusLine());
//
//            HttpEntity response_entity = httpResponse.getEntity();
//            String response_string = EntityUtils.toString(response_entity);
//            System.out.println(response_string);
//
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//}
