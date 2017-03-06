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
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //LayoutInflater reads xml and gives you Java views
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.president_pager_item, null);

        President president = presidents[position];

        setPresName(view, president);
        setPresNumber(view, president);
        setPresLifeYears(view, president);
        setPresOfficeDates(view, president);
        setPresParty(view, president);
        setPresPic(view);

        container.addView(view);
        return view;
    }

    private void setPresName(View view, President president) {
        TextView name = (TextView) view.findViewById(R.id.name);
        presName = president.getPresident();
        name.setText(presName);
    }

    private void setPresNumber(View view, President president) {
        TextView number = (TextView) view.findViewById(R.id.number);
        number.setText(String.valueOf(president.getNumber()));
    }

    private void setPresLifeYears(View view, President president) {
        TextView born = (TextView) view.findViewById(R.id.born);
        TextView died = (TextView) view.findViewById(R.id.died);

        born.setText(String.valueOf(president.getBirthYear()));

        String deathStr = String.valueOf(president.getDeathYear());
        if ("null".equals(deathStr)) {
            deathStr = "---";
        }
        died.setText(deathStr);
    }

    private void setPresOfficeDates(View view, President president) {
        TextView tookOffice = (TextView) view.findViewById(R.id.tookOffice);
        TextView leftOffice = (TextView) view.findViewById(R.id.leftOffice);
        TextView yearsInOffice = (TextView) view.findViewById(R.id.yearsInOffice);

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
        if (leftOfficeStr != null) {
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
            String timeInOfficeStr = timeInOfficeNum + " Months";
            yearsInOffice.setText(timeInOfficeStr);
        }
    }

    private void setPresParty(View view, President president) {
        TextView party = (TextView) view.findViewById(R.id.party);
        party.setText(president.getParty());
    }

    private void setPresPic(View view) {
        ImageView pic = (ImageView) view.findViewById(R.id.pic);

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
                    picUrl = results.getItems()[0].getPagemap().getCseImage()[0].getSrc();
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
        if (picUrl != null && !"".equals(picUrl)) {
            Picasso.with(context).load(picUrl).into(pic);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
