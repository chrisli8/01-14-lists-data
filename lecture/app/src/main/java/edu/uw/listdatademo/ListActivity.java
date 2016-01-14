package edu.uw.listdatademo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListActivity extends AppCompatActivity {

    final ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

//        model!
        String[] data = new String[99];
        for(int i = 99; i > 0; i--) {
            data[99 - i] = i + " bottles of beer on the wall";
        }

//        String[] data = downloadMovieData("Die%20Hard");

        MovieDownloadTask task = new MovieDownloadTask();
        task.execute("Die%20Hard");

        //controller
        adapter = new ArrayAdapter<String>(
                this, R.layout.list_item, R.id.txtItem, data);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public class MovieDownloadTask extends AsyncTask<String, Void, String[]> {
         protected String[] doInBackground(String... params) {

                String movie = params[0];

                 //construct the url for the omdbapi API
                 String urlString = "http://www.omdbapi.com/?s=" + movie + "&type=movie";

                 HttpURLConnection urlConnection = null; // new HttpURLConnection (connection to url data)
                 BufferedReader reader = null; //new Buffered Reader

                 String movies[] = null; //new String array

                 try {

                     URL url = new URL(urlString); //initalize URL object

                     urlConnection = (HttpURLConnection) url.openConnection(); //cast url.openConnection to HttpURLConection
                     urlConnection.setRequestMethod("GET"); //set request to GET
                     urlConnection.connect(); //sends request

                     InputStream inputStream = urlConnection.getInputStream(); // get input data stream? get's data from database
                     StringBuffer buffer = new StringBuffer();
                     if (inputStream == null) {   //if null string[] return null
                         return null;
                     }
                     reader = new BufferedReader(new InputStreamReader(inputStream));

                     String line; //new string varable
                     while ((line = reader.readLine()) != null) { //builds string line by line fron reader (BufferedReader)
                         buffer.append(line + "\n");
                     }

                     if (buffer.length() == 0) {
                         return null;
                     }
                     String results = buffer.toString();
                     results = results.replace("{\"Search\":[",""); //repaces characters
                     results = results.replace("]}","");
                     results = results.replace("},", "},\n");

                     movies = results.split("\n");
                 }
                 catch (IOException e) { //if there is a problem
                     return null;
                 }
                 finally {
                     if (urlConnection != null) {  //disconnects if still connected
                         urlConnection.disconnect();
                     }
                     if (reader != null) {  //close reader
                         try {
                             reader.close();
                         }
                         catch (final IOException e) {
                         }
                     }
                 }

                 return movies; //returns movie[] String array

         }
    }

    protected void onPostExecute(String[] movies) {
        adapter.clear();
        for(String movie: movies) {
            adapter.add(movie);
        }
    }

}
