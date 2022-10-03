package mutablejava.immutable

// Because of Kotlin's functional-style language features, many of the functions that are multi-line operations
// in Java due to copy-on-write become simple one-line functions in Kotlin

var mailingList = listOf<String>();

fun addContact(list: List<String>, email: String): List<String> {
    return list + email  // Kotlin's + operator on lists does copy-on-write!
}

fun submitFormHandler(email: String) {
    mailingList = addContact(mailingList, email)
}

// Avoiding Read-And-Write functions by splitting read from write
// The original READ is simply getting the nth element
fun getNthContact(list: List<String>, n: Int): String {
    return list[n-1]
}
// The original WRITE is the element removal (for which we can use copy-on-write to turn the write into a read)
fun removeNthContact(list: List<String>, n: Int): List<String> {
    return list.minus(list[n-1]) // Kotlin's minus method on lists returns a new list minus the given element (copy-on-write)
}

// Avoiding Read-And-Write functions by returning two values
fun removeNthContact2(list: List<String>, n: Int): Pair<String, List<String>> {
    return Pair(list[n-1], list.minus(list[n-1]))   // Kotlin has a built-in Pair type that allows for easy
}

data class Person(val name: String, val age: Int, val hairColor: String)

fun dyeHair(p: Person, color: String): Person {
    return p.copy(hairColor=color); // Make a copy with the new hair color
}

fun dyeHairByName(people: List<Person>, name: String, color: String): List<Person> {
    // Here, since we are changing the element of a list we must do copy-on-write on the list
    val newList = mutableListOf<Person>();
    var i = 0
    for ( p in people ) {
        if ( p.name == name ) {
            // But since we are also changing the item in a specific element of the list we call the copy-on-write dyeHair that gives us a NEW person object
            newList.add(dyeHair(p, color));
        } else {
            newList.add(p)
        }
    }
    return newList;
}