package pe.pcs.postgresqlbasicoyt.presentation.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage
import pe.pcs.postgresqlbasicoyt.R
import pe.pcs.postgresqlbasicoyt.data.dao.ProductoDao
import pe.pcs.postgresqlbasicoyt.data.model.ProductoModel
import pe.pcs.postgresqlbasicoyt.databinding.ActivityOperacionProductoBinding
import pe.pcs.postgresqlbasicoyt.presentation.common.UiState
import pe.pcs.postgresqlbasicoyt.presentation.common.makeCall

class OperacionProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOperacionProductoBinding
    private var _id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperacionProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()

        if (intent.extras != null)
            obtenerProducto()
    }

    private fun initListener() {
        binding.includeToolbar.toolbar.apply {
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

            subtitle = "Registrar | Editar producto"
            navigationIcon = AppCompatResources.getDrawable(
                this@OperacionProductoActivity,
                R.drawable.baseline_arrow_back_24
            )
        }

        binding.includeToolbar.ibAccion.setImageResource(R.drawable.baseline_done_all_24)

        binding.includeToolbar.ibAccion.setOnClickListener {
            if (binding.etDescripcion.text.toString().trim().isEmpty() ||
                binding.etCodigoBarra.text.toString().trim().isEmpty() ||
                binding.etPrecio.text.toString().trim().isEmpty()
            ) {
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA", "Debe llenar todos los campos", this
                )
                return@setOnClickListener
            }

            grabar(
                ProductoModel(
                    id = _id,
                    descripcion = binding.etDescripcion.text.toString(),
                    codigobarra = binding.etCodigoBarra.text.toString(),
                    precio = binding.etPrecio.text.toString().toDouble()
                )
            )
        }
    }

    private fun obtenerProducto() {
        _id = intent.extras?.getInt("id", 0) ?: 0
        binding.etDescripcion.setText(intent.extras?.getString("descripcion"))
        binding.etCodigoBarra.setText(intent.extras?.getString("codigobarra"))
        binding.etPrecio.setText(intent.extras?.getDouble("precio").toString())
    }

    private fun grabar(producto: ProductoModel) = lifecycleScope.launch {
        binding.progressBar.isVisible = true

        makeCall { ProductoDao.grabar(producto) }.let {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk(
                        "ERROR", it.message, this@OperacionProductoActivity
                    )
                }

                is UiState.Success -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showToast(this@OperacionProductoActivity, "Datos grabados")
                    UtilsCommon.cleanEditText(binding.root.rootView)
                    binding.etDescripcion.requestFocus()
                    _id = 0
                    MainActivity.existeCambio = true
                }
            }
        }
    }

}