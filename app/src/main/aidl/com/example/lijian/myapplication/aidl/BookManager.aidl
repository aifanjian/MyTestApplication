// BookManager.aidl
package com.example.lijian.myapplication.aidl;

import com.example.lijian.myapplication.aidl.Book;

// Declare any non-default types here with import statements

interface BookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

     List<Book> geBooks();
     void addBook(in Book book);
     void removeBook(in String name);

}
