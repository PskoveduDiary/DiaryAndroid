package com.alex.materialdiary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.materialdiary.databinding.FragmentMsgAvailablecontactsBinding
import com.alex.materialdiary.sys.adapters.HolderAvailableContacts
import com.alex.materialdiary.sys.messages.API
import com.treeview.TreeNode
import com.treeview.TreeViewAdapter
import com.treeview.TreeViewHolderFactory
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AvailableContactsFragment : Fragment(), API.Callback_Available {
    private var _binding: FragmentMsgAvailablecontactsBinding? = null

    var expandableListView: ExpandableListView? = null
    private var treeViewAdapter: TreeViewAdapter? = null
    var expandableListAdapter: ExpandableListAdapter? = null

    var expandableListTitle: List<String>? = null
    var expandableListDetail: HashMap<String, List<String>>? = null
    var All: List<List<*>>? = null
    var LoginSelected = ""
    var NameSelected = ""
    var cookies: String = ""
    lateinit var api: API
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMsgAvailablecontactsBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("cks", CookieManager.getInstance().getCookie("one.pskovedu.ru"))
        cookies = CookieManager.getInstance().getCookie("one.pskovedu.ru")
        val api = API.getInstance(cookies)
        api.GetAvailableContacts(this)

        val recyclerView: RecyclerView = binding.recyclerView
        val textView: TextView = binding.selectedname
        val factory = TreeViewHolderFactory { v: View?, layout: Int ->
            HolderAvailableContacts(
                v!!
            )
        }
        treeViewAdapter = TreeViewAdapter(factory)
        val lm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = lm
        recyclerView.adapter = treeViewAdapter
        treeViewAdapter!!.setTreeNodeClickListener { treeNode, v ->
            if (treeNode.value2 != null) {
                textView.text = treeNode.value.toString()
                LoginSelected = treeNode.value2.toString()
                NameSelected = treeNode.value.toString()
            }
        }
        //binding.addButton.setOnClickListener {
        //    val action = UserInfoFragmentDirections.toUserInfo(LoginSelected, NameSelected)
        //    findNavController().navigate(action)
        //}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun Available(info: JSONObject?) {
        val d = getData(info!!)
        treeViewAdapter!!.updateTreeNodes(d)
    }
    fun getData(info: JSONObject): List<TreeNode>? {
        val users: MutableList<TreeNode> = ArrayList()
        try {
            if (!info.isNull("CHILDREN")) {
                val c = info.getJSONArray("CHILDREN")
                val Users = TreeNode("Пользователи", R.layout.single_availabel)
                iterate(c, Users, 0)
                users.add(Users)
                Log.d("json", users.toString())
                return users
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return users
    }

    fun iterate(a: JSONArray, tre: TreeNode, iterlevel: Int): TreeNode? {
        var iterlevel = iterlevel
        try {
            for (i in 0 until a.length()) {
                val ij = a.getJSONObject(i)
                val tr = TreeNode(ij.getString("NAME"), R.layout.single_availabel)
                if (!ij.isNull("LOGIN")) tr.value2 = ij["LOGIN"]
                if (!ij.isNull("CHILDREN")) {
                    val c = ij.getJSONArray("CHILDREN")
                    Log.d("json", c.toString())
                    iterate(c, tr, iterlevel)
                    iterlevel++
                }
                tre.addChild(tr)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        //Log.d("iter", String.valueOf(iterlevel));
        return tre
    }

}
