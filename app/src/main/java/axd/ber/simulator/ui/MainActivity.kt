package axd.ber.simulator.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import axd.ber.simulator.R
import axd.ber.simulator.data.MatchAPI
import axd.ber.simulator.databinding.ActivityMainBinding
import axd.ber.simulator.domain.Match
import axd.ber.simulator.ui.adapter.MatchListAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var matchListAdapter: MatchListAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var matchApi: MatchAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // view binding config
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        httpClientSetup()
        matchListSetup()
        matchListRefreshSetup()
        fabSetup()
    }

    private fun httpClientSetup() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://axdr3.github.io/match-simulator-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        matchApi = retrofit.create(MatchAPI::class.java)
    }

    private fun fabSetup() {
        binding.fabMatches.setOnClickListener {
            it.animate()
                .rotationBy(360F)
                .setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        val itemCount = matchListAdapter.itemCount

                        for (i in 0 until (itemCount)) {
                            val match = matchListAdapter.matchList[i]
                            val strengthHomeTeam = (match.homeTeam.strength).roundToInt()
                                .plus(1)
                            val strengthAwayTeam = (match.awayTeam.strength).roundToInt()
                                .plus(1)
                            match.homeTeam.score =
                                Random.nextInt(0, strengthHomeTeam)
                            match.awayTeam.score =
                                Random.nextInt(0, strengthAwayTeam)
                            matchListAdapter.notifyItemChanged(i)
                        }
                    }

                })
        }
    }

    private fun matchListRefreshSetup() {
        binding.srlMatches.setOnRefreshListener(this::findMatchesFromApi)
    }

    private fun matchListSetup() {
        binding.rvMatches.setHasFixedSize(true)
        binding.rvMatches.layoutManager = LinearLayoutManager(this)
        findMatchesFromApi()
    }

    private fun findMatchesFromApi() {
        // loading on
        binding.srlMatches.isRefreshing = true

        matchApi.getMatches().enqueue(object : Callback<List<Match>?> {
            override fun onResponse(call: Call<List<Match>?>, response: Response<List<Match>?>) {
                if (response.isSuccessful) {
                    val matchList: List<Match>? = response.body()
                    matchListAdapter = MatchListAdapter(matchList ?: emptyList())
                    binding.rvMatches.adapter = matchListAdapter
                } else {
                    showErrorMessage()
                }
                // loading off
                binding.srlMatches.isRefreshing = false

            }

            override fun onFailure(call: Call<List<Match>?>, t: Throwable) {
                showErrorMessage()
            }
        })
    }

    private fun showErrorMessage() {
        Snackbar.make(binding.fabMatches, R.string.api_error, Snackbar.LENGTH_LONG).show()
    }
}