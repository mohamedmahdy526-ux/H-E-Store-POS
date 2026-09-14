package com.example.util

import android.content.Context
import android.util.Log
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

/**
 * Google Barcode Scanner helper utilizing the official Google Play Services Code Scanner API
 * and Google ML Kit Barcode Scanning.
 */
object GoogleBarcodeScannerHelper {
    private const val TAG = "GoogleBarcodeScanner"

    private val scannerOptions: GmsBarcodeScannerOptions by lazy {
        GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_UPC_E,
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_CODE_39,
                Barcode.FORMAT_CODE_93,
                Barcode.FORMAT_ITF,
                Barcode.FORMAT_DATA_MATRIX,
                Barcode.FORMAT_AZTEC,
                Barcode.FORMAT_PDF417
            )
            .enableAutoZoom()
            .build()
    }

    /**
     * Launches Google Play Services native code scanner bottom-sheet without needing
     * camera runtime permission.
     */
    fun scanWithGooglePlayServices(
        context: Context,
        onSuccess: (String) -> Unit,
        onCanceled: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        try {
            val scanner = GmsBarcodeScanning.getClient(context, scannerOptions)
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    val rawValue = barcode.rawValue
                    if (!rawValue.isNullOrBlank()) {
                        onSuccess(rawValue)
                    }
                }
                .addOnCanceledListener {
                    onCanceled()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Google Play Services scanner error: ${e.message}", e)
                    onFailure(e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to launch Google code scanner: ${e.message}", e)
            onFailure(e)
        }
    }
}
