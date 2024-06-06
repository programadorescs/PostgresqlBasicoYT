package pe.pcs.postgresqlbasicoyt.data.model

data class ProductoModel(
    var id: Int = 0,
    var descripcion: String = "",
    var codigobarra: String = "",
    var precio: Double = 0.0
)
