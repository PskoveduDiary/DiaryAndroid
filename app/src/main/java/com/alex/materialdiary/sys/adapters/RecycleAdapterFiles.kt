package com.alex.materialdiary.sys.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alex.materialdiary.BuildConfig
import com.alex.materialdiary.MainActivity
import com.alex.materialdiary.R
import com.alex.materialdiary.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSink
import okio.BufferedSource
import okio.Okio
import xdroid.toaster.Toaster.toast
import java.io.File


class RecycleAdapterFiles(context: Context, links: MutableList<String>) :
    RecyclerView.Adapter<RecycleAdapterFiles.ViewHolder>() {
    private val context: Context
    private val inflater: LayoutInflater
    var links: MutableList<String>
    fun String.endsWithMulti(vararg chars: String): Boolean
    {
        return chars.any {
            endsWith(it)
        }
    }
    init {
        inflater = LayoutInflater.from(context)
        this.context = context
        this.links = links
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.file_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val guid = links.get(position).replace("https://one.pskovedu.ru:/file/download/", "")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val (name, file) = getFileName(links.get(position))
                //val file = FilesService.instance.jSONApi.getFile(guid)
                withContext(Dispatchers.Main) {
                    holder.progress.visibility = View.GONE
                    if (name == "Истекло время авторизации") {
                        holder.login_need.visibility = View.VISIBLE
                        for (i in 1 until links.size){
                            notifyItemRemoved(i)
                            links.removeAt(i)
                        }
                        holder.login.setOnClickListener {
                            CookieManager.getInstance().removeAllCookies(null)
                            context.startActivity(Intent(context, LoginActivity::class.java))
                        }
                        return@withContext
                    }
                    holder.name.visibility = View.VISIBLE
                    holder.icon.visibility = View.VISIBLE//URLUtil.guessFileName(links.get(position), file.headers().get("Content-Disposition"), file.headers().get("Content-Type"))
                    holder.name.text = name
                    with(name.lowercase()) {
                        when {
                            endsWithMulti("pptx", "ppt") -> holder.icon.setImageResource(R.drawable.powerpoint)
                            endsWithMulti("doc", "docx") -> holder.icon.setImageResource(R.drawable.word)
                            endsWithMulti("xls", "xlsx") -> holder.icon.setImageResource(R.drawable.excel)
                            endsWithMulti("mp3", "wav") -> holder.icon.setImageResource(R.drawable.music)
                            endsWithMulti("mp4", "avi", "mpeg", "mkv") -> holder.icon.setImageResource(R.drawable.video)
                            endsWithMulti("png", "jpg", "cr2", "bmp", "jpeg") -> holder.icon.setImageResource(R.drawable.picture)
                            endsWithMulti("txt") -> holder.icon.setImageResource(R.drawable.txt)
                            else -> holder.icon.setImageResource(R.drawable.file)
                        }
                    }
                    holder.itemView.setOnClickListener {
                        if (file != null) {
                            val downloadedFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                                , name)
                            val sink: BufferedSink = Okio.buffer(Okio.sink(downloadedFile))
                            sink.writeAll(file)
                            sink.close()
                            val uri: Uri = FileProvider.getUriForFile(
                                context,
                                BuildConfig.APPLICATION_ID + ".provider",
                                downloadedFile
                            )
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent);
                            }
                            else toast("Нет приложения для данного типа файлов")

                        }
                        else toast("Произошла ошибка")
                    }

                }
            }
            catch (e: Exception){
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    links.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return links.size
    }
    suspend fun getFileName(link: String): Pair<String, BufferedSource?> {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder
                        .header("Cookie",
                            CookieManager.getInstance()
                                .getCookie("one.pskovedu.ru") + "; " + CookieManager.getInstance()
                                .getCookie("pskovedu.ru")
                        )
                    return@Interceptor chain.proceed(builder.build())
                }
            )
            .build()
        val request = okhttp3.Request.Builder()
            .url(link)
            .post(RequestBody.create(null, ByteArray(0)))
            .build()
        val response = client.newCall(request).execute()
        val headers = response.headers()
        if (headers.get("Location") == "/auth/login") return "Истекло время авторизации" to null
        return URLUtil.guessFileName(link, headers.get("Content-Disposition"), headers.get("Content-Type")) to response.body()?.source()
    }
    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val progress: ProgressBar
        val name: TextView
        val icon: ImageView
        val login: Button
        val login_need: LinearLayout

        init {
            progress = view.findViewById(R.id.progressBar)
            name = view.findViewById(R.id.file_name)
            icon = view.findViewById(R.id.file_icon)
            login = view.findViewById(R.id.login)
            login_need = view.findViewById(R.id.login_needed)
        }
    }
}