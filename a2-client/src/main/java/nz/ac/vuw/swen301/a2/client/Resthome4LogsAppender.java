package nz.ac.vuw.swen301.a2.client;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class Resthome4LogsAppender{

    String logServiceURL = "http://localhost:8080/resthome4logs/logs";



    public Resthome4LogsAppender(){

    }


    public String getLogServiceURL(){
        return logServiceURL;
    }

    public void setLogServiceURL(String s){
        logServiceURL = s;
    }

    public void post_up(String content) throws URISyntaxException, IOException {
        String scheme = logServiceURL.substring(0,4);
        String host = logServiceURL.substring(7,21);
        String path = logServiceURL.substring(22);
        URIBuilder builder = new URIBuilder();
       // builder.setScheme(scheme).setHost(host).setPath(path);
        builder.setScheme("http").setHost("localhost:8080").setPath("/resthome4logs/logs");
        URI uri = builder.build();


        // create and execute the request
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(uri);
        request.setEntity(new StringEntity(content));
        request.setHeader("Content-type","application/json");
        /*
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("body", content));
        request.setEntity(new UrlEncodedFormEntity(params));


         */
        HttpResponse response = httpClient.execute(request);

        // this string is the unparsed web page (=html source code)
        String con = EntityUtils.toString(response.getEntity());
    }

}
