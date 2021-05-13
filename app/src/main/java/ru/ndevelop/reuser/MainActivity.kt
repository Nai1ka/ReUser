package ru.ndevelop.reuser

import android.app.PendingIntent
import android.content.Intent
import android.hardware.Camera
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.ndevelop.reuser.utils.RequestCodes.nfcRequestCode
import ru.ndevelop.reuser.repositories.DataBaseHandler
import ru.ndevelop.reuser.repositories.PreferencesRepository
import ru.ndevelop.reuser.ui.actionsList.ActionsSelectedActivity
import ru.ndevelop.reuser.ui.faqPage.FaqActivity
import ru.ndevelop.reuser.utils.Action
import ru.ndevelop.reuser.utils.Utils


class MainActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null

    // Pending intent for NFC intent foreground dispatch.
    // Used to read all NDEF tags while the app is running in the foreground.
    private var nfcPendingIntent: PendingIntent? = null
    // Optional: filter NDEF tags this app receives through the pending intent.
    //private var nfcIntentFilters: Array<IntentFilter>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val registeredTag =
            Utils.byteArrayToHexString(intent?.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)?.id)
        val actions = DataBaseHandler.getTagActions(registeredTag)

        if (actions.isNotEmpty()) {
            try {
                actions.forEach {
                    it.actionType.performAction(this,it.status,it.specialData)

                }
            } catch (e: Exception) {
                Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            if(PreferencesRepository.isFirstLaunch()){
                val i = Intent(this, FaqActivity::class.java)
                startActivity(i)
                PreferencesRepository.setIsNotFirstLaunch()
            }
            setContentView(R.layout.activity_main)
            val navView: BottomNavigationView = findViewById(R.id.nav_view)

            val navController = findNavController(R.id.nav_host_fragment)
            navView.setupWithNavController(navController)
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            nfcPendingIntent = PendingIntent.getActivity(
                this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
            )


        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) {
            nfcAdapter!!.disableForegroundDispatch(this)
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

            val resultByteArray = intent?.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)?.id
            //TODO проверить корректность метки(возможно)
            val tagId = Utils.byteArrayToHexString(resultByteArray)
            if (DataBaseHandler.isTagAlreadyExist(tagId)) {
                Toast.makeText(
                    this,
                    "Такая метка уже существует. Информация будет перезаписана",
                    Toast.LENGTH_SHORT
                ).show()
            }
        startActionsSelectionActivity(tagId)


    }
    fun startActionsSelectionActivity(tagId:String){
        val i = Intent(this, ActionsSelectedActivity::class.java)
        i.putExtra("tagId", tagId)
        startActivityForResult(i, nfcRequestCode)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            nfcRequestCode -> {
                val tagId = data.getStringExtra("tagId") ?: "" //TODO тоже сделать проверку
                val tagName = data.getStringExtra("tagName")?:"New Tag"
                val actions: ArrayList<Action> =
                    (data.getSerializableExtra("actions")) as ArrayList<Action>
                DataBaseHandler.updateIfExistsElseInsert(
                    tagId,
                   tagName,
                    actions
                )
            }

        }

    }

    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    private fun turnOnFlash() {
        val camera = Camera.open()
        val parameters: Camera.Parameters = camera.parameters
        parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
        camera.parameters = parameters
        camera.startPreview()
    }
}