package se.magictechnology.piaxrecept

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.recipeSaveButton.setOnClickListener {

            // TODO: Validera så inmatning är korrekt

            currentrecipe.title = binding.recipeTitleEdittext.text.toString()

            model.saveRecipe(currentrecipe)

        }

        val savestatusobserver = Observer<Boolean> {
            Log.i("PIAXDEBUG", "SAVE STATUS NÅGOT HÄNDE")
            requireActivity().supportFragmentManager.popBackStack()
        }
        model.saveRecipeStatus.observe(requireActivity(), savestatusobserver)

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}