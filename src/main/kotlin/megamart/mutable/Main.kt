package megamart.mutable

import megamart.*

import megamart.setCartTotalDom

var shoppingCart = mutableListOf<CartItem>()                // Updating of global variables is an action

// Calculation: only explicit inputs and outputs are used
// We can now use this calculation for any purpose; not just in calcCartTotal, which does some additional work
fun calcTotal(cart: List<CartItem>): Double {
    var total = 0.0
    for ( item in cart ) {
        total += item.price
    }
    return total
}

// Action
fun addItemToCart(name: String, price: Double) {
    addItem(shoppingCart, name, price)                      // Reading global is an action
    val total = calcTotal(shoppingCart)                     // Reading global is an action
    setCartTotalDom(total)                                  // setCartTotalDom is an action
    updateShippingIcons(shoppingCart)                       // updateShippingIcons is an action
    updateTaxDom(total)                                     // updateTaxDom is an action

    // YIKES! We have no control over this function, and it might change shoppingCart
    // We need to do some defensive copying
    blackFridayPromotion(shoppingCart)
}

// Action (reading global variable is an action)
fun deleteHandler(name: String) {
    removeItemByName(shoppingCart, name)
    val total = calcTotal(shoppingCart)
    setCartTotalDom(total)
    updateShippingIcons(shoppingCart)
    updateTaxDom(total)
}

// MUTABLE WRITE
fun removeItemByName(cart: MutableList<CartItem>, name: String) {
    for ( item in cart ) {
        if ( item.name == name ) {
            cart.remove(item)
            return;
        }
    }
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

// Action
fun updateShippingIcons(cart: List<CartItem>) {
    // YIKES! We are obtaining mutable data from an untrusted source
    // Defensive coding is required to prevent buyButtons from mutating
    val buyButtons = getBuyButtonsDom()                     // Reading from the DOM is an action

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