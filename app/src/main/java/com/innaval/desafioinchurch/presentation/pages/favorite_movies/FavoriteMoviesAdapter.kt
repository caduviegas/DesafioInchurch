package com.innaval.desafioinchurch.presentation.pages.favorite_movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.innaval.desafioinchurch.BuildConfig
import com.innaval.desafioinchurch.InChurchApplication
import com.innaval.desafioinchurch.R
import com.innaval.desafioinchurch.databinding.ItemMovieCompleteBinding
import com.innaval.desafioinchurch.domain.entities.Movie
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class FavoriteMoviesAdapter(
                            private var movieList: MutableList<Movie>,
                            private val onClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<FavoriteMoviesAdapter.MyViewHolder>() {

    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_complete, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = movieList[position]

        holder.itemView.findViewById<TextView>(R.id.tv_title).text = item.title
        holder.itemView.findViewById<TextView>(R.id.tv_overview).text = item.overview
        holder.itemView.findViewById<TextView>(R.id.tv_release_date).text = simpleDateFormat.format(item.releaseDate)
        Picasso.with(holder.itemView.context).load("${BuildConfig.IMAGE_URL}${item.posterPath}").into(holder.itemView.findViewById<ImageView>(R.id.iv_poster))
        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    fun updateAllItems(list: MutableList<Movie>){
        this.movieList = list
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}