package com.azhar.texttospeech

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.design.widget.Snackbar
import com.azhar.texttospeech.translation_engine.ConversionCallback
import com.azhar.texttospeech.translation_engine.TranslatorFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BasePermissionActivity() {

    override fun getActivityLayout(): Int {

        return R.layout.activity_main
    }

    @SuppressLint("SetTextI18n")
    override fun setUpView() {

        setSupportActionBar(toolBar)

        //SPEECH TO TEXT
        speechToText.setOnClickListener({ view ->

            Snackbar.make(view, "Bicara Sekarang", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            TranslatorFactory
                .instance
                .with(TranslatorFactory.TRANSLATORS.SPEECH_TO_TEXT,
                    object : ConversionCallback {
                        override fun onSuccess(result: String) {
                            sttOutput.text = result
                        }

                        override fun onCompletion() {
                        }

                        override fun onErrorOccurred(errorMessage: String) {
                            erroConsole.text = "Speech2Text Error: $errorMessage"
                        }

                    }).initialize("Bicara Sekarang !!", this@MainActivity)

        })

        //TEXT TO SPEECH
        textToSpeech.setOnClickListener { view ->

            val stringToSpeak: String = ttsInput.text.toString()

            if (stringToSpeak.isNotEmpty()) {

                TranslatorFactory
                    .instance
                    .with(TranslatorFactory.TRANSLATORS.TEXT_TO_SPEECH,
                        object : ConversionCallback {
                            override fun onSuccess(result: String) {
                            }

                            override fun onCompletion() {
                            }

                            @SuppressLint("SetTextI18n")
                            override fun onErrorOccurred(errorMessage: String) {
                                erroConsole.text = "Text2Speech Error: $errorMessage"
                            }

                        })
                    .initialize(stringToSpeak, this)

            } else {
                ttsInput.setText("Invalid input")
                Snackbar.make(view, "Please enter some text to speak", Snackbar.LENGTH_LONG).show()
            }

        }

    }

    fun findString(listOfPossibleMatches: ArrayList<String>?, stringToMatch: String): Boolean {

        if (null != listOfPossibleMatches) {

            for (transaltion in listOfPossibleMatches) {

                if (transaltion.contains(stringToMatch)) {

                    return true
                }
            }
        }
        return false
    }

    fun share(messageToShare: String, activity: Activity) {

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, messageToShare)
        activity.startActivity(Intent.createChooser(shareIntent, "Share using"))
    }
}
