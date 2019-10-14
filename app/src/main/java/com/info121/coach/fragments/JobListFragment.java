package com.info121.coach.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.info121.coach.App;
import com.info121.coach.R;
import com.info121.coach.adapters.JobsAdapter;
import com.info121.coach.api.RestClient;
import com.info121.coach.models.Job;
import com.info121.coach.models.JobRes;
import com.info121.coach.models.ObjectRes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobListFragment extends Fragment {

    List<Job> mJobList;

    @BindView(R.id.no_data)
    TextView mNoData;

    @BindView(R.id.rv_jobs)
    RecyclerView mRecyclerView;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout mSwipeLayout;

    Context mContext = getActivity();

    String mCurrentTab = "";


    public static JobListFragment newInstance(String param1) {
        JobListFragment fragment = new JobListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        fragment.mCurrentTab = param1;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (mCurrentTab) {
            case "TODAY": {
                getTodayJobs();
            }
            break;
            case "TOMORROW": {
                getTomorrowJobs();
            }
            break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_job_list, container, false);

        ButterKnife.bind(this, view);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (mCurrentTab) {
                    case "TODAY": {
                        getTodayJobs();
                    }
                    break;
                    case "TOMORROW": {
                        getTomorrowJobs();
                    }
                    break;
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }





    private void getTodayJobs() {
        Call<JobRes> call = RestClient.COACH().getApiService().GetTodayJobs();

        call.enqueue(new Callback<JobRes>() {
            @Override
            public void onResponse(Call<JobRes> call, Response<JobRes> response) {
                mSwipeLayout.setRefreshing(false);
                mJobList = (List<Job>) response.body().getJobs();


                if(mJobList.size() > 0)
                    mNoData.setVisibility(View.GONE);
                else
                    mNoData.setVisibility(View.VISIBLE);

                // set to recyclerview
                mRecyclerView.setHasFixedSize(false);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setAdapter(new JobsAdapter(mContext, mJobList));
                mRecyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<JobRes> call, Throwable t) {

            }
        });


    }

    private void getTomorrowJobs() {
        Call<JobRes> call = RestClient.COACH().getApiService().GetTomorrowJobs();

        call.enqueue(new Callback<JobRes>() {
            @Override
            public void onResponse(Call<JobRes> call, Response<JobRes> response) {
                mSwipeLayout.setRefreshing(false);
                mJobList = (List<Job>) response.body().getJobs();


                if(mJobList.size() > 0)
                    mNoData.setVisibility(View.GONE);
                else
                    mNoData.setVisibility(View.VISIBLE);

                // set to recyclerview
                mRecyclerView.setHasFixedSize(false);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setAdapter(new JobsAdapter(mContext, mJobList));
                mRecyclerView.getAdapter().notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<JobRes> call, Throwable t) {

            }
        });


    }

    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
