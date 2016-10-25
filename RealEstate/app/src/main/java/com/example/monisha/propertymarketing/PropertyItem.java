package com.example.monisha.propertymarketing;

/**
 * Created by monisha on 10/24/2016.
 */

public class PropertyItem {

    public String id, name, type, category, address1, address2,
            zip, image1, image2, image3, latitude, longitude, cost, size, desc;

    public PropertyItem(String id, String name, String type, String category, String address1,
                        String address2, String zip, String image1, String image2, String image3,
                        String latitude, String longitude, String cost, String size, String desc){
        this.id=id;
        this.name=name;
        this.type=type;
        this.category=category;
        this.address1=address1;
        this.address2=address2;
        this.zip=zip;
        this.image1=image1;
        this.image2=image2;
        this.image3=image3;
        this.latitude=latitude;
        this.longitude=longitude;
        this.cost=cost;
        this.size=size;
        this.desc=desc;
    }
}
