package se.magictechnology.piaxrecept

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import se.magictechnology.piaxrecept.databinding.FragmentStartBinding


class StartFragment : Fragment(), RecipeFrag {

    private var _binding : FragmentStartBinding? = null
    private val binding get() = _binding!!

    val startadapter = StartAdapter()

    val model : RecipeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startadapter.startfrag = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_start, container, false)
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recipesRV.layoutManager = LinearLayoutManager(requireContext())
        binding.recipesRV.adapter = startadapter

        val recipeobserver = Observer<List<Recipe>> {
            startadapter.notifyDataSetChanged()
        }

        model.recipes.observe(viewLifecycleOwner, recipeobserver)

        model.loadRecipes()

        binding.logoutButton.setOnClickListener {
            model.logout()
        }

        binding.addRecipeButton.setOnClickListener {
            RecipeHelper.doCounter()

            requireActivity().supportFragmentManager.beginTransaction().add(R.id.fragContainer, RecipeDetailFragment()).addToBackStack(null).commit()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    fun goRecipe(rowrecipe : Recipe)
    {
        val recdetailfrag = RecipeDetailFragment()
        recdetailfrag.currentrecipe = rowrecipe

        requireActivity().supportFragmentManager.beginTransaction().add(R.id.fragContainer, recdetailfrag).addToBackStack(null).commit()
    }

    override fun doingBack(): Boolean {
        Log.i("PIAXINTERFACE", "START GO BACK")

        return true
    }

}