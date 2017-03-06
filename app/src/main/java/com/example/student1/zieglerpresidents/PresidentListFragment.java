package com.example.student1.zieglerpresidents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PresidentListFragment extends Fragment {

    //Keep local data code for testing purposes and retrofit failure backup
    private final boolean USE_LOCAL_DATA = false;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_president_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (USE_LOCAL_DATA) {
            loadLocalData();
        } else {
            loadRetrofitData();
        }
    }

    private void loadLocalData() {
        //GsonBuilder is what creates Gson
        //tell it to read all under_scores as cammelCase
        //GsonBuilder builder = new GsonBuilder();
        //builder.setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        //Gson gson = new GsonBuilder().create();

        Gson gson = new GsonBuilder()
                .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setLenient()
                .create();

        InputStream in = getResources().openRawResource(R.raw.presidents);
        final President presidents[] = gson.fromJson(new InputStreamReader(in), President[].class);

        PresidentAdapter adapter = new PresidentAdapter(presidents, (OnPresidentSelectedListener) getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void loadRetrofitData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PresidentsService service = retrofit.create(PresidentsService.class);

        Call<List<President>> call = service.listPresidents();

        call.enqueue(new Callback<List<President>>() {
            @Override
            public void onResponse(Call<List<President>> call, Response<List<President>> response) {
                List<President> list = response.body();

                PresidentAdapter adapter = new PresidentAdapter(list.toArray(new President[0]), (OnPresidentSelectedListener) getActivity());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<President>> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.list_retrieval_error), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                loadLocalData();
            }
        });
    }
}
