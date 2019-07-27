package com.example.ppeepfinal.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

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
    final static String REGISTER_BASE_URL = "https://foodexpress.com.bd/ppeep/public/api/api/user/register";

    final static String PHONE_CHECK_URL = "https://foodexpress.com.bd/ppeep/public/api/api/user/checkPhoneNo";

    final static String RESTAURANT_URL = "https://foodexpress.com.bd/ppeep/public/api/api/nearby/restaurnats";

    final static String NEARBY_RESTAURANT_URL = "https://foodexpress.com.bd/ppeep/public/api/api/getnearby/restaurnats";

    final static String POPULAR_RESTAURANT_URL = "https://foodexpress.com.bd/ppeep/public/api/api/getPopular/restaurnats";

    final static String FREE_DELIVERY_RESTAURANT_URL = "https://foodexpress.com.bd/ppeep/public/api/api/getFreeDelivery/restaurnats";

    final static String RECOMMENDED_RESTAURANT_URL ="https://foodexpress.com.bd/ppeep/public/api/api/recomanded/restaurnats";

    final static String RESTAURANT_MENU_URL = "https://foodexpress.com.bd/ppeep/public/api/api/categories";

    final static String FRIEND_LIST_URL = "https://foodexpress.com.bd/ppeep/public/api/api/friend";

    final  static  String ADD_FRIEND = "https://foodexpress.com.bd/ppeep/public/api/api/add/friend";

    final  static  String REMOVE_FRIEND = "https://foodexpress.com.bd/ppeep/public/api/api/remove/friend";

    final  static  String ORDER_CREATE_URL = "https://foodexpress.com.bd/ppeep/public/api/api/order_create";

    final  static  String RESTAURANT_SEARCH_URL = "https://foodexpress.com.bd/ppeep/public/api/api/search/nearby/restaurnats";

    final  static  String OFFER_URL = "https://foodexpress.com.bd/ppeep/public/api/api/offers";

    final  static  String PROFILE_DETAIL = "https://foodexpress.com.bd/ppeep/public/api/api/userinfo/phone";

    final static  String PROFILE_UPDATE= "https://foodexpress.com.bd/ppeep/public/api/api/userinfo/update";

    final static String DRIVER_IMAGE= "https://foodexpress.com.bd/ppeep/public/images/driver_images/";

    final static String ORDER_DETAILS_URL ="https://foodexpress.com.bd/ppeep/public/api/api/getOrder";

    final static String ORDER_DELIVER_INFO = "https://foodexpress.com.bd/ppeep/public/api/api/user/orderDeliver";

    final static  String CURRENT_ORDER_HISTORY = "https://foodexpress.com.bd/ppeep/public/api/api/order/current";

    final static  String ALL_ORDER_HISTORY = "https://foodexpress.com.bd/ppeep/public/api/api/order/all";

    // final static String PARAM_QUERY = "q";
   /* final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";*/

    public static URL buildDriverIamgeUrl(String imageUrl) {
        Uri builtUri = Uri.parse(DRIVER_IMAGE+""+imageUrl).buildUpon().build();
        URL driverIamgeUrl = null;
        try {
            driverIamgeUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return driverIamgeUrl;
    }

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
    public static URL buildPopularRestaurantUrl() {
        Uri builtUri = Uri.parse(POPULAR_RESTAURANT_URL).buildUpon().build();
        URL retauranturl = null;
        try {
            retauranturl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retauranturl;
    }
    public static URL buildFreeDeliverRestaurantUrl() {
        Uri builtUri = Uri.parse(FREE_DELIVERY_RESTAURANT_URL).buildUpon().build();
        URL retauranturl = null;
        try {
            retauranturl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retauranturl;
    }

    public static URL buildNearByRestaurantUrl() {
        Uri builtUri = Uri.parse(NEARBY_RESTAURANT_URL).buildUpon().build();
        URL nearbyRetaurantUrl = null;
        try {
            nearbyRetaurantUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return nearbyRetaurantUrl;
    }

    public static URL buildRecommendedRestaurantUrl() {
        Uri builtUri = Uri.parse(RECOMMENDED_RESTAURANT_URL).buildUpon().build();
        URL recommendedRetauranturl = null;
        try {
            recommendedRetauranturl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return recommendedRetauranturl;
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

    public static URL buildOfferUrl() {
        Uri builtUri = Uri.parse(OFFER_URL).buildUpon().build();
        URL offerUrl = null;
        try {
            offerUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return offerUrl;
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


    public static URL buildProfileDetailInfoUrl() {
        Uri builtUri = Uri.parse(PROFILE_DETAIL).buildUpon().build();
        URL profileDetailInfoUrl = null;
        try {
            profileDetailInfoUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return profileDetailInfoUrl;
    }
   /****** Profile update******/
    public static URL buildProfileUpdateUrl() {
        Uri builtUri = Uri.parse(PROFILE_UPDATE).buildUpon().build();
        URL profileUpdateUrl = null;
        try {
            profileUpdateUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return profileUpdateUrl;
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

    public static URL buildAddFriendUrl() {
        Uri builtUri = Uri.parse(ADD_FRIEND).buildUpon().build();
        URL addfriendurl = null;
        try {
            addfriendurl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return addfriendurl;
    }

    public static URL buildRemoveFriendUrl() {
        Uri builtUri = Uri.parse(REMOVE_FRIEND).buildUpon().build();
        URL removefriendurl = null;
        try {
            removefriendurl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return removefriendurl;
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

    public static URL buildOrderInfoUrl() {
        Uri builtUri = Uri.parse(ORDER_DETAILS_URL).buildUpon().build();
        URL orderInfoUrl = null;
        try {
            orderInfoUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return orderInfoUrl;
    }
    public static URL buildDeliverOrderInfoUrl() {
        Uri builtUri = Uri.parse(ORDER_DELIVER_INFO).buildUpon().build();
        URL orderDeliverInfoUrl = null;
        try {
            orderDeliverInfoUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return orderDeliverInfoUrl;
    }

    public static URL buildCurrentOrderInfoUrl() {
        Uri builtUri = Uri.parse(CURRENT_ORDER_HISTORY).buildUpon().build();
        URL orderCurrentInfoUrl = null;
        try {
            orderCurrentInfoUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return orderCurrentInfoUrl;
    }

    public static URL buildAllOrderInfoUrl() {
        Uri builtUri = Uri.parse(ALL_ORDER_HISTORY).buildUpon().build();
        URL orderCurrentInfoUrl = null;
        try {
            orderCurrentInfoUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return orderCurrentInfoUrl;
    }




    public static String getOfferFromHttpUrl(URL offerUrl) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) offerUrl.openConnection();


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

    public static String getNearByRestaurantFromHttpUrl(URL retauranturl,String lat,String lng) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) retauranturl.openConnection();

        urlConnection.setRequestMethod("POST");//use post method

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "lat", lat);
        param.put( "lng", lng);

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

    public static String getRecommendedRestaurantFromHttpUrl(URL recommendedRetauranturl) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) recommendedRetauranturl.openConnection();


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

    public static String getRemoveFriendResponseFromHttpUrl(URL removefriendurl, String phone) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) removefriendurl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

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


    public static String getAddFriendResponseFromHttpUrl(URL addfriendurl, String phone,String phoneNoOfFriend) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) addfriendurl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "phone", phone);
        param.put( "phone_no_of_friend", phoneNoOfFriend);

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

    public static String getProfileUpdateResponseFromHttpUrl(URL profileDetailInfoUrl, String phone,String updateString,String updateData) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) profileDetailInfoUrl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

        // setPhoneNo();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "phone", phone);
        param.put( updateString, updateData);

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
    public static String getProfileDetailResponseFromHttpUrl(URL profileDetailInfoUrl, String phone) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) profileDetailInfoUrl.openConnection();//establish connection
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

    public static String getRestaurantSearchFromHttpUrl(URL retaurantSearchurl, String search,String lat,String lng) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) retaurantSearchurl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

        // setPhoneNo();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "search", search);
        param.put( "lat", lat);
        param.put( "lng", lng);

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

    public static String getFoodOrderFromHttpUrl(URL orderUrl, String itemId,String amount,String client_phone_no,String merchant_id,String lat,String lng,String address) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) orderUrl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

        // setPhoneNo();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "item_id", itemId);
        param.put( "amount", amount);
        param.put( "client_phone_no", client_phone_no);
        param.put( "merchant_id", merchant_id);
        param.put( "lat", lat);
        param.put( "lng", lng);
        param.put("delivery_address",address);


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

    public static String getOrderDetailsResponseFromHttpUrl(URL orderInfoUrl, String OrderId) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) orderInfoUrl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

        HashMap<String, String> param = new HashMap<String, String>();
        param.put( "order_id", OrderId);

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

    //This api will get response of order deliver information

    public static String getDeliverOrderDetailsOfUserResponseFromHttpUrl(URL orderDeliverInfoUrl, String phone) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) orderDeliverInfoUrl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

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
    public static String getCurrentOrderResponseFromHttpUrl(URL orderCurrentInfoUrl, String phone) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) orderCurrentInfoUrl.openConnection();//establish connection
        urlConnection.setRequestMethod("POST");//use post method

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
