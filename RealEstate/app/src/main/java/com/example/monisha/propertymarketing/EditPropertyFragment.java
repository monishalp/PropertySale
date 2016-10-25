package com.example.monisha.propertymarketing;

/**
 * Created by monisha on 10/21/2016.
 */
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class EditPropertyFragment extends Fragment {
    private SimpleLocation mLocation;
    ProgressDialog pDialog;
    final private String[] types = new String[]{"Apartment", "Condominium", "Individual House", "Villa", "Townhouse"};
    final private String[] categories = new String[]{"Rent", "Outright Purchase"};
    private String id, name, type, category, address1, address2, zip, latitude, longitude, cost, size, description;
    private TextView submit;
    private EditText propertyName, property_address1, property_address2, property_zip, property_location, property_value, property_size, property_description;

    private ImageView propertyImage1, propertyImage2, propertyImage3;
    Bitmap bitmap1, bitmap2, bitmap3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_property_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Property");

        // construct a new instance
        mLocation = new SimpleLocation(getActivity());

        // reduce the precision to 5,000m for privacy reasons
        mLocation.setBlurRadius(5000);

        // if we can't access the location yet
        if (!mLocation.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(getActivity());
        }

        final Spinner categorySpinner, typeSpinner;
        typeSpinner = (Spinner) v.findViewById(R.id.edit_property_type);
        categorySpinner = (Spinner) v.findViewById(R.id.edit_property_category);
        submit = (TextView) v.findViewById(R.id.edit_property_submit);

        propertyName = (EditText) v.findViewById(R.id.edit_property_name);
        property_address1 = (EditText) v.findViewById(R.id.edit_property_address_1);
        property_address2 = (EditText) v.findViewById(R.id.edit_property_address_2);
        property_zip = (EditText) v.findViewById(R.id.edit_property_zip);
        property_location = (EditText) v.findViewById(R.id.edit_property_location);
        property_value = (EditText) v.findViewById(R.id.edit_property_cost);
        property_size = (EditText) v.findViewById(R.id.edit_property_size);
        property_description = (EditText) v.findViewById(R.id.edit_property_description);

        propertyImage1 = (ImageView) v.findViewById(R.id.upload_img1);
        propertyImage2 = (ImageView) v.findViewById(R.id.upload_img2);
        propertyImage3 = (ImageView) v.findViewById(R.id.upload_img3);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        switch (ViewPropertyFragment.editProperty.getType()) {
            case "Apartment":
                typeSpinner.setSelection(0);
                break;
            case "Condominium":
                typeSpinner.setSelection(1);
                break;
            case "Individual House":
                typeSpinner.setSelection(2);
                break;
            case "Villa":
                typeSpinner.setSelection(3);
                break;
            case "Townhouse":
                typeSpinner.setSelection(4);
                break;
            default:
                break;
        }

        switch (ViewPropertyFragment.editProperty.getCategory()) {
            case "Rent":
                categorySpinner.setSelection(0);
                break;
            case "Outright Purchase":
                categorySpinner.setSelection(1);
                break;
            default:
                break;
        }

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = types[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = String.valueOf(i + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        latitude = ViewPropertyFragment.editProperty.getLatitude();
        longitude = ViewPropertyFragment.editProperty.getLongitude();

        propertyName.setText(ViewPropertyFragment.editProperty.getName());
        property_address1.setText(ViewPropertyFragment.editProperty.getAddress1());
        property_address2.setText(ViewPropertyFragment.editProperty.getAddress2());
        property_zip.setText(ViewPropertyFragment.editProperty.getZip());
        property_location.setText(ViewPropertyFragment.editProperty.getLatitude() + ", " + ViewPropertyFragment.editProperty.getLongitude());
        property_value.setText(ViewPropertyFragment.editProperty.getCost());
        property_size.setText(ViewPropertyFragment.editProperty.getSize());
        property_description.setText(ViewPropertyFragment.editProperty.getDescription());

        property_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude = String.valueOf(new DecimalFormat("#0.000000").format(mLocation.getLatitude()));
                longitude = String.valueOf(new DecimalFormat("#0.000000").format(mLocation.getLongitude()));

                property_location.setText(latitude + ", " + longitude);
            }
        });

        propertyImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });

        propertyImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        propertyImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id = getActivity().getIntent().getStringExtra("userid");
                name = propertyName.getText().toString().trim();
                address1 = property_address1.getText().toString().trim();
                address2 = property_address2.getText().toString().trim();
                zip = property_zip.getText().toString();
                cost = property_value.getText().toString();
                size = property_size.getText().toString();
                description = property_description.getText().toString().trim();

                editProperty();

            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // make the device update its location
        mLocation.beginUpdates();
    }

    @Override
    public void onPause() {
        // stop location updates (saves battery)
        mLocation.endUpdates();

        super.onPause();
    }

    public void editProperty() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        /*StringRequest strReq = new StringRequest(Request.Method.POST,

                Constant.EDIT_PROPERTY_URL + ViewPropertyFragment.editProperty.getId(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

                android.app.FragmentManager fragmentManager = getFragmentManager();
                android.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.framentContainer, new ViewPropertyFragment());
                ft.commit();

                pDialog.hide();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", id);
                params.put("propertyname", name);
                params.put("propertytype", type);
                params.put("propertycat", category);
                params.put("propertyaddress1", address1);
                params.put("propertyaddress2", address2);
                params.put("propertyzip", zip);
                params.put("propertylat", latitude);
                params.put("propertylong", longitude);
                params.put("propertycost", cost);
                params.put("propertysize", size);
                params.put("propertydesc", description);
                params.put("propertystatus", "yes");

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq);*/

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.EDIT_PROPERTY_URL + ViewPropertyFragment.editProperty.getId(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
/*
                String resultResponse = new String(response.data);
                Toast.makeText(getActivity(), resultResponse, Toast.LENGTH_LONG).show();
*/

                android.app.FragmentManager fragmentManager = getFragmentManager();
                android.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.framentContainer, new ViewPropertyFragment());
                ft.commit();

                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", id);
                params.put("propertyname", name);
                params.put("propertytype", type);
                params.put("propertycat", category);
                params.put("propertyaddress1", address1);
                params.put("propertyaddress2", address2);
                params.put("propertyzip", zip);
                params.put("propertylat", latitude);
                params.put("propertylong", longitude);
                params.put("propertycost", cost);
                params.put("propertysize", size);
                params.put("propertydesc", description);
                params.put("propertystatus", "yes");

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                if (bitmap1 != null)
                    params.put("propertyimg1", new DataPart("file_avatar1.jpg", helper(bitmap1), "image/jpeg"));
                if (bitmap2 != null)
                    params.put("propertyimg2", new DataPart("file_avatar2.jpg", helper(bitmap2), "image/jpeg"));
                if (bitmap3 != null)
                    params.put("propertyimg3", new DataPart("file_avatar3.jpg", helper(bitmap3), "image/jpeg"));

                return params;
            }
        };

       // AppController.getInstance().addToRequestQueue(multipartRequest);
        Volley.newRequestQueue(getActivity()).add(multipartRequest);
    }

    public byte[] helper(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == getActivity().RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                propertyImage1.setImageBitmap(bitmap1);
            } catch (IOException e) {
            }
        } else if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                propertyImage2.setImageBitmap(bitmap2);
            } catch (IOException e) {
            }
        } else if (requestCode == 2 && resultCode == getActivity().RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap3 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                propertyImage3.setImageBitmap(bitmap3);
            } catch (IOException e) {
            }
        }
    }

}
