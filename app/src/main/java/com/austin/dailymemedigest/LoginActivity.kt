package com.austin.dailymemedigest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    companion object {
        val SHARED_USERNAME = "SHARED_USERNAME"
        val SHARED_ID = "SHARED_ID"
        val FIRST_NAME = "FIRST_NAME"
        val LAST_NAME = "LAST_NAME"
        val PRIVACY_SETTING = "PRIVACY_SETTING"
        val REG_DATE = "REG_DATE"
        val URL_AVATAR = "URL_AVATAR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var username = ""
        var id = ""
        var fname = ""
        var lname = ""
        var privacy_setting = ""
        var reg_date = ""
        var url_avatar = ""

        var sharedUname = packageName
        var shared = getSharedPreferences(sharedUname, Context.MODE_PRIVATE)
        var name = shared.getString(SHARED_USERNAME, null)
        if (name != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignIn.setOnClickListener {
            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.fun/native/160420079/api/login.php"

            var uname = txtUsername.text
            var pass = txtPassword.text
            val stringRequest = object : StringRequest(
                Method.POST,
                url,
                Response.Listener {
                    Log.d("apiresult", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "success") {

                        val data = obj.getJSONArray("data")
                        val objData = data.getJSONObject(0)
                        username = objData.getString("username")
                        id = objData.getString("id")
                        fname = objData.getString("fname")
                        lname = objData.getString("lname")
                        privacy_setting = objData.getString("privacy_setting")
                        reg_date = objData.getString("reg_date")
                        url_avatar = objData.getString("url_avatar")

                        var editor = shared.edit()
                        editor.putString(SHARED_USERNAME,username)
                        editor.putString(SHARED_ID, id)
                        editor.putString(FIRST_NAME, fname)
                        editor.putString(LAST_NAME, lname)
                        editor.putString(PRIVACY_SETTING, privacy_setting)
                        editor.putString(REG_DATE, reg_date)
                        editor.putString(URL_AVATAR, url_avatar)
                        editor.apply()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra(SHARED_ID, id)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Username or password is not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                Response.ErrorListener {
                    Log.e("apierror", it.message.toString())
                }) {

                override fun getParams(): MutableMap<String, String> {
                    return hashMapOf("username" to uname.toString(), "password" to pass.toString())
                }
            }
            q.add(stringRequest)
        }

        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}