package se.magictechnology.piaxrecept

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StartAdapter() : RecyclerView.Adapter<StartViewHolder>() {

    lateinit var startfrag : StartFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartViewHolder {
        val vh = StartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.startrecipe_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: StartViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            startfrag.goRecipe()
        }

    }

}

class StartViewHolder (view: View) : RecyclerView.ViewHolder(view) {



}