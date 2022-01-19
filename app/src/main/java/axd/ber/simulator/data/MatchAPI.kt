package axd.ber.simulator.data

import axd.ber.simulator.domain.Match
import retrofit2.Call
import retrofit2.http.GET

interface MatchAPI {
    @GET("matches.json")
    fun getMatches(): Call<List<Match>>
}