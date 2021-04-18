package naffate.mcihelle.misnotas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import java.io.File
import kotlinx.android.synthetic.main.activity_nota.view.*

class AdaptadorNotas : BaseAdapter {

    var context: Context? = null
    var notas = ArrayList<Nota>()

    constructor(context: Context, nota: ArrayList<Nota>) {
        this.context = context
        this.notas = nota
    }

    override fun getCount(): Int {
        return notas.size
    }

    override fun getItem(position: Int): Any {
        return notas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflador = LayoutInflater.from(context)
        var vista = inflador.inflate(R.layout.activity_nota, null)
        var nota = notas[position]
        vista.titulo.text = nota.titulo
        vista.tv_contenido_det.text = nota.contenido
        vista.btn_borrar.setOnClickListener {
            eliminar(nota.titulo)
            notas.remove(nota)
            this.notifyDataSetChanged()
        }

        return vista
    }

    private fun eliminar(titulo: String) {
        if (titulo.isEmpty()) {
            Toast.makeText(context, "Error: título vacío", Toast.LENGTH_SHORT).show()
        } else {
            try {
                val archivo = File(ubicacion(), titulo + ".txt")
                archivo.delete()
                Toast.makeText(context, "Se eliminó la nota", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error: no se pudo eliminar la nota", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun ubicacion(): File? {
        val carpeta = File(context?.getExternalFilesDir(null), "notas")
        if (!carpeta.exists()) {
            carpeta.mkdir()
        }
        return carpeta
    }
}