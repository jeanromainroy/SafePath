package HTTPS;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;


public class HTTPS_PATCH extends AsyncTask<Void,Void,Void> {

    // Attributes
    private String accessPoint_;
    private String webApiKey_;
    private String endPoint_;
    private ArrayList<Field_Value> fieldMasks_;

    private HttpsURLConnection myHttpsConnection;


    // Response Var
    private RESPONSE myRESPONSE_;
    private int myResponseCode;

    // Interface for callbacks
    public interface RESPONSE{
        void RESPONSE(int responseCode);
    }

    // Constructor
    HTTPS_PATCH(String endPoint, ArrayList<Field_Value> fieldMasks, RESPONSE myRESPONSE){
        this.myRESPONSE_ = myRESPONSE;
        this.accessPoint_ = Constants.accessPoint;
        this.webApiKey_ = Constants.webApiKey;
        this.endPoint_ = endPoint;
        this.fieldMasks_ = fieldMasks;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        myResponseCode = 0;

        try {

            // Create URL
            URL endPointURL = new URL(accessPoint_ + "/" + endPoint_ + "?key=" + webApiKey_);

            // Create connection
            myHttpsConnection = (HttpsURLConnection) endPointURL.openConnection();

            // Set Request Method
            myHttpsConnection.setRequestMethod("PATCH");

            // Set Writable
            myHttpsConnection.setDoOutput(true);

            // Set Https Connection properties
            myHttpsConnection.setRequestProperty("Content-Type", "application/json");

            // Generate JSON from the data
            JSONObject myJSON = JSON_Methods.buildFirestoreJSON(fieldMasks_);

            // Create output stream linked to our https connection
            OutputStreamWriter streamWriter = new OutputStreamWriter(myHttpsConnection.getOutputStream());

            // Write to buffer
            if (myJSON != null) {
                streamWriter.write(myJSON.toString());
            }

            // Send out the buffer
            streamWriter.flush();

            // Close the stream
            streamWriter.close();

            // Get Response Code
            myResponseCode = myHttpsConnection.getResponseCode();


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {

            // Disconnect
            myHttpsConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        endTask();
    }

    private void endTask(){

        // Return null callback
        myRESPONSE_.RESPONSE(myResponseCode);
    }
}