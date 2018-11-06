package com.example.pavan.mobiofire.Activities;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pavan.mobiofire.DatabaseHandler.DatabaseHandler;
import com.example.pavan.mobiofire.DatabaseHandler.thumbs_model;
import com.example.pavan.mobiofire.Helpers.Apis;
import com.example.pavan.mobiofire.Helpers.Connectivity;
import com.example.pavan.mobiofire.Helpers.Constants;
import com.example.pavan.mobiofire.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog progressDialog;
    static ProgressDialog authDialog;


    private static final String TAG = "MainActivity";
    String ADMIN_CHANNEL_ID = "MY_ADMIN_CHANNEL_ID";
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 1;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);

//        authDialog.show();

        if (Connectivity.isConnected(LoginActivity.this)) {
            createAuthDialog(getApplicationContext());
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(Constants.webClientId)
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            mAuth = FirebaseAuth.getInstance();


            findViewById(R.id.sign_in_button).setOnClickListener(this);
        }else {
            Toast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
        if (Connectivity.isConnected(LoginActivity.this)) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            try {
                updateUI(currentUser);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(LoginActivity.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                authDialog=createAuthDialog(LoginActivity.this);
                authDialog.show();

                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
//            updateUI(account);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    private void updateUI(Object o) throws JSONException {
        if (o != null) {
//            dismissDialog();

            save_thumbs(this);


        }else {
            dismissDialog();
            Toast.makeText(LoginActivity.this, "No existing user", Toast.LENGTH_SHORT).show();

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            try {
                                updateUI(user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            try {
                                updateUI(null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // ...
                    }
                });
    }
    private ProgressDialog createAuthDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Signing In");
        progressDialog.setCancelable(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                cancelSendTask();
            }

            private void cancelSendTask() {
                //    Log.i(Setting_actvity.TAG, "Cancel Authenticated task!!");
            }
        });

        return progressDialog;
    }
    public static void dismissDialog() {
        if (authDialog != null) {
            try {
                authDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }

    public String save_thumbs(final Context mContext) throws JSONException {
        final OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASEURL)

                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30,
                                TimeUnit.SECONDS).build())
                .build();

        Apis mInterfaceService = retrofit.create(Apis.class);
        Call<JsonElement> mService = mInterfaceService.getdesc();
        mService.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    JSONArray response_array= new JSONArray(response.body().toString());
                    DatabaseHandler DatabaseHandler=new DatabaseHandler(mContext);
                    DatabaseHandler.delete_allThumb();
                    for (int i=0;i<=response_array.length()-1;i++){
                        JSONObject individual_object=new JSONObject(response_array.get(i).toString());
                        thumbs_model thumbs_model=new thumbs_model();
                        thumbs_model.setDescription(String.valueOf(individual_object.get("description")));
                        thumbs_model.setId(Integer.valueOf(individual_object.get("id").toString()));
                        thumbs_model.setThumb(String.valueOf(individual_object.get("thumb")));
                        thumbs_model.setTitle(String.valueOf(individual_object.get("title")));
                        thumbs_model.setUrl(String.valueOf(individual_object.get("url")));
                        DatabaseHandler.addThumb(thumbs_model);
                    }
                    Intent intent=new Intent(LoginActivity.this, Thumb_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Log.i(",","");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });


        return " ";
    }

}
