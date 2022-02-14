package se.magictechnology.piaxrecept

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var isLoggedIn = true

        if(isLoggedIn)
        {
            // GO START
            supportFragmentManager.beginTransaction().replace(R.id.fragContainer, StartFragment()).commit()
        } else {
            // GO LOGIN
            supportFragmentManager.beginTransaction().replace(R.id.fragContainer, LoginFragment()).commit()
        }

    }
}


/*

* Login/Register
* Lista med recept -> Recept -> Skapa recept
* Recept detail

+ Recept
Titel
Beskrivning
Bild flera?
Receptrader flera

+ Receptrad
Titel
Mängd
Enhet dl/st/osv

 */