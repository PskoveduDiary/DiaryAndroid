package com.alex.materialdiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.materialdiary.databinding.FragmentUsersBinding
import com.alex.materialdiary.sys.adapters.RecycleAdapterUsers
import com.alex.materialdiary.sys.net.PskoveduApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class ChangeUserFragment : Fragment() {
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        binding.users.layoutManager = LinearLayoutManager(requireContext())
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.findNavController()
        getUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getUserInfo()
    }
    internal class MyTimerTask(val fragment: ChangeUserFragment) : Runnable {
        override fun run() {
            if (android.webkit.CookieManager.getInstance().getCookie("one.pskovedu.ru") == null) {
                val scheduler = Executors.newSingleThreadScheduledExecutor()
                scheduler.schedule(MyTimerTask(fragment), 2, TimeUnit.SECONDS)
            } else {
                fragment.getUserInfo()
            }
        }
    }
    fun getUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val userinfo = PskoveduApi.getInstance(requireContext(), findNavController()).getUserInfo()
            withContext(Dispatchers.Main){
                if (userinfo != null) {
                    activity?.runOnUiThread(object : Runnable {
                        override fun run() {
                            if(_binding == null) return
                            binding.users.adapter = RecycleAdapterUsers(this@ChangeUserFragment, userinfo.schools)
                        }
                    })
                }
                else{
                    val scheduler = Executors.newSingleThreadScheduledExecutor()
                    scheduler.schedule(MyTimerTask(this@ChangeUserFragment), 1, TimeUnit.SECONDS)

                }
            }
        }
    }
}
