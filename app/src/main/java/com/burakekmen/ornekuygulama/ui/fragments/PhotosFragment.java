package com.burakekmen.ornekuygulama.ui.fragments;


import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.burakekmen.ornekuygulama.R;
import com.burakekmen.ornekuygulama.adapters.RcListAdapter;
import com.burakekmen.ornekuygulama.adapters.UrlAdapter;
import com.burakekmen.ornekuygulama.models.PhotoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = PhotosFragment.class.getSimpleName();
    private static final int COLUMN_NUM = 3;
    private static final int ITEM_PER_PAGE = 20;

    private RequestQueue requestQueue;
    private RecyclerView rcList;
    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FrameLayout photosFragment = null;

    private RcListAdapter rcListAdapter;

    private boolean isLoading = false;
    private boolean dahaFazla = true;

    private SearchView searchView=null;

    private boolean ilkAcilisMi=true;

    public PhotosFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());

        rcList = view.findViewById(R.id.fragment_photos_rcList);
        rcList.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(), COLUMN_NUM);
        rcList.setLayoutManager(layoutManager);
        rcListAdapter = new RcListAdapter(getActivity(), new ArrayList<PhotoModel>());
        rcList.setAdapter(rcListAdapter);

        rcList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int toplamResim = layoutManager.getItemCount();
                int sonResimPosition = layoutManager.findLastVisibleItemPosition();
                if (dahaFazla && !isLoading && toplamResim - 1 != sonResimPosition) {
                    resimleriCek();
                }
            }
        });

        swipeRefreshLayout =  view.findViewById(R.id.fragment_photos_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        photosFragment = view.findViewById(R.id.fragment_photos_fragmentlayout);

        if(isOnline())
            resimleriCek();
        else
            internetYokMesaji();

        return view;
    }

    private void internetYokMesaji(){
        Snackbar snackBar = Snackbar.make(photosFragment, "İnternet Bağlantınızı Kontrol Ediniz!", Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void yenile() {
        if(isOnline()) {
            Toast.makeText(getContext(), "Yenileniyor...", Toast.LENGTH_SHORT).show();
            rcListAdapter = new RcListAdapter(getActivity(), new ArrayList<PhotoModel>());
            rcList.setAdapter(rcListAdapter);
            resimleriCek();
        }else
            internetYokMesaji();
    }

    private void resimleriCek() {
        isLoading = true;
        int toplamResim = layoutManager.getItemCount();
        final int page = toplamResim / ITEM_PER_PAGE + 1;

        String query = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(UrlAdapter.PREF_SEARCH_QUERY, null);

        if(ilkAcilisMi){
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .edit()
                    .putString(UrlAdapter.PREF_SEARCH_QUERY, null)
                    .apply();

            query = null;
            ilkAcilisMi=false;
        }

        UrlAdapter.getInstance();
        String url = UrlAdapter.getItemUrl(query, page);

        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        List<PhotoModel> resimListesi = new ArrayList<>();
                        try {
                            JSONObject resimler = response.getJSONObject("photos");
                            if (resimler.getInt("pages") == page) {
                                dahaFazla = false;
                            }
                            JSONArray resimDizisi = resimler.getJSONArray("photo");
                            for (int i = 0; i < resimDizisi.length(); i++) {
                                JSONObject resim = resimDizisi.getJSONObject(i);
                                PhotoModel r = new PhotoModel(
                                        resim.getString("id"),
                                        resim.getString("secret"),
                                        resim.getString("server"),
                                        resim.getString("farm")
                                );
                                resimListesi.add(r);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Beklenmedik Bir Hata Oldu", Toast.LENGTH_SHORT).show();
                        }
                        rcListAdapter.addAll(resimListesi);
                        rcListAdapter.notifyDataSetChanged();
                        isLoading = false;
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        request.setTag(TAG);
        requestQueue.add(request);
    }

    private void yuklemeyiDurdur() {
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        yuklemeyiDurdur();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menuler, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {

        }
        SearchManager searchManager = (SearchManager) getActivity()
                .getSystemService(Context.SEARCH_SERVICE);
        ComponentName name = getActivity().getComponentName();
        SearchableInfo searchInfo = searchManager.getSearchableInfo(name);
        searchView.setSearchableInfo(searchInfo);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean selectionHandled = false;
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                selectionHandled = true;
                break;
            default:
                selectionHandled = super.onOptionsItemSelected(item);
                break;
        }
        return selectionHandled;
    }

    @Override
    public void onRefresh() {
        yenile();
        swipeRefreshLayout.setRefreshing(false);
    }
}
