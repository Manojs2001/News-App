package com.example.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    List<SliderItems> sliderItems;
    LayoutInflater sLayoutInflater;
    Context context;
    ArrayList<String> titles;
    ArrayList<String> desc;
    ArrayList<String> newsLink;
    ArrayList<String> heads;
    VerticalViewPager verticalViewPager;

    int newPostion;
    float x1,x2;


    public ViewPagerAdapter(Context context, List<SliderItems> sliderItems, ArrayList<String> titles,ArrayList<String> desc,ArrayList<String> images,ArrayList<String> newsLinks,ArrayList<String> heads,VerticalViewPager verticalViewPager){
        this.context =context;
        this.sliderItems = sliderItems;
        this.titles = titles;
        this.desc = desc;
        this.newsLink = newsLinks;
        this.heads = heads;
        this.verticalViewPager = verticalViewPager;
        sLayoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sliderItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = sLayoutInflater.inflate(R.layout.item_container,container,false);
        ImageView imageView = itemView.findViewById(R.id.img);
        ImageView imageView1 = itemView.findViewById(R.id.img_bottom);
        TextView title = itemView.findViewById(R.id.headline);
        TextView desctv = itemView.findViewById(R.id.desc);
        TextView head = itemView.findViewById(R.id.head);

        title.setText(titles.get(position));
        desctv.setText(desc.get(position));
        head.setText(heads.get(position));

        Glide.with(context)
                .load(sliderItems.get(position).getImage())
                .centerCrop()
                .into(imageView);


        Glide.with(context)
                .load(sliderItems.get(position).getImage())
                .centerCrop()
                .override(12,12)
                .into(imageView1);

        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                    newPostion=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        verticalViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        break;

                        case MotionEvent.ACTION_UP:
                            x2 = motionEvent.getX();
                            float deltaX = x1-x2;
                            if(deltaX > 300){
                                Intent i = new Intent(context,NewsDetailActivity.class);
                                if(position==1){
                                    i.putExtra("url",newsLink.get(0));
                                    context.startActivity(i);
                                }else{
                                    i.putExtra("url",newsLink.get(newPostion));
                                    context.startActivity(i);
                                }
                            }
                            break;
                }

                return false;
            }
        });

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,NewsDetailActivity.class);
                i.putExtra("url",newsLink.get(0));
                context.startActivity(i);
            }
        });
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
