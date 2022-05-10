import com.google.gson.Gson
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import db.model.User
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.charset.Charset


object Server {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val server = HttpServer.create(InetSocketAddress("localhost", 2121), 0)
        server.createContext("/get_users", GetUsersHandler())
        server.createContext("/add_user", AddUserHandler())
        server.createContext("/update_user", UpdateUserHandler())
            server.createContext("/delete_user", DeleteUserHandler())
        server.executor = null // creates a default executor
        server.start()
    }


    internal class AddUserHandler : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {
            val addUserBody = Gson().fromJson(
                t.requestBody.readAllBytes().toString(Charset.defaultCharset()),
                Api.AddUserBody::class.java
            )

            UserDB().insertUser(
                User(
                    -1,
                    addUserBody.name,
                    addUserBody.surname,
                    addUserBody.country
                )
            )

            val response = "OK"


            t.sendResponseHeaders(201, response.length.toLong())
            val os = t.responseBody
            os.write(response.toByteArray())
            os.close()
        }
    }

    internal class GetUsersHandler : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {

            val users = UserDB().selectAllUsers()
            val response = Gson().toJson(Api.UserListResponse(users))

            t.responseHeaders.add("Access-Control-Allow-Origin", "*");
            t.responseHeaders.add("Access-Control-Allow-Credentials", "true");
            t.responseHeaders.add("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
            t.responseHeaders.add(
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"
            )

            t.sendResponseHeaders(200, response.length.toLong())
            val os = t.responseBody
            os.write(response.toByteArray())
            os.close()
        }
    }

    internal class UpdateUserHandler : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {
            val updateBody = Gson().fromJson(
                t.requestBody.readAllBytes().toString(Charset.defaultCharset()),
                Api.UpdateUserBody::class.java
            )

            val isUpdated = UserDB().updateUser(
                User(
                    updateBody.id,
                    updateBody.name,
                    updateBody.surname,
                    updateBody.country
                )
            )

            val response = if (isUpdated) {
                "OK"
            } else "Not Found"

            t.sendResponseHeaders(200, response.length.toLong())
            val os = t.responseBody
            os.write(response.toByteArray())
            os.close()
        }
    }

    internal class DeleteUserHandler : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {
            val body = t.requestBody.readAllBytes().toString(Charset.defaultCharset())
            println("delete responce: request method - ${t.requestMethod}, body - $body")
            val deleteBody = Gson().fromJson(
                body,
                Api.DeleteBody::class.java
            )

            t.responseHeaders.add("Access-Control-Allow-Origin", "*");
            t.responseHeaders.add("Access-Control-Allow-Credentials", "true");
            t.responseHeaders.add("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
            t.responseHeaders.add(
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"
            )

            val isDeleted = if (deleteBody != null) UserDB().deleteUser(deleteBody.id)
            else false

            val response = if (isDeleted) {
                "OK"
            } else "Not Found"

            println("response: $response")

            t.sendResponseHeaders(200, response.length.toLong())
            val os = t.responseBody
            os.write(response.toByteArray())
            os.close()
        }
    }
}