package mutablejava.mutable;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static List<String> mailingList = new ArrayList<String>();

    public static void addContact(String email) {
        mailingList.add(email);  // Write to mutable data structure makes this function an action!
    }

    public static void submitFormHandler(String email) {
        addContact(email);
    }

    public static String removeNthContact(int n) {
        // This function both modifies the list AND returns a value (the item that was removed from the list)
        return mailingList.remove(n-1);
    }

    public static void dyeHair(Person p, String color) {
        // p.hairColor = color;  // Since Person is a record, it is already immutable and code like this won't work!
    }

    public static void dyeHairByName(List<Person> people, String name, String color) {
        for ( Person p : people ) {
            if ( p.name().equals(name) ) {
                // p.hairColor = color;  // Since Person is a record, it is already immutable and code like this won't work!

                // BUT neither will this, because this just changes the object to which p is pointing and NOT the list:
                // p = dyeHair(p, color);
            }
        }
    }

    public record Person(String name, int age, String hairColor) { }
}
