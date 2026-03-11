package edu.nd.pmcburne.hwapp.one

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.nd.pmcburne.hwapp.one.ui.theme.UnselectedButton
import edu.nd.pmcburne.hwapp.one.ui.theme.UnselectedText
import edu.nd.pmcburne.hwapp.one.ui.theme.Selected
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoresScreen(vm:MainViewModel) {
    val state by vm.uiState.collectAsState()
    val context= LocalContext.current
    Scaffold(
        topBar ={
            TopAppBar(
                title= { Text("College Basketball Scores") }
            )},
    ){ innerPadding ->
        Column(
            modifier=Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(14.dp)
        ){
            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Button(
                    onClick = {
                        val curDate= LocalDate.parse(state.dateSelected)
                        DatePickerDialog(
                            context,
                            { _, year,month,dayOfMonth ->
                                val pickedDate = LocalDate.of(year,month+1,dayOfMonth).toString()
                                vm.setDate(pickedDate)
                            },
                            curDate.year,
                            curDate.monthValue - 1,
                            curDate.dayOfMonth
                        ).show()
                    }
                ){
                    Text(state.dateSelected)
                }
                Button(onClick = {vm.refresh() }) {
                    Text("Refresh Page")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text="Displaying: " +if(state.genderSelected == "men") "Men's Basketball" else "Women's Basketball",
                style=MaterialTheme.typography.titleMedium
            )
            Spacer(modifier=Modifier.height(10.dp))
            Row(
                horizontalArrangement=Arrangement.spacedBy(10.dp)
            ){
                Button(
                    onClick = {vm.setGender("men") },
                    colors=ButtonDefaults.buttonColors(
                        containerColor= if(state.genderSelected == "men") Selected else UnselectedButton,
                        contentColor= if(state.genderSelected == "men") Color.White else UnselectedText
                    )
                ){
                    Text("Mens")
                }
                Button(
                    onClick = {vm.setGender("women") },
                    colors= ButtonDefaults.buttonColors(
                        containerColor=if (state.genderSelected == "women") Selected else UnselectedButton,
                        contentColor=if (state.genderSelected == "women") Color.White else UnselectedText
                    )
                ) {
                    Text("Womens")
                }
            }
            Spacer(modifier=Modifier.height(14.dp))
            if(state.loading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(14.dp))
            }
            if (state.games.isEmpty() && !state.loading) {
                Text("No games currently found.")
            }
            else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement= Arrangement.spacedBy(10.dp),
                    contentPadding= PaddingValues(bottom=20.dp)
                ){
                    items(state.games){ game ->
                        GameCard(game)
                    }
                }
            }
        }
    }
}

// each individual game card
@Composable
fun GameCard(game: Game) {
    val gameState=game.gameState
    val statusText: String
    if(gameState == "live") {
        statusText = "LIVE"
    }
    else if (gameState == "final") {
        statusText = "Final"
    }
    else{
        statusText = "Upcoming"
    }
    Card(
        modifier=Modifier.fillMaxWidth(),
        shape=RoundedCornerShape(14.dp),
        colors=CardDefaults.cardColors()
    ){
        Column(modifier =Modifier.padding(14.dp)){
            Row(
                modifier=Modifier.fillMaxWidth(),
                verticalAlignment= Alignment.CenterVertically
            ){
                Box(
                    modifier=Modifier
                        .clip(RoundedCornerShape(25))
                        .padding(horizontal=12.dp, vertical=6.dp)
                ){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (gameState == "live") {
                            Box(
                                modifier=Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text=statusText,
                            color= Color.Black,
                            style =MaterialTheme.typography.bodyMedium,
                            fontWeight=FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier= Modifier.height(10.dp))
            Row(
                modifier =Modifier.fillMaxWidth(),
                horizontalArrangement=Arrangement.SpaceBetween
            ){
                Text("Away: " + game.awayT)
                if (gameState == "pre") {
                    Text("-")
                }
                else {
                    Text(game.awayScore)
                }
            }
            Spacer(modifier=Modifier.height(8.dp))

            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement= Arrangement.SpaceBetween
            ){
                Text("Home: " + game.homeT)
                if (gameState == "pre") {
                    Text("-")
                }
                else {
                    Text(game.homeScore)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (gameState == "pre") {
                Text("Game Start Time: " + game.startTime)
            }
            else if(gameState == "live") {
                Text(game.currentPeriod+"   "+game.contestClock)
            }
            else if(gameState == "final") {
                Spacer(modifier = Modifier.height(4.dp))
                if(game.awayWin) {
                    Text("Winner: " +game.awayT,color = Color.Black)
                }
                else if(game.homeWin) {
                    Text("Winner: " +game.homeT,color = Color.Black)
                }
            }
        }
    }
}

