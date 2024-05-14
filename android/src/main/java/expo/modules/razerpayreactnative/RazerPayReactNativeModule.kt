package expo.modules.razerpayreactnative

import android.R
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.exception.Exceptions
import expo.modules.kotlin.exception.CodedException
import org.json.JSONObject
import java.net.URL

import java.util.Calendar

class RazerPayReactNativeModule : Module() {
  private val context: Context
    get() = appContext.reactContext ?: throw Exceptions.ReactContextLost()
  private val currentActivity
    get() = appContext.currentActivity ?: throw CodedException("Activity which was provided during module initialization is no longer available")

  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('RazerPayReactNative')` in JavaScript.
    Name("RazerPayReactNative")

    Function("pay") { paymentDetailsStr: String ->
      val obj = JSONObject(paymentDetailsStr)
      val paymentDetails = HashMap<String, Any>()
      val iter: Iterator<String> = obj.keys()
      while (iter.hasNext()) {
        val key: String = iter.next()
        val value = obj.get(key)
        if (value is Boolean) {
          paymentDetails[key] = value
        } else {
          paymentDetails[key] = value.toString()
        }
      }
      paymentDetails["is_submodule"] = true
      paymentDetails["module_id"] = "molpay-mobile-xdk-reactnative"
      paymentDetails["wrapper_version"] = "0"

      pay(paymentDetails)
    }

    View(RazerPayView::class) {
      Events("onLoad")

      Prop("url") { view: RazerPayView, url: URL? ->
        view.webView.loadUrl(url.toString())
      }
    }
  }

  private fun pay(paymentDetails: HashMap<String, Any>) {
    val intent = Intent(context, MOLPayActivity::class.java)

    intent.putExtra("paymentDetails", paymentDetails)
    currentActivity.startActivityForResult(intent, 9999)
//      startActivity(context, intent)

//    currentActivity.startActivity(intent)
  }
}
