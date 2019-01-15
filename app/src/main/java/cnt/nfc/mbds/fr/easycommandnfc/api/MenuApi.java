package cnt.nfc.mbds.fr.easycommandnfc.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MenuApi {
    @GET("menu")
    Call<ResponseBody> getMenu();
}
