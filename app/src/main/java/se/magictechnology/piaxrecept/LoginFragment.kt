package se.magictechnology.piaxrecept

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import se.magictechnology.piaxrecept.databinding.FragmentLoginBinding
import se.magictechnology.piaxrecept.databinding.FragmentStartBinding

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val model : RecipeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmailEdittext.text.toString()
            val password = binding.loginPasswordEdittext.text.toString()
            model.login(email, password)
        }

        binding.registerButton.setOnClickListener {
            val email = binding.loginEmailEdittext.text.toString()
            val password = binding.loginPasswordEdittext.text.toString()
            model.signup(email, password)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}