package com.screenguard.app
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class WhitelistAdapter(private val apps: List<ApplicationInfo>, private val selected: MutableSet<String>, private val pm: PackageManager) : RecyclerView.Adapter<WhitelistAdapter.VH>() {
    class VH(v: View): RecyclerView.ViewHolder(v) {
        val tv: TextView = v.findViewById(R.id.tvApp)
        val cb: CheckBox = v.findViewById(R.id.cbApp)
    }
    override fun onCreateViewHolder(p: ViewGroup, v: Int): VH {
        return VH(LayoutInflater.from(p.context).inflate(R.layout.item_whitelist, p, false))
    }
    override fun getItemCount() = apps.size
    override fun onBindViewHolder(h: VH, pos: Int) {
        val app = apps[pos]
        val label = pm.getApplicationLabel(app).toString()
        h.tv.text = "$label (${app.packageName})"
        h.cb.isChecked = selected.contains(app.packageName)
        h.cb.setOnCheckedChangeListener { _, checked ->
            if (checked) selected.add(app.packageName) else selected.remove(app.packageName)
        }
    }
}
