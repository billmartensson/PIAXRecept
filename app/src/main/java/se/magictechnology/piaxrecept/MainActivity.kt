package se.magictechnology.piaxrecept

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model : RecipeViewModel by viewModels()

        model.checkLogin()

        val loginObserver = Observer<Boolean> { loginStatus ->
            if(loginStatus == true)
            {
                // GO START
                supportFragmentManager.beginTransaction().replace(R.id.fragContainer, StartFragment()).commit()
            } else {
                // GO LOGIN
                supportFragmentManager.beginTransaction().replace(R.id.fragContainer, LoginFragment()).commit()
            }
        }

        model.loginOK.observe(this, loginObserver)



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



    UI
    ^
    |
MAINACT                         LOGINFRAG
  ^                                 | Login()
  |                                 |
................VIEWMODEL............................



 */