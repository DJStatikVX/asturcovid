package es.uniovi.eii.asturcovid.ui.areas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import es.uniovi.eii.asturcovid.ListaAreasFragment;
import es.uniovi.eii.asturcovid.MainActivity;
import es.uniovi.eii.asturcovid.MapaFragment;
import es.uniovi.eii.asturcovid.R;

public class AreasFragment extends Fragment {
    private SharedPreferences sharedPreferencesMainActivity;
    private String areaPreferida;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabItem mapa;
    private TabItem listaAreas;
    protected static String fecha;
    private View root;
    private ViewGroup container;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_areas, container, false);
        this.container = container;

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_areas, this.container, false);

        sharedPreferencesMainActivity =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        areaPreferida = sharedPreferencesMainActivity.getString("keyAreaSanitaria", "");

        viewPager = root.findViewById(R.id.view_pager);
        tabLayout = root.findViewById(R.id.tab_layout);

        mapa = root.findViewById(R.id.tabitem_mapa);
        listaAreas = root.findViewById(R.id.tabitem_listaAreas);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        //mapaFragment = new MapaFragment();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        //private List<Fragment> fragments = new ArrayList<>();
        private int numeroDeElementos;
        private String[] titulos = new String[]{"Mapa", "Lista Áreas"};

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            numeroDeElementos = behavior;
        }

        /*public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitles.add(title);
        }*/
        @Override
        public CharSequence getPageTitle(int position){
            return titulos[position];
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new MapaFragment();
                case 1:
                    return new ListaAreasFragment(areaPreferida, fecha);
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return numeroDeElementos;
        }
    }
}