package com.example.ppeepfinal.utilities;

import android.net.Uri;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NetworkUtils {
    final static String REGISTER_BASE_URL =
            "http://192.168.0.108/api/api/user/register";
    final static String PHONE_CHECK_URL =
            "http://192.168.0.108/api/api/user/checkPhoneNo";


    // final static String PARAM_QUERY = "q";
   /* final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";*/



    public static URL buildRegisterUrl() {
        Uri builtUri = Uri.parse(REGISTER_BASE_URL).buildUpon().build();
        URL registerurl = null;
        try {
            registerurl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return registerurl;
    }

    public static URL buildPhoneCheckUrl() {
        Uri builtUri = Uri.parse(PHONE_CHECK_URL).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */


    public static String getResponseFromHttpUrl(URL url, String phone) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

       // setPhoneNo();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "phone", phone);

        OutputStream os = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(param));
        writer.flush();
        writer.close();
        os.close();
        urlConnection.connect();



        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String postUserNameResponseUrl(URL registerurl, String phone,String name) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) registerurl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

        // setPhoneNo();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "contact", phone);
        param.put( "first_name",name);

        OutputStream os = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(param));
        writer.flush();
        writer.close();
        os.close();
        urlConnection.connect();



        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }



    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
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
}