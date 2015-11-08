package com.example.student1.zieglerpresidents;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PresidentPagerAdapter extends PagerAdapter {

    private President[] presidents;
    private Context context;
    private String presName;
    private String picUrl;

    public PresidentPagerAdapter(President[] presidents, Context context) {
        this.presidents = presidents;
        this.context = context;
    }

    @Override
    public int getCount() {
        return presidents.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //LayoutInflater reads xml and gives you Java views
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.president_pager_item, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView number = (TextView) view.findViewById(R.id.number);
        TextView born = (TextView) view.findViewById(R.id.born);
        TextView died = (TextView) view.findViewById(R.id.died);
        TextView tookOffice = (TextView) view.findViewById(R.id.tookOffice);
        TextView leftOffice = (TextView) view.findViewById(R.id.leftOffice);
        TextView yearsInOffice = (TextView) view.findViewById(R.id.yearsInOffice);
        TextView party = (TextView) view.findViewById(R.id.party);
        ImageView pic = (ImageView) view.findViewById(R.id.pic);

        President president = presidents[position];
        presName = president.getPresident();
        name.setText(presName);
        number.setText(String.valueOf(president.getNumber()));

        born.setText(String.valueOf(president.getBirthYear()));

        String deathStr = String.valueOf(president.getDeathYear());
        if(deathStr.equals("null")) {
            deathStr = "---";
        }
        died.setText(deathStr);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat monthFormat = new SimpleDateFormat("MMM");
        DateFormat dayFormat = new SimpleDateFormat("EEEE");
        Calendar cal = Calendar.getInstance();

        Date tookOfficeDate = new Date();
        try {
            tookOfficeDate = dateFormat.parse(president.getTookOffice());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.setTime(tookOfficeDate);
        String tookDayOfWeek = dayFormat.format(tookOfficeDate);
        int tookYear = cal.get(Calendar.YEAR);
        int tookMonth = cal.get(Calendar.MONTH);
        String tookMonthStr = monthFormat.format(cal.getTime());
        int tookDay = cal.get(Calendar.DAY_OF_MONTH);
        tookOffice.setText(tookDayOfWeek + ", " + tookMonthStr + " " + tookDay + ", " + tookYear);


        Date leftOfficeDate = new Date();
        String leftOfficeStr = president.getLeftOffice();
        if(leftOfficeStr != null) {
            try {
                leftOfficeDate = dateFormat.parse(president.getLeftOffice());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        cal.setTime(leftOfficeDate);
        String leftDayOfWeek = dayFormat.format(leftOfficeDate);
        int leftYear = cal.get(Calendar.YEAR);
        int leftMonth = cal.get(Calendar.MONTH);
        String leftMonthStr = monthFormat.format(cal.getTime());

        int leftDay = cal.get(Calendar.DAY_OF_MONTH);

        leftOfficeStr = leftOfficeStr == null ? "---" : leftDayOfWeek + ", " + leftMonthStr + " " + leftDay + ", " + leftYear;
        leftOffice.setText(leftOfficeStr);

        int timeInOfficeNum = leftYear - tookYear;
        if (timeInOfficeNum > 0) {
            yearsInOffice.setText(String.valueOf(timeInOfficeNum));
        } else {
            timeInOfficeNum = leftMonth + tookMonth;
            yearsInOffice.setText(timeInOfficeNum + " Months");
        }

        party.setText(president.getParty());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    presName = presName.replace(' ', '+');
                    URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyB7Dp4XAToWDu80weFWjFi80Ghcv03XAjo&cx=009877138924476938272:nzucfxgdb8w&q=" + presName);
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    String json = IOUtils.toString(in);
                    SearchPicResults results = new Gson().fromJson(json, SearchPicResults.class);
                    picUrl = results.getItems()[0].getPagemap().getCse_image()[0].getSrc();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*presName = presName.replace(' ', '+');
        String picUrl = "";
        try {
            picUrl = new RetrieveImgTask().execute(presName).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }*/
        if (picUrl != null && picUrl != "") {
            Picasso.with(context).load(picUrl).into(pic);
        }

        party.setText(president.getParty());


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
