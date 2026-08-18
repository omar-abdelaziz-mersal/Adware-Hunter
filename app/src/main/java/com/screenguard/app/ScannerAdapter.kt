package com.screenguard.app
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class ScannerAdapter(private val items: List<ScannerActivity.SuspiciousApp>, private val onUninstall: (String)->Unit) : RecyclerView.Adapter<ScannerAdapter.VH>() {
    class VH(v: View): RecyclerView.ViewHolder(v) {
        val tvName: TextView = v.findViewById(R.id.tvAppName)
        val tvReason: TextView = v.findViewById(R.id.tvReason)
        val btnDel: Button = v.findViewById(R.id.btnUninstall)
    }
    override fun onCreateViewHolder(p: ViewGroup, v: Int): VH {
        return VH(LayoutInflater.from(p.context).inflate(R.layout.item_app_result, p, false))
    }
    override fun getItemCount() = items.size
    override fun onBindViewHolder(h: VH, pos: Int) {
        val it = items[pos]
        h.tvName.text = "${it.label} (${it.pkg})"
        h.tvReason.text = it.reason
        h.btnDel.setOnClickListener { onUninstall(it.pkg) }
    }
}
