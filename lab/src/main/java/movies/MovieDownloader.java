package movies;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * A class for downloading movie data from the internet.
 * Code adapted from Google.
 *
 * YOUR TASK: Add comments explaining how this code works!
 * 
 * @author Joel Ross & Kyungmin Lee
 */
public class MovieDownloader {

    //takes in movie string, returns string array
	public static String[] downloadMovieData(String movie) { 

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


	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in); //new scanner looking at system.in
		
		System.out.print("Enter a movie name to search for: ");
		String searchTerm = sc.nextLine().trim(); //trims end spaces from input
		String[] movies = downloadMovieData(searchTerm); //get's moview string array
		for(String movie : movies) {
			System.out.println(movie); //print each element in movie array
		}
		
		sc.close(); //close scanner
	}
}
