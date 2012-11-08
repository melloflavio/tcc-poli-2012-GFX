package com.poli.gfx.util;

import android.util.Log;

import com.loopj.android.http.*;

public class AppHttpClient {

	private static final String TAG = "httpClient";

  private static final String BASE_URL = Paths.BASE_URL;

  private static AsyncHttpClient client = new AsyncHttpClient();

  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	  Log.d(TAG, "get:" + getAbsoluteUrl(url) + "params: " + params.toString());
      client.get(getAbsoluteUrl(url), params, responseHandler);
  }

  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	  Log.d(TAG, "post:" + url + "params: " + params.toString());
      client.post(getAbsoluteUrl(url), params, responseHandler);
  }

  private static String getAbsoluteUrl(String relativeUrl) {
      return BASE_URL + relativeUrl;
  }
}