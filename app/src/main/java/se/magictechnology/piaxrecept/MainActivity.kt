package se.magictechnology.piaxrecept

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        if(auth.currentUser != null)
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