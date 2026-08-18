package com.screenguard.app
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class ScannerActivity : AppCompatActivity() {
    data class SuspiciousApp(val pkg: String, val label: String, val reason: String)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        val results = doScan()
        findViewById<TextView>(R.id.tvScanResult).text = if(results.isEmpty()) "هاتفك نظيف 🟢" else "تم العثور على ${results.size} تطبيق مشبوه"
        val rv = findViewById<RecyclerView>(R.id.recyclerResults)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = ScannerAdapter(results) { pkg ->
            startActivity(Intent(Intent.ACTION_DELETE, Uri.parse("package:$pkg")))
        }
    }
    private fun doScan(): List<SuspiciousApp> {
        val pm = packageManager
        val list = mutableListOf<SuspiciousApp>()
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        for (app in apps) {
            if ((app.flags and ApplicationInfo.FLAG_SYSTEM)!= 0) continue
            if ((app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!= 0) continue
            val pkg = app.packageName
            val label = pm.getApplicationLabel(app).toString()
            if (label.isBlank()) {
                list.add(SuspiciousApp(pkg, label, "اسم فارغ Ghost App"))
                continue
            }
            if (pm.getLaunchIntentForPackage(pkg) == null) {
                list.add(SuspiciousApp(pkg, label, "مخفي بدون أيقونة - خطر 99%"))
                continue
            }
            try {
                val info = pm.getPackageInfo(pkg, PackageManager.GET_PERMISSIONS)
                val perms = info.requestedPermissions?.toList()?: emptyList()
                if (perms.contains("android.permission.SYSTEM_ALERT_WINDOW") && perms.contains("android.permission.RECEIVE_BOOT_COMPLETED")) {
                    list.add(SuspiciousApp(pkg, label, "صلاحيات مشبوهة OVERLAY + BOOT"))
                }
            } catch (e: Exception) {}
        }
        return list
    }
}
