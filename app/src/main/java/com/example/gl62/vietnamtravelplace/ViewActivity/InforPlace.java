package com.example.gl62.vietnamtravelplace.ViewActivity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gl62.vietnamtravelplace.Adapter.ImageViewAdapter;
import com.example.gl62.vietnamtravelplace.R;
import com.example.gl62.vietnamtravelplace.object.Cover;
import com.example.gl62.vietnamtravelplace.object.Image;
import com.example.gl62.vietnamtravelplace.object.Place;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InforPlace extends AppCompatActivity {
    ImageView img_cover_detail;
    TextView tv_place_Name_detail, tv_address_detail, tv_desc_detail,tvTitle;
    RecyclerView rcImg;
    ImageViewAdapter imageViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);
//            getSupportActionBar().setCustomView(R.layout.cutomview_actionbar);
        }
        //Init
        setContentView(R.layout.activity_infor_place);
        img_cover_detail = (ImageView) findViewById(R.id.img_cover_detail);
        tv_address_detail = (TextView) findViewById(R.id.tv_address_detail);
        tv_place_Name_detail = (TextView) findViewById(R.id.tv_place_Name_detail);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tv_desc_detail = (TextView) findViewById(R.id.tv_desc_detail);
        rcImg = (RecyclerView)findViewById(R.id.rcImg);

        //get Data
        Intent receivedIntent = getIntent();
        Place place = (Place) receivedIntent.getSerializableExtra("extra_data_place");
        List<Cover> arlCover = place.getCover();
        List<Image> arlImage = place.getImages();

        String url = "";
        for (int i = 0; i < arlCover.size(); i++) {
            url = arlCover.get(i).getUrl();
        }
        Picasso.with(getBaseContext()).load("http://bwhere.vn/uploads/big/"+url)
                .placeholder(R.drawable.ic_loading)
                .into(img_cover_detail);
        getSupportActionBar().setTitle(place.getNameVi());
        tv_place_Name_detail.setText(place.getNameVi());
        tv_address_detail.setText(place.getAddressVi());
        tv_desc_detail.setText(place.getDescriptionVi());


        //RecycleView
        List<String>urlImage = new ArrayList<>();
        for (int i = 0; i <arlImage.size() ; i++) {
            urlImage.add(arlImage.get(i).getUrl());
        }
        imageViewAdapter = new ImageViewAdapter(urlImage,getBaseContext());
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(InforPlace.this, LinearLayoutManager.HORIZONTAL, false);
        rcImg.setLayoutManager(horizontalLayoutManagaer);
        rcImg.setAdapter(imageViewAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(getBaseContext(),MapFragmentActivity.class);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
