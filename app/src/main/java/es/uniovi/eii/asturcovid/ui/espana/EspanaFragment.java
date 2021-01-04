package es.uniovi.eii.asturcovid.ui.espana;

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

import es.uniovi.eii.asturcovid.R;

public class EspanaFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_espana, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        textView.setText("This is España Fragment");

        return root;
    }
}