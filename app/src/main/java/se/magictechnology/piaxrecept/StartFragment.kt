package se.magictechnology.piaxrecept

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import se.magictechnology.piaxrecept.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var _binding : FragmentStartBinding? = null
    private val binding get() = _binding!!

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

        binding.addRecipeButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().add(R.id.fragContainer, RecipeDetailFragment()).commit()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}