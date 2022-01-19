package axd.ber.simulator.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import axd.ber.simulator.databinding.MatchItemBinding
import axd.ber.simulator.domain.Match
import axd.ber.simulator.ui.DetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule

@GlideModule
class MatchListAdapter(val matchList: List<Match>) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MatchItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val match = matchList[position]

        // bind data from api to views
        Glide.with(context).load(match.homeTeam.image).circleCrop().into(holder.binding.ivHomeTeam)
        holder.binding.txHomeTeam.text = match.homeTeam.name
        if (match.homeTeam.score != null) {
            holder.binding.txHomeScore.text = "${match.homeTeam.score}"
        }
        Glide.with(context).load(match.awayTeam.image).circleCrop().into(holder.binding.ivAwayTeam)
        holder.binding.txAwayTeam.text = match.awayTeam.name
        if (match.awayTeam.score != null) {
            holder.binding.txAwayScore.text = "${match.awayTeam.score}"
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.Extras.MATCH, match)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = matchList.size
}