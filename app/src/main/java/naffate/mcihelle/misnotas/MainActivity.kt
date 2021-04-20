package naffate.mcihelle.misnotas

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.trace
import kotlinx.android.synthetic.main.activity_agregar_nota.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_nota.*
import kotlinx.android.synthetic.main.activity_nota.view.*
import java.io.*

class MainActivity : AppCompatActivity() {
    var notas = ArrayList<Nota>()
    lateinit var adaptador: AdaptadorNotas
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        leerNotas()

        fab.setOnClickListener {
            var intent = Intent(this, AgregarNota::class.java)
            startActivityForResult(intent, 123)
        }

        adaptador = AdaptadorNotas(this, notas)
        listviw.adapter = adaptador
listviw.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
    var nota = notas[position]
    var titulo2 = nota.titulo
    var cuerpo = nota.contenido
    var intent = Intent(this, AgregarNota::class.java)
    intent.putExtra("titulo", titulo2)
    intent.putExtra("contenido", cuerpo)
    startActivityForResult(intent, 123)
})
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

        var titulo= archivo.name.substring(0, archivo.name.length - 4)

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
