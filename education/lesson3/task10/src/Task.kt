// Return the set of products that were ordered by every customer
fun Shop.getSetOfProductsOrderedByEveryCustomer(): Set<Product> {
    val products = customers.flatMap { it.orders.flatMap { it.products } }.toSet()
    return customers.fold(products,
            {everyoneBought, c ->
        everyoneBought.intersect(c.orders.flatMap { it.products })
    })
}