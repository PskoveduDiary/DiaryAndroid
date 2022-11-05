package com.alex.materialdiary

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentMsgMessagesBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterChat
import com.alex.materialdiary.sys.messages.API


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatFragment : Fragment(), API.Callback_Chat, API.Callback_About {
    private var _binding: FragmentMsgMessagesBinding? = null
    public var isGroup: Boolean = false
    var cookies: String = ""
    lateinit var api: API
    // This property is only valid between onCreateView and
    // onDestroyView.
    val args: ChatFragmentArgs by navArgs()
    private val binding get() = _binding!!
    var last_size = 0

    lateinit var updateHandler: Handler


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMsgMessagesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cookies = android.webkit.CookieManager.getInstance().getCookie("one.pskovedu.ru")
        api = API.getInstance(cookies)
        openMsg(args.name, args.login, args.group)
        binding.sendButton.setOnClickListener {
            send()
            openMsg(args.name, args.login, args.group)
        }
        setHasOptionsMenu(true)
        (activity as MainActivity?)?.bottomNav?.visibility = View.GONE
        (activity as AppCompatActivity?)?.supportActionBar?.title = args.name
        updateHandler = Handler(Looper.getMainLooper())
        updateHandler.post(object: Runnable{
            override fun run() {
                openMsg(args.name, args.login, args.group)
                updateHandler.postDelayed(this, 2000)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.bottomNav?.visibility = View.VISIBLE
        (activity as AppCompatActivity?)?.supportActionBar?.subtitle = ""
        updateHandler.removeCallbacksAndMessages(null)
        last_size = 0
        _binding = null
    }
    public fun openMsg(name: String?, login: String?, isGroup: Boolean){
        api.GetMessages(this, name, login, isGroup)
    }

    fun send(){
        val text = binding.messageText.text.toString()
        if (text != "") api.SendMessage(this, args.login, text, args.group)
        binding.messageText.setText("")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("menu.clear()"))
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_chat, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about_contact){
            api.GetUserInfo(this, args.login, args.name)
        }
        if (item.itemId == R.id.show_qr){
            val action = QRFragmentDirections.toQr(args.login,
            "QR-код для пользователя "+args.name)
            findNavController().navigate(action)
        }
        return when(item.itemId){
            R.id.about_contact -> true
            R.id.show_qr -> true
            else -> true
        }
    }



    override fun Messages(
        Name: String?,
        oth_id: String?,
        messages: MutableList<String>?,
        from: MutableList<String>?,
        to: MutableList<String>?,
        unread: MutableList<Boolean>?,
        dates: MutableList<String>?,
        isGroup: Boolean,
        users: Int,
        names: MutableList<String>?
    ) {
        if (last_size != messages?.size){
        val rv = binding.messagehistory
        val adapter = RecycleAdapterChat(this, names, dates, messages, unread)
        val lm = LinearLayoutManager(requireContext())
        lm.stackFromEnd = true
        rv.adapter = adapter
        rv.layoutManager = lm
        this.isGroup = isGroup
            last_size = messages?.size!!
        }

        val catPlural = this.resources.getQuantityString(R.plurals.users_in_chat, users, users)
        if (isGroup) (activity as AppCompatActivity?)?.supportActionBar?.subtitle = catPlural
    }

    override fun About(info: String?) {
        if (info != null) {
            AboutUserDialog(info).show(requireActivity().supportFragmentManager, "user_info")
        }
    }
    class AboutUserDialog(un: String) : DialogFragment() {
        var username = ""
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(
                requireActivity()
            )
            builder.setTitle("О пользователе")
                .setMessage(Html.fromHtml(username))
                .setIcon(R.drawable.user)
                .setPositiveButton("ОК") { dialog, id -> // Закрываем окно
                    dialog.cancel()
                }
            return builder.create()
        }

        init {
            username = un
        }
    }
}
