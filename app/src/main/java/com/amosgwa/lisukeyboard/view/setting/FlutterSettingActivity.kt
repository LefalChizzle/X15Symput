//package com.amosgwa.lisukeyboard.view.setting
//
//import android.content.Context
//import android.media.AudioManager
//import android.os.Bundle
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import androidx.appcompat.app.AppCompatActivity
//import com.amosgwa.lisukeyboard.R
//import io.flutter.facade.Flutter
//import io.flutter.plugin.common.BasicMessageChannel
//import io.flutter.plugin.common.StringCodec
//import io.flutter.view.FlutterView
//
//class FlutterSettingActivity : AppCompatActivity() {
//
//    private lateinit var flutterView : FlutterView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_flutter_setting)
//
//        val frameLayout = FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//        )
//
//        flutterView = Flutter.createView(
//                this,
//                lifecycle,
//                "keyboard_setting"
//        )
//
//        addContentView(flutterView, frameLayout)
//
//        setupChannel()
//    }
//
//    private fun setupChannel() {
//        val channel = BasicMessageChannel<String>(
//                flutterView,
//                "play_sound",
//                StringCodec.INSTANCE
//        )
//        // Receive messages from Dart and send replies.
//        channel.setMessageHandler { _, _ ->
//            playSound()
//        }
//    }
//
//    private fun playSound() {
//        (getSystemService(Context.AUDIO_SERVICE) as AudioManager).apply {
//            playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
//        }
//    }
//}
