package com.alex.materialdiary

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alex.materialdiary.databinding.FragmentScanQrBinding
import com.alex.materialdiary.sys.PermissionUtils
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ScanQrFragment : Fragment(), ZBarScannerView.ResultHandler {
    private lateinit var zbViev: ZBarScannerView
    private var _binding: FragmentScanQrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentScanQrBinding.inflate(inflater, container, false)
        //cameraEnhancer.addListener(this);
        zbViev = ZBarScannerView(requireContext())

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
                    zbViev.setResultHandler(this@ScanQrFragment)
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
            /*val action = UserInfoFragmentDirections.toUserInfo(p0.contents, null)
            findNavController().navigate(action)*/
        }
    }
}

