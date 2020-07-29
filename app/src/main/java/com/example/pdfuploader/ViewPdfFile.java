package com.example.pdfuploader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPdfFile extends AppCompatActivity {

    private ListView myPdfListView;
    DatabaseReference databaseReference;
    List<uplodepdf> uplodePDFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_pdf_file);

        myPdfListView = (ListView) findViewById(R.id.myListView);
        uplodePDFS = new ArrayList<>();



        viewAllFiles();

        myPdfListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                uplodepdf uplodepdf = uplodePDFS.get(position);

                Intent intent = new Intent();
                intent.setType( Intent.ACTION_VIEW );
                intent.setData( Uri.parse(uplodepdf.getUrl()) );
                startActivity(intent);

            }
        } );
    }


    private void viewAllFiles(){
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    uplodepdf uplodepdF  = postSnapshot.getValue(com.example.pdfuploader.uplodepdf.class);

                    uplodePDFS.add(uplodepdF);

                }
                String[] uploads = new String[uplodePDFS.size()];

                for(int i = 0; i<uploads.length;i++)
                {
                    uploads[i]=uplodePDFS.get(i).getName();

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads){

                   //see here
                    @Override
                    public View getView(int position,View convertView,  ViewGroup parent) {

                        View view = super.getView(  position, convertView, parent);
                        TextView myText = (TextView) view.findViewById( android.R.id.text1 );
                        myText.setTextColor( Color.BLACK ); ;




                        return view;
                    }
                };
                myPdfListView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
