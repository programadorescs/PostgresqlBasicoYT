package pe.pcs.postgresqlbasicoyt.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage
import pe.pcs.postgresqlbasicoyt.data.dao.ProductoDao
import pe.pcs.postgresqlbasicoyt.data.model.ProductoModel
import pe.pcs.postgresqlbasicoyt.databinding.ActivityMainBinding
import pe.pcs.postgresqlbasicoyt.presentation.adapter.ProductoAdapter
import pe.pcs.postgresqlbasicoyt.presentation.common.UiState
import pe.pcs.postgresqlbasicoyt.presentation.common.makeCall

class MainActivity : AppCompatActivity(), ProductoAdapter.IOnClickListner {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()

        leerProducto("")
    }

    override fun onResume() {
        super.onResume()

        if (!existeCambio) return

        existeCambio = false
        leerProducto(binding.etBuscar.text.toString().trim())
    }

    private fun initListener() {
        binding.includeToolbar.ibAccion.setOnClickListener {
            startActivity(
                Intent(this, OperacionProductoActivity::class.java)
            )
        }

        binding.rvLista.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ProductoAdapter(this@MainActivity)
        }

        binding.tilBuscar.setEndIconOnClickListener {
            leerProducto(binding.etBuscar.text.toString().trim())
        }

        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if(binding.etBuscar.text.toString().trim().isEmpty()){
                    leerProducto("")
                    UtilsCommon.hideKeyboard(
                        this@MainActivity, binding.root.rootView
                    )
                }
            }
        })
    }

    override fun clickEditar(producto: ProductoModel) {
        startActivity(
            Intent(this, OperacionProductoActivity::class.java).apply {
                putExtra("id", producto.id)
                putExtra("descripcion", producto.descripcion)
                putExtra("codigobarra", producto.codigobarra)
                putExtra("precio", producto.precio)
            }
        )
    }

    override fun clickEliminar(producto: ProductoModel) {
        MaterialAlertDialogBuilder(this).apply {
            setTitle("Eliminar")
            setMessage("¿Desea eliminar el registro: ${producto.descripcion}?")
            setCancelable(false)

            setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("SI") { dialog, _ ->
                eliminar(producto)
                leerProducto(binding.etBuscar.text.toString().trim())
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun leerProducto(dato: String) = lifecycleScope.launch {
        binding.progressBar.isVisible = true

        makeCall { ProductoDao.listar(dato) }.let {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk(
                        "ERROR", it.message, this@MainActivity
                    )
                }

                is UiState.Success -> {
                    binding.progressBar.isVisible = false
                    (binding.rvLista.adapter as ProductoAdapter).setList(it.data)
                }
            }
        }
    }

    private fun eliminar(model: ProductoModel) = lifecycleScope.launch {
        binding.progressBar.isVisible = true

        makeCall { ProductoDao.eliminar(model) }.let {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk(
                        "ERROR", it.message, this@MainActivity
                    )
                }

                is UiState.Success -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showToast(this@MainActivity, "Registro eliminado")
                    leerProducto(binding.etBuscar.text.toString().trim())
                }
            }
        }
    }

    companion object {
        var existeCambio = false
    }
}