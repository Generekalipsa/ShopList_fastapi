data class ShoppingItem(
    val id: Int,
    val name: String,
    val quantity: Int,
    var is_purchased: Boolean = false
)
