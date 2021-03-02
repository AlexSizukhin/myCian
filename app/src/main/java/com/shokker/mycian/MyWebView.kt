package com.shokker.mycian

import android.util.Log
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class MyWebViewClient: WebViewClient() {
    val TAG = "MyWebViewClient"
    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
        if(url?.contains("jpg", true)==true)
            Log.d(TAG, "Loading ${url} on ${view}")
    }


    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Log.d(TAG,"page loading finished")
        view?.loadUrl(
                "javascript:(function() { " +
                        "var element = document.getElementsByClassName('search2__button');"
                        + "element.parentNode.removeChild(element);"
                        + "alert('aaa')"
                        +"})()")
    }



}
class MyWebChromeClient: WebChromeClient(){
    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        return super.onJsAlert(view, url, message, result)
    }


}