package final_work.hasby.pewapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Hasby on 10-Mar-19.
 */

public class Requester {
    private String TAG = "Requester Class";
    private String url;
    private String encode;

    private RequestQueue mRequestQueue;

    public  Requester(Context context){
        this.url = "http://127.0.0.1:5000/post";
        this.encode = "";

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        this.mRequestQueue = new RequestQueue(cache, network);
        this.mRequestQueue.start();
    }

    public int request_expression(){
        return 0;
    }
    private int send_bitmap(Bitmap faceImage){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.encode = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return 0;
    }

    public void jsonPOST(Bitmap image){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.encode = Base64.encodeToString(byteArray, Base64.DEFAULT);

        JSONObject data = new JSONObject();
        try{
            data.put("image", "hmm");
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, this.url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                error.printStackTrace();
            }
        });

        this.mRequestQueue.add(jsonObjectRequest);
    }

}
