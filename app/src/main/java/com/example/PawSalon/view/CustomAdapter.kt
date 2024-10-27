// CustomAdapter.kt
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    private val typeface: Typeface? = Typeface.createFromAsset(context.assets, "Poppins-Regular.ttf") // Ensure the font file is in assets/fonts

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        (view as TextView).apply {
            typeface = this@CustomAdapter.typeface // Set the custom font
            setTextColor(0xFF7BB6C8.toInt()) // Set the custom color
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        (view as TextView).apply {
            typeface = this@CustomAdapter.typeface // Set the custom font
            setTextColor(0xFF7BB6C8.toInt()) // Set the custom color
        }
        return view
    }
}
