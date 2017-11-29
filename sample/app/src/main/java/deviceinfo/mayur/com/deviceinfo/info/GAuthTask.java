package deviceinfo.mayur.com.deviceinfo.info;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

import deviceinfo.mayur.com.deviceinfo.info.MainActivity;

/**
 * Created by arindam.d.dutta on 4/22/2015.
 */
public class GAuthTask extends AsyncTask<Void, Void, String> {

    /**
     * The Constant SCOPE.
     */
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";

    /**
     * The m fragment.
     */
    protected MainActivity mFragment;

    /**
     * The m email.
     */
    protected String mEmail;

    /**
     * The pd.
     */
    private ProgressDialog pd;

    /**
     * Instantiates a new g auth task.
     *
     * @param fragment the fragment
     * @param email    the email
     */
    public GAuthTask(MainActivity fragment, String email) {
        this.mFragment = fragment;
        this.mEmail = email;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPreExecute()
     * <p>Execute before it makes an API call.</p>
     * <p>Show the progress dialog box.</p>
     */
    @Override
    protected void onPreExecute() {
        pd = ProgressDialog.show(mFragment, null, "Please wait ...");
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     * <p>Fetch Google Token based on parameters available.</p>
     */
    @Override
    protected String doInBackground(Void... params) {
        try {
            return fetchToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onCancelled()
     * <p>Hide the progress dialog box.</p>
     */
    @Override
    protected void onCancelled() {
        if (pd != null && pd.isShowing()) pd.dismiss();
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     * <p>Executes Post API call.</p>
     * <p>If result is not null, set authToken value to Google.</p>
     * <p>Hide the progress dialog box.</p>
     */
    @Override
    protected void onPostExecute(String result) {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        Toast.makeText(mFragment,result,Toast.LENGTH_LONG).show();
    }

    /**
     * Get a authentication token if one is not available. If the error is not
     * recoverable then it displays the error message on parent activity right
     * away.
     *
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected String fetchToken() throws IOException {
        try {
            String token =GoogleAuthUtil.getToken(mFragment, mEmail, SCOPE);

            return token;
        } catch (GooglePlayServicesAvailabilityException playEx) {
            // GooglePlayServices.apk is either old, disabled, or not present.
            //if (!isCancelled())
                //mFragment.showErrorDialog(playEx.getConnectionStatusCode());
        } catch (UserRecoverableAuthException userRecoverableException) {
            // Unable to authenticate, but the user can fix this.
            // Forward the user to the appropriate activity.
            if (!isCancelled())
                mFragment.startActivityForResult(userRecoverableException.getIntent(), MainActivity.REQUEST_CODE_RECOVER_FROM_AUTH_ERROR);
            return userRecoverableException.getMessage();       //TEJ-29744
        } catch (GoogleAuthException fatalException) {
            if (!isCancelled())
                Toast.makeText(mFragment, "Unrecoverable error " + fatalException.getMessage(),Toast.LENGTH_LONG).show();
        }
        return null;
    }


}
