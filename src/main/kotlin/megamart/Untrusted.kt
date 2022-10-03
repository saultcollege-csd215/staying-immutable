package megamart

var shoppingCartTotal = 0.0;

data class CartItem(val name: String, val price: Double)

data class Employee(val name: String, val email: String)
data class PayrollCheck(val employeeName: String, val amount: Double)

class Button(val item: CartItem) {

    fun showFreeShippingIcon() { println("Showing free shipping icon on $item") }
    fun hideFreeShippingIcon() { println("Hiding free shipping icon on $item") }
}

var buyButtons = listOf(
    Button(CartItem("Lip chap", 2.99)),
    Button(CartItem("Gatorade", 4.85)),
    Button(CartItem( "Lighter", 1.49)),
    Button(CartItem("Gummy Bears", 5.99))
)

// This function returns a reference to a global variable, so any other function might change the returned list!
// We need to use defensive coding when using this function!
fun getBuyButtonsDom() = buyButtons

fun setCartTotalDom(total: Double) {
    println("Setting the cart total in the DOM to $total...")
}

fun setTaxDom(tax: Double) {
    println("Setting tax to $tax")
}

fun blackFridayPromotion(cart: List<CartItem>) {
    println(cart)
}

val currentPayrollChecks = listOf(
    PayrollCheck("Ali Array", 1200.0),
    PayrollCheck("Billie Byte", 2300.0),
    PayrollCheck("Charlie Char", 2022.0)
)
fun payrollCalc(employees: List<Employee>): List<PayrollCheck> {

    // This code might update the employees list, for example it might remove all employees currently on vacation

    // And it might return a data structure that is mutable and shared with other code,
    // for example by returning a reference to a global variable
    return currentPayrollChecks;
}