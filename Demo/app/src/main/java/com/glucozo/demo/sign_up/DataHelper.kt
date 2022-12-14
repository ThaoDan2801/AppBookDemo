package com.glucozo.demo.sign_up

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.core.content.contentValuesOf

class DataHelper(context: Context) : SQLiteOpenHelper(context, dbname, factory, version) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE  user (id INTEGER primary key autoincrement NOT NULL,  name varchar(30), email varchar(100), password varchar(20) )");
        p0?.execSQL("CREATE TABLE  book (id INTEGER primary key autoincrement NOT NULL,  author varchar(30), evaluate varchar(100), img varchar(20) ,name varchar(40),price varchar(10))");
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
    companion object {
        var dbname = "userDB"
        var factory = null
        var version = 1
    }

    fun insertUserData(name: String, email: String, password: String) {
        var db: SQLiteDatabase = writableDatabase
        var values: ContentValues = ContentValues()
        values.put("name", name)
        values.put("email", email)
        values.put("password", password)

        db.insert("user", null, values)
        db.close()
    }

    fun userPresent(email: String, password: String): Boolean {
        var db = writableDatabase
        var query = "select * from user where email = '$email' and password ='$password'"
        var cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun insertUserDataBook(author: String, evaluate: String, img: String, name: String, price: String) {
        var db: SQLiteDatabase = writableDatabase
        var values: ContentValues = ContentValues()
        values.put("author", author)
        values.put("evaluate", evaluate)
        values.put("img", img)
        values.put("name", name)
        values.put("price", price)

        db.insert("book", null, values)
        db.close()
    }

}