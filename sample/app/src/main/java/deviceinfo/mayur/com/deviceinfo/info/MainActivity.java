package deviceinfo.mayur.com.deviceinfo.info;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.eyeem.deviceinfo.DeviceInfo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;

import deviceinfo.mayur.com.deviceinfo.R;
import deviceinfo.mayur.com.deviceinfo.album.AlbumActivity;
import deviceinfo.mayur.com.deviceinfo.model.SettingInfo;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    public static final int PICK_ACCOUNT_REQUEST = 101;
    public static final int ADD_ACCOUNT_REQUEST = 102;
    public static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;


    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";


    DeviceInfoAdapter mDeviceInfoAdapter;
    RecyclerView mDeviceInfoRecyclerView;
    ArrayList<SettingInfo> infoList= new ArrayList<>();
    private GoogleApiClient mGoogleApiClient;
    private GAuthTask mGAuthTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DeviceInfo di = DeviceInfo.get(this);
        mDeviceInfoRecyclerView= findViewById(R.id.maincollection);
        mDeviceInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mDeviceInfoRecyclerView.setAdapter();
        populateSettings(di);
        mDeviceInfoRecyclerView.setAdapter(new DeviceInfoAdapter(this,infoList));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signIn();
                loadMediaItems();
            }
        });


    }

    private void loadMediaItems() {
        Intent launchIntent = new Intent(this, AlbumActivity.class);
        startActivity(launchIntent);

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void populateSettings(DeviceInfo info){
        infoList.clear();
        infoList.add(new SettingInfo("is7inch",info.is7inch+""));
        infoList.add(new SettingInfo("is10inch",info.is10inch+""));
        infoList.add(new SettingInfo("isPortrait",info.isPortrait+""));
        infoList.add(new SettingInfo("isLandscape",info.isLandscape+""));
        infoList.add(new SettingInfo("isPhone",info.isPhone+""));
        infoList.add(new SettingInfo("isTablet",info.isTablet+""));
        infoList.add(new SettingInfo("isInMultiWindowMode",info.isInMultiWindowMode+""));
        infoList.add(new SettingInfo("isInPictureInPictureMode",info.isInPictureInPictureMode+""));
        infoList.add(new SettingInfo("isAmazon",info.isAmazon+""));
        infoList.add(new SettingInfo("heightPixels",info.heightPixels+""));
        infoList.add(new SettingInfo("heightDip",info.heightDip+""));
        infoList.add(new SettingInfo("widthPixels",info.widthPixels+""));
        infoList.add(new SettingInfo("widthDip",info.widthDip+""));
        infoList.add(new SettingInfo("smallestWidthDp",info.smallestWidthDp+""));
        infoList.add(new SettingInfo("statusBarHeight",info.statusBarHeight+""));
        infoList.add(new SettingInfo("navigationBarHeight",info.navigationBarHeight+""));
        infoList.add(new SettingInfo("diagonalScreenSize",info.diagonalScreenSize+""));
        infoList.add(new SettingInfo("displayRealSize",info.getDisplayRealSize()+""));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR) {
            try {
                handleAuthorizeResult(resultCode, data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            new GAuthTask(this, acct.getEmail()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(this, "Signin failed",Toast.LENGTH_LONG).show();

        }
    }

//    protected String fetchToken(String email) throws IOException {
//        try {
//            String token =GoogleAuthUtil.getToken(this, email, SCOPE);
//            return token;
//        } catch (GooglePlayServicesAvailabilityException playEx) {
//            // GooglePlayServices.apk is either old, disabled, or not present.
//
//        } catch (UserRecoverableAuthException userRecoverableException) {
//            // Unable to authenticate, but the user can fix this.
//            // Forward the user to the appropriate activity.
//            startActivityForResult(userRecoverableException.getIntent(), REQUEST_CODE_RECOVER_FROM_AUTH_ERROR);
//            return userRecoverableException.getMessage();
//        } catch (GoogleAuthException ignored) {
//
//        }
//        return null;
//    }

    private void handleAuthorizeResult(int resultCode, Intent data) throws IOException {
        if (data == null) {
            Toast.makeText(this, "Unknown Error handleAuthorizeResult", Toast.LENGTH_LONG).show();
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            mGAuthTask = new GAuthTask(this, email);
            mGAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

}
