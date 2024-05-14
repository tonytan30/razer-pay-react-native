package expo.modules.razerpayreactnative

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*

import android.content.Context
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView

class RazerPayView (context: Context, appContext: AppContext) : ExpoView(context, appContext) {
    private val mpopenmolpaywindow = "mpopenmolpaywindow://"
    private val mpcloseallwindows = "mpcloseallwindows://"
    private val mptransactionresults = "mptransactionresults://"
    private val mprunscriptonpopup = "mprunscriptonpopup://"
    private val mppinstructioncapture = "mppinstructioncapture://"
    
    private val onLoad by EventDispatcher()
    private val onSuccess by EventDispatcher()
    private val onError by EventDispatcher()
    private val onEnd by EventDispatcher()

    internal val webView = WebView(context).also {
        it.settings.javaScriptEnabled = true
        it.settings.allowUniversalAccessFromFileURLs = true

        it.loadUrl("https://www.baidu.com")

        it.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
//
//        it.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                print("MPMainUIWebClient shouldOverrideUrlLoading url = $url")
//
//                when {
//                    url != null && url.startsWith(mpopenmolpaywindow) -> {
//                        val base64String = url.replace(mpopenmolpaywindow, "")
//                        print("MPMainUIWebClient mpopenmolpaywindow base64String = $base64String")
//
//                        // Decode base64
//                        val data = Base64.decode(base64String, Base64.DEFAULT)
//                        val dataString = String(data)
//                        print("MPMainUIWebClient mpopenmolpaywindow dataString = $dataString")
//
//                        if (dataString.isNotEmpty()) {
//                            print("MPMainUIWebClient mpopenmolpaywindow success")
//                            it.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "")
//                            it.visibility = View.VISIBLE
//                        } else {
//                            print("MPMainUIWebClient mpopenmolpaywindow empty dataString")
//                        }
//                    }
//                    url != null && url.startsWith(mpcloseallwindows) -> {
//                        mpBankUI?.apply {
//                            loadUrl("about:blank")
//                            visibility = View.GONE
//                            clearCache(true)
//                            clearHistory()
//                            removeAllViews()
//                            destroy()
//                        }
//                        mpMOLPayUI?.apply {
//                            loadUrl("about:blank")
//                            visibility = View.GONE
//                            clearCache(true)
//                            clearHistory()
//                            removeAllViews()
//                            destroy()
//                        }
//                    }
//                    url != null && url.startsWith(mptransactionresults) -> {
//                        val base64String = url.replace(mptransactionresults, "")
//                        print("MPMainUIWebClient mptransactionresults base64String = $base64String")
//
//                        // Decode base64
//                        val data = Base64.decode(base64String, Base64.DEFAULT)
//                        val dataString = String(data)
//                        print("MPMainUIWebClient mptransactionresults dataString = $dataString")
//
//                        // 显示成功
////                        val result = Intent().apply {
////                            putExtra(MOLPayTransactionResult, dataString)
////                        }
////                        setResult(Activity.RESULT_OK, result)
//
//                        // Check if mp_request_type is "Receipt", if it is, don't finish()
//                        try {
//                            val jsonResult = JSONObject(dataString)
//
//                            print("MPMainUIWebClient jsonResult = $jsonResult")
//
//                            if (!jsonResult.has("mp_request_type") || jsonResult.getString("mp_request_type") != "Receipt" || jsonResult.has("error_code")) {
//                                finish()
//                            } else {
//                                // Next close button click will finish() the activity
//                                isClosingReceipt = true
//                                // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
//                            }
//                        } catch (t: Throwable) {
//                            finish()
//                        }
//                    }
//                    url != null && url.startsWith(mprunscriptonpopup) -> {
//                        val base64String = url.replace(mprunscriptonpopup, "")
//                        print("MPMainUIWebClient mprunscriptonpopup base64String = $base64String")
//
//                        // Decode base64
//                        val data = Base64.decode(base64String, Base64.DEFAULT)
//                        val jsString = String(data)
//                        print("MPMainUIWebClient mprunscriptonpopup jsString = $jsString")
//
//                        mpBankUI?.loadUrl("javascript:$jsString")
//                        print("mpBankUI loadUrl = javascript:$jsString")
//                    }
//                    url != null && url.startsWith(mppinstructioncapture) -> {
//                        val base64String = url.replace(mppinstructioncapture, "")
//                        print("MPMainUIWebClient mppinstructioncapture base64String = $base64String")
//
//                        // Decode base64
//                        val data = Base64.decode(base64String, Base64.DEFAULT)
//                        val dataString = String(data)
//                        print("MPMainUIWebClient mppinstructioncapture dataString = $dataString")
//
//                        try {
//                            val jsonResult = JSONObject(dataString)
//
//                            base64Img = jsonResult.getString("base64ImageUrlData")
//                            filename = jsonResult.getString("filename")
//                            print("MPMainUIWebClient jsonResult = $jsonResult")
//
//                            val decodedBytes = Base64.decode(base64Img, 0)
//                            imgBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//                            // print("Bitmap Img = $imgBitmap")
//
//                            isStoragePermissionGranted()
//                        } catch (t: Throwable) {
//                            print("MPMainUIWebClient jsonResult error = $t")
//                        }
//                    }
//                }
//
//                return true
//            }
//
//            override fun onPageFinished(view: WebView, url: String) {
//                onLoad(mapOf("url" to url))
//            }
//        }

        addView(it)
    }

    private fun nativeWebRequestUrlUpdates(url: String) {
        val data = HashMap<String, String>()
        data["requestPath"] = url

        // Create JSON object for Payment details
        val json = JSONObject(data as Map<*, *>?)

        // Init javascript
        webView.loadUrl("javascript:nativeWebRequestUrlUpdates(${json.toString()})")
    }
}