package com.alex.materialdiary

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alex.materialdiary.databinding.FragmentUserInfoBinding
import com.alex.materialdiary.sys.messages.API
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class UserInfoFragment : Fragment(), API.Callback_About, API.Callback_AddContact {
    private lateinit var webView: WebView
    private var _binding: FragmentUserInfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: UserInfoFragmentArgs by navArgs()
    lateinit var api: API

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        api = API.getInstance(CookieManager.getInstance().getCookie("one.pskovedu.ru"))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        api.GetUserInfo(this, args.login, args.name?: "")
        binding.addToContacts.setOnClickListener {
            if (args.name == null){
                var text = binding.aboutUser.text.toString()
                try {
                    text = text.split("\n")[1]
                }
                catch (e: Exception){
                    text = "Новый контакт"
                }
                api.AddUserToContacts(this@UserInfoFragment, args.login, text as String)
            }
            else{
                api.AddUserToContacts(this@UserInfoFragment, args.login, args.name)
            }

            findNavController().navigate(R.id.to_contacts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun About(info: String?) {
        binding.aboutUser.text = Html.fromHtml(info)
    }

    override fun AddContact(info: String?) {
        val aboutUserDialog = info?.let { ContactUserDialog(it) }
        if (aboutUserDialog != null) {
            aboutUserDialog.show(requireActivity().supportFragmentManager, "add_contact")
        }
    }
    class ContactUserDialog(data: String) : DialogFragment() {
        var data = ""
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(
                requireActivity()
            )
            builder.setTitle("Добавить пользователя")
                .setMessage(Html.fromHtml(data))
                .setIcon(R.drawable.user)
                .setPositiveButton("ОК") { dialog, id -> // Закрываем окно
                    dialog.cancel()
                }
            return builder.create()
        }

        init {
            this.data = data
        }
    }
}
