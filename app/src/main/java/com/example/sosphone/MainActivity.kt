package com.example.sosphone

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sosphone.databinding.ActivityPpalBinding
import android.Manifest
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding : ActivityPpalBinding
    private  var phoneSOS : String? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var permisionPhone = false

    private val TAG = "[CICLO DE VIDA] "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "\"onCreate()\": La actividad se esta creando.")

        mainBinding = ActivityPpalBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(mainBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        initEventCall()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "\"onStart()\": La actividad está a punto de hacerse visible.")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "\"onResume()\": La actividad está en primer plano y el usuario ya puede interaccionar.")

        permisionPhone = isPermissionCall()
        val stringPhone  = getString(R.string.string_phone)
        phoneSOS = intent.getStringExtra(stringPhone)
        mainBinding.txtPhone.setText(phoneSOS)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "\"onPause()\": La actividad pierde el foco de la pantalla.")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "\"onStop()\": La actividad deja de estar visible para el usuario.")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "\"onRestart()\": La actividad va a reiniciarse y volver a estar visible.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "\"onDestroy()\": La actividad va a ser destruida y muere.")
    }

    private fun init(){
        registerLauncher()
        if (!isPermissionCall())
            requestPermissionLauncher.launch(Manifest.permission. CALL_PHONE)

        mainBinding.ivChangePhone.setOnClickListener {
            val nameSharedFich = getString(R.string.name_preferen_shared_fich)
            val nameSharedPhone = getString(R.string.name_shared_phone)
            val sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
            val edit = sharedFich.edit()
            edit.remove(nameSharedPhone)
            edit.apply()
            val intent = Intent(this, ConfActivity::class.java )
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra("back", true)//volvemos desde El ACtivity2
                }
            startActivity(intent)
        }

    }

    private fun registerLauncher(){
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { //lambda
            /*
            Se ejecuta esta lambda, cuando el usuario pulsa aceptar/denegar
             */
                isGranted->
            if (isGranted) {
                permisionPhone = true
            }
            else {
                Toast.makeText( this, "Necesitas habilitar los permisos",
                    Toast.LENGTH_LONG).show()
                goToConfiguracionApp()  //abrimos la configuración de la aplicación.
            }
        }
    }

    private fun initEventCall() {
        mainBinding.button.setOnClickListener {
            permisionPhone=isPermissionCall()
            if (permisionPhone)
                call()
            else
                requestPermissionLauncher.launch(Manifest.permission. CALL_PHONE)
        }
    }
    private fun isPermissionCall():Boolean{
        //Para versión del sdk inferior a la API 23, no hace falta pedir permisos en t. ejecución.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true  //no hace falta pedir permisos en t. real al usuario
        else
            return isPermissionToUser() //Hay que ver si se concedieron en ejecución o no.
    }

    private fun isPermissionToUser() = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

    private fun call() {
        val intent = Intent(Intent.ACTION_CALL).apply {  //creamos la intención
            //Indicamos la Uri que es la forma de indicarle a Android que es un teléfono.
            data = Uri.parse("tel:"+phoneSOS!!)
        }
        startActivity(intent)
    }

    private fun goToConfiguracionApp(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }

        startActivity(intent)
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

}