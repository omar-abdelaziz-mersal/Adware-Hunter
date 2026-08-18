package com.screenguard.app
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WhitelistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whitelist)
        val pm = packageManager
        val apps = pm.getInstalledApplications(0).filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
        val selected = WhitelistManager.get(this)
        val rv = findViewById<RecyclerView>(R.id.recyclerWhitelist)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = WhitelistAdapter(apps, selected, pm)
        findViewById<Button>(R.id.btnSaveWhitelist).setOnClickListener {
            WhitelistManager.save(this, selected)
            finish()
        }
    }
}
