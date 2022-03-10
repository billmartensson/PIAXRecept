package se.magictechnology.piaxrecept

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// Ny funktion är klar!!!

/*

BUGG:
------Klicka igenom recipe detail på recept i listan bakom
Vid nytt recept välja bild innan spara ger krasch
------Gå in på recept utan bild visar gammal bild från tidigare valt recept

FIX:
-------Skala ner bild innan uppladdning

by ActivityViewModels

        ACTIVITY
            |
           RECIPEVIEWMODEL
             |          |
           START      DETAIL

by viewModel

        ACTIVITY
            |
           RECIPEVIEWMODEL          RECIPEDETAILVIEWMODEL
             |                              |
           START                          DETAIL


 */

interface Myblueprint {

    var somenumber : Int

    fun dostuff()

    fun domore() {
        /////
        Log.i("PIAXINTERFACE", "DOMORE")
    }
}

interface RecipeFrag {

    fun dummy() {

    }

    fun doingBack() : Boolean
}

class MainActivity : AppCompatActivity(), Myblueprint {
    override var somenumber: Int = 7
    /*
    override var somenumber: Int
        get() = TODO("Not yet implemented")
        set(value) {}
       */
    override fun dostuff() {
        TODO("Not yet implemented")
    }

    override fun domore() {
        super.domore()
        Log.i("PIAXINTERFACE", "DOMORE ACTIVITY")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        domore()



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


    override fun onBackPressed() {
        //super.onBackPressed()

        Log.i("PIAXINTERFACE", supportFragmentManager.fragments.size.toString())

        var lastfrag = supportFragmentManager.fragments.last() as RecipeFrag

        if(lastfrag.doingBack() == true)
        {
            super.onBackPressed()
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



    UI
    ^
    |
MAINACT                         LOGINFRAG
  ^                                 | Login()
  |                                 |
................VIEWMODEL............................



 */