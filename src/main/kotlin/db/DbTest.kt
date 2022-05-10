package db

import UserDB

fun main() {
    val db = UserDB()
    println(db.selectAllUsers())
//    db.insertUser(User(-1, "user", "qqq", "qwq"))
//    db.updateUser(User(1, "First", "User", "EN"))
//    println(db.selectAllUsers())

}

class DbTest {

}