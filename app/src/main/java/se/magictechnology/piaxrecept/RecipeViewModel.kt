package se.magictechnology.piaxrecept

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

enum class LoginResult {
    LOGINOK, LOGINFAIL, REGISTERFAIL
}

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

    val loginStatus: MutableLiveData<LoginResult> by lazy {
        MutableLiveData<LoginResult>()
    }

    val imageResult: MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
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
                loginStatus.value = LoginResult.LOGINOK
            } else {
                loginStatus.value = LoginResult.LOGINFAIL
            }
            loginStatus.value = null
        }
    }

    fun signup(email : String, password : String)
    {
        val auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginOK.value = true
                loginStatus.value = LoginResult.LOGINOK
            } else {
                loginStatus.value = LoginResult.REGISTERFAIL
            }
            loginStatus.value = null
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

        if(auth.currentUser == null)
        {
            return
        }

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
        saveRecipeStatus.value = null

        loadRecipes()

    }

    fun uploadImage(imgbitmap : Bitmap, imgrecipe : Recipe)
    {
        val smallerbitmap = resizeBitmap(imgbitmap, 800)

        val storage = Firebase.storage.reference

        val recipeimagepath = storage.child("recipe").child(imgrecipe.fbid!!)

        val baos = ByteArrayOutputStream()
        smallerbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = recipeimagepath.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            imageResult.value = smallerbitmap
        }
    }

    fun downloadImage(imgrecipe : Recipe)
    {
        if(imgrecipe.fbid == null)
        {
            return
        }

        var imgRef = Firebase.storage.reference.child("recipe").child(imgrecipe.fbid!!)

        val ONE_MEGABYTE: Long = 1024 * 1024
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            val imagebitmap = BitmapFactory.decodeByteArray(it, 0, it.size)

            imageResult.value = imagebitmap
        }.addOnFailureListener {
            // Handle any errors
        }
    }


    fun resizeBitmap(source: Bitmap, maxLength: Int): Bitmap {
        try {
            if (source.height >= source.width) {
                if (source.height <= maxLength) { // if image height already smaller than the required height
                    return source
                }

                val aspectRatio = source.width.toDouble() / source.height.toDouble()
                val targetWidth = (maxLength * aspectRatio).toInt()
                val result = Bitmap.createScaledBitmap(source, targetWidth, maxLength, false)
                return result
            } else {
                if (source.width <= maxLength) { // if image width already smaller than the required width
                    return source
                }

                val aspectRatio = source.height.toDouble() / source.width.toDouble()
                val targetHeight = (maxLength * aspectRatio).toInt()

                val result = Bitmap.createScaledBitmap(source, maxLength, targetHeight, false)
                return result
            }
        } catch (e: Exception) {
            return source
        }
    }
}