package megamart.immutable

import megamart.*

import megamart.setCartTotalDom

var shoppingCart = listOf<CartItem>()

// Calculation: only explicit inputs and outputs are used
// We can now use this calculation for any purpose; not just in calcCartTotal, which does some additional work
fun calcTotal(cart: List<CartItem>): Double {
    var total = 0.0
    for ( item in cart ) {
        total += item.price
    }
    return total
}

// Perform a deep copy of a cart
fun deepCopyCart(cart: List<CartItem>): List<CartItem> {
    val copy = mutableListOf<CartItem>()
    for ( item in cart ) {
        copy.add(item.copy())
    }
    return copy
}

fun deepCopyEmployees(employees: List<Employee>): List<Employee> {
    val copy = mutableListOf<Employee>()
    for ( item in employees ) {
        copy.add(item.copy())
    }
    return copy
}

fun deepCopyPayrollChecks(employees: List<PayrollCheck>): List<PayrollCheck> {
    val copy = mutableListOf<PayrollCheck>()
    for ( item in employees ) {
        copy.add(item.copy())
    }
    return copy
}

// Action
fun addItemToCart(name: String, price: Double) {
    addItem(shoppingCart, name, price)                      // Reading global is an action
    val total = calcTotal(shoppingCart)                     // Reading global is an action
    setCartTotalDom(total)                                  // setCartTotalDom is an action
    updateShippingIcons(shoppingCart)                       // updateShippingIcons is an action
    updateTaxDom(total)                                     // updateTaxDom is an action

    // Using our safe function we know shoppingCart remains immutable
    shoppingCart = safeBlackFridayPromotion(shoppingCart)
}

fun safeBlackFridayPromotion(cart: List<CartItem>): List<CartItem> {
    // YIKES! We have no control over this function, and it might change shoppingCart
    // We need to do some defensive copying
    val cartCopy = deepCopyCart(cart)  // The call below may change its arguments; this is outgoing data we need to handle with a defensive copy
    blackFridayPromotion(cartCopy)
    return deepCopyCart(cartCopy) // The call above may have changed cartCopy; this is incoming data we need to handle with a defensive copy
}

fun safePayrollCalc(employees: List<Employee>): List<PayrollCheck> {
    val listCopy = deepCopyEmployees(employees)
    val payrollChecks = payrollCalc(listCopy)
    return deepCopyPayrollChecks(payrollChecks)
}


// Action (reading global variable is an action)
fun deleteHandler(name: String) {
    shoppingCart = removeItemByName(shoppingCart, name)   // removeItemByName now uses copy-on-write
    val total = calcTotal(shoppingCart)
    setCartTotalDom(total)
    updateShippingIcons(shoppingCart)
    updateTaxDom(total)
}

// MUTABLE WRITE has been converted to IMMUTABLE READ using copy-on-write
//fun removeItemByName(cart: List<CartItem>, name: String): List<CartItem> {
////    for ( item in cart ) {
////        if ( item.name == name ) {
////            cart.remove(item)
////            return;
////        }
////    }
//    val newCart = mutableListOf<CartItem>()
//    // Make a copy (without the removed item)
//    for ( item in cart ) {
//        if ( item.name != name ) {
//            newCart.add(item)
//        }
//    }
//    return newCart  // Return the copy
//}

// Using Kotlin's minus method on immutable lists...
fun removeItemByName(cart: List<CartItem>, name: String): List<CartItem> {
    for ( item in cart ) {
        if ( item.name == name ) {
            return cart.minus(item) // Returns a NEW list with all elements in cart minus the given element
        }
    }
    return cart // If we get here, no item was removed, so just return the original list
}

// Calculation: only explicit inputs and outputs
// This is much easier to test than addItemToCart
fun addItem(cart: List<CartItem>, name: String, price: Double) = cart + CartItem(name, price)

// Action
fun updateTaxDom(total: Double) {
    setTaxDom(calcTax(total))                  // Modifying DOM is an action
}

// Calculation: only explicit inputs and outputs
// This now encodes a business rule that can be used at any time, not just in updateTaxDom
fun calcTax(total: Double) = total * 0.13

fun deepCopyBuyButtons(buttonList: List<Button>): List<Button> {
    val copy = mutableListOf<Button>()
    for ( button in buttonList ) {
        // Since Button is a regular class, we don't have the copy method
        // We just create a new Button instance instead with a copy of the current button's item
        copy.add(Button(button.item.copy()))
    }
    return copy
}

// Action
fun updateShippingIcons(cart: List<CartItem>) {
    // Defensive copying prevents us from having to deal with the mutable data returned from getBuyButtonsDom()
    val buyButtons = deepCopyBuyButtons(getBuyButtonsDom())

    for (button in buyButtons) {
        if ( getsFreeShipping(addItem(cart, button.item.name, button.item.price)) ) {
            // Can't test this without analyzing the DOM; there is no return statement that gives the answer
            button.showFreeShippingIcon()
        } else {
            button.hideFreeShippingIcon()                   // Modifying the DOM is an action
        }
    }
}

// Calculation: only explicit inputs and outputs
// This now encodes a business rule that can be used at any time, not just in updateShippingIcons
fun getsFreeShipping(cart: List<CartItem>) =  calcTotal(cart) >= 20