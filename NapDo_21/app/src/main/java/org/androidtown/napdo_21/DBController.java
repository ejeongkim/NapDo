package org.androidtown.napdo_21;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Vector;
import java.util.concurrent.ExecutionException;


        import org.apache.http.NameValuePair;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.ResponseHandler;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.utils.URLEncodedUtils;
        import org.apache.http.impl.client.BasicResponseHandler;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;

/**
 * Created by ejeong on 2016-11-26.
 */

/* 이정 남은 부분 - 데이터가 없을 때, 백그라운드를 바꾼다 */
public class DBController {

    private Vector<NameValuePair> v_para_arr;    //php에 보낼 변수
    private int n_parser_cnt;   //현재 파싱중인 데이터 순번

    private JSONObject jsonobject;
    private JSONArray Json_item;
    public static String s_result;    //PHP로 부터 받은 결과

    public DBController() {
        v_para_arr = new Vector<NameValuePair>();
        n_parser_cnt = 0;
        jsonobject = null;
    }

    public void connect(String s_url) {

        DBConnecter dbConnecter = new DBConnecter();
        dbConnecter.execute(s_url);

        try {
            s_result = dbConnecter.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        dbConnecter.cancel(true);
    }

    //php에 보낼 변수를 설정할때 사용
    public void setParam(String s_name, String s_value) {
        int i;

        //동일한 이름의 s_name이 있다면 제거 (중복 방지)
        for (i = 0; i < v_para_arr.size(); i++) {
            if (s_name.equals(v_para_arr.get(i).getName())) {
                v_para_arr.remove(i);
            }
        }

        v_para_arr.add(new BasicNameValuePair(s_name, s_value));
    }


    //사용자 지정명의 데이터를 파싱하고자 할때,
    public String getResult(String s_name) {

        if(jsonobject == null){
            try {
                //{"data":[{"date" :"2016-11-26"}]}
                //s_result
                jsonobject = new JSONObject(s_result);
                Json_item = jsonobject.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String s_value = "";
        try {
            s_value =   URLDecoder.decode(Json_item.getJSONObject(n_parser_cnt).getString(s_name), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return s_value;
    }

    public boolean setNextData() {
        if(++n_parser_cnt < Json_item.length()){
            return true;
        }else{
            return false;
        }
    }

    public String getResult() {
        return s_result;
    }


    //네트워크 연결 스레드
    private class DBConnecter extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... arg0) {
            return connect((String) arg0[0]);
        }

        protected void onPostExecute(String result) {

        }

        protected String connect(String s_url) {
            String result = "";
            try {
                String url = s_url + "?" + URLEncodedUtils.format(v_para_arr, "utf-8");
                HttpGet request = new HttpGet(url);

                HttpClient client = new DefaultHttpClient();
                ResponseHandler<String> reshandler = new BasicResponseHandler();
                result = client.execute(request, reshandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch(Exception e){
                    return "error";
                }
            }

            @Override
            protected void onPostExecute(String result){
                s_result=result;
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}