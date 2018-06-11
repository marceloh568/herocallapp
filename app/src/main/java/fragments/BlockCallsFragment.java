package fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.CallAdapterRV;
import br.com.ownard.forgetme.MainActivity;
import br.com.ownard.forgetme.R;
import models.Contato;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlockCallsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TextView emptyView;
    private CallAdapterRV adapterRV;

    public BlockCallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_block_calls, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rvContact);
        emptyView = (TextView)view.findViewById(R.id.isEmpty);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        adapterRV = new CallAdapterRV(getBlockCalls(), R.layout.item_contato, getActivity());
        mRecyclerView.setAdapter(adapterRV);

        this.changeView();


        return view;
    }

    private List<Contato> getBlockCalls(){
        MainActivity mainActivity = (MainActivity)getActivity();
        return mainActivity.getCallController().load();
    }

    public void changeView(){
        if (getBlockCalls().isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }else{
            emptyView.setVisibility(View.GONE);
        }
    }

    public void search(String phone){
        MainActivity mainActivity = (MainActivity)getActivity();
        adapterRV.notifyData(mainActivity.getCallController().search(phone));
    }
    public void update(){
        this.changeView();
        adapterRV.notifyData(getBlockCalls());
    }

}
