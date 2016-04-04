fun renderProductTable(): String {
    return html {
        table {
            tr (color = getTitleColor()){
                td {
                    text("Product")
                }
                td {
                    text("Price")
                }
                td {
                    text("Popularity")
                }
            }
            val products = getProducts()
            for((index, item) in products.withIndex()) {
                tr {
                    td(color = getCellColor(index, 0)) {
                        text(item.description)
                    }
                    td(color = getCellColor(index, 1)) {
                        text(item.price)
                    }
                    td(color = getCellColor(index, 2)) {
                        text(item.popularity)
                    }
                }
            }
        }
    }.toString()
}

fun getTitleColor() = "#b9c9fe"
fun getCellColor(index: Int, row: Int) = if ((index + row) %2 == 0) "#dce4ff" else "#eff2ff"