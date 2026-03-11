package edu.nd.pmcburne.hwapp.one

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import edu.nd.pmcburne.hwapp.one.ui.theme.BballScores

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db =ScoreDatabase.getDatabase(applicationContext)
        val repo =ScoreRepo(Network.api, db.gameDao())
        val vm =MainViewModel(repo)
        setContent {
            BballScores {
                ScoresScreen(vm)
            }
        }
    }
}