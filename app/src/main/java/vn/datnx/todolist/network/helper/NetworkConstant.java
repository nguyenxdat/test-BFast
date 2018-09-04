package vn.datnx.todolist.network.helper;

public class NetworkConstant {

    private static final String BASE_URL = "https://api.todo.ql6625.fr/api/";

    public class API {
        public static final String LOGIN = BASE_URL + "Accounts/login";
        public static final String LIST_TODO = BASE_URL + "Accounts/%d/todos";
        public static final String CREATE_TODO = BASE_URL + "Accounts/%d/todos";
    }

}
