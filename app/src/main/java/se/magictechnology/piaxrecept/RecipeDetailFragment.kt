package se.magictechnology.piaxrecept

import android.graphics.Bitmap
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
import androidx.lifecycle.Observer
import se.magictechnology.piaxrecept.databinding.FragmentRecipeDetailBinding
import se.magictechnology.piaxrecept.databinding.FragmentStartBinding

class RecipeDetailFragment : Fragment() {

    private var _binding : FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    val model : RecipeViewModel by activityViewModels()

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

        binding.recipeTitleEdittext.setText(currentrecipe.title)
        binding.recipeDescriptionEdittext.setText(currentrecipe.description)

        binding.recipeSaveButton.setOnClickListener {

            // TODO: Validera så inmatning är korrekt

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
            Log.i("PIAXDEBUG", "SAVE STATUS NÅGOT HÄNDE")
            if(it == true)
            {
                Log.i("PIAXDEBUG", "NU STÄNG FRAG " + currentrecipe.title)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        model.saveRecipeStatus.observe(viewLifecycleOwner, savestatusobserver)

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}