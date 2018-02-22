package com.genesis.ksrtcbusfinder;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.genesis.ksrtcbusfinder.ipconfig.IpConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

public class BookingActivity extends AppCompatActivity {
    String bus_id;
    String username;
    SharedPreferences preferences;
    TextView userIdTextView=null;
    TextView busNumberTextView=null;
    EditText seatNoEditText=null;
    Button seatConfirmButton=null;
    TextView bookedTextView=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        bus_id=getIntent().getStringExtra("bus_id");
        preferences=getSharedPreferences("account_info",MODE_PRIVATE);
        username=preferences.getString("username","null");
        Toast.makeText(this, bus_id, Toast.LENGTH_SHORT).show();
        userIdTextView=findViewById(R.id.userIdTextView);
        busNumberTextView=findViewById(R.id.busNumberTextView);
        seatNoEditText=findViewById(R.id.seatNoEditText);
        seatConfirmButton=findViewById(R.id.seatConfirmButton);
        bookedTextView=findViewById(R.id.bookedTextView);
        userIdTextView.setText(username);
        busNumberTextView.setText(bus_id);

        seatConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seatNoEditText.getText().toString().equals("")){
                    seatNoEditText.setError("invalid number");
                }else{
                    String number=seatNoEditText.getText().toString();
                    Booking booking=new Booking();
                    booking.execute(bus_id,number);
                }
            }
        });
    }
    public class Booking extends AsyncTask<String,Void,String>{
        String Seat_Booking = "http://" + IpConfig.IP + "/ksrtc/seat_booking.php";

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(Seat_Booking);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("bus_id", "UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8")+ "&"
                        + URLEncoder.encode("seat_no", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8");
                bufferedwriter.write(data);
                bufferedwriter.flush();
                bufferedwriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String code= jsonObject1.getString("code");
                if(code.equals("true")) {
                    Toast.makeText(BookingActivity.this, "Seat Booked Successfully", Toast.LENGTH_SHORT).show();
                    Random rand = new Random();
                    int number = rand.nextInt(100);
                    bookedTextView.setText("KSRTCID"+(number+9955)+"AC"+number);
                }else {
                    Toast.makeText(BookingActivity.this, "Seat Booking Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
