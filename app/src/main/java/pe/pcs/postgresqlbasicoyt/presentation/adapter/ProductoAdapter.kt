package pe.pcs.postgresqlbasicoyt.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.postgresqlbasicoyt.data.model.ProductoModel
import pe.pcs.postgresqlbasicoyt.databinding.ItemsProductoBinding

class ProductoAdapter(
    private val onClickListner: IOnClickListner
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    private var lista = emptyList<ProductoModel>()

    interface IOnClickListner {
        fun clickEditar(producto: ProductoModel)
        fun clickEliminar(producto: ProductoModel)
    }

    inner class ProductoViewHolder(private val binding: ItemsProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun enlazar(producto: ProductoModel) {
            binding.tvTitulo.text = producto.descripcion
            binding.tvCodigoBarra.text = producto.codigobarra
            binding.tvPrecio.text = UtilsCommon.formatFromDoubleToString(producto.precio)

            binding.ibEditar.setOnClickListener { onClickListner.clickEditar(producto) }
            binding.ibEliminar.setOnClickListener { onClickListner.clickEliminar(producto) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        return ProductoViewHolder(
            ItemsProductoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.enlazar(lista[position])
    }

    fun setList(listaProducto: List<ProductoModel>) {
        this.lista = listaProducto
        notifyDataSetChanged()
    }
}