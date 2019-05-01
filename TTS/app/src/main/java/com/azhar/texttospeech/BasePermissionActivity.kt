package com.azhar.texttospeech

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import java.util.*

abstract class BasePermissionActivity : AppCompatActivity() {

    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getActivityLayout())

        if (Build.VERSION.SDK_INT >= 23) {

            fuckMarshmallow()
        } else {

            setUpView()
        }
    }

    //SetUp views after permission granted
    abstract fun setUpView()

    // activity view
    abstract fun getActivityLayout(): Int

    @TargetApi(Build.VERSION_CODES.M)
    private fun fuckMarshmallow() {

        val permissionsList = ArrayList<String>()

        if (!isPermissionGranted(permissionsList, Manifest.permission.RECORD_AUDIO))

            if (permissionsList.size > 0) {

                requestPermissions(
                    permissionsList.toTypedArray(),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                )
                return
            }
        //add listeners on view
        setUpView()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun isPermissionGranted(permissionsList: MutableList<String>, permission: String): Boolean {

        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission)

            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                val perms = HashMap<String, Int>()
                perms[Manifest.permission.RECORD_AUDIO] = PackageManager.PERMISSION_GRANTED

                for (i in permissions.indices)
                    perms[permissions[i]] = grantResults[i]

                if (perms[Manifest.permission.RECORD_AUDIO] == PackageManager.PERMISSION_GRANTED) {

                    // All Permissions Granted
                    setUpView()

                } else {
                    // Permission Denied
                    Toast.makeText(applicationContext, "Some Permissions are Denied Exiting App", Toast.LENGTH_SHORT)
                        .show()

                    finish()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}

