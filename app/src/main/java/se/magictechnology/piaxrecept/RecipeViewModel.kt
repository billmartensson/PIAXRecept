package se.magictechnology.piaxrecept

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class RecipeViewModel : ViewModel() {

    val recipes: MutableLiveData<List<Recipe>> by lazy {
        MutableLiveData<List<Recipe>>()
    }

    val loginOK: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val saveRecipeStatus: MutableLiveData<Boolean> by lazy {
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
        val database = Firebase.database.reference
        val auth = Firebase.auth

        database.child("recipeapp").child(auth.currentUser!!.uid).child("recipes").get().addOnSuccessListener {

            val temprecipelist = mutableListOf<Recipe>()
            for (snap in it.children) {
                val temprecipe = snap.getValue<Recipe>()!!
                temprecipe.fbid = snap.key
                temprecipelist.add(temprecipe)
            }
            recipes.value = temprecipelist

        }.addOnFailureListener {

        }


    }

    fun saveRecipe(reci : Recipe)
    {
        val database = Firebase.database.reference
        val auth = Firebase.auth

        val recipepath = database.child("recipeapp").child(auth.currentUser!!.uid).child("recipes")

        if(reci.fbid == null)
        {
            // NYTT RECEPT
            recipepath.push().setValue(reci)
        } else {
            // SPARA ÖVER GAMMALT RECEPT
            recipepath.child(reci.fbid!!).setValue(reci)
        }

        saveRecipeStatus.value = true

        loadRecipes()

    }
}