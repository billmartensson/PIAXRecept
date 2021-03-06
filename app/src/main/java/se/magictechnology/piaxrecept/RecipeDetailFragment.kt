package se.magictechnology.piaxrecept

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import se.magictechnology.piaxrecept.databinding.FragmentRecipeDetailBinding
import se.magictechnology.piaxrecept.databinding.FragmentStartBinding

class RecipeDetailFragment : Fragment(), RecipeFrag {

    private var _binding : FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    val model : RecipeDetailViewModel by viewModels()

    var currentrecipe = Recipe()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageobserver = Observer<Bitmap> {
            binding.recipeDetailimageView.setImageBitmap(it)
        }
        model.imageResult.observe(viewLifecycleOwner, imageobserver)

        model.downloadImage(currentrecipe)




        binding.recipeTitleEdittext.setText(currentrecipe.title)
        binding.recipeDescriptionEdittext.setText(currentrecipe.description)

        binding.recipeSaveButton.setOnClickListener {

            // TODO: Validera s?? inmatning ??r korrekt

            currentrecipe.title = binding.recipeTitleEdittext.text.toString()
            currentrecipe.description = binding.recipeDescriptionEdittext.text.toString()

            model.saveRecipe(currentrecipe)

        }

        val img = registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback {
            //binding.recipeDetailimageView.setImageURI(it)

            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), it)

            model.uploadImage(bitmap, currentrecipe)

        })
        binding.galleryButton.setOnClickListener {
            img.launch("image/*")
        }

        binding.cameraButton.setOnClickListener {
            val camfrag = CameraFragment()
            camfrag.currentrecipe = currentrecipe
            requireActivity().supportFragmentManager.beginTransaction().add(R.id.fragContainer, camfrag).addToBackStack(null).commit()
        }


        val savestatusobserver = Observer<Boolean> {
            Log.i("PIAXDEBUG", "SAVE STATUS N??GOT H??NDE")
            if(it == true)
            {
                Log.i("PIAXDEBUG", "NU ST??NG FRAG " + currentrecipe.title)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        model.saveRecipeStatus.observe(viewLifecycleOwner, savestatusobserver)

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun doingBack(): Boolean {
        Log.i("PIAXINTERFACE", "DETAIL GO BACK")

        RecipeHelper.doCounter()

        if(currentrecipe.fbid != null)
        {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("St??nga?")
            builder.setMessage("??r du s??ker p?? att du vill st??nga receptet?")

            builder.setPositiveButton("Ja") { dialog, which ->
                requireActivity().supportFragmentManager.popBackStack()
            }

            builder.setNegativeButton("Nej") { dialog, which ->

            }

            builder.show()

            return false
        }

        return true
    }
}