package azuresky.smartdrug;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2015/12/17.
 */
public class FragmentHttp extends Fragment {


    private HttpURLConnection httpURLConnection;
    private URL url;
    private Button post;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_http,container,false);
        post = (Button)view.findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("table","a");
                params.put("col1","2015-12-1202:23:33");
                params.put("col2","2015/12/12 02:23:33");
                params.put("col3","2015-12-12 02:23");
                params.put("col4", "2015-12-12 02:23:33");
                params.put("col5","drug");
                params.put("col6", "hello");

                try {
                    doPostAsyn("http://114.45.143.100:8080/test3.php",getPostDataString(params));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static void doPostAsyn(final String urlStr, final String params) throws Exception
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    String result = doPost(urlStr, params);
                    Log.d("POST", result);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            };
        }.start();

    }
    public static String doPost(String url, String param)
    {
        PrintWriter printWriter = null;
        BufferedReader bufReader = null;
        String result = "";
        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            //--- 設定連線的屬性---
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setUseCaches(false);
            //--------------------
            connection.setDoOutput(true);   //設定是否向http提出請求,預設為false
            connection.setDoInput(true);
            connection.setReadTimeout(10000);   //設定time-out時間
            connection.setConnectTimeout(10000); //設定time-out時間

            if (param != null && !param.trim().equals(""))
            {
                // 透過PrintWriter處理資料流串接URLConnection獲取的輸出資料流，得到物件實體
                printWriter = new PrintWriter(connection.getOutputStream());
                // 將資料寫至輸出串流
                printWriter.print(param);
                // 強制寫出所有資料至串流中
                printWriter.flush();
            }

            // 透過BufferReader處理資料流串接InputStreamReader節點資料流讀取資料
            bufReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufReader.readLine()) != null)   //如果還有下一行就繼續讀取
            {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 關閉與釋放資源
        finally
        {
            try
            {
                if (printWriter != null)
                    printWriter.close();
                if (bufReader != null)
                    bufReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}


