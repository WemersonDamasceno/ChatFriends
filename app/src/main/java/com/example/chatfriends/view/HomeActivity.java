package com.example.chatfriends.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.chatfriends.R;
import com.example.chatfriends.controll.ViewPageAdapter;
import com.example.chatfriends.model.Usuario;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class HomeActivity extends AppCompatActivity {
    Bundle bundlePerfil;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bundlePerfil = new Bundle();
        intent = new Intent(this,MyPerfilActivity.class);

        if(FirebaseAuth.getInstance().getUid() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6875FF")));
        ViewPager viewPager = findViewById(R.id.pager);

        ViewPageAdapter viewPagerAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_conversas);
        tabLayout.getTabAt(0).setText("Conversas");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_contatos);
        tabLayout.getTabAt(1).setText("Contatos");
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_grupos);
        tabLayout.getTabAt(2).setText("Grupos");









    }


    //criar icones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_procurar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Quando clicar nos icones
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.ic_procurar){
            startActivity(new Intent(getApplicationContext(), ProcurarActivity.class));
        }

        if(item.getItemId() == R.id.ic_perfil){
            FirebaseFirestore.getInstance().collection("/users")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                Usuario user1 = doc.toObject(Usuario.class);
                                if(user1.getIdUser().equals(FirebaseAuth.getInstance().getUid())){
                                    bundlePerfil.putParcelable("useratual", user1);
                                    intent.putExtra("bundleperfil", bundlePerfil);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }

}