package axd.ber.simulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import axd.ber.simulator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // view binding config
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}