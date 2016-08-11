package org.edx.mobile.authentication;

import org.edx.mobile.http.ApiConstants;
import org.edx.mobile.model.api.ProfileModel;
import org.edx.mobile.model.api.ResetPasswordResponse;
import org.edx.mobile.module.prefs.LoginPrefs;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static org.edx.mobile.http.ApiConstants.URL_MY_USER_INFO;

public interface LoginService {

    /**
     * If there are form validation errors, this call will fail with 400 or 409 error code.
     * In case of validation errors the response body will be {@link org.edx.mobile.model.api.FormFieldMessageBody}.
     */
    @FormUrlEncoded
    @POST(ApiConstants.URL_REGISTRATION)
    Call<ResponseBody> register(@FieldMap Map<String, String> parameters);

    /**
     * Depending on the query parameters for this endpoint, a different action will be triggered
     * on the server side. In this case, we are sending a user and password to get the AuthResponse.
     */
    @FormUrlEncoded
    @POST(ApiConstants.URL_ACCESS_TOKEN)
    Call<AuthResponse> getAccessToken(@Field("grant_type") String grant_type,
                                      @Field("client_id") String client_id,
                                      @Field("username") String username,
                                      @Field("password") String password);

    /**
     * Depending on the query parameters for this endpoint, a different action will be triggered
     * on the server side. In this case, we are using our refresh_token to get a new AuthResponse.
     */
    @FormUrlEncoded
    @POST(ApiConstants.URL_ACCESS_TOKEN)
    Call<AuthResponse> refreshAccessToken(@Field("grant_type") String grant_type,
                                          @Field("client_id") String client_id,
                                          @Field("refresh_token") String refresh_token);


    /**
     * Authenticate with edX using an access token from a third party OAuth provider.
     * @param accessToken access token retrieved from third party OAuth provider (i.e. Facebook, Google)
     * @param clientId edX OAuth client ID from config
     * @param groupId Group ID as returned from {@link ApiConstants#getOAuthGroupIdForAuthBackend(LoginPrefs.AuthBackend)}
     */
    @FormUrlEncoded
    @POST(ApiConstants.URL_EXCHANGE_ACCESS_TOKEN)
    Call<AuthResponse> exchangeAccessToken(@Field("access_token") String accessToken,
                                           @Field("client_id") String clientId,
                                           @Path(ApiConstants.GROUP_ID) String groupId);

    /**
     * Reset password for account associated with an email address.
     */
    @FormUrlEncoded
    @POST(ApiConstants.URL_PASSWORD_RESET)
    Call<ResetPasswordResponse> resetPassword(@Field("email") String email);


    /**
     * @return basic profile information for currently authenticated user.
     */
    @GET(URL_MY_USER_INFO)
    Call<ProfileModel> getProfile();
}
