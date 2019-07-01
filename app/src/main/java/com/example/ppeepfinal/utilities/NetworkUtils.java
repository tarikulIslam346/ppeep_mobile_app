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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NetworkUtils {
    final static String REGISTER_BASE_URL =
            "https://foodexpress.com.bd/ppeep/public/api/api/user/register";

    final static String PHONE_CHECK_URL =
            "https://foodexpress.com.bd/ppeep/public/api/api/user/checkPhoneNo";
    final static String RESTAURANT_URL =
            "https://foodexpress.com.bd/ppeep/public/api/api/restaurnats";
    final static String RESTAURANT_MENU_URL =
            "https://foodexpress.com.bd/ppeep/public/api/api/categories";
    final static String FRIEND_LIST_URL =
            "https://foodexpress.com.bd/ppeep/public/api/api/friend";
    final  static  String ORDER_CREATE_URL =
            "https://foodexpress.com.bd/ppeep/public/api/api/order_create";
    final  static  String RESTAURANT_SEARCH_URL =
            "https://foodexpress.com.bd/ppeep/public/api/api/search/restaurnats";

    // final static String PARAM_QUERY = "q";
   /* final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";*/

    public static URL buildRestaurantUrl() {
        Uri builtUri = Uri.parse(RESTAURANT_URL).buildUpon().build();
        URL retauranturl = null;
        try {
            retauranturl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retauranturl;
    }
    public static URL buildSearchRestaurantUrl() {
        Uri builtUri = Uri.parse(RESTAURANT_SEARCH_URL).buildUpon().build();
        URL retaurantSearchurl = null;
        try {
            retaurantSearchurl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retaurantSearchurl;
    }

    public static URL buildOrderUrl() {
        Uri builtUri = Uri.parse(ORDER_CREATE_URL).buildUpon().build();
        URL orderUrl = null;
        try {
            orderUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return orderUrl;
    }


    public static URL buildRestaurantMenuUrl() {
        Uri builtUri = Uri.parse(RESTAURANT_MENU_URL).buildUpon().build();
        URL retaurantMenuUrl = null;
        try {
            retaurantMenuUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retaurantMenuUrl;
    }

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

    public static URL buildFriendListUrl() {
        Uri builtUri = Uri.parse(FRIEND_LIST_URL).buildUpon().build();
        URL friendListurl = null;
        try {
            friendListurl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return friendListurl;
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

    public static String getRestaurantFromHttpUrl(URL retauranturl) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) retauranturl.openConnection();


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

    public static String getRestaurantSearchFromHttpUrl(URL retaurantSearchurl, String search) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) retaurantSearchurl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

        // setPhoneNo();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "search", search);

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

    public static String getRestaurantMenuFromHttpUrl(URL retaurantMenuUrl, String merchantId) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) retaurantMenuUrl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

        // setPhoneNo();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "merchant_id", merchantId);

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

    public static String getFoodOrderFromHttpUrl(URL orderUrl, String itemId,String amount,String client_phone_no,String merchant_id) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) orderUrl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

        // setPhoneNo();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "item_id", itemId);
        param.put( "amount", amount);
        param.put( "client_phone_no", client_phone_no);
        param.put( "merchant_id", merchant_id);


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

    public static String getFriendListResponseFromHttpUrl(URL friendListurl, String phone) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) friendListurl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

        // setPhoneNo();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "contact", phone);

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
