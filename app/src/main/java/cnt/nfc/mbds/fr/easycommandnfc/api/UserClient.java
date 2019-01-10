package cnt.nfc.mbds.fr.easycommandnfc.api;

import cnt.nfc.mbds.fr.easycommandnfc.api.model.Login;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserClient {
    @POST("user")
    Call<ResponseBody> createUser(@Body Login login);
}
