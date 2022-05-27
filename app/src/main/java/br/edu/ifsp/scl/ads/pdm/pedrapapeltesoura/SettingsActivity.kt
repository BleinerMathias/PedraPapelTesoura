package br.edu.ifsp.scl.ads.pdm.pedrapapeltesoura
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import br.edu.ifsp.scl.ads.pdm.pedrapapeltesoura.MainActivity.Extras.PARAMETRO
import br.edu.ifsp.scl.ads.pdm.pedrapapeltesoura.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var activitySettingsBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)

        activitySettingsBinding.salvarBt.setOnClickListener{
            val numberPlayers: Int = (activitySettingsBinding.numeroPlayerSp.selectedView as TextView).text.toString().toInt()
            val settings = Setting(numberPlayers)

            val  retornoIntent = Intent()
            retornoIntent.putExtra(PARAMETRO, settings)

            setResult(RESULT_OK,retornoIntent)
            finish()
        }

    }
}