package HTTPS;

import android.content.Context;
import android.widget.Toast;

import com.safepath.safepath.Hazard;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.UUID;


public class FirestoreRequest {


    public static void sendHazard(Context myContext, final Hazard myHazard){

        ArrayList<Field_Value> myFields = new ArrayList<>();
        myFields.add(new Field_Value("Latitude",myHazard.getLatitude()));
        myFields.add(new Field_Value("Longitude",myHazard.getLongitude()));
        myFields.add(new Field_Value("DateTime",myHazard.getDateTime()));

        String endpoint = "Hazards/" + UUID.randomUUID().toString();

        HTTPS_PATCH myRequest = new HTTPS_PATCH(endpoint, myFields, new HTTPS_PATCH.RESPONSE() {
            @Override
            public void RESPONSE(int responseCode) {


                if(responseCode == HttpURLConnection.HTTP_OK){

                    Toast.makeText(myContext,"The Hazard was sent!",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(myContext,"Connection Issues, couldn't send",Toast.LENGTH_SHORT).show();
                }

            }
        });
        myRequest.execute();
    }

}
