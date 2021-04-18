package naffate.mcihelle.misnotas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_nota.view.*
import java.io.*

class MainActivity : AppCompatActivity() {
    var notas = ArrayList<Nota>()
    lateinit var adaptador: AdaptadorNotas
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            var intent = Intent(this, AgregarNota::class.java)
            startActivityForResult(intent,123)
        }

        adaptador = AdaptadorNotas(this,notas)
        listviw.adapter = adaptador

    }
    private fun leerNotas(){
        notas.clear()

        var carpeta = File(ubicacion().absolutePath)

        if(carpeta.exists()){
            var archivos=carpeta.listFiles()
            if(archivos!=null){
                archivos.forEach {
                    leerArchivo(it)
                }
            }
        }
    }

    private fun leerArchivo(archivo: File) {
        val fis= FileInputStream(archivo)
        val di= DataInputStream(fis)
        val br= BufferedReader(InputStreamReader(di))
        var strLine: String? = br.readLine()
        var myData=""

        while(strLine!=null){
            myData = myData + strLine
            strLine=br.readLine()
        }

        br.close()
        di.close()
        fis.close()

        var titulo= archivo.name.substring(0,archivo.name.length-4)

        var nota=Nota(titulo, myData)
        notas.add(nota)
    }

    private fun ubicacion(): File {
        val carpeta = File(getExternalFilesDir(null), "notas")
        if(!carpeta.exists()){
            carpeta.mkdir()
        }
        return carpeta
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123){
            leerNotas()
            adaptador.notifyDataSetChanged()
        }

    }


    }
