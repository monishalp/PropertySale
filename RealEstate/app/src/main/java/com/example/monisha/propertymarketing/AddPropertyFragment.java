package com.example.monisha.propertymarketing;

/**
 * Created by monisha on 10/20/2016.
 */
import android.app.Fragment;
import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class AddPropertyFragment extends Fragment {
    private SimpleLocation mLocation;
    ProgressDialog pDialog;
    final private String[] types = new String[]{"Apartment", "Condominium", "Individual House", "Villa", "Townhouse"};
    final private String[] categories = new String[]{"Rent", "Outright Purchase"};
    private String id, name, type, category, address1, address2, zip, latitude, longitude, cost, size, description;
    private TextView submit;
    private EditText propertyName, property_address1, property_address2, property_zip, property_location, property_value, property_size, property_description;
    private ImageView propertyImage1, propertyImage2, propertyImage3;
}
