import db.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataProvider {

//    companion object {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            val client = DataProvider()
//
//            client.sendGetRequest()
//            client.sendAddUserRequest(User(-1, "Name", "sur", "we"))
//            client.sendUpdateRequest(User(10, "Name", "sur", "we"))
//            client.sendDeleteRequest(10)
//        }
//    }


    fun getUsers(usersCallback: (users: List<User>) -> Unit){
        Api.invoke().getUsers(
        ).enqueue(object : Callback<Api.UserListResponse> {
            override fun onResponse(
                call: Call<Api.UserListResponse>,
                response: Response<Api.UserListResponse>
            ) {
                println("Пользователи: ${response.body()!!}")
                println(response)
                usersCallback(response.body()?.response ?: listOf())
            }

            override fun onFailure(call: Call<Api.UserListResponse>, t: Throwable) {
                println("failure ${t.message}")
                usersCallback(listOf())
            }
        })
    }

    fun sendDeleteRequest(id: Int) {
        Api.invoke().deleteUser(Api.DeleteBody(id)).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                println("deleteUser отправлен: ${response.body()!!}")
                println(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("failure ${t.message}")
            }
        })
    }

    fun sendAddUserRequest(user: User) {
        Api.invoke().addUser(Api.AddUserBody(user.name, user.surname, user.country)).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                println("addUser отправлен: ${response.body()!!}")
                println(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("failure ${t.message}")
            }
        })
    }

    fun sendUpdateRequest(user: User) {
        Api.invoke().updateUser(Api.UpdateUserBody(user.id, user.name, user.surname, user.country)).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                println("updateUser отправлен: ${response.body()!!}")
                println(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("failure ${t.message}")
            }
        })
    }
}