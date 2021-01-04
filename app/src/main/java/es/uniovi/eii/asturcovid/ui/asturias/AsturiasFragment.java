package es.uniovi.eii.asturcovid.ui.asturias;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import es.uniovi.eii.asturcovid.MainActivity;
import es.uniovi.eii.asturcovid.R;

public class AsturiasFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_asturias, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        DownloadFilesTaskAsturias task = new DownloadFilesTaskAsturias();
        try {
            task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return root;
    }

    public static class DownloadFilesTaskAsturias extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {

            try {
                //realizarPeticionDatos();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    /*public static void realizarPeticionDatos(){
        try {

            URL url = new URL("https://api.covid19tracking.narrativa.com/api/country/spain/region/asturias?date_from=2020-12-21&date_to=2020-12-28");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                //Using the JSON simple library parse the string into a json object
                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);

                JSONObject fechas = (JSONObject) data_obj.get("Dates");

                HashMap<String, List<Integer>> map = new HashMap<>();
                Set keys = fechas.keys;
                for (String key:keys) {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}