package com.screenguard.app
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var tvStatus: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvLogs: TextView
    private lateinit var statusCard: android.view.View
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvStatus = findViewById(R.id.tvStatus)
        tvDesc = findViewById(R.id.tvDesc)
        tvLogs = findViewById(R.id.tvLogs)
        statusCard = findViewById(R.id.statusCard)

        val btnStart = findViewById<AppCompatButton>(R.id.btnStart)
        val btnStop = findViewById<AppCompatButton>(R.id.btnStop)
        val btnClear = findViewById<AppCompatButton>(R.id.btnClear)
        val btnScan = findViewById<AppCompatButton>(R.id.btnScan)
        val btnWhitelist = findViewById<AppCompatButton>(R.id.btnWhitelist)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        btnStart.setOnClickListener {
            if (!isAccessibilityEnabled()) {
                Toast.makeText(this, getString(R.string.permission_needed), Toast.LENGTH_LONG).show()
                openAccessibilitySettings()
                return@setOnClickListener
            }
            AppPreferences.setProtectionOn(this, true)
            updateUI()
        }
        btnStop.setOnClickListener {
            AppPreferences.setProtectionOn(this, false)
            updateUI()
        }
        btnClear.setOnClickListener {
            AppPreferences.clearLogs(this)
            updateUI()
        }
        btnScan.setOnClickListener {
            startActivity(Intent(this, ScannerActivity::class.java))
        }
        btnWhitelist.setOnClickListener {
            startActivity(Intent(this, WhitelistActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val isOn = AppPreferences.isProtectionOn(this)
        val logs = AppPreferences.getLogs(this)
        if (isOn) {
            tvStatus.text = getString(R.string.status_on)
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.green_start))
            tvDesc.text = getString(R.string.desc_on)
            statusCard.setBackgroundResource(R.drawable.bg_status_green)
        } else {
            tvStatus.text = getString(R.string.status_off)
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.red_stop))
            tvDesc.text = getString(R.string.desc_off)
            statusCard.setBackgroundResource(R.drawable.bg_status_red)
        }
        tvLogs.text = if (logs.isEmpty()) getString(R.string.log_empty) else logs
    }

    private fun isAccessibilityEnabled(): Boolean {
        val service = "$packageName/${GuardAccessibilityService::class.java.canonicalName}"
        val enabled = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: return false
        return enabled.contains(service)
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}
