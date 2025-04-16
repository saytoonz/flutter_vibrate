package flutter.plugins.vibrate

import android.content.Context
import android.os.Vibrator
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** VibratePlugin */
class VibratePlugin: FlutterPlugin, MethodCallHandler {
  private var methodChannel: MethodChannel? = null


  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    val context: Context = binding.applicationContext
    val messenger: BinaryMessenger = binding.binaryMessenger
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val methodCallHandler: VibrateMethodCallHandler = VibrateMethodCallHandler(vibrator)

    this.methodChannel = MethodChannel(messenger, "vibrate")
    methodChannel!!.setMethodCallHandler(methodCallHandler)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    this.methodChannel?.setMethodCallHandler(null);
    this.methodChannel = null;
  }
}
