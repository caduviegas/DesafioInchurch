package com.innaval.desafioinchurch.presentation.pages.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.innaval.desafioinchurch.BuildConfig
import com.innaval.desafioinchurch.InChurchApplication
import com.innaval.desafioinchurch.R
import com.innaval.desafioinchurch.domain.entities.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(
    private var movieList: MutableList<Movie>,
    private val onClick: (movie: Movie) -> Unit,
    private val onFavoriteClick: (movie: Movie, pos: Int) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    fun movieList() = movieList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie_simple, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = movieList[position]

        holder.itemView.findViewById<TextView>(R.id.tv_title).text = item.title
        Picasso.with(holder.itemView.context).load(
            "${
                BuildConfig.IMAGE_URL
            }${item.posterPath}"
        ).into(holder.itemView.findViewById<ImageView>(R.id.iv_poster))
        holder.itemView.findViewById<ImageView>(R.id.iv_poster).setOnClickListener {
            onClick(item)
        }

        holder.itemView.findViewById<ImageView>(R.id.iv_fav).setImageResource(
            if (item.isFavorite)
                R.drawable.ic_star_primary_24dp
            else
                R.drawable.ic_star_border_primary_24dp
        )
        holder.itemView.findViewById<ImageView>(R.id.iv_fav).setOnClickListener {
            onFavoriteClick(item,position)
        }
    }

    fun updateItem(pos: Int) {
        notifyItemChanged(pos)
    }

    fun updateAllItems(list: MutableList<Movie>) {
        this.movieList = list
        notifyDataSetChanged()
    }

    fun addItems(list: MutableList<Movie>) {
        this.movieList.addAll(list)
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}