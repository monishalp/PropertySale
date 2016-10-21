package com.dada.realestatemanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.dada.realestatemanager.R;
import com.dada.realestatemanager.util.PropertyItem;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int pos=getIntent().getIntExtra("pos",-1);
        //Toast.makeText(this, ""+pos,Toast.LENGTH_LONG).show();

        PropertyItem item=BuyerMainActivity.propertyList.get(pos);
        SliderLayout sliderShow=(SliderLayout) findViewById(R.id.slider);
        for(int i=0;i<3;i++){
            TextSliderView textSliderView=new TextSliderView(this);
            switch (i){
                case 0:
                    textSliderView.description(item.name)
                            .image("http://static1.squarespace.com/static/5564e014e4b03b1392f5cf94/55d1efe2e4b0c22f9bea7017/55d1f0ebe4b061baebe9bf79/1439871278995/?format=1500w");
                    break;
                case 1:
                    textSliderView.description(item.category)
                            .image("http://jstsolutions.net/wp-content/themes/realty/lib/images/key_img2.png");
                    break;
                case 2:
                    textSliderView.description(item.size)
                            .image("http://images.all-free-download.com/images/graphicthumb/green_house_icon_312519.jpg");
                    break;
            }
            sliderShow.addSlider(textSliderView);
        }

        TextView textView=(TextView)findViewById(R.id.detail);
        textView.setText("Id: "+item.id+"\n"
                +"Name: "+item.name+"\n"
                +"Type: "+item.type+"\n"
                +"Category: "+item.category+"\n"
                +"Address 1: "+item.address1+"\n"
                +"Address 2: "+item.address2+"\n"
                +"Size: "+item.size+"\n"
                +"Cost: "+item.cost+"\n"
                +"Description: "+item.desc
        );
        Button btn=(Button)findViewById(R.id.detail_btn);
        if(item.category.equals("1")){
            btn.setText("Rent");
        }
        else{
            btn.setText("Buy");
        }

    }
}
