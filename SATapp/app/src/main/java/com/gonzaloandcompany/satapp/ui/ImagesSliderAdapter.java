package com.gonzaloandcompany.satapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;


import java.util.List;

public class ImagesSliderAdapter extends PagerAdapter {
    private Activity act;
    private List<String> photos;

    public ImagesSliderAdapter(Activity act, List<String> photos) {
        this.act = act;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return this.photos.size();
    }

    public String getItem(int pos) {
        return photos.get(pos);
    }

    public void setItems(List<String> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final String o = photos.get(position);
        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_slider_image, container, false);

        ImageView image = v.findViewById(R.id.image);

        //TODO:CAMBIAR EL TOKEN DEL HEADER DE GLIDEURL CUANDO GONZALO TERMINE CON EL LOGIN
        GlideUrl glideUrl = new GlideUrl(Constants.BASE_URL + o,
                new LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer " + Constants.TOKEN_PROVISIONAL)
                        .build());
        Log.d("GLIDEURL",glideUrl.toString());
        Glide.with(v.getContext()).load(glideUrl).into(image);

        ((ViewPager) container).addView(v);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeFullSize = new Intent(act, FullSizeImageActivity.class);
                seeFullSize.putExtra("imgID", o);
                act.startActivity(seeFullSize);
            }
        });
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
