package com.austin.dailymemedigest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_add_meme.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    val memes:ArrayList<Meme> = ArrayList()

    fun UpdateList(userid:String){
        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.memeView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = lm
        recyclerView?.adapter = MemeAdapter(memes, userid, requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        memes.clear()
        memeView?.adapter?.notifyDataSetChanged()

        var sharedId = "com.austin.dailymemedigest"
        var shared =this.activity!!
            .getSharedPreferences(sharedId, Context.MODE_PRIVATE)
        var userid =  shared.getString(LoginActivity.SHARED_ID, null)

        val queue = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/native/160420079/api/get_meme.php"
        var stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("APIRESULT",it)
                Log.d("userid",userid.toString())
                val obj = JSONObject(it)
                if(obj.getString("result")=="OK"){
                    val data = obj.getJSONArray("data")
                    val liked = obj.getJSONArray("lk")
                    val saved = obj.getJSONArray("sv")
                    var likec ="0"
                    var savec ="0"
                    for (i in 0 until data.length()){
                        val objPlay = data.getJSONObject(i)
                        for (a in 0 until liked.length()){
                            val objLike = liked.getJSONObject(a)
                            if (objLike.getString("meme_id") == objPlay.getInt("id").toString()){
                                likec = objLike.getString("meme_id")
                                break
                            }else{
                                likec="0"
                            }
                        }
                        for (b in 0 until saved.length()){
                            val objSave = saved.getJSONObject(b)
                            if (objSave.getString("meme_id") == objPlay.getInt("id").toString()){
                                savec = objSave.getString("meme_id")
                                break
                            }else{
                                savec="0"
                            }
                        }
                        val meme = Meme(objPlay.getInt("id")
                            ,objPlay.getString("url")
                            ,objPlay.getString("top_text")
                            ,objPlay.getString("bottom_text")
                            ,objPlay.getInt("users_id")
                            ,objPlay.getInt("num_like")
                            ,objPlay.getString("date_create")
                            ,objPlay.getInt("num_count")
                            ,likec
                            ,savec
                        )
                        memes.add(meme)
                    }
                    UpdateList(userid.toString())
                    Log.d("cekisiarray",memes.toString())
                    Log.d("cekisiarraylike",liked.toString())
                }
            },
            {
                Log.e("APIERROR",it.toString())
            }
        ){
            override fun getParams(): MutableMap<String, String>? {
                var map = HashMap<String, String>()
                map.set("userid", userid.toString())
                map.set("cmd", "1")
                return map
            }
        }
        queue.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_home, container, false)

        var fab : FloatingActionButton? = view?.findViewById(R.id.fabAdd)

        fab?.setOnClickListener{
            val intent = Intent(activity, AddMemeActivity::class.java)
            activity?.startActivity(intent)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}