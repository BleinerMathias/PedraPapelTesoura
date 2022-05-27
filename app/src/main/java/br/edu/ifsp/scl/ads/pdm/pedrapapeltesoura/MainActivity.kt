package br.edu.ifsp.scl.ads.pdm.pedrapapeltesoura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.scl.ads.pdm.pedrapapeltesoura.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    companion object Extras {
        // const torna o objeto estático em java
        const val SELECAO_PEDRA = 0
        const val SELECAO_PAPEL = 1
        const val SELECAO_TESOURA = 2
        const val PARAMETRO = "PARAMETRO"
    }

    // Instancia mainBinding
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var geradorRandom: Random

    // Cria objeto activity result launcher
    private lateinit var settingsActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        geradorRandom = Random(System.currentTimeMillis())

        var player = 0
        var resultGame =0

        val list: List<String> = arrayListOf("PEDRA", "PAPEL", "TESOURA")
        val listResultados: List<String> = arrayListOf("empatou", "foi derrotado", "ganhou")

        var configInit = restoreSettings()
        var configurationGamePlayers = if(configInit== 0) 2 else configInit
        Toast.makeText(this@MainActivity,"Iniciando jogo com ${configInit} jogadores!", Toast.LENGTH_SHORT).show()

        with(activityMainBinding){
            acaoPedraIv.setOnClickListener {
                player = SELECAO_PEDRA
                jogadaPlayer.text = "Você escolheu PEDRA"
                var (botOne, botTwo) = gerarBot()
                jogadaBotOne.text = "Bot 1 - Jogou ${list[botOne]}"
                if(configurationGamePlayers == 3) {jogadaBotTwo.text = "Bot 2 - Jogou ${list[botTwo]}"}
                resultGame = auditGame(player,botOne,botTwo,configurationGamePlayers)
                textView2.text = "No jogo você ${listResultados[resultGame]}!";
            }
            acaoPapelIv.setOnClickListener {
                player = SELECAO_PAPEL
                jogadaPlayer.text = "Você escolheu PAPEL"
                var (botOne, botTwo) = gerarBot()
                jogadaBotOne.text = "Bot 1 - Jogou ${list[botOne]}"
                if(configurationGamePlayers == 3) {jogadaBotTwo.text = "Bot 2 - Jogou ${list[botTwo]}"}
                resultGame = auditGame(player,botOne,botTwo,configurationGamePlayers)
                textView2.text = "No jogo você ${listResultados[resultGame]}!";
            }
            acaoTesouraIv.setOnClickListener {
                player = SELECAO_TESOURA
                jogadaPlayer.text = "Você escolheu TESOURA"
                var (botOne, botTwo) = gerarBot()
                jogadaBotOne.text = "Bot 1 - Jogou ${list[botOne]}"
                if(configurationGamePlayers == 3) {jogadaBotTwo.text = "Bot 2 - Jogou ${list[botTwo]}"}
                resultGame = auditGame(player,botOne,botTwo,configurationGamePlayers)
                textView2.text = "No jogo você ${listResultados[resultGame]}!";
            }
        }

        settingsActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if(result.resultCode == RESULT_OK){
                // Modificações na view quando chegar alterações (configurações)
                if(result.data !=null) {

                    val setting: Setting? = result.data?.getParcelableExtra<Setting>(PARAMETRO)

                    if (setting != null) {
                        var numberPlayers = setting.numberPlayers
                        if(numberPlayers == 3) {
                            activityMainBinding.jogadaBotTwo.visibility = View.VISIBLE
                        }else{
                            activityMainBinding.jogadaBotTwo.visibility = View.GONE
                        }
                        configurationGamePlayers = numberPlayers
                        saveSettings(numberPlayers)
                    }

                }
            }
        }

    }

    private fun restoreSettings():Int{
        val preferences = getPreferences(MODE_PRIVATE)
        return preferences.getInt("players",0)
    }

    private fun saveSettings(numeroPlayers: Int?) {
        val preferences = getPreferences(MODE_PRIVATE)
        val editor = preferences.edit()
        // Salva a configuracao de jogadores
        if (numeroPlayers != null) {
            editor.putInt("players", numeroPlayers)
        }
        editor.commit()
    }

    private fun gerarBot(): Pair<Int, Int> {
        var botOne = generateRandomNumber()
        var botTwo = generateRandomNumber()
        return Pair(botOne, botTwo)
    }

    private fun auditGame(player:Int, botOne:Int, botTwo:Int, configuracao: Int):Int{

        var retorno:Int = 0
        if(configuracao == 3){
            activityMainBinding.jogadaBotTwo.visibility = View.VISIBLE
            if (botOne.compareTo(botTwo) == 0) {
                // Se empatado e usuário for igual os outros dois -> jogo empata por completo
                return if (player.compareTo(botOne) == 0) 0 else analyzeGame(player, botOne)
            } else {
                var playerBotOne = player.compareTo(botOne);
                var playerBotTwo = player.compareTo(botTwo);

                return if (playerBotOne == 0) {
                    analyzeGame(player, botTwo)
                } else if (playerBotTwo == 0) {
                    analyzeGame(player, botOne)
                } else {
                    // regra -> todos jogaram diferente (empate)
                    0
                }
            }
        }else{
            return analyzeGame(player, botOne)
        }
        return retorno
    }


    private fun analyzeGame(jogadaPlayer: Int, jogadaBot :Int): Int {

        when(jogadaPlayer){
            SELECAO_PEDRA -> {
                return when(jogadaBot) {
                    SELECAO_PAPEL -> 1
                    SELECAO_PEDRA -> 0
                    SELECAO_TESOURA -> 2
                    else -> 2
                }
            }

            SELECAO_PAPEL -> {
                return when(jogadaBot) {
                    SELECAO_PAPEL -> 0
                    SELECAO_PEDRA -> 2
                    SELECAO_TESOURA -> 1
                    else -> 2
                }
            }

            SELECAO_TESOURA -> {
                return when(jogadaBot) {
                    SELECAO_PAPEL -> 2
                    SELECAO_PEDRA -> 1
                    SELECAO_TESOURA -> 0
                    else -> 2
                }
            }
        }
        return 0
    }

    private fun generateRandomNumber(): Int {
        return geradorRandom.nextInt(0..2)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settingsMi){
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            settingsActivityLauncher.launch(settingsIntent)
            return true
        }
        return false
    }

}