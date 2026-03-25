package com.filleasy.iamsmartdebug

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private val log = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scrollView = ScrollView(this)
        textView = TextView(this).apply {
            setPadding(24, 24, 24, 24)
            textSize = 13f
            setTextIsSelectable(true)
            typeface = android.graphics.Typeface.MONOSPACE
        }
        scrollView.addView(textView)
        setContentView(scrollView)

        appendLine("=== iAM Smart Debug Receiver ===")
        appendLine("Package: ${packageName}")
        appendLine("Activity: ${this::class.java.name}")
        appendLine("")

        dumpIntent("onCreate", intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        appendLine("\n========== NEW INTENT ==========\n")
        dumpIntent("onNewIntent", intent)
    }

    private fun dumpIntent(source: String, intent: Intent?) {
        val ts = SimpleDateFormat("HH:mm:ss.SSS", Locale.US).format(Date())
        appendLine("[$ts] Intent received via $source")

        if (intent == null) {
            appendLine("  (null intent)")
            return
        }

        appendLine("--- Basic ---")
        appendLine("  action:     ${intent.action}")
        appendLine("  data:       ${intent.data}")
        appendLine("  dataString: ${intent.dataString}")
        appendLine("  type:       ${intent.type}")
        appendLine("  scheme:     ${intent.scheme}")
        appendLine("  categories: ${intent.categories}")
        appendLine("  flags:      0x${Integer.toHexString(intent.flags)}")
        appendLine("  component:  ${intent.component}")
        appendLine("  package:    ${intent.`package`}")

        // Parse URI query params if present
        intent.data?.let { uri ->
            appendLine("")
            appendLine("--- URI Breakdown ---")
            appendLine("  scheme:    ${uri.scheme}")
            appendLine("  host:      ${uri.host}")
            appendLine("  port:      ${uri.port}")
            appendLine("  path:      ${uri.path}")
            appendLine("  query:     ${uri.query}")
            appendLine("  fragment:  ${uri.fragment}")

            val paramNames = uri.queryParameterNames
            if (paramNames.isNotEmpty()) {
                appendLine("")
                appendLine("--- URI Query Params (${paramNames.size}) ---")
                for (name in paramNames) {
                    appendLine("  $name = ${uri.getQueryParameter(name)}")
                }
            }
        }

        // Dump all extras
        intent.extras?.let { extras ->
            appendLine("")
            appendLine("--- Extras (${extras.size()}) ---")
            for (key in extras.keySet()) {
                val value = extras.get(key)
                appendLine("  $key = $value")
                appendLine("    type: ${value?.javaClass?.name ?: "null"}")
            }
        } ?: appendLine("\n--- Extras: (none) ---")

        appendLine("")
        appendLine("--- Full URI ---")
        appendLine("  ${intent.toUri(Intent.URI_INTENT_SCHEME)}")

        appendLine("\n================================\n")
    }

    private fun appendLine(text: String) {
        Log.i("IAMS_DEBUG", text)
        log.appendLine(text)
        textView.text = log.toString()
    }
}
