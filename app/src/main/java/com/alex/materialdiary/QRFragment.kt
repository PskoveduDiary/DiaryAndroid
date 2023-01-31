package com.alex.materialdiary

import android.R
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.alex.materialdiary.databinding.FragmentQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter


class QRFragment : Fragment() {
    private var _binding: FragmentQrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    /*val args: QRFragmentArgs by navArgs()*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrBinding.inflate(inflater, container, false)
        /*binding.InfoText.text = args.forUser
        val writer = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix = writer.encode(args.data, BarcodeFormat.QR_CODE, 256, 256)
            val width: Int = bitMatrix.width
            val height: Int = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            binding.QrCode.setImageBitmap(bmp)
        } catch (e: WriterException) {
            e.printStackTrace()
        }*/
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}