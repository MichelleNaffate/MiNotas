package naffate.mcihelle.misnotas

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_agregar_nota.*
import kotlinx.android.synthetic.main.activity_nota.*
import java.io.File
import java.io.FileOutputStream


class AgregarNota : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nota)
        mostrarTexto()


        btn_guardar.setOnClickListener{
            guardarNota()

        }

    }
    fun mostrarTexto(){
        var Bundle = intent.extras

        if (Bundle != null){
            var titulo = Bundle.getString("titulo")
            var contenido= Bundle.getString("contenido")

            et_titulo.setText(titulo)
            et_contenido.setText(contenido)

        }
    }

    private fun guardarNota() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                235
            )
        }else{
            guardar()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            235 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    guardar()
                } else {
                    Toast.makeText(this, "Error: permisos denegados", Toast.LENGTH_SHORT)
                }
            }
        }

    }
    fun  guardar(){
        var titulo = et_titulo.text.toString()
        var cuerpo = et_contenido.text.toString()
        if (titulo== "" || cuerpo== ""){
            Toast.makeText(this,"Error: campos vac??os",Toast.LENGTH_SHORT).show()
        }else{
            try{
                val archivo= File(ubicacion(), titulo+ ".txt")
                val fos= FileOutputStream(archivo)
                fos.write(cuerpo.toByteArray())
                fos.close()
                Toast.makeText(this,"Nota guardada", Toast.LENGTH_SHORT).show()
            }catch (e: Exception){
                Toast.makeText(this,"Error: no se guardo el archivo",Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
    private fun ubicacion(): String{
        val carpeta= File(getExternalFilesDir(null), "notas")
        if (!carpeta.exists()){
            carpeta.mkdir()
        }
        return carpeta.absolutePath
    }

}
