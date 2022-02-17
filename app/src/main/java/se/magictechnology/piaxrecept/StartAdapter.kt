package se.magictechnology.piaxrecept

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StartAdapter() : RecyclerView.Adapter<StartViewHolder>() {

    lateinit var startfrag : StartFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartViewHolder {
        val vh = StartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.startrecipe_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {

        startfrag.model.recipes.value?.let {
            return it.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: StartViewHolder, position: Int) {

        val rowrecipe = startfrag.model.recipes.value!![position]

        holder.recipetitleTV.text = rowrecipe.title

        holder.itemView.setOnClickListener {
            startfrag.goRecipe(rowrecipe)
        }

    }

}

class StartViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    val recipetitleTV = view.findViewById<TextView>(R.id.recipeItemTitleTextview)

}