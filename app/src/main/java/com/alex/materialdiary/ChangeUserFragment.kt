package com.alex.materialdiary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alex.materialdiary.databinding.FragmentUsersBinding
import com.alex.materialdiary.sys.adapters.ProgramAdapterUsers
import com.alex.materialdiary.sys.common.CommonAPI
import com.alex.materialdiary.sys.common.models.get_user.UserData
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChangeUserFragment : Fragment(), CommonAPI.UserCallback {
    private var _binding: FragmentUsersBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.findNavController()
        CommonAPI.getInstance().getUserInfo(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        CommonAPI.getInstance().getUserInfo(this)
    }
    internal class MyTimerTask(cb: CommonAPI.UserCallback) : Runnable {
        val cb = cb
        override fun run() {
            if (android.webkit.CookieManager.getInstance().getCookie("one.pskovedu.ru") == null){
                val scheduler = Executors.newSingleThreadScheduledExecutor()
                scheduler.schedule(MyTimerTask(cb), 2, TimeUnit.SECONDS)
            }
            else{
                CommonAPI.getInstance().getUserInfo(cb)
            }

        }
    }
    override fun user(user: UserData?) {
        Log.d("taf", user.toString())
        if (user != null) {
            Log.d("usssser", user.login)
            activity?.runOnUiThread(object : Runnable {
                override fun run() {
                    if(_binding == null) return
                    binding.users.adapter = ProgramAdapterUsers(this@ChangeUserFragment, user.schools)
                }
            })
        }
        else{
            val scheduler = Executors.newSingleThreadScheduledExecutor()
            scheduler.schedule(MyTimerTask(this), 1, TimeUnit.SECONDS)

        }
    }


}
