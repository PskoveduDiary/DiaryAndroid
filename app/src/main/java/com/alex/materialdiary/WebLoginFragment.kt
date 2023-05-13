package com.alex.materialdiary

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alex.materialdiary.databinding.FragmentScanQrBinding
import com.alex.materialdiary.sys.PermissionUtils
import me.dm7.barcodescanner.zbar.BarcodeFormat
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import okhttp3.*
import xdroid.toaster.Toaster
import java.io.IOException


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WebLoginFragment : Fragment(), ZBarScannerView.ResultHandler {
    private lateinit var zbViev: ZBarScannerView
    private var _binding: FragmentScanQrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: WebLoginFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentScanQrBinding.inflate(inflater, container, false)
        //cameraEnhancer.addListener(this);
        val formats: MutableList<BarcodeFormat> = mutableListOf()
        zbViev = ZBarScannerView(requireContext())
        formats += BarcodeFormat.QRCODE
        zbViev.setFormats(formats);
        val tv = TextView(requireContext())
        tv.text = "Войдите в аккаунт на web.pskovedu.ml"
        zbViev.addView(tv)
        return zbViev
        //return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    override fun onPause() {
        super.onPause()
        zbViev.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        PermissionUtils.checkPermission(requireActivity(), Manifest.permission.CAMERA,
            object: PermissionUtils.PermissionAskListener {
                override fun onPermissionGranted(){
                    zbViev.setResultHandler(this@WebLoginFragment)
                    zbViev.startCamera()
                }
                override fun onPermissionRequest(){
                    ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(Manifest.permission.CAMERA),
                        12);
                }
                override fun onPermissionPreviouslyDenied(){
                    ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(Manifest.permission.CAMERA),
                        12);
                }
                override fun onPermissionDisabled(){
                    AlertDialog.Builder(requireContext())
                        .setTitle("Вы не выдали разрешение!")
                        .setMessage("Пожалуйста, разрешите доступ к камере в Настройки>Приложения>Дневник>Разрешения")
                        .setPositiveButton("В настройки",
                            DialogInterface.OnClickListener { dialog, which ->
                                startActivity(
                                    Intent(
                                        Settings.ACTION_SETTINGS
                                    )
                                )
                            })
                        .setNegativeButton(
                            "Отмена",
                            DialogInterface.OnClickListener { dialog, which ->
                                dialog.cancel()
                                findNavController().navigateUp()
                            })
                        .show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun handleResult(p0: Result?) {
        if (p0 != null) {
            Log.d("qr", p0.contents)
            if (p0.barcodeFormat == BarcodeFormat.QRCODE) {
                val client = OkHttpClient()

                val formBody: RequestBody = FormBody.Builder()
                    .add("test", "test")
                    .build()
                val request: Request = Request.Builder()
                    .url("https://pskovedu.ml/api/auth/do_auth?code="+ p0.contents + "&guid=" + args.guid +
                             "&name=" + args.name)
                    .post(formBody)
                    .build()
                try {
                    client.newCall(request).enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                            Toaster.toast("Не удалось войти")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            Toaster.toast("Успешный вход")
                        }

                    })

                    // Do something with the response.
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                findNavController().navigateUp()
            }
        }
    }
}

