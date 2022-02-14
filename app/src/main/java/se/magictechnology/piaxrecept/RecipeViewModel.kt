package se.magictechnology.piaxrecept

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecipeViewModel : ViewModel() {

    val recipes: MutableLiveData<List<Recipe>> by lazy {
        MutableLiveData<List<Recipe>>()
    }


    val loginOK: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun checkLogin()
    {
        if(Firebase.auth.currentUser == null)
        {
            loginOK.value = false
        } else {
            loginOK.value = true
        }
    }

    fun login(email : String, password : String)
    {
        val auth = Firebase.auth

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginOK.value = true
            }
        }
    }

    fun signup(email : String, password : String)
    {
        val auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginOK.value = true
            }
        }
    }

    fun logout()
    {
        val auth = Firebase.auth

        auth.signOut()
        loginOK.value = false
    }


    fun loadRecipes()
    {
        // FB HÄMTA
        //recipes.value = XXXXXXXX
    }

    fun saveRecipe()
    {
        // SPARA TILL FB
    }
}