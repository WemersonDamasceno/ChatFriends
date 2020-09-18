package com.example.chatfriends.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.chatfriends.R;

public class ConversasFragment extends Fragment {
    LinearLayout layoutLostConversas;
    RecyclerView rvConversas;

    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        layoutLostConversas = view.findViewById(R.id.layoutLostConversas);
        rvConversas = view.findViewById(R.id.rvConversas);




        return view;
    }
}