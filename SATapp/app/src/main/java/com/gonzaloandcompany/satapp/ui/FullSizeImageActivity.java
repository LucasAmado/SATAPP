package com.gonzaloandcompany.satapp.ui;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullSizeImageActivity extends AppCompatActivity {
    @BindView(R.id.fullSizeImg)
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_size_image);
        ButterKnife.bind(this);

        GlideUrl glideUrl = new GlideUrl(Constants.BASE_URL+getIntent().getStringExtra("imgID"),
                new LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer " + Constants.TOKEN_PROVISIONAL)
                        .build());

        Glide.with(this).load(glideUrl).into(img);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
