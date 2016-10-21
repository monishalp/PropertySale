package com.dada.realestatemanager.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.dada.realestatemanager.AppController;
import com.dada.realestatemanager.R;
import com.dada.realestatemanager.adapter.PropertyRecyclerAdapter;
import com.dada.realestatemanager.util.Constant;
import com.dada.realestatemanager.util.PropertyList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WYD on 7/25/2016.
 */
public class ViewPropertyFragment extends Fragment {

    private PropertyRecyclerAdapter propertyRecyclerAdapter;
    private List<PropertyList> propertyList = new ArrayList<>();

    static PropertyList editProperty;

    private Callbacks mCallbacks;

    public interface Callbacks{
        public void refreshProperty();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_property_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Properties");
        return v;
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.property_recycler_view);
        propertyRecyclerAdapter = new PropertyRecyclerAdapter(getActivity(), propertyList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(propertyRecyclerAdapter);

        JsonArrayRequest jsonArray = new JsonArrayRequest(Constant.MYPROPERTY_URL + getActivity().getIntent().getStringExtra("userid"),
//        JsonArrayRequest jsonArray = new JsonArrayRequest(Constant.PROPERTY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject obj = response.getJSONObject(i);
                                PropertyList property = new PropertyList();
                                property.setId(obj.getString("Property Id"));
                                property.setName(obj.getString("Property Name"));
                                property.setType(obj.getString("Property Type"));
                                property.setCategory(obj.getString("Property Category"));
                                property.setAddress1(obj.getString("Property Address1"));
                                property.setAddress2(obj.getString("Property Address2"));
                                property.setLatitude(obj.getString("Property Latitude"));
                                property.setLongitude(obj.getString("Property Longitude"));
                                property.setZip(obj.getString("Property Zip"));
                                property.setCost(obj.getString("Property Cost"));
                                property.setSize(obj.getString("Property Size"));
                                property.setDescription(obj.getString("Property Desc"));
                                property.setImage1(obj.getString("Property Image 1"));
                                property.setImage2(obj.getString("Property Image 2"));
                                property.setImage3(obj.getString("Property Image 3"));

                                ViewPropertyFragment.this.propertyList.add(property);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        propertyRecyclerAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonArray);

        propertyRecyclerAdapter.setOnItemClickListener(new PropertyRecyclerAdapter.MyClickListener() {
            @Override
            public void onItemClick(final int position, View v) {

                final CharSequence[] items = {"Edit Property", "Delete Property"};
                AlertDialog dlg = new AlertDialog.Builder(getActivity()).setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {

                                if (item == 0) {
                                    editProperty = propertyList.get(position);
                                    android.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                                    android.app.Fragment fragment = new EditPropertyFragment();
                                    ft.replace(R.id.framentContainer, fragment);
                                    ft.commit();
                                } else if (item == 1) {
                                    deleteProperty(Integer.valueOf(propertyList.get(position).getId()));
                                }
                            }
                        }).create();
                dlg.show();
            }
        });
    }

    private void deleteProperty(int id) {

        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.DELETE_PROPERTY_URL + String.valueOf(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "Delete Success", Toast.LENGTH_LONG).show();
                mCallbacks.refreshProperty();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        AppController.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mCallbacks = (Callbacks) context;
    }
}
