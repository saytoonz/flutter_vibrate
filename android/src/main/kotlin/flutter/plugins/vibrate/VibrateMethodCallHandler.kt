package flutter.plugins.vibrate

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler


internal class VibrateMethodCallHandler(vibrator: Vibrator) : MethodCallHandler {
    private val vibrator: Vibrator
    private val hasVibrator: Boolean
    private val legacyVibrator: Boolean

    init {
        checkNotNull(vibrator)
        this.vibrator = vibrator
        this.hasVibrator = vibrator.hasVibrator()
        this.legacyVibrator = Build.VERSION.SDK_INT < 26
    }

    @Suppress("deprecation")
    private fun vibrate(duration: Int) {
        if (hasVibrator) {
            if (legacyVibrator) {
                vibrator.vibrate(duration.toLong())
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            duration.toLong(),
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                }
            }
        }
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "canVibrate" -> result.success(hasVibrator)
            "vibrate" -> {
                val duration = call.argument<Int>("duration")!!
                vibrate(duration)
                result.success(null)
            }

            "impact" -> {
                vibrate(HapticFeedbackConstants.VIRTUAL_KEY)
                result.success(null)
            }

            "selection" -> {
                vibrate(HapticFeedbackConstants.KEYBOARD_TAP)
                result.success(null)
            }

            "success" -> {
                vibrate(50)
                result.success(null)
            }

            "warning" -> {
                vibrate(250)
                result.success(null)
            }

            "error" -> {
                vibrate(500)
                result.success(null)
            }

            "heavy" -> {
                vibrate(100)
                result.success(null)
            }

            "medium" -> {
                vibrate(40)
                result.success(null)
            }

            "light" -> {
                vibrate(10)
                result.success(null)
            }

            else -> result.notImplemented()
        }
    }
}
