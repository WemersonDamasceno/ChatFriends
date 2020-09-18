package com.example.chatfriends.controll;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatfriends.view.fragments.AmigosFragment;
import com.example.chatfriends.view.fragments.ConversasFragment;
import com.example.chatfriends.view.fragments.GruposFragment;


public class ViewPageAdapter extends FragmentPagerAdapter {

    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ConversasFragment();
                break;

            case 1:
                fragment = new AmigosFragment();
                break;

            case 2:
                fragment = new GruposFragment();
        }
        return fragment;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String str = null;
        switch (position) {
            case 0:
                //str = "Caronas Gratis";
                break;
            case 1:
                //str = "Caronas Pagas";
                break;
        }
        return str;
    }


    @Override
    public int getCount() {
        return 3;
    }
}